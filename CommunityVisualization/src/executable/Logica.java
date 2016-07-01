package executable;

import java.util.ArrayList;
import comparators.InDegreeComparator;
import comparators.OutDegreeComparator;
import graphElements.Graph;
import graphElements.SubGraph;
import gui.GUI;
import utilities.GraphReader;
import utilities.RandomGraphFactory;
import utilities.visualArrangements.Container;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {

	// Graph Elements
	Graph rootGraph;
	ArrayList<Graph> graphs;
	RandomGraphFactory randomFactory;

	// Visual Elements
	Container rootContainer;
	VCommunity vRootCommunity;
	ArrayList<Container> containers;
	ArrayList<VCommunity> vCommunities;

	// RootGraph Parameters
	int nA = 1000;
	int communities = 5;

	// GUI
	GUI gui;

	public Logica(PApplet app) {
		// *** randomGraph. This is to create a random graph easily
		//makeRandomGraph(nA, communities);

		// *** Initialization GraphReader
		String XML_FILE = "../data/L-UN-MOV.graphml";
		//String XML_FILE = "../data/Risk.graphml";
		GraphReader gr = new GraphReader(XML_FILE);

		// *** Initialization Collections
		graphs = new ArrayList<Graph>();
		containers = new ArrayList<Container>();
		vCommunities = new ArrayList<VCommunity>();

		// ***** RootGraph *****
		rootGraph = gr.getGraphJuan();
		rootGraph.setID(0);

		// Container of visual rootGraph
		rootContainer = new Container(app, rootGraph);
		rootContainer.sort(new OutDegreeComparator());
		// rootContainer.sort(new InDegreeComparator());
		rootContainer.updateContainer();

		// Add to collections
		graphs.add(rootGraph); // always at position 0
		containers.add(rootContainer);

		// Instantiating & root visual community
		vRootCommunity = new VCommunity(app, rootContainer, app.width / 2, 150);

		// ***** SubGraphs *****
		for (int i = 1; i <= communities; i++) {
			// SubGraph instantiation
			SubGraph tmp = new SubGraph();
			tmp.setID(i);
			tmp.setNodesFromGraph(rootGraph, i);
			graphs.add(tmp);

			// SubGraph Containers
			Container cTmp = new Container(app, (SubGraph) graphs.get(i));
			cTmp.setRootGraph(rootGraph);
			// cTmp.sort(new InDegreeComparator());
			cTmp.retrieveVisualElements(rootContainer);
			containers.add(cTmp);

			// SubGraph Communities
			VCommunity vComTmp = new VCommunity(app, cTmp, 200 + (i - 1) * 200, 350);
			vCommunities.add(vComTmp);
		}

		// GUI
		gui = new GUI(app);
		gui.infoBox.setBox(100, 20, 300, 50);
		gui.infoBox.addContent(rootGraph.getBasicStats());
	}

	public void show(PApplet app) {
		vRootCommunity.show();
		for (VCommunity vCom : vCommunities) {
			vCom.show();
		}
		gui.show();
	}

	public void makeRandomGraph(int size, int communities) {
		randomFactory = new RandomGraphFactory();
		rootGraph = randomFactory.makeRandomGraph(size, communities,"radial");
	}

}
