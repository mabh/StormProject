/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package com.sag.tn.storm.stormmaven.mongo;

public class MongoConnectionInfoBean {
	private String dbUrl = "vmmabh01:27017";
	private String dbName = "TNStorm";

	private static MongoConnectionInfoBean _instance = new MongoConnectionInfoBean();
	
	/* Singleton */
	private MongoConnectionInfoBean(){}
	
	public static synchronized MongoConnectionInfoBean getInstance() {
		return _instance;
	}
	
	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	
	
	
}
