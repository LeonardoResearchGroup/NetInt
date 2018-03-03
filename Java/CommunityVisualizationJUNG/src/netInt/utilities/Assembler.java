/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.utilities;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.jcolorbrewer.ColorBrewer;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;

import netInt.containers.RootContainer;
import netInt.containers.SubContainer;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.filters.Filters;
import netInt.canvas.Canvas;
import netInt.visualElements.VCommunity;
import processing.core.PConstants;

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
 * it is not enabled in this version yet.
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
	
	//Data structure for nested communities
	private HashMap<String,ArrayList<Edge>> communitiesOrderEdgeList;

	/**
	 * Loads and builds a graph from a given graph-formated file (graphml or
	 * pajek) using the import attributes selected by the user
	 * 
	 * @param file
	 *            The path to the source file
	 * @param nestedAttributesOrder
	 *            User selection from Import Menu
	 * @param nodeLabelAtts
	 *            User selection from Import Menu
	 * @param edgeImportAtts
	 *            User selection from Import Menu
	 * @param layout
	 *            The node distribution layout
	 * @param format
	 *            Graphml or Pajek. Graphml by default.
	 * @return true if the graph was loaded successfully
	 */
	public boolean loadGraph(File file, String[] nestedAttributesOrder, String[] nodeLabelAtts, String[] edgeImportAtts,
			int layout, int format) {

		// Progress report on console
		System.out.println(this.getClass().getName() + " Loading graph");
		Canvas.app.cursor(PConstants.WAIT);

		// Instantiate a graphLoader
		GraphLoader rootGraph = new GraphLoader(file.getAbsolutePath(), nestedAttributesOrder, nodeLabelAtts,
				edgeImportAtts, format);

		// Set rootGraph to Assembler and Filters
		Filters.getInstance().setRootGraph();
		
		communitiesOrderEdgeList =  rootGraph.getCommunitiesOrderEdgeList();

		/*
		 * ArrayList<String>comm1 = rootGraph.getCommunityNames();
		 * ArrayList<String>comm2 = rootGraph.getCommunityNames2();
		 * TreeMap<String, ArrayList<String>> hmap = new TreeMap<String,
		 * ArrayList<String>>();
		 * 
		 * //This is a test. Later it has to come from GraphLoader entirely
		 * hmap.put("Continent", comm1); hmap.put("ax", comm2);
		 */
		LinkedHashMap<String, ArrayList<String>> hmap = rootGraph.getNestedCommunities();

		// Root visual community.
		// Keep it commented unless you want to visualize the root graph with no
		// communities
		// rootVCommunity = createRootVCommunity(rootGraph.jungGraph);

		// List of Second Order Communities: sub communities
		// secondOrderVComm =
		// createSecondOrderVCommunities(GraphLoader.theGraph,
		// rootGraph.getCommunityNames(), layout);

		// First order community: Community of communities
		//firstOrderVComm = createFirstOrderVCommunity(rootGraph.getFirstOrderEdgeList(), secondOrderVComm, "Root",
		//		layout);
		// firstOrderVComm =
		// createFirstOrderVCommunity(rootGraph.getFirstOrderEdgeList(),
		// secondOrderVComm,
		// "FirstOrderCommunity", layout);
		firstOrderVComm = createStructureRecursive((DirectedSparseMultigraph<Node, Edge>) GraphLoader.theGraph, hmap,
				"basic", layout);
		//firstOrderVComm.initialize();


		Canvas.app.cursor(PConstants.ARROW);

		System.out.println(this.getClass().getName() + " Loading graph DONE!" + "\n");

		return true;
	}

	/**
	 * Creates a single VCommunity of the graph with no subCommunities yet
	 * contains all the VNodes
	 * 
	 * @param graph
	 *            the graph
	 * @return a VCommunity for the graph
	 */
	public VCommunity createRootVCommunity(Graph<Node, Edge> graph) {

		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(graph, RootContainer.CONCENTRIC, rootDimension);

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

		// make a SubContainer
		SubContainer subContainer = new SubContainer(graphTemp, layout, rootDimension);

		// make graph from communities
		// subContainer.populateGraphfromVCommunities(communities);

		// make graph from first order edge list
		subContainer.populateGraphfromEdgeList(firstOrderEdgeList);

		// Set the degrees of the internal graph. The graph is usually made of
		// community nodes
		subContainer.setGraphDegrees();

		// Name the community
		subContainer.setName(comName);

		// Assign visual elements to First Order Community
		subContainer.assignVisualElements(communities);

		// Initialize container NOTE: SEE VCommunity.show(). The container is
		// only initialized if it going to be shown, That's is why this method
		// is invoked inside VCommunity.show()
		// subContainer.initialize();

		String nodeID = comName + "_" + String.valueOf(0);

		VCommunity communityTemp = new VCommunity(new Node(nodeID), subContainer);

		// Set tier sequence
		communityTemp.setTierSequence(0);

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
		for (int i = 0; i < myGradient.length; i++) {
			myGradient[i] = new Color(myGradient[i].getRed(), myGradient[i].getGreen(), myGradient[i].getBlue(), 100);
		}

		System.out.println("     Generating Graphs for " + comNames.size() + " communities ..." + "\n"
				+ "     Creating Second Order VCommunity: ");

		// The subsetter containing each community subset. Subsetting is made
		// following the user defined import Nested attribute order. As of
		// October 2017 it uses only the first one
		// GraphSubsetterFilter subsetter = new GraphSubsetterFilter();

		int i = 0;
		for (String communityName : comNames) {

			// Get subGraph of each community
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.subGraphs.get(communityName);

			// Set In and Out Degree of each node of this community
			for (Node n : graphTemp.getVertices()) {
				n.setOutDegree(n.getMetadataSize() - 1, graphTemp.getSuccessorCount(n));
				n.setInDegree(n.getMetadataSize() - 1, graphTemp.getPredecessorCount(n));
				n.setDegree(n.getMetadataSize() - 1, graphTemp.degree(n));
			}

			// SubContainers for each VCommunity
			SubContainer containerTemp = new SubContainer(graphTemp, layout, new Dimension(600, 600), myGradient[i]);

			// Name container
			containerTemp.setName(communityName);

			containerTemp.setGraphDegrees();

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

			// Set tier sequence
			communityTemp.setTierSequence(1);

			// Set label visibility for overcrowded first order communities
			if (comNames.size() > 100)
				communityTemp.getComCover().setShowLabel(false);

			// Add VCommunity to list of VCommunities
			vCommunities.add(communityTemp);

			i++;
		}

		// for (VCommunity vC : vCommunities) {
		// vC.init();
		// }

		return vCommunities;
	}

	public void show() {
		// rootVCommunity.show();
		firstOrderVComm.showCommunity();
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


	public HashMap<Integer, ArrayList<String>> hmap = new HashMap<Integer, ArrayList<String>>();

	/**
	 * A recursive method which constructs a VCommunity with a hierarchy of
	 * nested VCommunities inside it.
	 * 
	 * @param graph
	 *            The graph which will be split in different according to the
	 *            first list in communityClasifiers
	 * @param communityClassifiers
	 *            Lists of communities in the different levels of the hierarchy
	 * @param nameCommunity
	 *            Name of the community that is returned.
	 * @param layout
	 * @return
	 */
	public VCommunity createStructureRecursive(DirectedSparseMultigraph<Node, Edge> graph,
			LinkedHashMap<String, ArrayList<String>> communityClassifiers, String nameCommunity, int layout) {

		// VCommunities whose will be added to 'nameCommunity'
		ArrayList<VCommunity> vCommunities = new ArrayList<VCommunity>();

		// The first partition comes from the first list of communityClassifiers
		Map.Entry<String,ArrayList<String>> entry = communityClassifiers.entrySet().iterator().next();
		String communityTag= entry.getKey();
		//String communityTag = communityClassifiers.firstKey();

		int numberOfCommunities = communityClassifiers.get(communityTag).size();

		System.out.println(
				this.getClass().getName() + " Number of communities for " + nameCommunity + " " + numberOfCommunities);

		// Color
		boolean colorBlindSafe = false;
		ColorBrewer[] qualitativePalettes = ColorBrewer.getQualitativeColorPalettes(colorBlindSafe);
		ColorBrewer myBrewer = qualitativePalettes[2];
		Color[] myGradient = myBrewer.getColorPalette(numberOfCommunities);
		//
		
		
		for (int i = 0; i < numberOfCommunities; i++) {

			String communityName = communityClassifiers.get(communityTag).get(i);

			System.out.println(this.getClass().getName() + " Working on community " + communityName);

			// SubGraph of each community
			DirectedSparseMultigraph<Node, Edge> graphTemp = Filters.filterNodeInCommunity(communityName, graph,
					communityTag);
			
			// Set In and Out Degree of each node of this community
			for (Node n : graphTemp.getVertices()) {
				n.setOutDegree(n.getMetadataSize() - 1, graphTemp.getSuccessorCount(n));
				n.setInDegree(n.getMetadataSize() - 1, graphTemp.getPredecessorCount(n));
				n.setDegree(n.getMetadataSize() - 1, graphTemp.degree(n));
			}
			

			// System.out.println(" Vertex Count After filter " +
			// graphTemp.getVertexCount());
			VCommunity communityTemp = null;

			if (graphTemp.getVertexCount() == 0) {
				continue;
			}

			// stopping criterion for the recursive method. CommunityClassifiers'
			// size is regularly larger than 1. At each iteration its size
			// decreases by 1 until there is only 1 object in that list, thus
			// when its size is equal to 1 it reached the last instance
			if (communityClassifiers.size() == 1) {

				// Creation of a community made of nodes instead of communities

				SubContainer containerTemp = new SubContainer(graphTemp, layout, new Dimension(600, 600),
						myGradient[i]);

				// Name container
				containerTemp.setName(communityName);
				containerTemp.setCommunityTag(communityTag);

				// Make Node for CommunityCover
				Node tmpNode = new Node(communityName);

				// Name Node
				tmpNode.setName(communityName);

				// Create temporal community
				communityTemp = new VCommunity(tmpNode, containerTemp);
				communityTemp.init();

			} else {
				// RECURSION
				// Creation of a community made of communities

				// communityClasifiers is copied
				LinkedHashMap<String, ArrayList<String>> communityClasifiersCopy = new LinkedHashMap<String, ArrayList<String>>(
						communityClassifiers);
				communityClasifiersCopy.remove(communityTag);
				communityTemp = createStructureRecursive(graphTemp, communityClasifiersCopy, communityName, layout);
				communityTemp.container.setCommunityTag(communityTag);
			}

			vCommunities.add(communityTemp);
		}
		
		// HACER AQUI UN JUNG GRAPH DE VCOMMUNITIES

		System.out.println(this.getClass().getName() + " Communities of " + nameCommunity + ":" + vCommunities.size());
		
		/*
		//This approach will be improved when edgesBetweenCommunies are implemented	
		DirectedSparseMultigraph<Node, Edge> graphBetweenCommunities = new DirectedSparseMultigraph<Node, Edge>();
		for(VCommunity vC : vCommunities){
			graphBetweenCommunities.addVertex(vC.getNode());
		}
		*/
		Graph<Node, Edge> graphBetweenCommunities = new DirectedSparseMultigraph<Node, Edge>();

		// SubContainers for each VCommunity
		SubContainer containerTemp = new SubContainer(graphBetweenCommunities, layout, new Dimension(600, 600), new Color(5));
		
		containerTemp.populateGraphfromEdgeList(communitiesOrderEdgeList.get(communityTag));
		containerTemp.setGraphDegrees();
		
		//
		//containerTemp.setGraphOfNodes(graph);

		// Name container
		containerTemp.setName(nameCommunity);
		

		// Initialize container
		// containerTemp.initialize();

		// Make Node for CommunityCover
		Node tmpNode = new Node(nameCommunity);

		containerTemp.assignVisualElements(vCommunities);
		

		// Name Node
		tmpNode.setName(nameCommunity);

		// Create temporal community
		VCommunity communityFather = new VCommunity(tmpNode, containerTemp);
		communityFather.init();

		return communityFather;
	}

	/**
	 * public void createStructureIterative(VCommunity parentCommunity,
	 * HashMap<Integer, ArrayList<String>> communityClasifiers){ for(ArrayList
	 * <String> a : communityClasifiers.get(arg0)){
	 * 
	 * } }
	 */
}
