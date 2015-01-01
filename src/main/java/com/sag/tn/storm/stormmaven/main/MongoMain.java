/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package com.sag.tn.storm.stormmaven.main;

import java.util.List;

import com.sag.tn.storm.stormmaven.mongo.AttributeInfo;
import com.sag.tn.storm.stormmaven.mongo.BizDocTypeDef;
import com.sag.tn.storm.stormmaven.mongo.BizDocTypeDefImpl;

public class MongoMain {
	public static void main(String[] args) {
		//make a bizdoctypedef
		
		BizDocTypeDef bdtd = new BizDocTypeDef();
		bdtd.setTypeID("1234")
			.setTypeName("typeBooks")
			.setRootTag("books")
			.setAttributeinfo(new AttributeInfo("authorname", "/books/book[1]/author"));
		
		BizDocTypeDefImpl.getInstance().insert(bdtd);
		
		
		//List<BizDocTypeDef> list = (new BizDocTypeDefImpl()).getAll();
		//System.out.println(list);

		System.out.println("Done");
	}
}
