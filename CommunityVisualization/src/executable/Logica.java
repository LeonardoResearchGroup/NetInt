package executable;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Graph;
import graphElements.Node;
import graphElements.SubGraph;
import utilities.Filter;
import utilities.visualArrangements.Container;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {

	// Graph Elements
	Graph rootGraph;
	ArrayList<Graph> graphs;
<<<<<<< HEAD
	Container rootContainer, containerSubGraph1,containerSubGraph2,containerSubGraph3;
	VCommunity vRootCommunity, vSubCommunity1, vSubCommunity2, vSubCommunity3;
	int nA = 500;
	int communities = 3;
=======
	// Visual Elements
	Container rootContainer, containerSubGraphs, containerSubGraph1,
			containerSubGraph2, containerSubGraph3;
	VCommunity vRootCommunity, vSubCommunity, vSubCommunity1, vSubCommunity2,
			vSubCommunity3;
	// RootGraph Parameters
	int nA = 15;
	int communities = 1;
>>>>>>> eb5c47cbcc6759275d776a65c50bc5511df3425c

	public Logica(PApplet app) {

		// RootGraph
		rootGraph = randomGraphFactory(nA);
		rootGraph.setID(0);
		rootGraph.printEdges();

		// Collection of graph and subGraphs
		graphs = new ArrayList<Graph>();
		graphs.add(rootGraph);
		for (int i = 1; i <= communities; i++) {
			SubGraph tmp = new SubGraph();
			tmp.setID(i);
			tmp.setNodesFromGraph(rootGraph, i);
			tmp.printEdges();
			graphs.add(tmp);
		}

		// Container of visual rootGraph
		rootContainer = new Container(app, rootGraph);
		// Other containers
		containerSubGraph1 = new Container(app, (SubGraph) graphs.get(1));
		containerSubGraph1.setRootGraph(rootGraph);
		containerSubGraph1.retrieveVisualElements(rootContainer);
		
		// Instantiating & visualizing community
		vRootCommunity = new VCommunity(app, rootContainer, app.width / 2, 150,100);
		vSubCommunity1 = new VCommunity(app, containerSubGraph1, 200, 350, 100);

	}

	public void show(PApplet app) {
		vRootCommunity.show();
		vSubCommunity1.show();
	}

	// Factories
	private Graph randomGraphFactory(int graphSize) {
		Graph rtn = new Graph();
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Node> nodes = new ArrayList<Node>();

		// create nodes
		for (int i = 0; i < graphSize; i++) {
			Node tmpA = new Node(i);
			nodes.add(tmpA);
		}

		// include node in communities
		for (int i = 0; i < graphSize; i++) {
			Node tmp = nodes.get((int) (Math.random() * graphSize));
			int com = (int) Math.round(Math.random() * communities);
			if (!tmp.belongsTo(com))
				tmp.includeInSubGraph(com);
		}

		// create edges
		edges = randomEdgeFactory(nodes);

		// Making the graph
		rtn = new Graph(nodes, edges);
		return rtn;
	}

	/**
	 * This class returns a list of edges with source and target chosen randomly
	 * according to a distribution filter (radial, sinusoidal, sigmoid, linear).
	 * The idea of the filter is to skew the distribution of source and target
	 * nodes so it is possible to emulate social phenomena distribution of links
	 * 
	 * @param nodes
	 * @return
	 */
	private ArrayList<Edge> randomEdgeFactory(ArrayList<Node> nodes) {
		Filter filter = new Filter(0, 1);
		ArrayList<Edge> rtn = new ArrayList<Edge>();
		// Gets a random integer using a filter. In this case I use a
		// sinusoidal to emulate a social phenomena
		for (int i = 0; i < nodes.size(); i++) {
			// source
			float val = filter.sinusoidal((float) Math.random());
			int src = PApplet.floor((val * (nodes.size() - 1)));
			// target
			val = filter.sinusoidal((float) Math.random());
			int trg = PApplet.floor((val * (nodes.size() - 1)));
			// edge
			Edge tmp = new Edge(nodes.get(src), nodes.get(trg), true);
			rtn.add(tmp);
		}
		return rtn;
	}

}
