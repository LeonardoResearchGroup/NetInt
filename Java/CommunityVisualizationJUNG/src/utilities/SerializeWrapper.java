package utilities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import visualElements.VCommunity;
import visualElements.gui.UserSettings;

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
