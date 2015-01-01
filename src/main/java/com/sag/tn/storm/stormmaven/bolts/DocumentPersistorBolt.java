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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

import com.sag.tn.storm.stormmaven.mongo.BizDoc;
import com.sag.tn.storm.stormmaven.mongo.BizDocImpl;
import com.sag.tn.storm.stormmaven.mongo.MongoConnectionInfoBean;

public class DocumentPersistorBolt implements IRichBolt {

	private OutputCollector collector;
	private Logger logger = LoggerFactory.getLogger(DocumentPersistorBolt.class);
	private Integer taskId;
	private String componentId;
	
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.taskId = context.getThisTaskId();
		this.componentId = context.getThisComponentId();
		MongoConnectionInfoBean.getInstance().setDbUrl((String)stormConf.get("Component_MongoDbUrl"));
		MongoConnectionInfoBean.getInstance().setDbName((String)stormConf.get("Component_MongoDbName"));
	}

	public void execute(Tuple input) {
		//logger.info("Beginning to execute component {} task {} at time {} of message {}", this.componentId, this.taskId, System.currentTimeMillis(), input.getMessageId());
		
		BizDoc bd = (BizDoc)input.getValue(0);
		BizDocImpl.getInstance().insert(bd);
		this.collector.ack(input);

		//logger.info("Ending execute component {} task {} at time {} of message {}", this.componentId, this.taskId, System.currentTimeMillis(), input.getMessageId());
	}
	
	public void cleanup() {}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}
}
