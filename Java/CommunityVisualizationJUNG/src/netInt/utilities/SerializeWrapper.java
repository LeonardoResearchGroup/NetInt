/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 *
 * It makes extensive use of free libraries such as Processing, Jung, ControlP5, JOGL, 
 * Tinkerpop and many others. For details see the copyrights folder. 
 *
 * Contributors:
 * 	Juan Salamanca, Cesar Loaiza, Luis Felipe Rivera, Javier Diaz
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *
 * version alpha
 *******************************************************************************/
package netInt.utilities;

import java.io.Serializable;
import java.util.ArrayList;

import edu.uci.ics.jung.graph.Graph;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.visualElements.VCommunity;
import netInt.visualElements.gui.UserSettings;

@SuppressWarnings("serial")
public class SerializeWrapper implements Serializable{

	private VCommunity secondOrderCommunities;
	private ArrayList<VCommunity> firstOrderCommunities;
	private UserSettings  vSettings;
	private Graph<Node, Edge> theGraph;
	

	public SerializeWrapper(VCommunity vSubSubCommunity, ArrayList<VCommunity> vSubCommunities, UserSettings  vSettings, Graph<Node,Edge> theGraph) {
		super();
		this.secondOrderCommunities = vSubSubCommunity;
		this.firstOrderCommunities = vSubCommunities;
		this.vSettings = vSettings;
		this.theGraph = theGraph;
	}


	public VCommunity getvSubSubCommunity() {
		return secondOrderCommunities;
	}
	
	public Graph <Node,Edge> getTheGraph(){
		return theGraph;
	}


	public void setvSubSubCommunity(VCommunity vSubSubCommunity) {
		this.secondOrderCommunities = vSubSubCommunity;
	}


	public ArrayList<VCommunity> getvSubCommunities() {
		return firstOrderCommunities;
	}


	public void setvSubCommunities(ArrayList<VCommunity> vSubCommunities) {
		this.firstOrderCommunities = vSubCommunities;
	}


	public UserSettings getvSettings() {
		return vSettings;
	}


	public void setvSettings(UserSettings vSettings) {
		this.vSettings = vSettings;
	}

	
	
}
