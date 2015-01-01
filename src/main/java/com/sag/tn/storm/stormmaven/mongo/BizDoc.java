package com.sag.tn.storm.stormmaven.mongo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.mongodb.BasicDBObject;

public class BizDoc extends DataBaseObject {

	private UUID internalId;	//UUID uuid = UUID.randomUUID()
	private BizDocTypeDef docType;
	private Timestamp docTimeStamp;
	private String systemStatus;
	private Map<String, String> attributes;
	private byte[] content;
	
	public BizDoc() {}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public BizDoc setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
		return this;
	}

	public byte[] getContent() {
		return content;
	}

	public BizDoc setContent(byte[] content) {
		this.content = content;
		return this;
	}

	public UUID getInternalId() {
		return internalId;
	}

	public BizDoc setInternalId(UUID internalId) {
		this.internalId = internalId;
		return this;
	}

	public BizDocTypeDef getDocType() {
		return docType;
	}

	public BizDoc setDocType(BizDocTypeDef docType) {
		this.docType = docType;
		return this;
	}

	public Timestamp getDocTimeStamp() {
		return docTimeStamp;
	}

	public BizDoc setDocTimeStamp(Timestamp docTimeStamp) {
		this.docTimeStamp = docTimeStamp;
		return this;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BizDoc [internalId=" + internalId + ", docType=" + docType
				+ ", docTimeStamp=" + docTimeStamp + ", systemStatus="
				+ systemStatus + ", attributes=" + attributes + "]";
	}

	public String getSystemStatus() {
		return systemStatus;
	}

	public BizDoc setSystemStatus(String systemStatus) {
		this.systemStatus = systemStatus;
		return this;
	}
	
	@Override
	public BasicDBObject getDbObject() {

		Map<String, Object> map = new HashMap<>();
		map.put(Constants.db_bizDoc_internalId_fieldName, this.getInternalId());
		map.put(Constants.db_bizDoc_docType_fieldName,this.getDocType().getDbObject());
		map.put(Constants.db_bizDoc_systemStatus_fieldName, this.getSystemStatus());
		map.put(Constants.db_bizDoc_docTimeStamp_fieldName, this.getDocTimeStamp());
		map.put(Constants.db_bizDoc_attributes_fieldName, this.getAttributes());
		map.put(Constants.db_bizDoc_content_fieldName, this.getContent());
		BasicDBObject basicDBObject = new BasicDBObject(map);
		return basicDBObject;
		

	}
	

}
