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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.sag.tn.storm.stormmaven.mongo.AttributeInfo;
import com.sag.tn.storm.stormmaven.mongo.BizDoc;
import com.sag.tn.storm.stormmaven.mongo.BizDocTypeDef;
import com.sag.tn.storm.stormmaven.vtd.Util;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathParseException;

/*
 * makes a BizDoc out of a xml document object using the document
 * type passed
 */
public class DocumentMakerBolt implements IRichBolt {

	private OutputCollector collector;
	private Logger logger = LoggerFactory.getLogger(DocumentMakerBolt.class);
	private Integer taskId;
	private String componentId;
	
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.taskId = context.getThisTaskId();
		this.componentId = context.getThisComponentId();
	}

	public void execute(Tuple input) {
		//logger.info("Beginning to execute component {} task {} at time {} of message {}", this.componentId, this.taskId, System.currentTimeMillis(), input.getMessageId());
	
		VTDNav vn = (VTDNav)input.getValue(0);
		BizDocTypeDef bdtd = (BizDocTypeDef)input.getValue(1);
		Map<String, String> map = new HashMap<>();
		AttributeInfo attrinfo = bdtd.getAttributeinfo();
		try {
			map.put(attrinfo.getName(), Util.getXPathMatchedString(vn, attrinfo.getQuery()));
			BizDoc bd = new BizDoc();
			bd.setInternalId(UUID.randomUUID())
				.setDocType(bdtd)
				.setDocTimeStamp(new java.sql.Timestamp((new java.util.Date()).getTime()))
				.setSystemStatus("SomeStatus")
				.setAttributes(map)
				.setContent(null);
			
			List<Tuple> list = new ArrayList<>();
			list.add(input);
			//emit BizDoc made from xml document object using BizDocTypeDef
			this.collector.emit(list, new Values(bd));
			this.collector.ack(input);
		} catch (XPathParseException e) {
			e.printStackTrace();
		}
		//logger.info("Ending execute component {} task {} at time {} of message {}", this.componentId, this.taskId, System.currentTimeMillis(), input.getMessageId());
	}
	
	public void cleanup() {}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("bizdoc"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
