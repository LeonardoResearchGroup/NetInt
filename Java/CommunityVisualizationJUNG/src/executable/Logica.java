package executable;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
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

public class Logica {

	// Visual Communities
	// private VCommunity vMainCommunity;
	public static VCommunity vSubSubCommunity;
	public static ArrayList<VCommunity> vSubCommunities;
	// These Dimensions set the RootContainer and top SubContainer boundaries
	public Dimension rootDimension;
	public static Dimension HD720 = new Dimension(1280, 720);
	public static Dimension HD1080 = new Dimension(1920, 1080);
	public static Dimension UHD = new Dimension(3840, 2160);

	public Logica(int width, int height) {
		rootDimension = new Dimension(width, height);
	}

	public Logica(Dimension dim) {
		rootDimension = dim;
	}

	/**
	 * Creates a single VCommunity of the graph with no subCommunities yet
	 * contains all the VNodes
	 * 
	 * @param graph
	 * @return
	 */
	public VCommunity createRootVisualCommunity(Graph<Node, Edge> graph) {
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(graph, RootContainer.CIRCULAR, rootDimension);
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

		boolean colorBlindSave = false;
		ColorBrewer[] qualitativePalettes = ColorBrewer.getQualitativeColorPalettes(colorBlindSave);
		ColorBrewer myBrewer = qualitativePalettes[2];
		// System.out.println( "Name of this color brewer: " + myBrewer);
		// Color[] myGradient =
		// myBrewer.Spectral.getColorPalette(communityNames.size());
		Color[] myGradient = myBrewer.getColorPalette(communityNames.size());

		int i = 0;
		Mapper.getInstance().setMinCommunitySize(graph.getVertexCount());
		for (String communityName : communityNames) {
			System.out.println("Logica > Generating DirectedSparseMultigraph for community:" + communityName
					+ " out of " + communityNames.size());
			Canvas.app.text("Generating DirectedSparseMultigraph for community:" + communityName + " out of "
					+ communityNames.size(), 50, Canvas.app.height - 50);
	
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
		System.out.println("Logica > createCommunityOfvCommunities() for community: " + communityName);
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
		SubContainer subContainer = new SubContainer(graphTemp, layout, rootDimension);
		System.out.println("Logica > createCommunityOfvCommunities().  SubContainer: " + communityName + " created");
		subContainer.setName(communityName);
		System.out.println("Logica > createCommunityOfvCommunities().  Assigning visual elements to nodes");
		// Assign each vCommunity cover to this subContainer
		subContainer.assignVisualElements(communities);
		// CommunityCover
		String nodeID = communityName + "_" + String.valueOf(0);
		System.out.println("Logica > createCommunityOfvCommunities().  Making VCommunity for: " + communityName);
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
			System.out.println("Logica > createEdgesBetweenSubcommunities().  Making External Edges for community: "
					+ communities.get(i).getNode().getId());
			for (int j = i + 1; j < communities.size(); j++) {
				graphInter = GraphLoader.filterByInterCommunities(vSubSubCommunity.container.rootGraph,
						communities.get(i).container.getName(), communities.get(j).container.getName());
				// This condition decides which edges are created
				if (graphInter.getEdgeCount() >= 1) {
					graphElements.Edge e = new graphElements.Edge(communities.get(i).getNode(),
							communities.get(j).getNode(), true);
					vSubSubCommunity.container.getGraph().addEdge(e, communities.get(i).getNode(),
							communities.get(j).getNode(), EdgeType.DIRECTED);
				}
			}
		}
	}

	/**
	 * It adds the edgesBetweenCommunities to the jungGraph of vSubSubCommunity
	 * 
	 * @param edgesBetweenCommunities
	 */
	private void addEdgesBetweenSubcommunities(ArrayList<Edge> edgesBetweenCommunities) {
		for (Edge e : edgesBetweenCommunities) {
			vSubSubCommunity.container.getGraph().addEdge(e, e.getSource(), e.getTarget());
		}

	}

	public void show() {
		// vMainCommunity.show();
		vSubSubCommunity.show();
		vSubSubCommunity.searchNode();
	}

	/**
	 * @deprecated
	 * @param file
	 * @param communityFilter
	 * @param nodeName
	 * @param sector
	 * @param edgeWeight
	 * @param layout
	 * @param format
	 */
	public void loadGraph(File file, String communityFilter, String nodeName, String sector, String edgeWeight,
			int layout, int format) {
		String XML_FILE = file.getAbsolutePath();
		//// *************CORREGIR SECTOR *****************
		GraphLoader rootGraph = new GraphLoader(XML_FILE, communityFilter, nodeName, sector, edgeWeight, format);

		// Root visual community
		// vMainCommunity = createRootVisualCommunity(rootGraph.jungGraph);

		// Sub communities
		vSubCommunities = createVisualSubCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames(), layout);
		// Community of communities
		vSubSubCommunity = createCommunityOfvCommunities(vSubCommunities, "SubSubcommunities", layout);
		vSubSubCommunity.registerEvents();
		System.out.println(
				"Logica > loadGraph() Setting RootGraph to container of: " + vSubSubCommunity.getNode().getId());
		vSubSubCommunity.container.setRootGraph(rootGraph.jungGraph);
		System.out.println("Logica > loadGraph() Creating edges between communities");
		// if(format == GraphLoader.PAJEK)
		addEdgesBetweenSubcommunities(rootGraph.reader.getEdgesBetweenCommunuties());
		// else if(format == GraphLoader.GRAPHML)
		// createEdgesBetweenSubcommunities(vSubCommunities);

		System.out.println("Logica > loadGraph() Running edge factory");
		vSubSubCommunity.container.runEdgeFactory();
	}

	public void loadGraph(File file, String[] nodeImportAttributes, String[] edgeImportAttributes, int layout,
			int format) {
		String XML_FILE = file.getAbsolutePath();
		GraphLoader rootGraph = new GraphLoader(XML_FILE, nodeImportAttributes, edgeImportAttributes, format);
		// Root visual community. Keep it cancelled!!!!
		// vMainCommunity = createRootVisualCommunity(rootGraph.jungGraph);

		// Sub communities
		vSubCommunities = createVisualSubCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames(), layout);

		// Community of communities
		vSubSubCommunity = createCommunityOfvCommunities(vSubCommunities, "SubSubcommunities", layout);
		vSubSubCommunity.registerEvents();
		
		// Setting root Container & Reporting progress
		System.out.println(this.getClass().getName() + " Setting RootGraph to container of: "
				+ vSubSubCommunity.getNode().getId());
		vSubSubCommunity.container.setRootGraph(rootGraph.jungGraph);

		// Reporting progress
		System.out.println(this.getClass().getName() + " Creating edges between communities");
		// if(format == GraphLoader.PAJEK)
		addEdgesBetweenSubcommunities(rootGraph.reader.getEdgesBetweenCommunuties());
		// else if(format == GraphLoader.GRAPHML)
		// createEdgesBetweenSubcommunities(vSubCommunities);

		// Create edges & reporting progress
		System.out.println(this.getClass().getName() + " Running edge factory");
		vSubSubCommunity.container.runEdgeFactory();
	}

	public ArrayList<VCommunity> getVisualCommunities() {
		return vSubCommunities;
	}

}
