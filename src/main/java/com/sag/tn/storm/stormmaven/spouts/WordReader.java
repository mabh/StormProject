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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class WordReader implements IRichSpout {

	private SpoutOutputCollector collector;
	private FileReader fileReader;
	private boolean completed = false;
	private TopologyContext context;
	
	public boolean isDistributed() {
		return false;
	}
	
	public void open(Map conf, TopologyContext context,
			SpoutOutputCollector collector) {
		try {
			this.context = context;
			this.fileReader = new FileReader(conf.get("wordsFile").toString());
			
		} catch(FileNotFoundException e) {
			throw new RuntimeException("Error reading: " + conf.get("wordsFile"));
		}
		this.collector = collector;
	}

	public void close() {
		try {
			this.fileReader.close();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void activate() {
		// TODO Auto-generated method stub

	}

	public void deactivate() {
		// TODO Auto-generated method stub

	}

	/*
	 * read file and emit line by line
	 */
	public void nextTuple() {
		if(completed) {
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				
			}
			return;
		}
		
		String str;
		BufferedReader reader = new BufferedReader(this.fileReader);
		try {
			while((str = reader.readLine()) != null) {
				this.collector.emit(new Values(str), str);
			}
		} catch(Exception e) {
			throw new RuntimeException("Error reading tuple: " + e.getMessage());
		} finally {
			completed = true;
		}
		
		
	}

	public void ack(Object msgId) {
		System.out.println("OK: " + msgId);

	}

	public void fail(Object msgId) {
		System.out.println("FAIL: " + msgId);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("line"));
	}

	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
