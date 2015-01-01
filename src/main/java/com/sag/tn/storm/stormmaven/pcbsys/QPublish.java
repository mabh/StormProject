/*
    Copyright 2012 Software AG, Darmstadt, Germany and/or Software AG USA
  Inc., Reston, United States of America, and/or their licensors.
  In the event that you should download or otherwise use this software
  you hereby acknowledge and agree to the terms at
  http://um.terracotta.org/company/terms.html#legalnotices
 */
package com.sag.tn.storm.stormmaven.pcbsys;
import com.pcbsys.nirvana.client.*;
/**
 * Pushes events to a nirvana queue
 */
public class QPublish extends nSampleApp {
    private boolean isOk = true;
    private nBaseClientException asyncException;
    private static QPublish mySelf = null;
    /**
     * This method demonstrates the Universal Messaging API calls necessary to publish to a
     * queue. It is called after all command line arguments have been received
     * and validated
     * 
     * @param realmDetails
     *            a String[] containing the possible RNAME values
     * @param aqueueName
     *            the queue name to publish to
     * @param count
     *            the number of messages to publish
     * @param size
     *            the size (bytes) of each message to be published
     */
    private void doit(String[] realmDetails, String aqueueName, int count,
            int size) {
        mySelf.constructSession(realmDetails);
        // Publishes to the specified queue
        try {
            // Create a channel attributes object
            nChannelAttributes nca = new nChannelAttributes();
            nca.setName(aqueueName);
            // Obtain a reference to the queue
            nQueue myQueue = mySession.findQueue(nca);
            // Create a byte array filled with characters equal to
            // the message size specified. This could be a result
            // of String.getBytes() call in a real world scenario.
            /*byte[] buffer = new byte[size];
            for (int x = 0; x < size; x++) {
                buffer[x] = (byte) ((x % 90) + 32);
            }*/
            String xmlMessage = "<?xml version=\"1.0\"?>" +
            		"<books>" +
            		"<book id=\"bk101\"><author>Gambardella, Matthew</author><title>XML Developer's Guide</title>" +
            		"<genre>Computer</genre><price>45.95</price></book>" +
            		"</books>";
            		
            
            nEventProperties props = new nEventProperties();
            props.put("test", true);
            // Instantiate the message to be published with the specified TAG
            // and byte[]
            nConsumeEvent evt1 = new nConsumeEvent(props, xmlMessage.getBytes());
            nEventProperties props1 = new nEventProperties();
            props1.put("test", false);
            // Instantiate the message to be published with the specified TAG
            // and byte[]
            nConsumeEvent evt2 = new nConsumeEvent(props1, xmlMessage.getBytes());
            evt1.setPersistant(true);
            // Inform the user that publishing is about to start
            System.out.println("Starting push of " + count + " events of size "
                    + size);
            // Get a timestamp to be used to calculate the message publishing
            // rates
            long start = System.currentTimeMillis();
            // Loop as many times as the number of messages we want to publish
            for (int x = 0; x < count; x++) {
                // Publish the event
                myQueue.push(evt1);
                // Check if an asynchronous exception has been received
                if (!isOk) {
                    // If it has, then throw it
                    throw asyncException;
                }
            }
            // Do a synchronous call before exiting the sample to ensure all
            // buffers have been flushed
            myQueue.getDetails().getNoOfEvents();
            // Check if an asynchronous exception has been received
            if (!isOk) {
                // If it has, then throw it
                throw asyncException;
            }
            // Get a timestamp to calculate the publishing rates
            long end = System.currentTimeMillis();
            // Calculate the events / sec rate
            long eventPerSec = (((count) * 1000) / ((end + 1) - start));
            // Calculate the bytes / sec rate
            long bytesPerSec = eventPerSec * size;
            // Inform the user of the resulting rates
            System.out.println("Events = " + count + " Events/sec = "
                    + eventPerSec + " Bytes/Sec = " + bytesPerSec);
        }
        // Handle errors
        catch (nChannelNotFoundException cnfe) {
            System.out.println("The queue specified could not be found.");
            System.out.println("Please ensure that the queue exists in the REALM you connect to.");
            cnfe.printStackTrace();
            System.exit(1);
        } catch (nSecurityException se) {
            System.out.println("Insufficient permissions for the requested operation.");
            System.out.println("Please check the ACL settings on the server.");
            se.printStackTrace();
            System.exit(1);
        } catch (nSessionNotConnectedException snce) {
            System.out.println("The session object used is not physically connected to the Universal Messaging realm.");
            System.out.println("Please ensure the realm is up and check your RNAME value.");
            snce.printStackTrace();
            System.exit(1);
        } catch (nUnexpectedResponseException ure) {
            System.out.println("The Universal Messaging REALM has returned an unexpected response.");
            System.out.println("Please ensure the Universal Messaging REALM and client API used are compatible.");
            ure.printStackTrace();
            System.exit(1);
        } catch (nUnknownRemoteRealmException urre) {
            System.out.println("The queue specified resided in a remote realm which could not be found.");
            System.out.println("Please ensure the queue name specified is correct.");
            urre.printStackTrace();
            System.exit(1);
        } catch (nRequestTimedOutException rtoe) {
            System.out.println("The requested operation has timed out waiting for a response from the REALM.");
            System.out.println("If this is a very busy REALM ask your administrator to increase the client timeout values.");
            rtoe.printStackTrace();
            System.exit(1);
        } catch (nBaseClientException nbce) {
            System.out.println("An error occured while creating the Channel Attributes object.");
            nbce.printStackTrace();
            System.exit(1);
        }
        // Close the session we opened
        try {
            nSessionFactory.close(mySession);
        } catch (Exception ex) {
        }
        // Close any other sessions within this JVM so that we can exit
        nSessionFactory.shutdown();
    }
    protected void processArgs(String[] args){
    switch (args.length){
    case 3:
        System.getProperties().put("SIZE",args[2]);
    case 2:
        System.getProperties().put("COUNT",args[1]);
      case 1:
        if (args[0].equals("-?")) {
          Usage();
          UsageEnv();
        }
        System.getProperties().put("QUEUENAME",args[0]);
        break;
    }
  }
    public static void main(String[] args) {
        // Create an instance for this class
        mySelf = new QPublish();
        // Process command line arguments
        mySelf.processArgs(args);
        // Process Environment Variables
       // nSampleApp.processEnvironmentVariables();
        // Check the queue name specified
        String queueName = "myQueue";
        /*
        if (System.getProperty("QUEUENAME") != null)
            queueName = System.getProperty("QUEUENAME");
        else {
            Usage();
            System.exit(1);
        }*/
        int count = 5; // default value
        // Check if the number of messages to be published has been specified
        if (System.getProperty("COUNT") != null) {
            try {
                count = Integer.parseInt(System.getProperty("COUNT"));
            } catch (Exception num) {
            } // Ignore and use the defaults
        }
        int size = 100; // default value
        // Check if the size (bytes) of each message has been specified
        if (System.getProperty("SIZE") != null) {
            try {
                size = Integer.parseInt(System.getProperty("SIZE"));
            } catch (Exception num) {
            } // Ignore and use the default
        }
        // Check the local realm details
        int idx = 0;
        String RNAME = null;
        System.setProperty("RNAME", "nsp://10.60.25.187:9000");
        if (System.getProperty("RNAME") != null)
            RNAME = System.getProperty("RNAME");
        else {
            Usage();
            System.exit(1);
        }
        // Process the local REALM RNAME details
        String[] rproperties = new String[4];
        rproperties = parseRealmProperties(RNAME);
        // Push events to the queue specified
        mySelf.doit(rproperties, queueName, count, size);
    }
    /**
     * Prints the usage message for this class
     */
    private static void Usage() {
        System.out.println("Usage ...\n");
        System.out.println("npushq <queue name> [count] [size] \n");
        System.out.println("<Required Arguments> \n");
        System.out.println("<queue name> - Queue name parameter for the queue to publish to");
        System.out.println("\n[Optional Arguments] \n");
        System.out.println("[count] -The number of events to publish (default: 10)");
        System.out.println("[size] - The size (bytes) of the event to publish (default: 100)");
        System.out.println("\n\nNote: -? provides help on environment variables \n");
    }
} // End of publish Class
