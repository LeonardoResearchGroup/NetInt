/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.mapping.Mapper;

/**
 * 
 * @author jsalam
 *
 */
public class GraphmlReader {
	private Graph graph;
	// Hash map <Name of community, Node object of a community>
	private HashMap<String, Node> vCommunityNodes;
	// Hash map <Name of classifier, Node object of a community>
	private HashMap<String, Node> vCommunityNodesPerClassifier;
	private ArrayList<String> communities;
	private LinkedHashMap<String, ArrayList<String>> nestedCommunities = new LinkedHashMap<String, ArrayList<String>>();

	private ArrayList<String> communities2;
	// Edges between communities
	private ArrayList<Edge> edgesBetweenCommunities;
	// Edges between communities
	private  HashMap<String,ArrayList<Edge>> edgesBetweenCommunitiesPerClassifier;

	/**
	 * Reader usually used to load pajek format files
	 */
	public GraphmlReader() {
		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
		edgesBetweenCommunitiesPerClassifier = new HashMap<String,ArrayList<Edge>>();
		vCommunityNodesPerClassifier = new HashMap<String,Node>();
	}

	/**
	 * Reader used to read graphml file formats. It receives the path to the
	 * file, reads its contents and generates a Jung Graph. It relies on
	 * GraphMLReader from Thinkerpop
	 * 
	 * @param file
	 *            The url to the source file
	 */
	public GraphmlReader(String file) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);
		InputStream input;
		try {
			input = new BufferedInputStream(new FileInputStream(file));
			reader.inputGraph(input);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		communities = new ArrayList<String>();
		communities2 = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
		edgesBetweenCommunitiesPerClassifier = new HashMap<String,ArrayList<Edge>>();
		vCommunityNodesPerClassifier = new HashMap<String,Node>();

	}

	/**
	 * This class was initially designed to return an array of nodes but it
	 * needed to be changed to a TreeMap because it was not possible to
	 * determine the final size of the array in advance.
	 * 
	 * @param nodeImportAttributes
	 *            The list of user defined attributes for node importing
	 * @param saveCategoricalAttributes
	 *            true if you want to save the categorical attributes in the
	 *            general collection of attributes stored in Mapper Class. The
	 *            collection does not accept duplicates. Enabling this might
	 *            occupy a big chunk of memory because for instance all the node
	 *            names will be stored in the collection
	 * @return The collection of nodes ordered by the Integer version of their
	 *         Id
	 */
	private TreeMap<Integer, Node> makeNodes(String[] nestedAttributesOrder, String[] nodeImportAttributes, boolean saveCategoricalAttributes) {
		System.out.println(this.getClass().getName() + " Instantiating Nodes...");
		
		initializeNestedCommunties(nestedAttributesOrder);
		
		TreeMap<Integer, Node> theNodes = new TreeMap<Integer, Node>();

		// *** Go over graph vertex and set all nodes
		for (Vertex vertex : graph.getVertices()) {

			// Get vertex ID
			int id = Integer.parseInt(vertex.getId().toString().replace("n", ""));

			// Make a node with the retrieved ID
			Node nodeTmp = new Node(String.valueOf(id));

			try {
				for(String classifier: nestedAttributesOrder){
					System.out.println(this.getClass().getName() +
							" classifier " + classifier);
					Object value = vertex.getProperty(classifier);
					if (value != null) {
						System.out.println(this.getClass().getName() +
								" value " + value.toString());
						addCommunity(classifier, value.toString());
					}
				}
				// For the first two attributes: node community and node
				// name
				
				if (vertex.getProperty(nodeImportAttributes[0]) != null) {

					// set root community as absolute attribute
					nodeTmp.setCommunity("Root", 0);

					// set next community as relative attribute
					nodeTmp.setCommunity(vertex.getProperty(nodeImportAttributes[0]).toString(), 1);
					addCommunity(vertex.getProperty(nodeImportAttributes[0]).toString());
				} else {
					// for root importing, set root community to absolute and
					// relative attributes
					nodeTmp.setCommunity("Root", 0);
					nodeTmp.setCommunity("Root", 1);
				}
				

				// Label
				if (vertex.getProperty(nodeImportAttributes[0]) != null) {
					nodeTmp.setName(vertex.getProperty(nodeImportAttributes[0]).toString());
				} else {
					nodeTmp.setName("no name yet");
					//throw new NullPointerException();
				}

			} catch (NullPointerException e) {
				System.out.println(this.getClass().getName() + " NullPointerException making nodes ");
			}

			for (int i = 2; i < nodeImportAttributes.length; i++) {
				try {
					// For the remaining attributes
					// Check if it does exist a property matching other
					// attributes
//					System.out.println("nodeImportAttributes: " + nodeImportAttributes[i]);
//					System.out.println("value: " + vertex.getProperty(nodeImportAttributes[i]));
//					System.out.println("Boorrar: " );
//					addCommunity2(vertex.getProperty(nodeImportAttributes[i]).toString());
					//if (vertex.getProperty(nodeImportAttributes[i]) != null) {
						//nodeTmp.setAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]).toString());
						String a = vertex.getProperty(nodeImportAttributes[1]).toString().substring(vertex.getProperty(nodeImportAttributes[1]).toString().length() - 1);
						addCommunity2(a);
						nodeTmp.setAttribute("ax", a);
//					} else {
//						// for root importing
//						throw new NullPointerException();
//					}
				} catch (NullPointerException e) {
					System.out.println(this.getClass().getName()
							+ " NullPointerException making nodes with remaining attributes ");
				}
			}

			// Load all the node attributes from the graphml file
			for (String key : vertex.getPropertyKeys()) {
				if (nodeTmp.getAttribute(key) == null) {
					System.out.println(key);
					nodeTmp.setAttribute(key, vertex.getProperty(key));
				}
			}
			// Setting max min boundaries in Mapper class
			Mapper.getInstance().setMaxMinNodeAttributes(nodeTmp);
			if (saveCategoricalAttributes) {
				Mapper.getInstance().setCategoricalNodeAttributes(nodeTmp);
			}
			theNodes.put(id, nodeTmp);
		}
		return theNodes;
	}

	/**
	 * GENERAL METHOD TO GET THE JUNG GRAPH
	 * 
	 * @param nodeImportAttributes
	 *            the vector of node attributes
	 * @param edgeImportAttributes
	 *            the vector of edge attributes
	 * @param saveCategoricalAttributes
	 *            true if you want to save the categorical attributes in the
	 *            general collection of attributes stored in Mapper Class. The
	 *            collection does not accept duplicates. Enabling this might
	 *            occupy a big chunk of memory because for instance all the node
	 *            names will be stored in the collection
	 * @return
	 */
	public DirectedSparseMultigraph<Node, Edge> getJungDirectedGraph(String[] nestedAttributesOrder, String[] nodeImportAttributes, String[] edgeImportAttributes, boolean saveCategoricalAttributes) {
		// Create the graph to be returned
		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		// Notify progress on console
		System.out.println(this.getClass().getName() + " Getting Jung Directed Graph...");
		// Hash map <Name of community, Node object of a community>
		vCommunityNodes = new HashMap<String, Node>();

		// **** MAKE NODES ****
		TreeMap<Integer, Node> nodes = makeNodes(nestedAttributesOrder, nodeImportAttributes, saveCategoricalAttributes);
		
		initializeEdgesBetweenCommunities(nestedAttributesOrder);

		// **** CREATE EDGES ****
		System.out.println(this.getClass().getName() + " Instantiating Edges...");
		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.OUT);
			Vertex target = edge.getVertex(Direction.IN);
			// Get their ID
			Integer idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			Integer idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));

			// **** MAKE EDGE FROM SOURCE AND TARGET NODES ****
			netInt.graphElements.Edge e = new netInt.graphElements.Edge(nodes.get(idSource), nodes.get(idTarget), true);
			// Check if the edge has any of the edge Import attributes
			if (edgeImportAttributes.length > 0) {
				for (int i = 0; i < edgeImportAttributes.length; i++) {
					try {
						// Retrieve the attribute value and check if it is null
						if (edge.getProperty(edgeImportAttributes[i]) != null) {
							Object tmpProperty = edge.getProperty(edgeImportAttributes[i]);
							// Add the attribute value to the temporal edge;
							e.setAttribute(edgeImportAttributes[i], tmpProperty);
						} else
							throw new NullPointerException();
					} catch (NullPointerException exception) {
						System.out.println(this.getClass().getName()
								+ " Null Pointer Exception. Edges in the source file don't have attributes named: "
								+ edgeImportAttributes[i]);
					}
				}
			} else {
				// if no attributes selected set the weight to 1
				e.setAttribute("weight", 1);
			}

			// Load all the edge attributes from the graphml file
			for (String key : edge.getPropertyKeys()) {
				if (e.getAttribute(key) == null) {
					e.setAttribute(key, edge.getProperty(key));
				}
			}

			// Setting max min boundaries in Mapper class
			Mapper.getInstance().setMaxMinEdgeAttributes(e);
			if (saveCategoricalAttributes) {
				Mapper.getInstance().setCategoricalEdgeAttributes(e);
			}
			
			
			
			
			for(String classifier: nestedAttributesOrder){
				// For the first order community graph
				Edge metaE = new Edge(vCommunityNodesPerClassifier.get(nodes.get(idSource).getAttribute(classifier)),
						vCommunityNodesPerClassifier.get(nodes.get(idTarget).getAttribute(classifier)), true);
				// if no attributes selected set the weight to 1
				metaE.setAttribute("weight", 1);
	
				if (!edgesBetweenCommunitiesPerClassifier.get(classifier).contains(metaE)) {
					System.out.println(classifier);
					System.out.println("Veces");
					
					edgesBetweenCommunitiesPerClassifier.get(classifier).add(metaE);
				}
			}
			
			
			
			
			// Create the edge with source and target nodes
			rtnGraph.addEdge(e, nodes.get(idSource), nodes.get(idTarget), EdgeType.DIRECTED);
		}
		System.out.println(this.getClass().getName() +" Edges Continent" +  edgesBetweenCommunitiesPerClassifier.get("Continent").get(0).getTarget().getId());
		return rtnGraph;
	}

	/**
	 * Assigns attributes retrieved from a vertex of a JUNG graph to a node. The
	 * attribute names come from a list obtained from the source file, usually a
	 * graphml
	 * 
	 * @param vertex
	 * @param node
	 * @param nodeImportAttributes
	 */
	public void assignNodeAttributes(Vertex vertex, Node node, String[] nodeImportAttributes) {
		// For the first two attributes: node community and node name
		try {
			// Community key
			if (vertex.getProperty(nodeImportAttributes[0]) != null) {
				node.setCommunity("Root", 0);
				node.setCommunity(vertex.getProperty(nodeImportAttributes[0]).toString(), 1);
				addCommunity(node.getCommunity(1));
			} else
				throw new NullPointerException();
			// Label key
			if (vertex.getProperty(nodeImportAttributes[1]) != null) {
				node.setName(vertex.getProperty(nodeImportAttributes[1]).toString());

			} else
				throw new NullPointerException();

			for (int i = 2; i < nodeImportAttributes.length; i++) {

				// For the remaining attributes
				if (i > 1) {
					// Check if it does exist a property matching other
					// attributes
					if (vertex.getProperty(nodeImportAttributes[i]) != null) {
						node.setAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]));
					} else
						throw new NullPointerException();
				}
			}
		} catch (NullPointerException e) {
			// e.printStackTrace();
			System.out.println(this.getClass().getName() + ": No vertex label match, Check the attribute key");
		}
	}

	/**
	 * ArrayList of community values obtained from the import file
	 * 
	 * @return
	 */
	public ArrayList<String> getCommunities() {
		return communities;
	}
	
	public ArrayList<String> getCommunities2() {
		return communities2;
	}
	
	

	private void addCommunity(String string) {
		// If community not in the list yet
		if (!communities.contains(string)) {
			communities.add(string);
			vCommunityNodes.put(string, new Node(string));
		}
	}
	
	private void addCommunity(String classiffier, String community) {
		// If community not in the list yet
		ArrayList<String>communitiesOfClassifier = nestedCommunities.get(classiffier);
		if (!communitiesOfClassifier.contains(community)) {
			communitiesOfClassifier.add(community);
			vCommunityNodesPerClassifier.put(community, new Node(community));
		}
	}
	
	private void initializeNestedCommunties(String[] nestedAttributesOrder){
		for(String classifier:nestedAttributesOrder){
			nestedCommunities.put(classifier, new ArrayList<String>());
		}
	}
	
	private void initializeEdgesBetweenCommunities(String[] nestedAttributesOrder){
		for(String classifier:nestedAttributesOrder){
			edgesBetweenCommunitiesPerClassifier.put(classifier, new ArrayList<Edge>());
		}
	}
	
	private void addCommunity2(String string) {
		// If community not in the list yet
		if (!communities2.contains(string)) {
			communities2.add(string);
		}
	}

	/**
	 * Looks for the equal node in the graph
	 * 
	 * @param graph
	 * @param searchedNode
	 * @return
	 */
	protected Node getEqualNode(edu.uci.ics.jung.graph.Graph<Node, Edge> graph, Node searchedNode) {
		Node rtn = null;
		for (Node node : graph.getVertices()) {
			if (searchedNode.equals(node)) {
				rtn = node;
				return rtn;
			}
		}
		return rtn;
	}

	/**
	 * Return the list of all keys associated to vertex read from the graphml
	 * file
	 * 
	 * @return
	 */
	public String[] getNodesGraphmlKeys() {
		String[] rtn = null;
		for (Vertex v : graph.getVertices()) {
			v.getPropertyKeys().toArray(rtn);
			return rtn;
		}
		return rtn;
	}

	/**
	 * Return the list of all keys associated to edges read from the graphml
	 * file
	 * 
	 * @return
	 */
	public String[] getEdgesGraphmlKeys() {
		String[] rtn = null;
		for (com.tinkerpop.blueprints.Edge e : graph.getEdges()) {
			e.getPropertyKeys().toArray(rtn);
			return rtn;
		}
		return rtn;
	}

	public ArrayList<Edge> getEdgesBetweenCommunities() {
		return edgesBetweenCommunities;
	}
	
	public HashMap<String,ArrayList<Edge>> getEdgesBetweenCommunitiesPerClassifier() {
		return edgesBetweenCommunitiesPerClassifier;
	}
	
	public LinkedHashMap<String, ArrayList<String>> getNestedCommunities() {
		return nestedCommunities;
	}

}