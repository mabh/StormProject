package com.sag.tn.storm.stormmaven.mongo;

import java.util.List;

import com.mongodb.DB;
import com.mongodb.DBCollection;

public class BizDocAttributeDefImpl implements IDBOperations {
	
	private final String collectionName = BizDocAttributeDefImpl.class.getSimpleName();
	private DBCollection collection = null;
	private DB db = null;

	private static BizDocAttributeDefImpl instance = new BizDocAttributeDefImpl();
	
	private BizDocAttributeDefImpl() {
		DBConnection dbConnection = new DBConnection();
		db = dbConnection.getDatabase();
		collection = db.getCollection(collectionName);
	}

	public static BizDocAttributeDefImpl getInstance() {
		return instance;
	}
	
	public void insert(Object o) {

		BizDocAttributeDef bizDocAttributeDef= (BizDocAttributeDef) o;
		collection.insert(bizDocAttributeDef.getDbObject());

	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public List<Object> getAll() {
		// TODO Auto-generated method stub
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
