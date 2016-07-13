package executable;

import java.awt.Dimension;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import graphElements.Edge;
import graphElements.Node;
import utilities.visualArrangements.Container;
import utilities.visualArrangements.RootContainer;
import utilities.visualArrangements.SubContainer;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {

	// Graph Elements
	GraphLoader rootGraph;

	// Visual Elements
	RootContainer mainCommunity;
	SubContainer subCommunityAsia,subCommunityEuropa;
	VCommunity vMainCommunity, vAsia, vEuropa;

	public Logica(PApplet app) {
		// ***** GRAPHS and SUBGRAPHS*****
		// Root
		String XML_FILE = "../data/graphs/Risk.graphml";
		rootGraph = new GraphLoader(XML_FILE);
		rootGraph.setNodesOutDegree();
		rootGraph.setNodesInDegree();
		// SubCommunities
		DirectedSparseMultigraph<Node, Edge> asia = GraphLoader.filterByCommunity(rootGraph.jungGraph, "AS");
		DirectedSparseMultigraph<Node, Edge> europa = GraphLoader.filterByCommunity(rootGraph.jungGraph, "EU");

		// ***** CONTAINERS *****
		//Container of rootGraph
		mainCommunity = new RootContainer(app, rootGraph.jungGraph, RootContainer.CIRCULAR, new Dimension(250, 250));
		mainCommunity.setName("World");
		subCommunityAsia = new SubContainer(asia,mainCommunity,Container.CIRCULAR,new Dimension(180, 180));
		subCommunityAsia.setName("Asia");
		subCommunityEuropa = new SubContainer(europa,mainCommunity,Container.CIRCULAR,new Dimension(150, 150));
		subCommunityEuropa.setName("Europa");

		// ***** VISUALIZERS *****
		// Main Community
		vMainCommunity = new VCommunity(app, new Node(0), mainCommunity);
		// SubCommunities
		vAsia = new VCommunity(app, new Node(0), subCommunityAsia);
		vEuropa = new VCommunity(app, new Node(0), subCommunityEuropa);
	}

	public void show(PApplet app) {
		vMainCommunity.show();
		vAsia.show();
		vEuropa.show();
	}
}
