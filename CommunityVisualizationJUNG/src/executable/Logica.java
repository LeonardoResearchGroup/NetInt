package executable;

import java.awt.Dimension;

import graphElements.Node;
import utilities.visualArrangements.Container;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {

	// Graph Elements
	GraphLoader rootGraph;

	// Visual Elements
	Container rootContainer;
	VCommunity vRootCommunity;

	public Logica(PApplet app) {
		// ***** RootGraph *****
		String XML_FILE = "../data/graphs/Risk.graphml";
		rootGraph = new GraphLoader(XML_FILE);
		rootGraph.setNodesOutDegree();
		rootGraph.setNodesInDegree();

		// Container of visual rootGraph
		rootContainer = new Container(app, rootGraph.jungGraph, 0 ,new Dimension(app.width/2, app.height/2));
		rootContainer.setName("Root");

		// Instantiating & root visual community
		Node nodeTmp = new Node(0);
		vRootCommunity = new VCommunity(app, nodeTmp, rootContainer);
	}

	public void show(PApplet app) {
		vRootCommunity.show();
	}
}
