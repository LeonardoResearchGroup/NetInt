package netInt.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;

import org.jcolorbrewer.ColorBrewer;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import netInt.GraphPad;
import netInt.containers.RootContainer;
import netInt.containers.SubContainer;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.filters.Filters;
import netInt.visualElements.VCommunity;

/**
 * <p>
 * An instance of this class puts together all the components retrieved from the
 * source graph and arranges them in instances of <tt>VCommunity</tt> (visual
 * communities).
 * </p>
 * <p>
 * NetInt defines visual communities that may be nested. The simplest visual
 * community is named <i>root community</i>. As any <tt>VCommunity</tt>, it
 * contains all the nodes and edges in a unique <tt>Container</tt> but they are
 * not clustered in communities. It can be used to visualize a graph with no
 * partitions. Caveat: as the focus of NetInt is to visualize nested communities
 * it is not enabled in this version.
 * </p>
 * <p>
 * In a nested structure of <tt>VCommunity</tt>'s, a <i>first order</i>
 * <tt>VCommunity</tt> is the bottom tier that contains a set of
 * <tt>VCommunity</tt>'s named as <i>second order</i> communities. The latter
 * might contain the next tier of <tt>VCommunity</tt>'s named as <i>third
 * order</i> communities, and so on. The communities at the top tier must
 * contain the nodes and edges.
 * </p>
 * <p>
 * The current version of NetInt has been tested with two tiers but it is
 * designed to support virtually unlimited number of tiers.
 * </p>
 * 
 * @author juan salamanca
 *
 */
public class Assembler {

	// Visual Communities
	private VCommunity rootVCommunity;
	public static VCommunity firstOrderVComm;
	public static ArrayList<VCommunity> secondOrderVComm;

	// These Dimensions set the RootContainer and top SubContainer boundaries
	private Dimension rootDimension;

	/**
	 * Dimension to be used in screens with resolution at 1280 X 720 px.
	 */
	public static Dimension HD720 = new Dimension(1280, 720);

	/**
	 * Dimension to be used in screens with resolution at 1920 X 1080 px.
	 */
	public static Dimension HD1080 = new Dimension(1920, 1080);

	/**
	 * Dimension to be used in screens with resolution at 3840 X 2160 px.
	 */
	public static Dimension UHD = new Dimension(3840, 2160);

	public Assembler(int width, int height) {
		rootDimension = new Dimension(width, height);
	}

	public Assembler(Dimension dim) {
		rootDimension = dim;
	}

	/**
	 * Loads and builds a graph with partitions from a given graph-formated file
	 * (graphml or pajek) using the import attributes selected by the user
	 * 
	 * @param file
	 *            The path to the source file
	 * @param nodeImportAtts
	 *            User selection from Import Menu or Community and name keys
	 *            from graphml File
	 * @param edgeImportAtts
	 *            User selection from Import Menu or edge keys from graphml file
	 * @param layout
	 *            The node distribution layout. See constants in Container class
	 * @param format
	 *            Graphml or Pajek. Graphml by default. See constants in
	 *            GraphLoader class
	 * @return true if the graph was loaded successfully
	 */
	public boolean loadGraph(File file, String[] nodeImportAtts, String[] edgeImportAtts, int layout, int format) {
		// Progress report on console
		System.out.println(this.getClass().getName() + " Loading graph");

		// Set file if null
		if (GraphPad.getFile() == null)
			GraphPad.setFile(file);

		// Instantiate a graphLoader
		GraphLoader rootGraph = new GraphLoader(file.getAbsolutePath(), nodeImportAtts, edgeImportAtts, format);

		// Set rootGraph to Assembler and Filters
		Filters.getInstance().setRootGraph();

		// List of Second Order Communities: sub communities
		secondOrderVComm = createSecondOrderVCommunities(GraphLoader.theGraph, rootGraph.getCommunityNames(), layout);

		// First order community: Community of communities
		firstOrderVComm = createFirstOrderVCommunity(rootGraph.getFirstOrderEdgeList(), secondOrderVComm,
				"FirstOrderCommunity", layout);

		return true;
	}

	/**
	 * Loads and builds a graph with no partitions from a given graph-formated
	 * file (graphml or pajek) using only edge import attributes
	 * 
	 * @param file
	 *            The path to the source file
	 * @param edgeImportAtts
	 *            User defined edge keys from graphml file
	 * @param layout
	 *            The node distribution layout. See constants in Container class
	 * @param format
	 *            Graphml or Pajek. Graphml by default. See constants in
	 *            GraphLoader class
	 * @deprecated
	 * @return true if the graph was loaded successfully
	 */
	public boolean loadGraphAsRoot(File file, String[] edgeImportAtts, int layout, int format) {
		// Progress report on console
		System.out.println(this.getClass().getName() + " Loading graph as Root");

		// Set file if null
		if (GraphPad.getFile() == null)
			GraphPad.setFile(file);

		// In Graphml file format. Node attributes copied from the graphml file.
		// The first one defines the communities, the second the node names
		String[] nodeAtts = { "", "" };

		// Instantiate a graphLoader
		new GraphLoader(file.getAbsolutePath(), nodeAtts, edgeImportAtts, format);

		// Set rootGraph to Assembler and Filters
		Filters.getInstance().setRootGraph();

		// Keep it commented unless you want to visualize the root graph with no
		// communities
		rootVCommunity = createRootVCommunity(GraphLoader.theGraph);

		return true;
	}

	/**
	 * Creates a single VCommunity of the graph with no subCommunities yet
	 * contains all the VNodes
	 * 
	 * @param graph
	 * @return the root VCommunity
	 */
	private VCommunity createRootVCommunity(Graph<Node, Edge> graph) {
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
		firstOrderVComm.show();
		firstOrderVComm.searchNode();
	}

	/**
	 * @deprecated
	 */
	public void showRoot() {
		rootVCommunity.show();
	}

	public ArrayList<VCommunity> getVisualCommunities() {
		return secondOrderVComm;
	}

	public Dimension getRootDimension() {
		return rootDimension;
	}

	public void setRootDimension(Dimension rootDimension) {
		this.rootDimension = rootDimension;
	}
}
