/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package com.sag.tn.storm.stormmaven.bolts;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.sag.tn.storm.stormmaven.mongo.BizDocTypeDef;

public class DocumentTypeCounterBolt implements IRichBolt {

	private Map<String, Integer> counter;
	private OutputCollector collector;
	private Logger logger = LoggerFactory.getLogger(DocumentTypeCounterBolt.class);
	private Integer taskId;
	private String componentId;
	
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		counter = new HashMap<>();
		this.collector = collector;
		this.taskId = context.getThisTaskId();
		this.componentId = context.getThisComponentId();
	}

	public void execute(Tuple input) {
		logger.info("Beginning to execute component {} task {} at time {}", this.componentId, this.taskId, System.currentTimeMillis());
		
		BizDocTypeDef bdtd = (BizDocTypeDef)input.getValue(1);
		String key = bdtd.getTypeID() + " - " + bdtd.getTypeName();
		if(!counter.containsKey(key)) {
			counter.put(key, 1);
		} else {
			counter.put(key, counter.get(key) + 1);
		}
		this.collector.ack(input);

		logger.info("Ending execute component {} task {} at time {}", this.componentId, this.taskId, System.currentTimeMillis());
	}

	public void cleanup() {
		for(Map.Entry<String, Integer> entry: counter.entrySet()) {
			logger.info("Counter output {} - {} - {}", new Object[] {this.taskId, entry.getKey(), entry.getValue()});
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub

	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
