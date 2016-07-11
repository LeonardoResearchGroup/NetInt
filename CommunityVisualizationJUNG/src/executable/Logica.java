package executable;

import java.awt.Dimension;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import graphElements.Edge;
import graphElements.Node;
import utilities.visualArrangements.Container;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {

	// Graph Elements
	GraphLoader rootGraph;

	// Visual Elements
	Container rootContainer, subGraphContainer;
	VCommunity vRootCommunity, subGraphCommunity;

	public Logica(PApplet app) {
		// ***** RootGraph *****
		String XML_FILE = "../data/graphs/Risk.graphml";
		rootGraph = new GraphLoader(XML_FILE);
		rootGraph.setNodesOutDegree();
		rootGraph.setNodesInDegree();

		// Container of visual rootGraph
		rootContainer = new Container(app, rootGraph.jungGraph, Container.CIRCULAR, new Dimension(250, 250));
		rootContainer.setName("Root");

		// Instantiating & root visualization
		vRootCommunity = new VCommunity(app, new Node(0), rootContainer);

		// ***** Community *****
		DirectedSparseMultigraph<Node, Edge> community = GraphLoader.filterByCommunity(rootGraph.jungGraph, "AS");
		subGraphContainer = new Container(app, community, Container.SPRING, new Dimension(250, 150));
		subGraphContainer.setName("AU");
		subGraphCommunity = new VCommunity(app, new Node(0), subGraphContainer);
	}

	public void show(PApplet app) {
		vRootCommunity.show();
		subGraphCommunity.show();
	}
}
