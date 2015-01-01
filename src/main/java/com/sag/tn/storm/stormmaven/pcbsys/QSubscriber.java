/*
    Copyright 2012 Software AG, Darmstadt, Germany and/or Software AG USA
  Inc., Reston, United States of America, and/or their licensors.
  In the event that you should download or otherwise use this software
  you hereby acknowledge and agree to the terms at
  http://um.terracotta.org/company/terms.html#legalnotices
 */
package com.sag.tn.storm.stormmaven.pcbsys;
import com.pcbsys.nirvana.client.*;
import java.io.IOException;
/**
 * Creates an asynchronous queue listener using a queue reader
 */
public class QSubscriber extends nSampleApp implements nEventListener {
    static String selector = null;
    private long lastEID = 0;
    private long startTime = 0;
    private long byteCount = 0;
    private int logLevel = 0;
    private int count = -1;
    private int totalMsgs = 0;
    private int reportCount = 1000;
    private nQueue myQueue;
    private static QSubscriber mySelf = null;
    /**
     * This method demonstrates the Universal Messaging API calls necessary to create an
     * asynchronous listener on a queue
     * 
     * It is called after all command line arguments have been received and
     * validated
     * 
     * @param realmDetails
     *            a String[] containing the possible RNAME values
     * @param aqueueName
     *            the queue name to pop
     * @param selector
     *            the pop selector filter
     * @param startEid
     *            the eid to start popping from
     * @param loglvl
     *            the specified log level
     * @param repCount
     *            the specified report count
     */
    private void doit(String[] realmDetails, String aqueueName, int loglvl,
            int repCount, String selector) {
        logLevel = loglvl;
        reportCount = repCount;
        mySelf.constructSession(realmDetails);
        // Subscribes to the specified queue
        try {
            // Create a channel attributes object
            nChannelAttributes nca = new nChannelAttributes();
            nca.setName(aqueueName);
            // Obtain the queue reference
            myQueue = mySession.findQueue(nca);
            // output queue details
            nQueueDetails details = myQueue.getDetails();
            System.out.println("Current queue size = "
                    + details.getNoOfEvents());
            System.out.println("Current queue age  = "
                            + (details.getLastEventTime() - details
                                    .getFirstEventTime()));
            System.out.println("Current storage size = "
                    + details.getTotalMemorySize());
            System.out.println("Current readers    = "
                    + details.getNoOfReaders());
            // create the queue reader
            /*nQueueReader reader = myQueue
                    .createReader(new nQueueReaderContext());
            nQueuePeekContext ctx = nQueueReader.createContext(5, selector);  
            nConsumeEvent[] queue = reader.peek(ctx);  
            if (queue != null) {  
                for (int x = 0; x < queue.length; x++) {  
                    this.go(queue[x]);  
                }  
                // continue browsing the queue  
                while (ctx.hasMore()) {  
                    queue = reader.peek(ctx);  
                    if (queue != null) {  
                        for (int x = 0; x < queue.length; x++) {  
                            this.go(queue[x]);  
                        }  
                    }  
                }  
            } else {  
                System.out.println("No events received...");  
            }  */
            nQueueAsyncReader reader = myQueue  
                            .createAsyncReader(new nQueueReaderContext(this, selector));  
            // Stay subscribed until the user presses any key
            System.in.read();
            System.out.println("Finished. Consumed total of " + totalMsgs);
            // Destroy the queue reader
            nQueue.destroyReader(reader);
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
        } catch (IOException e) {
            e.printStackTrace(); // To change body of catch statement use
                                    // Options | File Templates.
        }
        // Close the session we opened
        try {
            nSessionFactory.close(mySession);
        } catch (Exception ex) {
        }
        // Close any other sessions within this JVM so that we can exit
        nSessionFactory.shutdown();
    }
    protected void processArgs(String[] args) {
        switch (args.length) {
        case 5:
            System.getProperties().put("SELECTOR", args[4]);
        case 4:
            System.getProperties().put("COUNT", args[3]);
        case 3:
            System.getProperties().put("DEBUG", args[2]);
        case 2:
            System.getProperties().put("TIMEOUT", args[1]);
        case 1:
            if (args[0].equals("-?")) {
                Usage();
                UsageEnv();
            }
            System.getProperties().put("QUEUENAME", args[0]);
            break;
        }
    }
    public static void main(String[] args) {
        // Create an instance for this class
        mySelf = new QSubscriber();
        // Process command line arguments
        mySelf.processArgs(args);
        // Process Environment Variables
        nSampleApp.processEnvironmentVariables();
        // Check the queue name specified
        String queueName = "myQueue";
        /*
        if (System.getProperty("QUEUENAME") != null)
            queueName = System.getProperty("QUEUENAME");
        else {
            Usage();
            System.exit(1);
        }*/
        
        int loglvl = 3; // Default value
        // Check to see if a DEBUG value has been specified.
        if (System.getProperty("DEBUG") != null) {
            try {
                loglvl = Integer.parseInt(System.getProperty("DEBUG"));
            } catch (Exception num) {
            } // Ignore and use the default
        }
        int reportCount = 1000; // Default value
        // Check to see if a value for report count has been specified. Every
        // time
        // N events get received where N = report count, a subscription rate
        // report will
        // be printed in System.out
        if (System.getProperty("COUNT") != null) {
            try {
                reportCount = Integer.parseInt(System.getProperty("COUNT"));
            } catch (Exception num) {
            } // Ignore and use the default
        }
        // Check to see if a value for the selector has been specified.
        if (System.getProperty("SELECTOR") != null) {
            try {
                selector = System.getProperty("SELECTOR");
            } catch (Exception num) {
            } // Ignore and use the default
        }
        // Check the local realm details
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
        // Pop to the queue specified
        mySelf.doit(rproperties, queueName, loglvl, reportCount, selector);
    }
    /**
     * Each time the queue.pop operation this method will be called to output
     * the event details.
     * 
     * @param evt
     *            An nConsumeEvent object containing the message received from
     *            the queue
     */
    public void go(nConsumeEvent evt) {
        // If this is the first message we receive
        if (count == -1) {
            // Get a timestamp to be used for message rate calculations
            startTime = System.currentTimeMillis();
            count = 0;
        }
        // Increment he counter
        count++;
        totalMsgs++;
        // Have we reached the point where we need to report the rates?
        if (count == reportCount) {
            // Reset the counter
            count = 0;
            // Get a timestampt to calculate the rates
            long end = System.currentTimeMillis();
            // Does the specified log level permits us to print on the screen?
            if (logLevel >= 1) {
                // Dump the rates on the screen
                if (end != startTime) {
                    System.out.println("Received " + reportCount + " in "
                            + (end - startTime) + " Evt/Sec = "
                            + ((reportCount * 1000) / (end - startTime))
                            + " Bytes/sec="
                            + ((byteCount * 1000) / (end - startTime)));
                } else {
                    System.out.println("Received " + reportCount
                            + " faster than the system millisecond counter");
                }
            }
            // Set the startTime for the next report equal to the end timestamp
            // for the previous one
            startTime = end;
            // Reset the byte counter
            byteCount = 0;
        }
        // If the last EID counter is not equal to the current event ID
        if (lastEID != evt.getEventID()) {
            lastEID = evt.getEventID() + 1;
        } else {
            // Increment the last eid counter
            lastEID++;
        }
        // Get the data of the message
        byte[] buffer = evt.getEventData();
        if (buffer != null) {
            // Add its length to the byte counter
            byteCount += buffer.length;
        }
        // If the loglevel permits printing on the screen
        if (logLevel >= 2) {
            // Print the eid
            System.out.println("Event id : " + evt.getEventID());
            // If the loglevel permits printing on the screen
            if (logLevel >= 3) {
                // Print the message tag
                System.out.println("Event tag : " + evt.getEventTag());
                // Print the message data
                System.out.println("Event data : "
                        + new String(evt.getEventData()));
                if (evt.hasAttributes()) {
                    displayEventAttributes(evt.getAttributes());
                }
                nEventProperties prop = evt.getProperties();
                if (prop != null) {
                    displayEventProperties(prop);
                }
            }
        }
    }
    /**
     * Prints the usage message for this class
     */
    private static void Usage() {
        System.out.println("Usage ...\n");
        System.out.println("npopqasync <queue name> [debug] [count] [selector] \n");
        System.out.println("<Required Arguments> \n");
        System.out.println("<queue name> - Queue name parameter for the queue to pop from");
        System.out.println("\n[Optional Arguments] \n");
        System.out.println("[debug] - The level of output from each event, 0 - none, 1 - summary, 2 - EIDs, 3 - All");
        System.out.println("[count] - The number of events to wait before printing out summary information");
        System.out.println("[selector] - The event filter string to use");
        System.out.println("\n\nNote: -? provides help on environment variables \n");
    }
} // End of QSubscriber Class
