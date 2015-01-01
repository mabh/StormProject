package com.sag.tn.storm.stormmaven.mongo;

import java.util.HashMap;
import java.util.Map;

import com.mongodb.BasicDBObject;

public class BizDocTypeDef extends DataBaseObject {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BizDocTypeDef [typeName=" + typeName + ", typeID=" + typeID
				+ ", rootTag=" + rootTag + ", attributeinfo=" + attributeinfo
				+ "]";
	}

	private String typeName;
	private String typeID;
	private String rootTag;
	private AttributeInfo attributeinfo;

	public BizDocTypeDef() {}
	
	public String getTypeName() {
		return typeName;
	}

	//fluent setters
	public BizDocTypeDef setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	public String getTypeID() {
		return typeID;
	}

	public BizDocTypeDef setTypeID(String typeID) {
		this.typeID = typeID;
		return this;
	}

	public String getRootTag() {
		return rootTag;
	}

	public BizDocTypeDef setRootTag(String rootTag) {
		this.rootTag = rootTag;
		return this;
	}

	public AttributeInfo getAttributeinfo() {
		return attributeinfo;
	}

	public BizDocTypeDef setAttributeinfo(AttributeInfo attributeinfo) {
		this.attributeinfo = attributeinfo;
		return this;
	}

	@Override
	public BasicDBObject getDbObject() {

		Map<String, Object> mapAttrib = new HashMap<>();

		mapAttrib.put(Constants.db_attributeInfo_name_fieldName,
				this.attributeinfo.getName());

		mapAttrib.put(Constants.db_attributeInfo_query_fieldName,
				this.attributeinfo.getQuery());

		BasicDBObject attribute = new BasicDBObject(mapAttrib);

		Map<String, Object> map = new HashMap<>();
		map.put(Constants.db_bizDocTypeDef_typeID_fieldName, this.typeID);
		map.put(Constants.db_bizDocTypeDef_typeName_fieldName, this.typeName);
		map.put(Constants.db_bizDocTypeDef_rootTag_fieldName, this.rootTag);
		map.put(Constants.db_bizDocTypeDef_attributeinfo_fieldName, attribute);

		BasicDBObject bd = new BasicDBObject(map);

		return bd;

	}

}
