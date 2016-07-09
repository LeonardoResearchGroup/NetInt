package executable;

import java.util.ArrayList;
import java.util.Iterator;

import comparators.InDegreeComparator;
import comparators.OutDegreeComparator;
import graphElements.Graph;
import graphElements.Node;
import graphElements.SubGraph;
import gui.GUI;
import utilities.GraphReader;
import utilities.RandomGraphFactory;
import utilities.visualArrangements.Container;
import visualElements.VCommunity;
import visualElements.interactive.VisualAtom;
import processing.core.*;

public class Logica {

	// Graph Elements
	Graph rootGraph;
	ArrayList<Graph> graphs;
	RandomGraphFactory randomFactory;

	// Visual Elements
	Container rootContainer;
	VCommunity vRootCommunity;
	ArrayList<VCommunity> vCommunities;

	// Elements for visualization of community set
	Graph communitiesInGraph;
	Container containerOfCommunities;
	VCommunity vCommunitySet;
	int newNodeCounter = 0;

	// RootGraph Parameters
	int nA = 1000;
	int communityNumber = 5;

	// GUI
	GUI gui;

	public Logica(PApplet app) {
		// *** randomGraph. This is to create a random graph easily
		// makeRandomGraph(nA, communities);

		// *** Initialization GraphReader
		String XML_FILE = "../data/L-UN-MOV.graphml";
		GraphReader gr = new GraphReader(XML_FILE);

		// ***** RootGraph *****
		rootGraph = gr.getGraphJuan();
		rootGraph.setID(0);

		// Container of visual rootGraph
		rootContainer = new Container(app, rootGraph);
		rootContainer.sort(new OutDegreeComparator());
		// rootContainer.sort(new InDegreeComparator());
		rootContainer.updateContainer();

		// Instantiating & root visual community
		Node nodeTmp = new Node(newNodeCounter);
		vRootCommunity = new VCommunity(app, nodeTmp, rootContainer, app.width / 2, 150);

		/// ****
		// For visualization of community set. Make the graph that contains the
		// communities
		communitiesInGraph = new Graph();
		// Make container of communities
		containerOfCommunities = new Container(app, communitiesInGraph);
		// Fill the Container with the vAtoms. Nodes are extracted from each
		// vCommunity and added to the container's graph

		// Make Visual Community of containerOfCommunities
		vCommunitySet = new VCommunity(vRootCommunity, containerOfCommunities);

		// // ***** SubGraphs for communities *****
		for (int i = 1; i <= communityNumber; i++) {
			// SubGraph instantiation
			SubGraph tmp = new SubGraph();
			tmp.setID(i);
			tmp.setNodesFromGraph(rootGraph, i);

			// SubGraph Containers
			Container cTmp = new Container(app, tmp);
			cTmp.retrieveVisualElements(rootContainer);

			// SubGraph VCommunities
			nodeTmp = new Node(newNodeCounter++);
			VCommunity vComTmp = new VCommunity(app, nodeTmp, cTmp, 200 + (i - 1) * 200, 350);
			containerOfCommunities.setVNode(vComTmp);
		}

		vCommunitySet.layoutContainer(vCommunitySet.pos, "linear", 200);

		for (int i = 0; i < containerOfCommunities.getVAtoms().size(); i++) {
			VCommunity tmp = (VCommunity) containerOfCommunities.getVAtoms().get(i);
			tmp.layoutContainer(vCommunitySet.container.getVAtoms().get(i).pos, "circular", 50);
		}

		// VCommunity tmp2 = (VCommunity)
		// containerOfCommunities.getVAtoms().get(0);
		// tmp2.container.circularArrangement(new PVector (500,350), 50);

		// GUI
		gui = new GUI(app);
		gui.infoBox.setBox(100, 20, 300, 50);
		gui.infoBox.addContent(rootGraph.getBasicStats());
	}

	public void show(PApplet app) {
		vRootCommunity.show();
//		 for (VCommunity vCom : vCommunities) {
//		 vCom.show();
//		 }
		vCommunitySet.show();
		gui.show();
	}

	public void makeRandomGraph(int size, int communities) {
		randomFactory = new RandomGraphFactory();
		rootGraph = randomFactory.makeRandomGraph(size, communities, "radial");
	}

}
