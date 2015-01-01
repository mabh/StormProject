package com.sag.tn.storm.stormmaven.mongo;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class DBConnection {
	
	private static MongoClient mongoClient = null;

	private static synchronized void set() throws UnknownHostException {
		if(mongoClient == null) {
			mongoClient =  new MongoClient(MongoConnectionInfoBean.getInstance().getDbUrl());
		}
	}
	
	public DBConnection() {
		try {
			set();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DB getDatabase(){
		if(mongoClient == null) {
			try {
				set();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return mongoClient.getDB(MongoConnectionInfoBean.getInstance().getDbName());
	}

}
