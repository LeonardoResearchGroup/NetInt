package executable;

import java.awt.Dimension;
import java.util.ArrayList;

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
					new Dimension(100 + (cont * 30), 100 + (cont * 30)));
			containerTemp.setName(communityName);
			// Visualizers
			VCommunity communityTemp = new VCommunity(app, new Node(0), containerTemp);
			subGraphs.add(graphTemp);
			containers.add(containerTemp);
			vCommunities.add(communityTemp);
			cont++;
		}

		// ***** VISUALIZERS *****

	}

	public void show(PApplet app) {

		//vMainCommunity.show();
		for (VCommunity vC : vCommunities) {
			vC.show();
		}
	}
}
