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

	Graph rootGraph;
	ArrayList<SubGraph> subGraphs;
	Container containerA, containerB;
	VCommunity communityA, communityB;
	int nA = 10;
	int comNumber = 3;

	public Logica(PApplet app) {

		rootGraph = randomGraphFactory(nA);
		subGraphs = new ArrayList<SubGraph>();

		for (int i = 0; i <= comNumber; i++) {
			SubGraph tmp = new SubGraph();
			tmp.setNodesFrom(rootGraph, i);
			subGraphs.add(tmp);
		}

		// Container of visual graph the graph
		containerA = new Container(app, rootGraph);
		containerB= new Container(app, subGraphs.get(1));
		

		// instantiating & visualizing community
		communityA = new VCommunity(app, containerA, app.width / 2, 150, 100);
		//communityB = new VCommunity(app, containerB, app.width / 2, 350, 100);
	}

	public void show(PApplet app) {
		communityA.show();
		//communityB.show();
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
			int com = (int) Math.round(Math.random() * comNumber);
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
	 * This class makes a set of edges distributed according to a filter
	 * (radial, sinusoidal, sigmoid, linear)
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
