package utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.jcolorbrewer.ColorBrewer;

import containers.RootContainer;
import containers.SubContainer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import graphElements.Edge;
import graphElements.Node;
import visualElements.VCommunity;
import visualElements.VNode;

public class Assembler {

	// Visual Communities
	// private VCommunity rootVCommunity;
	public static VCommunity firstOrderVCommunity;
	public static ArrayList<VCommunity> secondOrderVCommunities;
	// These Dimensions set the RootContainer and top SubContainer boundaries
	public Dimension rootDimension;
	public static Dimension HD720 = new Dimension(1280, 720);
	public static Dimension HD1080 = new Dimension(1920, 1080);
	public static Dimension UHD = new Dimension(3840, 2160);

	public Assembler(int width, int height) {
		rootDimension = new Dimension(width, height);
	}

	public Assembler(Dimension dim) {
		rootDimension = dim;
	}

	/**
	 * Loads and builds a graph from a given graph-formated file (graphml or
	 * pajek) using the import attributes selected by the user
	 * 
	 * @param file
	 *            The path to the source file
	 * @param nodeImportAttributes
	 *            User selection from Import Menu
	 * @param edgeImportAttributes
	 *            User selection from Import Menu
	 * @param layout
	 *            The node distribution layout
	 * @param format
	 *            Graphml or Pajek. Graphml by default.
	 */
	public void loadGraph(File file, String[] nodeImportAttributes, String[] edgeImportAttributes, int layout,
			int format) {
		System.out.println(this.getClass().getName() + " loadGraph");
		String XML_FILE = file.getAbsolutePath();
		GraphLoader rootGraph = new GraphLoader(XML_FILE, nodeImportAttributes, edgeImportAttributes, format);
		// Root visual community. Keep it commented unless you want to visualize
		// the graph with no partitions!!!!
		// rootVCommunity = createRootVCommunity(rootGraph.jungGraph);

		// List of Sub Communities
		System.out.println("     Retrieving the list of second order VCommunities ");
		secondOrderVCommunities = createSecondOrderVisualCommunities(rootGraph.jungGraph, rootGraph.getCommunityNames(),
				layout);

		// Community of communities
		System.out.println("     Adding " + secondOrderVCommunities.size()
				+ " second order VCommunities to First Order VCommunity ");
		firstOrderVCommunity = createFirstOrderVisualCommunities(secondOrderVCommunities, "FirstOrderCommunity",
				layout);

		// Setting root Container & Reporting progress
		// System.out.println(this.getClass().getName() + " Setting RootGraph to
		// container of: " + firstOrderVCommunity.getNode().getId());
		firstOrderVCommunity.container.setRootGraph(rootGraph.jungGraph);

		// Create edges & reporting progress
		// Reporting progress
		System.out.println("     Running VEdge factory ...");
		firstOrderVCommunity.container.runVEdgeFactory();
		firstOrderVCommunity.initialize();
	}

	/**
	 * Creates a single VCommunity of the graph with no subCommunities yet
	 * contains all the VNodes
	 * 
	 * @param graph
	 * @return
	 */
	public VCommunity createRootVCommunity(Graph<Node, Edge> graph) {
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(graph, RootContainer.CIRCULAR, rootDimension);
		mainCommunity.setName("Root");
		// Root Community
		String nodeID = mainCommunity.getName() + "_" + String.valueOf(0);
		VCommunity vCommunity = new VCommunity(new Node(nodeID), mainCommunity);
		return vCommunity;
	}

	private ArrayList<VCommunity> createSecondOrderVisualCommunities(DirectedSparseMultigraph<Node, Edge> graph,
			ArrayList<String> communityNames, int layout) {
		System.out.println(this.getClass().getName() + " createVisualSubCommunities");
		ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();
		boolean colorBlindSafe = false;
		ColorBrewer[] qualitativePalettes = ColorBrewer.getQualitativeColorPalettes(colorBlindSafe);
		ColorBrewer myBrewer = qualitativePalettes[2];
		Color[] myGradient = myBrewer.getColorPalette(communityNames.size());
		int i = 0;
		System.out
				.println("     Generating DirectedSparseMultigraph for " + communityNames.size() + " communities ...");
		for (String communityName : communityNames) {
			// SubGraphs
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByCommunity(graph, communityName);
			// SubContainers
			SubContainer containerTemp = new SubContainer(graphTemp, layout, new Dimension(600, 600), myGradient[i]);
			i++;
			containerTemp.setName(communityName);
			containerTemp.setRootGraph(graph);
			// CommunityCover
			String nodeID = communityName;
			Node tmpNode = new Node(nodeID);
			tmpNode.setName(communityName);
			VCommunity communityTemp = new VCommunity(tmpNode, containerTemp);
			communityTemp.setColor(myGradient[i - 1]);
			vCommunities.add(communityTemp);
		}
		return vCommunities;
	}

	private VCommunity createFirstOrderVisualCommunities(ArrayList<VCommunity> communities, String communityName,
			int layout) {
		System.out.println(this.getClass().getName() + " createCommunityOfvCommunities");
		// Make a temporary graph
		DirectedSparseMultigraph<Node, Edge> graphTemp = new DirectedSparseMultigraph<Node, Edge>();
		System.out.println(
				"     Creating Community of " + communities.size() + " vCommunities for community: " + communityName);
//		 for (VNode vN : communities) {
//		 VCommunity vC = (VCommunity) vN;
//		 // add Nodes
//		 graphTemp.addVertex(vC.getNode());
//		 }

		// Detect linked communities and build edges
		for (int i = 0; i < communities.size(); i++) {
			VCommunity vCA = communities.get(i);
			for (int j = i + 1; i < communities.size(); i++) {
				VCommunity vCB = communities.get(j);
				if (vCA.detectLinkedCommunities(vCB)) {
					Edge tempEdge = new Edge(vCA.getNode(), vCB.getNode(), false);
					graphTemp.addEdge(tempEdge, vCA.getNode(), vCB.getNode());
				}
			}
		}

		// make a Container
		SubContainer subContainer = new SubContainer(graphTemp, layout, rootDimension);

		// Create VEdges
		subContainer.runVEdgeFactory();

		// Name the community
		System.out.println("     SubContainer: " + communityName + " created");
		subContainer.setName(communityName);
		System.out.println("     Assigning CommunityCover to: " + communityName);
		// Assign each vCommunity cover to this subContainer
		subContainer.assignVisualElements(communities);
		// CommunityCover
		String nodeID = communityName + "_" + String.valueOf(0);
		System.out.println("     Making VCommunity for: " + communityName);
		VCommunity communityTemp = new VCommunity(new Node(nodeID), subContainer);
		return communityTemp;
	}

	/**
	 * It adds the edgesBetweenCommunities to the jungGraph of vSubSubCommunity
	 * 
	 * @param edgesBetweenCommunities
	 */
	private void addEdgesBetweenSubcommunities(ArrayList<Edge> edgesBetweenCommunities) {
		for (Edge e : edgesBetweenCommunities) {
			firstOrderVCommunity.container.getGraph().addEdge(e, e.getSource(), e.getTarget());
		}

	}

	public void show() {
		// rootVCommunity.show();
		firstOrderVCommunity.show();
		firstOrderVCommunity.searchNode();
	}

	public ArrayList<VCommunity> getVisualCommunities() {
		return secondOrderVCommunities;
	}

}
