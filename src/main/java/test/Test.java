/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;

import com.sag.tn.storm.stormmaven.vtd.Util;
import com.ximpleware.NavException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

class SWrapper implements Serializable {
	private VTDNav vn;

	public SWrapper(VTDNav vn) {
		super();
		this.vn = vn;
	}
	
	public VTDNav get() {
		return this.vn;
	}
}

public class Test {
	public static void main(String[] args) {
		
		
		try {
			VTDGen vg = new VTDGen();
			vg.parseFile("src/main/resources/xmlcopy/books.xml", true);
			VTDNav myObject = vg.getNav();
			
			SWrapper sw = new SWrapper(myObject);
			
			//myObject.getXML()
			
			
			
			new ObjectOutputStream(new ByteArrayOutputStream()).writeObject(sw);
			System.out.println("OK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
