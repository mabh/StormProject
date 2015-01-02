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

import org.apache.thrift7.TException;
import org.apache.thrift7.protocol.TBinaryProtocol;
import org.apache.thrift7.transport.TFramedTransport;
import org.apache.thrift7.transport.TSocket;
import org.apache.thrift7.transport.TTransportException;

import backtype.storm.generated.ClusterSummary;
import backtype.storm.generated.Nimbus.Client;

import com.ximpleware.AutoPilot;
import com.ximpleware.NavException;
import com.ximpleware.VTDNav;
import com.ximpleware.XPathEvalException;
import com.ximpleware.XPathParseException;

public class Util {
	public static String getRootTag(VTDNav vn) throws NavException {
		vn.toElement(VTDNav.ROOT);
		return vn.toString(vn.getCurrentIndex());
	}

	public static String getXPathMatchedString(VTDNav vn, String xpath)
			throws XPathParseException {
		// assumes it matches a node containing a string
		/* xpath */
		String match = "";
		AutoPilot ap = new AutoPilot(vn);
		ap.selectXPath(xpath);
		try {
			int i;
			while ((i = ap.evalXPath()) != -1) {
				match = vn.toString(vn.getText());
			}
		} catch (NavException | XPathEvalException e) {
			e.printStackTrace();
		} finally {
			ap.resetXPath();
		}
		return match;
	}

	public static int getNumberOfSupervisors(final String UIhost) throws TException {
		TSocket socket = new TSocket(UIhost, 6627);
		TFramedTransport transport = new TFramedTransport(socket);
		TBinaryProtocol protocol = new TBinaryProtocol(transport);
		Client client = new Client(protocol);
		transport.open();
		ClusterSummary summary = client.getClusterInfo();
		socket.close();
		return summary.get_supervisors_size();
	}

}
