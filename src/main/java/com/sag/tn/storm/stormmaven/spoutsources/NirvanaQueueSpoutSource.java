/*
 * Copyright (c) 2013-2014 Software AG, Darmstadt, Germany 
 * and/or Software AG USA Inc., Reston, VA, USA, and/or 
 * its subsidiaries and or/its affiliates and/or their 
 * licensors.
 * Use, reproduction, transfer, publication or disclosure 
 * is prohibited except as specifically provided for in your 
 * License Agreement with Software AG.
 */

package com.sag.tn.storm.stormmaven.spoutsources;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pcbsys.nirvana.client.nChannelAttributes;
import com.pcbsys.nirvana.client.nChannelNotFoundException;
import com.pcbsys.nirvana.client.nConsumeEvent;
import com.pcbsys.nirvana.client.nEventListener;
import com.pcbsys.nirvana.client.nIllegalArgumentException;
import com.pcbsys.nirvana.client.nIllegalChannelMode;
import com.pcbsys.nirvana.client.nIllegalStateException;
import com.pcbsys.nirvana.client.nQueue;
import com.pcbsys.nirvana.client.nQueuePeekContext;
import com.pcbsys.nirvana.client.nQueueReader;
import com.pcbsys.nirvana.client.nQueueReaderContext;
import com.pcbsys.nirvana.client.nQueueSyncReader;
import com.pcbsys.nirvana.client.nRequestTimedOutException;
import com.pcbsys.nirvana.client.nSecurityException;
import com.pcbsys.nirvana.client.nSessionFactory;
import com.pcbsys.nirvana.client.nSessionNotConnectedException;
import com.pcbsys.nirvana.client.nSessionPausedException;
import com.pcbsys.nirvana.client.nUnexpectedResponseException;
import com.pcbsys.nirvana.client.nUnknownRemoteRealmException;
import com.sag.tn.storm.stormmaven.pcbsys.nSampleApp;
import com.sag.tn.storm.stormmaven.vtd.Util;
import com.ximpleware.EOFException;
import com.ximpleware.EncodingException;
import com.ximpleware.EntityException;
import com.ximpleware.IndexReadException;
import com.ximpleware.NavException;
import com.ximpleware.ParseException;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;

public class NirvanaQueueSpoutSource extends nSampleApp implements
		ISpoutSource {

	private VTDGen vg = new VTDGen();
	private final Logger logger = LoggerFactory
			.getLogger(NirvanaQueueSpoutSource.class);
	private nQueue queue;
	private nQueueSyncReader reader;
	private int maxBatchSize = 100;
	
	public NirvanaQueueSpoutSource(final String queueName, final String realmName, final int maxBatchSize) {
		this.constructSession(parseRealmProperties(realmName));
		nChannelAttributes nca = new nChannelAttributes();
		try {
			nca.setName(queueName);
			this.queue = mySession.findQueue(nca);
			this.reader = this.queue.createReader(new nQueueReaderContext());
			this.maxBatchSize = maxBatchSize;
		} catch (nChannelNotFoundException | nSessionPausedException
				| nUnknownRemoteRealmException | nSecurityException
				| nSessionNotConnectedException | nIllegalArgumentException
				| nUnexpectedResponseException | nRequestTimedOutException
				| nIllegalChannelMode e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public List<VTDNav> fetch() throws IOException {
		nQueuePeekContext ctx = nQueueReader.createContext(2);
		nConsumeEvent event = null;
		List<VTDNav> list = new ArrayList<>();
		
		try {
			int batchIndex = 0;
			while(this.queue.size() > 0 && (batchIndex++) < this.maxBatchSize) {
				event = this.reader.pop();	//Synchronous blocking call
				this.vg.setDoc(event.getEventData());
				this.vg.parse(true);
				list.add(this.vg.getNav());
				this.vg.clear();
			}
		} catch (nSecurityException 
				| nSessionNotConnectedException 
				| nSessionPausedException
				| nIllegalStateException 
				| ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}

	public void close() throws IOException, nIllegalArgumentException, nUnexpectedResponseException, nSessionPausedException, nSessionNotConnectedException, nRequestTimedOutException {
		nQueue.destroyReader(reader);
		nSessionFactory.close(mySession);
	}

	/*
	public static void main(String[] args) throws IOException, nIllegalArgumentException, nUnexpectedResponseException, nSessionPausedException, nSessionNotConnectedException, nRequestTimedOutException, NavException {
		NirvanaQueueSpoutSource obj = new NirvanaQueueSpoutSource("myQueue", "nsp://10.60.25.187:9000", 20);
		List<VTDNav> list = obj.fetch();
		
		if(list.size() == 0) {
			System.out.println("empty");
		} else {
			for(VTDNav vn: list) {
				System.out.println(VTDUtil.getRootTag(vn));
			}
		}
		obj.close();
		System.out.println("end main");
		
	}*/
	
}
