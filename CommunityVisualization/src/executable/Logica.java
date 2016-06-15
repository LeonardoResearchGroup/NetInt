package executable;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Graph;
import graphElements.Node;
import visualElements.VCommunity;
import processing.core.*;
import visualElements.VNetwork;

public class Logica {
	ArrayList<Edge> edges;
	ArrayList<Node> vertices;
	Graph graph;
	VNetwork network;
	VCommunity community;
	int n = 100;

	public Logica(PApplet app) {
		vertices = new ArrayList<Node>();
		edges = new ArrayList<Edge>();

		// create nodes
		for (int i = 0; i < n; i++) {
			Node tmp = new Node(i);
			vertices.add(tmp);
		}
		// create edges
		for (int i = 1; i < vertices.size() * 2; i++) {
			int src = (int) (Math.random()* vertices.size());
			int trg = (int) (Math.random()* vertices.size());
			Edge tmp = new Edge(vertices.get(src), vertices.get(trg), true);
			edges.add(tmp);
		}

		// Making the graph
		graph = new Graph(vertices, edges);
		
		// Set Degree
		graph.setDegree();

		// Visualizing the graph
		network = new VNetwork(graph);
		
		// instantiating & visualizing community
		community = new VCommunity(app, network, 450, 250,100);
		
		// Creating visual edges
		community.vNet.sortInDegree();
		community.vNet.sortOutDegree();
	//	community.vNet.linearLayout(app, community., community.);
		//network.circularLayout(app, new PVector (app.width/2,app.height/2), 200);
	}

	public void show(PApplet app) {
		//network.show(app, true, true);
		community.show();
	}
}
