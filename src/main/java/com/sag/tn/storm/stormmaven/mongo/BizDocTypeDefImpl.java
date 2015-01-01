package com.sag.tn.storm.stormmaven.mongo;

import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanServer;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.management.ManagementService;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;

public class BizDocTypeDefImpl implements IDBOperations<BizDocTypeDef> {
	
	private final String collectionName = BizDocTypeDef.class.getSimpleName();
	private DBCollection collection = null;
	private DB db = null;
	private CacheManager cacheManager = null;
	private static BizDocTypeDefImpl instance = new BizDocTypeDefImpl();
	
	private final String CACHENAME = "DocTypeList";
	private final String CACHEKEY = "list";
	
	
	private BizDocTypeDefImpl() {
		DBConnection dbConnection = new DBConnection();
		db = dbConnection.getDatabase();
		collection = db.getCollection(collectionName);
		
		this.cacheManager = CacheManager.create();
		MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		ManagementService.registerMBeans(this.cacheManager, mBeanServer, false, false, false, true);
		Cache cache = new Cache(CACHENAME, 1, false, true, 0, 0);
		cache.getCacheConfiguration().setStatistics(true);
		this.cacheManager.addCache(cache);
	}

	public static BizDocTypeDefImpl getInstance() {
		return instance;
	}
	
	public void insert(BizDocTypeDef o) {

		BizDocTypeDef bizDocTypeDef= (BizDocTypeDef) o;
		collection.insert(bizDocTypeDef.getDbObject());

	}

	public void update() {
		// TODO Auto-generated method stub

	}

	public synchronized List<BizDocTypeDef> getAll() {
	
		if(cacheManager.getCache(CACHENAME).isKeyInCache(CACHEKEY)) {
			return (List<BizDocTypeDef>)cacheManager.getCache(CACHENAME).get(CACHEKEY).getObjectValue();
		}
		
		DBCursor dbCursor = collection.find();
		List<BizDocTypeDef> list = new ArrayList<>();
		while(dbCursor.hasNext()) {
			
			BasicDBObject bd = (BasicDBObject) dbCursor.next();
			
			BizDocTypeDef bizDocTypeDef = new BizDocTypeDef();
			
			BasicDBObject name = new BasicDBObject();
			name = (BasicDBObject) bd.get(Constants.db_bizDocTypeDef_attributeinfo_fieldName);
			
			BasicDBObject query = new BasicDBObject();
			query = (BasicDBObject) bd.get(Constants.db_bizDocTypeDef_attributeinfo_fieldName);
			
			AttributeInfo attribute = 
					new AttributeInfo(
							name.getString(Constants.db_attributeInfo_name_fieldName),
							query.getString(Constants.db_attributeInfo_query_fieldName)
					);
			
			bizDocTypeDef.setAttributeinfo(attribute);
			
			bizDocTypeDef.setRootTag(bd.getString(Constants.db_bizDocTypeDef_rootTag_fieldName));
			
			bizDocTypeDef.setTypeID(bd.getString(Constants.db_bizDocTypeDef_typeID_fieldName));
			
			bizDocTypeDef.setTypeName(bd.getString(Constants.db_bizDocTypeDef_typeName_fieldName));
			
			list.add(bizDocTypeDef);
		    
		}
		
		cacheManager.getCache(CACHENAME).put(new Element(CACHEKEY, list));
		return list;
	}

	public Object get(BizDocTypeDef o) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	public void delete(BizDocTypeDef o) {
		// TODO Auto-generated method stub

	}

}
