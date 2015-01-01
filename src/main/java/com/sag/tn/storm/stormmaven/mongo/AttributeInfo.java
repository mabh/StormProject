package com.sag.tn.storm.stormmaven.mongo;

public class AttributeInfo {

	private String name;
	private String query;
	
	public AttributeInfo() {}
	
	public AttributeInfo(String name, String query) {
		this.name = name;
		this.query = query;
	}
	
	public String getName() {
		return name;
	}

	public AttributeInfo setName(String name) {
		this.name = name;
		return this;
	}

	public String getQuery() {
		return query;
	}

	public AttributeInfo setQuery(String query) {
		this.query = query;
		return this;
	}

}
