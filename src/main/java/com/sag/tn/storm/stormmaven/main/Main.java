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

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

import com.sag.tn.storm.stormmaven.bolts.WordCounter;
import com.sag.tn.storm.stormmaven.bolts.WordNormalizer;
import com.sag.tn.storm.stormmaven.spouts.WordReader;

public class Main {
	public static void main(String[] args) throws InterruptedException, AlreadyAliveException, InvalidTopologyException {
		
		/*
		 * build topology
		 */
		
		/*
		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("word-reader", new WordReader());
		builder.setBolt("word-normalizer", new WordNormalizer()).shuffleGrouping("word-reader");
		
		builder.setBolt("word-counter", new WordCounter(), 4)
			   .fieldsGrouping("word-normalizer", new Fields("word"))
			   .setNumTasks(4);
		*/
		
		
		/*
		 * create conf
		 */
		/*
		Config conf = new Config();
		conf.put("wordsFile", args[0]);
		conf.setDebug(true);
		*/
		
		
		/*
		 * submit topology to LocalCluster to run it
		 */
		/*
		LocalCluster localCluster = new LocalCluster();
		localCluster.submitTopology("word counter", conf, builder.createTopology());
		Thread.sleep(20000);
		localCluster.shutdown();
		*/
		
		//StormSubmitter.submitTopology("wordcounter", conf, builder.createTopology());
		
		
	}
}
