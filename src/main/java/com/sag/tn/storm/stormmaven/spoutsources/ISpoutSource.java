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
import java.util.List;

import com.ximpleware.VTDNav;

public interface ISpoutSource {
	/* If only 1 doc is to be pushed wrap it also in a list */
	public List<VTDNav> fetch() throws IOException;
	public void close() throws Exception;
}
