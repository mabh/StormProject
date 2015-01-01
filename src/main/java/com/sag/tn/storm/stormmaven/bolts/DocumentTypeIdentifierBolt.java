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
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

import com.sag.tn.storm.stormmaven.mongo.BizDocTypeDef;
import com.sag.tn.storm.stormmaven.mongo.BizDocTypeDefImpl;
import com.sag.tn.storm.stormmaven.mongo.MongoConnectionInfoBean;
import com.sag.tn.storm.stormmaven.vtd.Util;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;

public class DocumentTypeIdentifierBolt implements IRichBolt {

	private OutputCollector collector;
	private Logger logger = LoggerFactory.getLogger(DocumentTypeIdentifierBolt.class);
	private Integer taskId;
	private String componentId;
	
	public void prepare(Map stormConf, TopologyContext context,
			OutputCollector collector) {
		this.collector = collector;
		this.taskId = context.getThisTaskId();
		this.componentId = context.getThisComponentId();
	}

	/*
	 * get parsed VTDNav objects - each object for 1 XML input
	 * do matching here with document types from doctype store
	 */
	public void execute(Tuple input) {
		//logger.info("Beginning to execute component {} task {} at time {} of message {}", this.componentId, this.taskId, System.currentTimeMillis(), input.getMessageId());
		VTDNav vn = (VTDNav)input.getValue(0);	//vn object to be processed
		List<BizDocTypeDef> doctypes = BizDocTypeDefImpl.getInstance().getAll();
		try {
			String root = Util.getRootTag(vn);
			for(BizDocTypeDef bdtd: doctypes) {
				if(bdtd.getRootTag().equalsIgnoreCase(root)) {
					List<Tuple> list = new ArrayList<>();
					list.add(input);
					//emit document object represented by vn with doc type against which it is identified
					this.collector.emit(list, new Values(vn, bdtd, bdtd.getTypeID()));
					//once matched get out of loop
					break;
				}
			}
			this.collector.ack(input);
		} catch (NavException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//logger.info("Ending execute component {} task {} at time {} of message {}", this.componentId, this.taskId, System.currentTimeMillis(), input.getMessageId());
	}

	public void cleanup() {/**/}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("xmlobject", "bizdoctypedef", "bizdoctypeid"));

	}

	public Map<String, Object> getComponentConfiguration() {
		return null;
	}

}
