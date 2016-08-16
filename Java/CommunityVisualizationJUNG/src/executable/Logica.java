package executable;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import containers.Container;
import containers.RootContainer;
import containers.SubContainer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import graphElements.Edge;
import graphElements.Node;
import utilities.GraphLoader;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.VNode;
import visualElements.primitives.VisualAtom;

public class Logica {

	// Visual Communities
	private VCommunity vMainCommunity, vSubSubCommunity;
	private ArrayList<VCommunity> vSubCommunities;

	public Logica() {

	}

	private VCommunity createRootVisualCommunity(Graph<Node, Edge> graph) {
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(graph, RootContainer.CIRCULAR, new Dimension(250, 250));
		mainCommunity.setName("Root");
		// Root Community
		String nodeID = mainCommunity.getName() + "_" + String.valueOf(0);
		VCommunity vCommunity = new VCommunity(new Node(nodeID), mainCommunity);
		return vCommunity;
	}

	private ArrayList<VCommunity> createVisualSubCommunities(DirectedSparseMultigraph<Node, Edge> graph,
			ArrayList<String> communityNames, int layout) {
		ArrayList<SubContainer> containers = new ArrayList<SubContainer>();
		ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();
		//
		ArrayList<DirectedSparseMultigraph<Node, Edge>> subGraphs = new ArrayList<DirectedSparseMultigraph<Node, Edge>>();
		for (String communityName : communityNames) {
			// SubGraphs
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByCommunity(graph, communityName);
			// SubContainers
			SubContainer containerTemp = new SubContainer(graphTemp, layout,
					new Dimension(600, 600));
			containerTemp.setName(communityName);
			// CommunityCover
			String nodeID = communityName;
			VCommunity communityTemp = new VCommunity(new Node(nodeID), containerTemp);
			subGraphs.add(graphTemp);
			containers.add(containerTemp);
			vCommunities.add(communityTemp);
		}
		subGraphs = null;
		containers = null;
		return vCommunities;
	}

	private VCommunity createCommunityOfvCommunities(ArrayList<VCommunity> communities, String communityName, int layout) {
		// Make a temporary graph
		DirectedSparseMultigraph<Node, Edge> graphTemp = new DirectedSparseMultigraph<Node, Edge>();
		for (VNode vN : communities) {
			VCommunity vC = (VCommunity) vN;
			// setCommunity color
			// Selected Communities
			if (vC.getNode().getId().equals("10.0") || vC.getNode().getId().equals("9.0")
					|| vC.getNode().getId().equals("22.0")) {
				System.out.println(vC.getNode().getId());
				vC.setColor(125, 0, 175, 120);
			} // Other communities
			else {
				vC.setColor(0, 125, 155, 120);
			}
			// add Nodes
			graphTemp.addVertex(vC.getNode());
			// add edges
			/*
			 * Edge compression will come here or inside the community
			 * initializer
			 */
		}

		// make a Container
		SubContainer subContainer = new SubContainer(graphTemp, layout,
				new Dimension(600, 600));
		subContainer.setName(communityName);
		// Assign each vCommunity cover to this subContainer
		subContainer.assignVisualElements(communities);
		// CommunityCover
		String nodeID = communityName + "_" + String.valueOf(0);
		VCommunity communityTemp = new VCommunity(new Node(nodeID), subContainer);
		return communityTemp;
	}

	public void show(Canvas canvas) {
		// vMainCommunity.show(canvas);
		vSubSubCommunity.show(canvas);
	}


	public void loadGraph(File file, String communityFilter, String nodeName, int layout) {
		
		String XML_FILE = file.getAbsolutePath();
		GraphLoader rootGraph = new GraphLoader(XML_FILE, communityFilter, nodeName,"sector");

		// Root visual community
		// vMainCommunity = createRootVisualCommunity(rootGraph.jungGraph);
		
		// Sub communities
		vSubCommunities = createVisualSubCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames(), layout);
		// Community of communities
		vSubSubCommunity = createCommunityOfvCommunities(vSubCommunities, "SubSubcommunities", layout);
		vSubSubCommunity.container.setRootGraph(rootGraph.jungGraph);
	}

}
