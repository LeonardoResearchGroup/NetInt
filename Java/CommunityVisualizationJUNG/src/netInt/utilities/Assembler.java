package netInt.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.jcolorbrewer.ColorBrewer;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import netInt.containers.RootContainer;
import netInt.containers.SubContainer;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.filters.Filters;
import netInt.visualElements.Canvas;
import netInt.visualElements.VCommunity;
import processing.core.PConstants;

public class Assembler {

	// Visual Communities
	// private VCommunity rootVCommunity;
	public static VCommunity firstOrderVComm;
	public static ArrayList<VCommunity> secondOrderVComm;
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
	 * @param nodeImportAtts
	 *            User selection from Import Menu
	 * @param edgeImportAtts
	 *            User selection from Import Menu
	 * @param layout
	 *            The node distribution layout
	 * @param format
	 *            Graphml or Pajek. Graphml by default.
	 * @return true if the graph was loaded successfully
	 */
	public boolean loadGraph(File file, String[] nodeImportAtts, String[] edgeImportAtts, int layout, int format) {
		// Progress repoort on console
		System.out.println(this.getClass().getName() + " Loading graph");
		Canvas.app.cursor(PConstants.WAIT);
		// Instantiate a graphLoader
		GraphLoader rootGraph = new GraphLoader(file.getAbsolutePath(), nodeImportAtts, edgeImportAtts, format);

		// Set rootGraph to Assembler and Filters
		Filters.getInstance().setRootGraph();

		// Root visual community.
		// Keep it commented unless you want to visualize the root graph with no
		// communities
		// rootVCommunity = createRootVCommunity(rootGraph.jungGraph);

		// List of Second Order Communities: sub communities
		secondOrderVComm = createSecondOrderVCommunities(GraphLoader.theGraph, rootGraph.getCommunityNames(), layout);

		// First order community: Community of communities
		firstOrderVComm = createFirstOrderVCommunity(rootGraph.getFirstOrderEdgeList(), secondOrderVComm,
				"FirstOrderCommunity", layout);
		Canvas.app.cursor(PConstants.ARROW);
		return true;
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
		// set diameter
		vCommunity.init();
		return vCommunity;
	}

	private VCommunity createFirstOrderVCommunity(ArrayList<Edge> firstOrderEdgeList, ArrayList<VCommunity> communities,
			String comName, int layout) {
		// Progress report on console
		System.out.println(this.getClass().getName() + " Create First Order Visual Community");
		System.out.println(
				"     Adding " + secondOrderVComm.size() + " Second Order VCommunities to Higher Order container");
		// Make a temporary graph
		Graph<Node, Edge> graphTemp = new DirectedSparseMultigraph<Node, Edge>();

		// make a Container
		SubContainer subContainer = new SubContainer(graphTemp, layout, rootDimension);

		// make graph from communities
		// subContainer.populateGraphfromVCommunities(communities);

		// make graph from first order edge list
		subContainer.populateGraphfromEdgeList(firstOrderEdgeList);

		// Name the community
		subContainer.setName(comName);

		subContainer.assignVisualElements(communities);

		// Initialize container NOTE: SEE VCommunity.show(). The container is
		// only initialized if it going to be shown, That's is why this method
		// is invoked inside VCommunity.show()
		// subContainer.initialize();

		String nodeID = comName + "_" + String.valueOf(0);
		VCommunity communityTemp = new VCommunity(new Node(nodeID), subContainer);
		// set diameter
		communityTemp.init();
		return communityTemp;
	}

	private ArrayList<VCommunity> createSecondOrderVCommunities(Graph<Node, Edge> graph, ArrayList<String> comNames,
			int layout) {
		System.out.println(this.getClass().getName() + " Creating Second Order VCommunities");

		// Make a list of VCommunities
		ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();

		// Color
		boolean colorBlindSafe = false;
		ColorBrewer[] qualitativePalettes = ColorBrewer.getQualitativeColorPalettes(colorBlindSafe);
		ColorBrewer myBrewer = qualitativePalettes[2];
		Color[] myGradient = myBrewer.getColorPalette(comNames.size());
		//

		System.out.println("     Generating Graphs for " + comNames.size() + " communities ...");

		for (int i = 0; i < comNames.size(); i++) {
			String communityName = comNames.get(i);

			System.out.println("     Working on community " + communityName);

			// SubGraph of each community
			DirectedSparseMultigraph<Node, Edge> graphTemp = Filters.filterNodeInCommunity(communityName);

			// SubContainers for each VCommunity
			SubContainer containerTemp = new SubContainer(graphTemp, layout, new Dimension(600, 600), myGradient[i]);

			// Name container
			containerTemp.setName(communityName);

			// Initialize container
			// containerTemp.initialize();

			// Make Node for CommunityCover
			Node tmpNode = new Node(communityName);

			// Name Node
			tmpNode.setName(communityName);

			// Create temporal community
			VCommunity communityTemp = new VCommunity(tmpNode, containerTemp);

			// Set VCommunity color
			communityTemp.setColor(myGradient[i]);

			// Add VCommunity to list of VCommunities
			vCommunities.add(communityTemp);

		}
		for (VCommunity vC : vCommunities) {
			vC.init();
		}
		return vCommunities;
	}

	public void show() {
		// rootVCommunity.show();
		firstOrderVComm.show();
		firstOrderVComm.searchNode();
	}

	public ArrayList<VCommunity> getVisualCommunities() {
		return secondOrderVComm;
	}

}
