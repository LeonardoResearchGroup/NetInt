package utilities;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Graph;
import graphElements.Node;
import processing.core.PApplet;

public class RandomGraphFactory {

	public RandomGraphFactory() {

	}

	// Factories
	public Graph makeRandomGraph(int graphSize, int communities, String filterName) {
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
		edges = randomEdgeFactory(nodes, filterName);

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
	private ArrayList<Edge> randomEdgeFactory(ArrayList<Node> nodes, String filterName) {
		Filter filter = new Filter(0, 1);
		ArrayList<Edge> rtn = new ArrayList<Edge>();
		// Gets a random integer using a filter
		for (int i = 0; i < nodes.size(); i++) {
			// source
			float val = filter.getRandomValue(filterName);
			int src = PApplet.floor((val * (nodes.size() - 1)));
			// target
			val = filter.getRandomValue(filterName);
			int trg = PApplet.floor((val * (nodes.size() - 1)));
			// edge
			Edge tmp = new Edge(nodes.get(src), nodes.get(trg), true);
			rtn.add(tmp);
		}
		return rtn;
	}
}
