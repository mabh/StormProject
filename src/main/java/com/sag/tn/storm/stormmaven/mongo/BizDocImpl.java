package com.sag.tn.storm.stormmaven.mongo;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class BizDocImpl implements IDBOperations {

	private final String collectionName = BizDoc.class.getSimpleName();
	private DBCollection collection = null;
	private DB db = null;

	private static BizDocImpl instance = new BizDocImpl();
	
	private BizDocImpl() {
		DBConnection dbConnection = new DBConnection();
		db = dbConnection.getDatabase();
		collection = db.getCollection(collectionName);
	}

	public static BizDocImpl getInstance() {
		return instance;
	}
	
	public void insert(Object o) {

		BizDoc bizDoc= (BizDoc) o;
		
		collection.insert(bizDoc.getDbObject());


	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public List<Object> getAll() {
		return null;
	}

	public Object get(Object o) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	public void delete(Object o) {
		// TODO Auto-generated method stub

	}

}
