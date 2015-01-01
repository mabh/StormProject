/*
* Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
* and/or Software AG USA Inc., Reston, VA, USA, and/or 
* its subsidiaries and or/its affiliates and/or their 
* licensors.
* Use, reproduction, transfer, publication or disclosure 
* is prohibited except as specifically provided for in your 
* License Agreement with Software AG.
*/

package com.sag.tn.storm.stormmaven.vtd;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.ximpleware.EOFException;
import com.ximpleware.EncodingException;
import com.ximpleware.EntityException;
import com.ximpleware.IByteBuffer;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class VSerializer extends Serializer<VTDNav> {

	@Override
	public VTDNav read(Kryo kryo, Input in, Class<VTDNav> clazz) {
		/*
		 * input to object
		 */
		VTDGen vg = new VTDGen();
		int len = in.readInt();
		byte[] bytes = in.readBytes(len);
		vg.setDoc(bytes);
		try {
			vg.parse(true);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return vg.getNav();
	}

	@Override
	public void write(Kryo kryo, Output out, VTDNav vn) {
		/*
		 * object to output
		 */
		out.writeInt(vn.getXML().getBytes().length);
		out.write(vn.getXML().getBytes());		
	}

}
