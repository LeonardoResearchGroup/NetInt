package executable;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Graph;
import graphElements.Node;
import visualElements.VCommunity;
import processing.core.*;
import visualElements.Arrangement;

public class Logica {
	ArrayList<Edge> edgesA, edgesB;
	ArrayList<Node> verticesA, verticesB;
	Graph graphA, graphB;
	Arrangement vNetA, vNetB;
	VCommunity communityA, communityB;
	int nA = 100;
	int nB = 10;

	public Logica(PApplet app) {
		verticesA = new ArrayList<Node>();
		edgesA = new ArrayList<Edge>();
		verticesB = new ArrayList<Node>();
		edgesB = new ArrayList<Edge>();

		// create nodes
		for (int i = 0; i < nA; i++) {
			Node tmpA = new Node(i);
			verticesA.add(tmpA);
		}
		for (int i = 0; i < nB; i++) {
			Node tmpB = new Node(i);
			verticesB.add(tmpB);
		}
		// create edges
		for (int i = 0; i < verticesA.size(); i++) {
			int src = (int) (Math.random()* verticesA.size());
			int trg = (int) (Math.random()* verticesA.size());
			Edge tmp = new Edge(verticesA.get(src), verticesA.get(trg), true);
			edgesA.add(tmp);
		}
		for (int i = 0; i < verticesB.size(); i++) {
			int src = (int) (Math.random()* verticesB.size());
			int trg = (int) (Math.random()* verticesB.size());
			Edge tmp = new Edge(verticesB.get(src), verticesB.get(trg), true);
			edgesB.add(tmp);
		}

		// Making the graph
		graphA = new Graph(verticesA, edgesA);
		graphB = new Graph(verticesB, edgesB);
		
		// Set Degree
		graphA.setDegree();
		graphB.setDegree();

		// Visualizing the graph
		vNetA = new Arrangement(app, graphA);
		vNetB = new Arrangement(app, graphB);
		//network.sortOutDegree();
		//network.linearLayout(app, new PVector(100, 100), new PVector(400,100));
		
		// instantiating & visualizing community
		communityA = new VCommunity(app, vNetA, app.width/2, 150,100);
		communityB = new VCommunity(app, vNetB, app.width/2, 400,100);
		
	}

	public void show(PApplet app) {
		//network.show(app, true, true);
		communityA.show();
		communityB.show();
	}
}
