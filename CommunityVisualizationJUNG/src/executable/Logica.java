package executable;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import containers.Container;
import containers.RootContainer;
import containers.SubContainer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import graphElements.Edge;
import graphElements.Node;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {
	// Interaction elements
	public PVector mousePos;

	// Graph Elements
	private GraphLoader rootGraph;
	private ArrayList<DirectedSparseMultigraph<Node, Edge>> subGraphs;

	// Visual Elements
	private ArrayList<SubContainer> containers;
	private ArrayList<VCommunity> vCommunities;
	private VCommunity vMainCommunity;

	public Logica(PApplet app) {
		String XML_FILE = "../data/graphs/MuestraCompletaLouvain.graphml";

		// ***** ROOT GRAPH*****
		rootGraph = new GraphLoader(XML_FILE, "comunidad", "name");
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(app, rootGraph.jungGraph, RootContainer.CIRCULAR,
				new Dimension(250, 250));
		mainCommunity.setName("Root");
		// Root Community
		vMainCommunity = new VCommunity(app, new Node(0), mainCommunity);

		containers = new ArrayList<SubContainer>();
		vCommunities = new ArrayList<VCommunity>();
		// ***** SUBGRAPHS & CONTAINERS *****
		subGraphs = new ArrayList<DirectedSparseMultigraph<Node, Edge>>();
		int cont = 0;
		for (String communityName : rootGraph.getCommunityNames()) {
			// SubGraphs
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByCommunity(rootGraph.jungGraph,
					communityName);
			// SubContainers
			SubContainer containerTemp = new SubContainer(graphTemp, mainCommunity, Container.FRUCHTERMAN_REINGOLD,
					new Dimension(300 + (cont * 30), 300 + (cont * 30)));
			containerTemp.setName(communityName);
			// Visualizers
			VCommunity communityTemp = new VCommunity(app, new Node(0), containerTemp);
			subGraphs.add(graphTemp);
			containers.add(containerTemp);
			vCommunities.add(communityTemp);
			cont++;
		}
	}

	private void tracePropagationForward(int nodeID, int steps) {
		// Retrieve the node
		Collection<Node> nodes = rootGraph.jungGraph.getVertices();
		Node tmp = (Node) nodes.toArray()[nodeID - 1];
		System.out.println(tmp.getName());
	}

	private void tracePropagationForward(Node from, int steps) {
		Collection<Node> sucessors = rootGraph.jungGraph.getSuccessors(from);
		for (int i=0; i< steps ; i++){
			
		}

		/*
		 * Iterator <Node> itr = sucessors.iterator(); while(itr.hasNext()){
		 * Node tmp2 = itr.next(); System.out.println(">"+tmp2.getName());
		 * Collection <Node> sucessors2 =
		 * rootGraph.jungGraph.getSuccessors(tmp2); Iterator <Node> itr2 =
		 * sucessors2.iterator(); while(itr2.hasNext()){ Node tmp3 =
		 * itr2.next(); System.out.println(">>"+tmp3.getName()); } }
		 */

	}

	public void show(PApplet app) {
		// vMainCommunity.show();
		for (VCommunity vC : vCommunities) {
			vC.show();
		}
	}
}
