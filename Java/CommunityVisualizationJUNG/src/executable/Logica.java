package executable;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.jcolorbrewer.ColorBrewer;

import containers.Container;
import containers.RootContainer;
import containers.SubContainer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graphElements.Edge;
import graphElements.Node;
import utilities.GraphLoader;
import utilities.mapping.Mapper;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.VNode;
import visualElements.gui.VisibilitySettings;

public class Logica {

	// Visual Communities
	private VCommunity vMainCommunity, vSubSubCommunity;
	private ArrayList<VCommunity> vSubCommunities;

	public Logica() {

	}

	private VCommunity createRootVisualCommunity(Graph<Node, Edge> graph) {
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(graph, RootContainer.CIRCULAR, new Dimension(600, 600));
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

		boolean colorBlindSave = true;
		ColorBrewer[] sequentialPalettes = ColorBrewer.getSequentialColorPalettes(colorBlindSave);
		ColorBrewer myBrewer = sequentialPalettes[3];
		// System.out.println( "Name of this color brewer: " + myBrewer);
		// Color[] myGradient =
		// myBrewer.Spectral.getColorPalette(communityNames.size());
		Color[] myGradient = myBrewer.getColorPalette(communityNames.size());

		int i = 0;
		Mapper.getInstance().setMinCommunitySize(graph.getVertexCount());
		for (String communityName : communityNames) {
			// SubGraphs
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByCommunity(graph, communityName);
			// SubContainers
			SubContainer containerTemp = new SubContainer(graphTemp, layout, new Dimension(600, 600), myGradient[i]);
			i++;
			containerTemp.setName(communityName);
			// CommunityCover
			String nodeID = communityName;
			VCommunity communityTemp = new VCommunity(new Node(nodeID), containerTemp);
			subGraphs.add(graphTemp);
			containers.add(containerTemp);
			communityTemp.setColor(myGradient[i - 1]);
			vCommunities.add(communityTemp);

			// SET MAX & MIN COMMUNITY SIZE
			if (communityTemp.container.getGraph()
					.getVertexCount() > Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[1]) {
				Mapper.getInstance().setMaxCommunitySize(communityTemp.container.getGraph().getVertexCount());
			}
			if (communityTemp.container.getGraph()
					.getVertexCount() < Mapper.getInstance().getMaxMin(Mapper.COMUNITY_SIZE)[0]) {
				Mapper.getInstance().setMinCommunitySize(communityTemp.container.getGraph().getVertexCount());
			}
		}
		subGraphs = null;
		containers = null;
		return vCommunities;
	}

	private VCommunity createCommunityOfvCommunities(ArrayList<VCommunity> communities, String communityName,
			int layout) {
		// Make a temporary graph
		DirectedSparseMultigraph<Node, Edge> graphTemp = new DirectedSparseMultigraph<Node, Edge>();
		for (VNode vN : communities) {
			VCommunity vC = (VCommunity) vN;
			// add Nodes
			graphTemp.addVertex(vC.getNode());
			// add edges
			/*
			 * Edge compression will come here or inside the community
			 * initializer
			 */
		}

		// make a Container
		SubContainer subContainer = new SubContainer(graphTemp, layout, new Dimension(600, 600));
		subContainer.setName(communityName);
		// Assign each vCommunity cover to this subContainer
		subContainer.assignVisualElements(communities);
		// CommunityCover
		String nodeID = communityName + "_" + String.valueOf(0);
		VCommunity communityTemp = new VCommunity(new Node(nodeID), subContainer);
		return communityTemp;
	}

	/**
	 * It creates the edges between communities before they are opened.
	 * 
	 * @param communities
	 */
	private void createEdgesBetweenSubcommunities(ArrayList<VCommunity> communities) {
		Graph<Node, Edge> graphInter;
		for (int i = 0; i < communities.size(); i++) {
			for (int j = i + 1; j < communities.size(); j++) {
				graphInter = GraphLoader.filterByInterCommunities(vSubSubCommunity.container.rootGraph,
						communities.get(i).container.getName(), communities.get(j).container.getName());
				// This condition decides wich edges are created
				if (graphInter.getEdgeCount() >= 1) {
					graphElements.Edge e = new graphElements.Edge(communities.get(i).getNode(),
							communities.get(j).getNode(), true);
					vSubSubCommunity.container.getGraph().addEdge(e, communities.get(i).getNode(),
							communities.get(j).getNode(), EdgeType.DIRECTED);
				}
			}
		}
	}



	public void show(Canvas canvas) {
		// vMainCommunity.show(canvas);
		vSubSubCommunity.show(canvas);
		vSubSubCommunity.searchNode();
	}

	public void loadGraph(File file, String communityFilter, String nodeName, String sector, String edgeWeight,
			int layout) {
		String XML_FILE = file.getAbsolutePath();
		//// *************CORREGIR SECTOR *****************
		GraphLoader rootGraph = new GraphLoader(XML_FILE, communityFilter, nodeName, sector, edgeWeight);

		// Root visual community
		// vMainCommunity = createRootVisualCommunity(rootGraph.jungGraph);

		// Sub communities
		vSubCommunities = createVisualSubCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames(), layout);
		// Community of communities
		vSubSubCommunity = createCommunityOfvCommunities(vSubCommunities, "SubSubcommunities", layout);
		vSubSubCommunity.container.setRootGraph(rootGraph.jungGraph);
		createEdgesBetweenSubcommunities(vSubCommunities);
		vSubSubCommunity.container.runEdgeFactory();

	}

	public void loadGraph(String file, String communityFilter, String nodeName, String sector, String edgeWeight) {
		String XML_FILE = file;
		//// *************CORREGIR SECTOR *****************
		GraphLoader rootGraph = new GraphLoader(XML_FILE, communityFilter, nodeName, sector, edgeWeight);

		// Root visual community
		// vMainCommunity = createRootVisualCommunity(rootGraph.jungGraph);

		// Sub communities
		vSubCommunities = createVisualSubCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames(),
				Container.FRUCHTERMAN_REINGOLD);
		// Community of communities
		vSubSubCommunity = createCommunityOfvCommunities(vSubCommunities, "SubSubcommunities",
				Container.FRUCHTERMAN_REINGOLD);
		vSubSubCommunity.container.setRootGraph(rootGraph.jungGraph);
		createEdgesBetweenSubcommunities(vSubCommunities);
		vSubSubCommunity.container.runEdgeFactory();

	}

	public ArrayList<VCommunity> getVisualCommunities() {
		return vSubCommunities;
	}

}
