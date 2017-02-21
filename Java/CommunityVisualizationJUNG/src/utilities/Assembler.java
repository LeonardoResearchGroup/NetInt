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
import graphElements.Edge;
import graphElements.Node;
import utilities.filters.Filters;
import visualElements.VCommunity;

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
	 */
	public void loadGraph(File file, String[] nodeImportAtts, String[] edgeImportAtts, int layout,int format) {
		// Progress repoort on console
		System.out.println(this.getClass().getName() + " Loading graph");

		// Instantiate a graphLoader
		GraphLoader rootGraph = new GraphLoader(file.getAbsolutePath(), nodeImportAtts, edgeImportAtts,
				format);
		// Set rootGraph to Assembler and Filters
		Filters.getInstance().setRootGraph();

		// Root visual community.
		// Keep it commented unless you want to visualize the root graph with no
		// communities!!!!
		// rootVCommunity = createRootVCommunity(rootGraph.jungGraph);

		// List of Second Order Communities: sub communities
		secondOrderVComm = createSecondOrderVCommunities(GraphLoader.theGraph, rootGraph.getCommunityNames(), layout);

		// First order community: Community of communities
		firstOrderVComm = createFirstOrderVCommunity(secondOrderVComm, "FirstOrderCommunity", layout);
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

	private VCommunity createFirstOrderVCommunity(ArrayList<VCommunity> communities, String comName, int layout) {
		// Progress report on console
		System.out.println(this.getClass().getName() + " Create First Order Visual Community");
		System.out.println("     Adding " + secondOrderVComm.size() + " Second Order VCommunities to Higher Order container");
		// Make a temporary graph
		Graph<Node, Edge> graphTemp = new DirectedSparseMultigraph<Node, Edge>();

		// make a Container
		SubContainer subContainer = new SubContainer(graphTemp, layout, rootDimension);

		// make graph from communities
		subContainer.populateGraph(communities);

		// Name the community
		subContainer.setName(comName);

		subContainer.assignVisualElements(communities);

		// Initialize container
		// subContainer.initialize();

		String nodeID = comName + "_" + String.valueOf(0);
		VCommunity communityTemp = new VCommunity(new Node(nodeID), subContainer);
		return communityTemp;
	}

	private ArrayList<VCommunity> createSecondOrderVCommunities(Graph<Node, Edge> graph, ArrayList<String> comNames, int layout) {
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
			
			// SubGraph of each community
			DirectedSparseMultigraph<Node, Edge> graphTemp = Filters.filterNodeInCommunity(communityName);
			
			// SubContainers for each VCommunity
			SubContainer containerTemp = new SubContainer(graphTemp, layout, new Dimension(600, 600), myGradient[i]);

			// Name container
			containerTemp.setName(communityName);
			
			// Initialize container
			//containerTemp.initialize();
			
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
