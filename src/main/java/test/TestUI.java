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

import org.apache.thrift7.TException;
import org.apache.thrift7.protocol.TBinaryProtocol;
import org.apache.thrift7.transport.TFramedTransport;
import org.apache.thrift7.transport.TSocket;
import org.apache.thrift7.transport.TTransportException;

import backtype.storm.generated.ClusterSummary;
import backtype.storm.generated.Nimbus.Client;

public class TestUI {
	public static void main(String[] args) throws TException {
		 TSocket socket = new TSocket("vmanat03.eur.ad.sag", 6627);
		  TFramedTransport transport = new TFramedTransport(socket);
		  TBinaryProtocol protocol = new TBinaryProtocol(transport);
		  Client client = new Client(protocol);
		   transport.open();
		   ClusterSummary summary = client.getClusterInfo();

		    // Cluster Details
		    System.out.println("**** Storm UI Home Page ****");
		    System.out.println(" ****Cluster Summary**** ");
		   System.out.println("Number of Supervisors: "  + summary.get_supervisors_size());
	}
}
