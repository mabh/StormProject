package com.sag.tn.storm.stormmaven.mongo;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;

public class BizDocAttributeDef extends DataBaseObject {

	private String attributeName;
	private String attributeID;
	private String attributeType;

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	public String getAttributeID() {
		return attributeID;
	}

	public void setAttributeID(String attributeID) {
		this.attributeID = attributeID;
	}

	public String getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(String attributeType) {
		this.attributeType = attributeType;
	}

	@Override
	public BasicDBObject getDbObject() {

		Map<String, Object> map = new HashMap<>();
		map.put(Constants.db_bizDocAttributeDef_attributeID_fieldName,
				this.attributeID);
		map.put(Constants.db_bizDocAttributeDef_attributeName_fieldName,
				this.attributeName);
		map.put(Constants.db_bizDocAttributeDef_attributeType_fieldName,
				this.attributeType);

		BasicDBObject db = new BasicDBObject(map);

		return db;

	}

}
