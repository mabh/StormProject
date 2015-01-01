/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package com.sag.tn.storm.stormmaven.spouts;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.sag.tn.storm.stormmaven.spoutsources.DirectorySpoutSource;
import com.sag.tn.storm.stormmaven.spoutsources.ISpoutSource;
import com.sag.tn.storm.stormmaven.spoutsources.NirvanaQueueSpoutSource;
import com.ximpleware.VTDNav;

/*
 * Pushes a list of document objects (which are to be pushed) to the topology
 */
public class DocumentFetcherSpout implements IRichSpout {

	private ISpoutSource source;
	private SpoutOutputCollector collector;
	private TopologyContext context;
	private Random random;
	private Logger logger = LoggerFactory.getLogger(DocumentFetcherSpout.class);
	
	private Map<String, Long> processingStartTimeMap = new HashMap<>();
	private int counter = 0;
	
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		/*
		 * this.source = new DirectorySpoutSource((String)conf.get("DirectorySpoutSource_directoryToProcess"));
		 */
		String queueName = (String)conf.get("NirvanaQueueSpoutSource_nirvanaQueueName");
		String realmName = (String)conf.get("NirvanaQueueSpoutSource_nirvanaRealmName");
		int maxBatchSize = ((Long)conf.get("NirvanaQueueSpoutSource_maxBatchSize")).intValue();
		this.source = new NirvanaQueueSpoutSource(queueName, realmName, maxBatchSize);
		this.collector = collector;
		this.context = context;
		this.random = new Random();
	}
	
	public void close() {
		try {
			this.source.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void activate() {}

	public void deactivate() {}

	/*
	 * emit tuples
	 */
	public void nextTuple() {
		try {
			List<VTDNav> list = this.source.fetch();
			if(list.size() == 0) {
				return;
			}
			for(VTDNav vn: list) {
				String id = "xmldoc-" + (counter++);
				this.processingStartTimeMap.put(id,  System.currentTimeMillis());
				this.collector.emit(new Values(vn), id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void ack(Object msgId) {
		/*this.logger.info("OK: {} - TIMER: {} - {} - {}", msgId, 
								(System.currentTimeMillis() - this.processingStartTimeMap.get(msgId)),
								System.currentTimeMillis(),
								this.processingStartTimeMap.get(msgId)
				);*/
	}

	public void fail(Object msgId) {
		this.logger.info("FAIL: {}", msgId);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("vnavobject"));

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
