package executable;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Graph;
import graphElements.Node;
import graphElements.SubGraph;
import utilities.Filter;
import utilities.GraphReader;
import utilities.visualArrangements.Container;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {

	Graph rootGraph;
	ArrayList<Graph> graphs;
	Container rootContainer, containerSubGraph1,containerSubGraph2,containerSubGraph3;
	VCommunity vRootCommunity, vSubCommunity1, vSubCommunity2, vSubCommunity3;
	int nA = 1000;
	int communities = 3;

	public Logica(PApplet app) {

		//rootGraph = randomGraphFactory(nA);
		String XML_FILE = "/home/cdloaiza/Dropbox/Docs Icesi/CAOBA/Normalizacion/sectores/L-UN-MOV.graphml";  
		GraphReader gr = new GraphReader(XML_FILE);
		rootGraph = gr.getGraph();
		rootGraph.setID(0);
		graphs = new ArrayList<Graph>();
		graphs.add(rootGraph);

		for (int i = 1; i <= communities; i++) {
			SubGraph tmp = new SubGraph();
			tmp.setID(i);
			tmp.setNodesFromGraph(rootGraph, i);
			graphs.add(tmp);
		}

		// Container of visual graph the graph
		rootContainer = new Container(app, rootGraph);
		/*
		containerSubGraph1 = new Container(app, graphs.get(1));
		containerSubGraph1.retrieveVElements(rootContainer);
		containerSubGraph2 = new Container(app, graphs.get(2));
		containerSubGraph2.retrieveVElements(rootContainer);
		containerSubGraph3 = new Container(app, graphs.get(3));
		containerSubGraph3.retrieveVElements(rootContainer);
		*/
		// instantiating & visualizing community
		vRootCommunity = new VCommunity(app, rootContainer, app.width / 2, 150, 100);
		/*
		vSubCommunity1 = new VCommunity(app, containerSubGraph1, 200, 350, 100);
		vSubCommunity2 = new VCommunity(app, containerSubGraph2, app.width / 2, 350, 100);
		vSubCommunity3 = new VCommunity(app, containerSubGraph3, app.width -200, 350, 100);
		*/
	}

	public void show(PApplet app) {
		vRootCommunity.show();
		/*
		vSubCommunity1.show();
		vSubCommunity2.show();
		vSubCommunity3.show();
		*/
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
