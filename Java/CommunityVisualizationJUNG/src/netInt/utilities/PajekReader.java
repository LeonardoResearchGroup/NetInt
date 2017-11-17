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



import java.io.File;
import java.io.FileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import com.tinkerpop.blueprints.Vertex;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;

public class PajekReader {
	private DirectedSparseMultigraph<Node, Edge> graph;
	// Hash map <Name of community, Node object of a community>
	private HashMap<String, Node> communityNodes;
	private ArrayList<String> communities;
	// Edges between communities
	private ArrayList<Edge> edgesBetweenCommunities;

	/**
	 * Reader usually used to load pajek format files
	 */
	public PajekReader() {
		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
	}

	/**
	 * Reader used to read graphml file formats. It receives the path to the
	 * file, reads its contents and generates a Jung Graph
	 * 
	 * @param file
	 *            The url to the source file
	 */
	public PajekReader(String file) {
		graph = readPajek(file);
		communities = new ArrayList<String>();
		edgesBetweenCommunities = new ArrayList<Edge>();
	}

	/**
	 * Build a JungGraph from a pajek file.
	 * 
	 * @param filename
	 * @return
	 */
	private DirectedSparseMultigraph<Node, Edge> readPajek(String filename) {
		// <Name of community, Node object of a community>
		communityNodes = new HashMap<String, Node>();

		DirectedSparseMultigraph<Node, Edge> rtnGraph = new DirectedSparseMultigraph<Node, Edge>();
		try {
			File file = new File(filename);
			FileReader fr = new FileReader(file);
			Scanner br = new Scanner(fr);

			String token;
			br.next();
			token = br.next();
			int numberOfNodes = Integer.parseInt(token);
			Node[] nodes = new Node[numberOfNodes];

			for (int i = 0; i < numberOfNodes; i++) {
				token = br.next();
				int idS = Integer.parseInt(token) - 1;
				Node node = new Node(token);
				node.setCommunity("Root", 0);
				node.setCommunity(br.next(), 1);
				addCommunity(node.getCommunity(1));
				nodes[idS] = node;
			}
			System.out.println(br.next());
			while (br.hasNext()) {
				token = br.next();
				int idS = Integer.parseInt(token) - 1;
				token = br.next();
				int idT = Integer.parseInt(token) - 1;
				netInt.graphElements.Edge e = new netInt.graphElements.Edge(nodes[idS], nodes[idT], true);
				rtnGraph.addEdge(e, nodes[idS], nodes[idT], EdgeType.DIRECTED);
			}
			br.close();
			System.out.println("Cantidad de Vertices");
			System.out.println(rtnGraph.getVertexCount());

		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return rtnGraph;
	}

	/**
	 * Assigns attributes retrieved from a vertex of a JUNG graph to a node. The
	 * attribute names come from a list obtained from the source file, usually a
	 * graphml
	 * 
	 * @param vertex vertex of a JUNG graph
	 * @param node the recipient node
	 * @param nodeImportAttributes attribute names
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
						node.setAbsoluteAttribute(nodeImportAttributes[i], vertex.getProperty(nodeImportAttributes[i]));
					} else
						throw new NullPointerException();
				}
			}
		} catch (NullPointerException e) {
			// e.printStackTrace();
			System.out.println(this.getClass().getName() + ": No vertex label match, Check the attribute key");
		}
	}

	public DirectedSparseMultigraph<Node, Edge> getGraph() {
		return graph;
	}

	/**
	 * ArrayList of community values obtained from the import file
	 * 
	 * @return ArrayList of community values
	 */
	public ArrayList<String> getCommunities() {
		return communities;
	}

	private void addCommunity(String string) {
		// If community not in the list yet
		if (!communities.contains(string)) {
			communities.add(string);
			communityNodes.put(string, new Node(string));
		}
	}

	/**
	 * Looks for the equal node in the graph
	 * 
	 * @param graph the edu.uci.ics.jung.graph.Graph
	 * @param searchedNode the Node
	 * @return the equal node in the graph
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
	
	public ArrayList<Edge> getEdgesBetweenCommunities() {
		return edgesBetweenCommunities;
	}
}
