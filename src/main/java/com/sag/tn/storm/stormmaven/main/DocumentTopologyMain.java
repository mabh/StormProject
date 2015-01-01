/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package com.sag.tn.storm.stormmaven.main;

import org.apache.thrift7.TException;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.sag.tn.storm.stormmaven.bolts.DocumentMakerBolt;
import com.sag.tn.storm.stormmaven.bolts.DocumentPersistorBolt;
import com.sag.tn.storm.stormmaven.bolts.DocumentTypeCounterBolt;
import com.sag.tn.storm.stormmaven.bolts.DocumentTypeIdentifierBolt;
import com.sag.tn.storm.stormmaven.spouts.DocumentFetcherSpout;
import com.sag.tn.storm.stormmaven.vtd.Util;

public class DocumentTopologyMain {
	public static void main(String[] args) throws InterruptedException, AlreadyAliveException, InvalidTopologyException, TException {
		
		int nSupervisors = Util.getNumberOfSupervisors("vmanat03.eur.ad.sag");
		int workersMultiple = 3;	//worker processes per supervisor node
		int executorsMultiple = 3;	//bolt executors per worker process
		int nWorkers = workersMultiple * nSupervisors;
		int nExecutors = executorsMultiple * nWorkers;
		
		
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("fetcher", new DocumentFetcherSpout(), nExecutors);
		builder.setBolt("identifier", new DocumentTypeIdentifierBolt(), nExecutors).shuffleGrouping("fetcher");
		builder.setBolt("maker", new DocumentMakerBolt(), nExecutors).shuffleGrouping("identifier");
		//builder.setBolt("counter", new DocumentTypeCounterBolt(), 1).fieldsGrouping("identifier", new Fields("bizdoctypeid"));
		builder.setBolt("persistor", new DocumentPersistorBolt(), nExecutors).shuffleGrouping("maker");
		
		/*
		 * create conf
		 */
		Config conf = new Config();
		//conf.put("DirectorySpoutSource_directoryToProcess", args[0]);
		
		conf.put("NirvanaQueueSpoutSource_nirvanaQueueName", "myQueue");
		conf.put("NirvanaQueueSpoutSource_nirvanaRealmName", "nsp://10.60.25.187:9000");
		conf.put("NirvanaQueueSpoutSource_maxBatchSize", 10);
		conf.put("Component_MongoDbUrl", "vmmabh01:27017");
		conf.put("Component_MongoDbName", "TNStorm");
		conf.setNumWorkers(nWorkers);
		conf.setDebug(false);
		
		conf.registerSerialization(com.ximpleware.VTDNav.class, com.sag.tn.storm.stormmaven.vtd.VSerializer.class);
		//use Kryo FieldSerialization for this class
		conf.registerSerialization(com.sag.tn.storm.stormmaven.mongo.BizDoc.class);
		conf.registerSerialization(com.sag.tn.storm.stormmaven.mongo.BizDocTypeDef.class);
		conf.registerSerialization(com.sag.tn.storm.stormmaven.mongo.AttributeInfo.class);
		
		/*conf.registerSerialization(com.ximpleware.UniByteBuffer.class);
		conf.registerSerialization(com.ximpleware.ContextBuffer.class);
		conf.registerSerialization(com.ximpleware.FastLongBuffer.class);
		*/
		
		/*
		 * submit topology to LocalCluster to run it
		 */
		if("local".equalsIgnoreCase(args[0])) {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("TNStorm", conf, builder.createTopology());
			Thread.sleep(20000);
			localCluster.shutdown();
		} else {
			StormSubmitter.submitTopology("TNStorm", conf, builder.createTopology());
		}
	}
}
