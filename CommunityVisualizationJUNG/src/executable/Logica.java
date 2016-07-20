package executable;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import containers.Container;
import containers.RootContainer;
import containers.SubContainer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import visualElements.Canvas;
import visualElements.VCommunity;

public class Logica {

	// Visual Communities
	private VCommunity vMainCommunity;
	private ArrayList<VCommunity> vSubCommunities;

	public Logica() {
		// String XML_FILE = "../data/graphs/MuestraCompletaLouvain.graphml";
		String XML_FILE = "../data/graphs/L-UN-MOV.graphml";
		GraphLoader rootGraph = new GraphLoader(XML_FILE, "comunidad", "name");
		// String XML_FILE = "../data/graphs/Risk.graphml";
		// GraphLoader rootGraph = new GraphLoader(XML_FILE, "Continent",
		// "label");

		// Root visual community
		vMainCommunity = createRootVisualCommunity(rootGraph.jungGraph);
		// Sub communities
		vSubCommunities = createVisualSubCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames());

	}

	private VCommunity createRootVisualCommunity(Graph<Node, Edge> graph) {
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(graph, RootContainer.CIRCULAR, new Dimension(250, 250));
		mainCommunity.setName("Root");
		// Root Community
		String nodeID = mainCommunity.getName() +"_"+ String.valueOf(0);
		VCommunity vCommunity = new VCommunity(new Node(nodeID), mainCommunity);
		return vCommunity;
	}

	private ArrayList<VCommunity> createVisualSubCommunities(DirectedSparseMultigraph<Node, Edge> graph,
			ArrayList<String> communityNames) {
		//
		ArrayList<SubContainer> containers = new ArrayList<SubContainer>();
		ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();
		//
		ArrayList<DirectedSparseMultigraph<Node, Edge>> subGraphs = new ArrayList<DirectedSparseMultigraph<Node, Edge>>();
		int cont = 0;
		for (String communityName : communityNames) {
			// SubGraphs
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByCommunity(graph, communityName);
			// SubContainers
			SubContainer containerTemp = new SubContainer(graphTemp, Container.FRUCHTERMAN_REINGOLD,
					new Dimension(300 + (cont * 30), 300 + (cont * 30)));
			containerTemp.setName(communityName);
			// Visualizers
			String nodeID = communityName +"_"+ String.valueOf(cont);
			VCommunity communityTemp = new VCommunity(new Node(nodeID), containerTemp);
			subGraphs.add(graphTemp);
			containers.add(containerTemp);
			vCommunities.add(communityTemp);
			cont++;
		}
		subGraphs = null;
		containers = null;
		return vCommunities;
	}

	private void tracePropagationForward(DirectedSparseMultigraph<Node, Edge> graph, int nodeID, int steps) {
		// Retrieve the node
		Collection<Node> nodes = graph.getVertices();
		Node tmp = (Node) nodes.toArray()[nodeID - 1];
		System.out.println(tmp.getName());
	}

	private void tracePropagationForward(DirectedSparseMultigraph<Node, Edge> graph, Node from, int steps) {
		Collection<Node> sucessors = graph.getSuccessors(from);
		for (int i = 0; i < steps; i++) {

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

	public void show(Canvas canvas) {
		// vMainCommunity.show(canvas);
		for (VCommunity vC : vSubCommunities) {
			vC.show(canvas);
		}
	}
}
