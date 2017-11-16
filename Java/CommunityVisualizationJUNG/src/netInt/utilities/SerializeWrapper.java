/*******************************************************************************
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 *  
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

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
