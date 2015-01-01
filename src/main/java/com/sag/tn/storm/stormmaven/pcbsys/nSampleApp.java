/*
    Copyright 2012 Software AG, Darmstadt, Germany and/or Software AG USA
  Inc., Reston, United States of America, and/or their licensors.
  In the event that you should download or otherwise use this software
  you hereby acknowledge and agree to the terms at
  http://um.terracotta.org/company/terms.html#legalnotices
 */
package com.sag.tn.storm.stormmaven.pcbsys;
import com.pcbsys.foundation.utils.fEnvironment;
import com.pcbsys.nirvana.client.*;
import java.util.Date;
import java.util.StringTokenizer;
/**
 * Base class that contains standard functionality for a nirvana sample app
 */
public abstract class nSampleApp implements nReconnectHandler, nAsyncExceptionListener{
  protected nSession mySession = null;
  protected nSessionAttributes nsa = null;
  private String myLastSessionID;
  protected nDataStream myStream = null;
  protected static void processEnvironmentVariable(String variable){
    String laxVAR=System.getProperty("lax.nl.env."+variable);
    if (laxVAR!=null) System.getProperties().put(variable,laxVAR);
  }
  
  protected static void processEnvironmentVariables(){
    //Process Environment Variables
    processEnvironmentVariable("RNAME");
    processEnvironmentVariable("LOGLEVEL");
    processEnvironmentVariable("HPROXY");
    processEnvironmentVariable("HAUTH");
    processEnvironmentVariable("CKEYSTORE");
    processEnvironmentVariable("CKEYSTOREPASSWD");
    processEnvironmentVariable("CAKEYSTORE");
    processEnvironmentVariable("CAKEYSTOREPASSWD");
    // Install any proxy server settings
    fEnvironment.setProxyEnvironments();
    // Install any ssl settings
    fEnvironment.setSSLEnvironments();
  }
  
  
  /**
   * This method processes a string consisting of one or more comma separated
   * RNAME values and splits them into a a String[]
   *
   * @param realmdetails The RNAME of the Universal Messaging realm
   */
  protected static String[] parseRealmProperties(String realmdetails){
    String[] rproperties = new String[4];
    int idx = 0;
    String RNAME=null;
    StringTokenizer st=new StringTokenizer(realmdetails,",");
    while (st.hasMoreTokens()){
      String someRNAME=(String)st.nextToken();
      rproperties[idx] = someRNAME;
      idx++;
    }
    //Trim the array
    String[] rpropertiesTrimmed=new String[idx];
    System.arraycopy(rproperties,0,rpropertiesTrimmed,0,idx);
    return rpropertiesTrimmed;
  }
  /**
   * This method demonstrates the Universal Messaging API calls necessary to construct a
   * nirvana session
   *
   * @param realmDetails a String[] containing the possible RNAME values
   */
  protected void constructSession( String[] realmDetails) {
    constructSession(realmDetails, null);
  }
  /**
   * This method demonstrates the Universal Messaging API calls necessary to construct a
   * nirvana session
   *
   * @param realmDetails a String[] containing the possible RNAME values
   */
  protected void constructSession( String[] realmDetails, nDataStreamListener listener) {
    //Create a realm session attributes object from the array of strings
    try {
      nsa = new nSessionAttributes(realmDetails, 2);
    }
    catch (Exception ex) {
      System.out.println(
        "Error creating Session Attributes. Please check your RNAME");
      System.exit(1);
    }
    //Add this class as an asynchronous exception listener
    try {
      //Create a session object from the session attributes object, passing this
      //as a reconnect handler class (optional). This will ensure that the reconnection
      // methods will get called by the API.
      mySession = nSessionFactory.create(nsa, this);
      mySession.addAsyncExceptionListener(this);
      mySession.enableThreading(4);
    }
    catch (nIllegalArgumentException ex) {}
    //Initialise the Universal Messaging session. This physically opens the connection to the
    //Universal Messaging realm, using the specified protocols. If multiple interfaces are supported
    //these will be attempted in weight order (SSL, HTTPS, socket, HTTP).
    try {
      if (listener == null) {
        mySession.init();
      } else {
        myStream = mySession.init(listener);
      }
      myLastSessionID=mySession.getId();
    }
    //Handle errors
    catch(nSecurityException sec){
      System.out.println("The current user is not authorised to connect to the specified Realm Server");
      System.out.println("Please check the realm acls or contact support");
      sec.printStackTrace();
      System.exit(1);
    }
    catch (nRealmUnreachableException rue) {
      System.out.println(
        "The Universal Messaging Realm specified by the RNAME value is not reachable.");
      System.out.println(
        "Please ensure the Realm is running and check your RNAME value.");
      rue.printStackTrace();
      System.exit(1);
    }
    catch (nSessionNotConnectedException snce) {
      System.out.println(
        "The session object used is not physically connected to the Universal Messaging Realm.");
      System.out.println(
        "Please ensure the Realm is up and check your RNAME value.");
      snce.printStackTrace();
      System.exit(1);
    }
    catch (nSessionAlreadyInitialisedException ex) {
      System.out.println("The session object has already been initialised.");
      System.out.println("Please make only one call to the .init() function.");
      ex.printStackTrace();
      System.exit(1);
    }
  }
  /**
   * A callback is received by the API to this method to notify the user of a disconnection
   * from the realm. The method is enforced by the nReconnectHandler interface but is normally optional.
   * It gives the user a chance to log the disconnection or do something else about it.
   *
   * @param anSession The Universal Messaging session being disconnected
   */
  public void disconnected(nSession anSession){
    try {
      System.out.println( "You have been disconnected from "+myLastSessionID);
    }
    catch (Exception ex) {
      System.out.println("Error while disconnecting "+ex.getMessage());
    }
  }
  /**
   * A callback is received by the API to this method to notify the user of a successful reconnection
   * to the realm. The method is enforced by the nReconnectHandler interface but is normally optional.
   * It gives the user a chance to log the reconnection or do something else about it.
   *
   * @param anSession The Universal Messaging session being reconnected
   */
  public void reconnected(nSession anSession){
    try {
      myLastSessionID=mySession.getId();
      System.out.println ("You have been Reconnected to "+myLastSessionID);
    }
    catch (Exception ex) {
      System.out.println ("Error while reconnecting "+ ex.getMessage());
    }
  }
  /**
   * A callback is received by the API to this method to notify the user that the API is about
   * to attempt reconnecting to the realm. The method is enforced by the nReconnectHandler
   * interface but is normally optional. It allows the user to decide whether further
   * attempts are required or not, whether custom delays should be enforced etc.
   *
   * @param anSession The Universal Messaging session that will be used to reconnect
   */
  public boolean tryAgain(nSession anSession){
    try {
      System.out.println ("Attempting to reconnect to "+System.getProperties().get("RNAME"));
    }
    catch (Exception ex) {
      System.out.println ("Error while trying to reconnect "+ ex.getMessage());
    }
    return true;
  }
  /**
   * A callback is received by the API to this method to notify the user that the an
   * asynchronous exception (in a thread different than the current one) has occured.
   *
   * @param ex The asynchronous exception that was thrown
   */
  public void handleException(nBaseClientException ex){
    System.out.println("An Asynchronous Exception was received from the Universal Messaging realm.");
    ex.printStackTrace();
  }
  protected static void UsageEnv() {
    System.out.println(
      "\n\n(Environment Variables) \n");
    System.out.println(
      "(RNAME) - One or more RNAME entries in the form protocol://host:port" );
    System.out.println(
      "   protocol - Can be one of nsp, nhp, nsps, or nhps, where:" );
    System.out.println(
      "   nsp - Specifies Universal Messaging Socket Protocol (nsp)" );
    System.out.println(
      "   nhp - Specifies Universal Messaging HTTP Protocol (nhp)" );
    System.out.println(
      "   nsps - Specifies Universal Messaging Socket Protocol Secure (nsps), i.e. using SSL/TLS" );
    System.out.println(
      "   nhps - Specifies Universal Messaging HTTP Protocol Secure (nhps), i.e. using SSL/TLS" );
    System.out.println(
      "   port - The port number of the server" );
    System.out.println(
      "\nHint: - For multiple RNAME entries, use comma separated values which will be attempted in connection weight order\n" );
    System.out.println(
      "(LOGLEVEL) - This determines how much information the nirvana api will output 0 = verbose 7 = quiet\n" );
    System.out.println(
      "(CKEYSTORE) - If using SSL, the location of the keystore containing the client cert\n");
    System.out.println(
      "(CKEYSTOREPASSWD) - If using SSL, the password for the keystore containing the client cert\n");
    System.out.println(
      "(CAKEYSTORE) - If using SSL, the location of the ca truststore\n");
    System.out.println(
      "(CAKEYSTOREPASSWD) - If using SSL, the password for the ca truststore\n");
    System.out.println(
      "(HPROXY) - HTTP Proxy details in the form proxyhost:proxyport, where:" );
    System.out.println(
      "   proxyhost - The HTTP proxy host" );
    System.out.println(
      "   proxyport - The HTTP proxy port\n" );
    System.out.println(
      "(HAUTH) - HTTP Proxy authentication details in the form user:pass, where:" );
    System.out.println(
      "   user - The HTTP proxy authentication username" );
    System.out.println(
      "   pass - The HTTP proxy authentication password\n" );
    System.exit(1);
  }
  protected void displayEventProperties(nEventProperties prop){
    System.out.println("----------------------------------------------------------------");
    displayEventProperties(prop, 0);
    System.out.println("----------------------------------------------------------------");
  }
  protected void displayEventProperties(nEventProperties prop, int level){
    String tab = "";
    for(int x=0;x<level;x++){
      tab += "\t";
    }
    System.out.println(tab+"Event Prop : ");
    nEventPropertiesIterator keys = prop.getKeyIterator();
    while(keys.hasNext()){
      Object key = keys.next();
      Object value = prop.get(key.toString());
      if (value instanceof nEventProperties){
        nEventProperties pvalue=(nEventProperties)value;
        System.out.println(tab+"["+key +"(event prop)]:");
        displayEventProperties(pvalue, level+1);
      }
      else if (value instanceof nEventProperties[]){
        nEventProperties[] pvalue=(nEventProperties[])value;
        System.out.println(tab+"["+key +"(event prop[])]:");
        for (int x = 0; x < pvalue.length; x++) {
          displayEventProperties(pvalue[x], level+1);
        }
      } else if (value instanceof String[]){
        String[] pvalue = (String[])value;
        System.out.println(tab+"["+key +"(String[])]:");
        for (int x = 0; x < pvalue.length; x++) {
          System.out.println("   ["+key+"]:["+x+"]="+pvalue[x]);
        }
      } else if (value instanceof long[]){
        long[] pvalue = (long[])value;
        System.out.println(tab+"["+key +"(long[])]:");
        for (int x = 0; x < pvalue.length; x++) {
          System.out.println("   ["+key+"]:["+x+"]="+pvalue[x]);
        }
      } else if (value instanceof int[]){
        int[] pvalue = (int[])value;
        System.out.println(tab+"["+key +"(int[])]:");
        for (int x = 0; x < pvalue.length; x++) {
          System.out.println(tab+"   ["+key+"]:["+x+"]="+pvalue[x]);
        }
      } else if (value instanceof byte[]){
        byte[] pvalue = (byte[])value;
        System.out.println(tab+"["+key +"(byte[])]:");
        for (int x = 0; x < pvalue.length; x++) {
          System.out.println(tab+"   ["+key+"]:["+x+"]="+pvalue[x]);
        }
      } else if (value instanceof boolean[]){
        boolean[] pvalue = (boolean[])value;
        System.out.println(tab+"["+key +"(boolean[])]:");
        for (int x = 0; x < pvalue.length; x++) {
          System.out.println(tab+"   ["+key+"]:["+x+"]="+pvalue[x]);
        }
      } else {
        System.out.println(tab+key.toString()+" = "+value.toString());
      }
    }
  }
  protected void displayEventAttributes(nEventAttributes attributes){
    System.out.println("Merge Allowed : "+attributes.allowMerging());
    System.out.println("Message Type  : "+attributes.getMessageType());
    System.out.println("Delivery mode : "+attributes.getDeliveryMode());
    System.out.println("Priority      : "+attributes.getDeliveryMode());
    if(attributes.getApplicationId() != null)
      System.out.println("Application Id : "+new String(attributes.getApplicationId()));
    if(attributes.getCorrelationId() != null)
      System.out.println("Correlation Id : "+new String(attributes.getCorrelationId()));
    if(attributes.getMessageId() != null)
      System.out.println("Message Id     : "+new String(attributes.getMessageId()));
    if(attributes.getPublisherHost() != null)
      System.out.println("Published from : "+new String(attributes.getPublisherHost()));
    if(attributes.getPublisherName() != null)
      System.out.println("Published by   : "+new String(attributes.getPublisherName()));
    if(attributes.getTimestamp() != 0)
      System.out.println("Published on   : "+new Date(attributes.getTimestamp()).toString());
    if(attributes.getReplyToName() != null)
      System.out.println("Reply To       : "+new String(attributes.getReplyToName()));
    if(attributes.getExpiration() != 0)
      System.out.println("Expires on     : "+attributes.getExpiration());
    if(attributes.isRedelivered())
      System.out.println("Redelivered event : "+attributes.getRedeliveredCount());
  }
} // End of subscriber Class
