package utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TreeSet;

import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.util.io.graphml.GraphMLReader;

import graphElements.Node;

public class GraphReader {

	private Graph graph;

	public static void main(String[] args) throws Exception {
		String XML_FILE = "/home/cdloaiza/Dropbox/Docs Icesi/CAOBA/Normalizacion/sectores/L-UN-MOV.graphml";
		GraphReader gr = new GraphReader(XML_FILE);
		gr.getGraph().printNodes();
		gr.getGraph().printEdges();

	}

	public GraphReader(String xmlFile) {
		graph = new TinkerGraph();
		GraphMLReader reader = new GraphMLReader(graph);

		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(xmlFile));
			reader.inputGraph(is);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public graphElements.Graph getGraph() {
		graphElements.Graph rtn = new graphElements.Graph();
		ArrayList<graphElements.Edge> edges = new ArrayList<graphElements.Edge>();
		ArrayList<Node> nodes = new ArrayList<Node>();

		// System.out.println("Vertices of " + graph);
		for (Vertex vertex : graph.getVertices()) {
			// System.out.println(vertex.getId().toString().replace("n", "") + "
			// está en el comunidad " + vertex.getProperty("comunidad"));
			int i = Integer.parseInt(vertex.getId().toString().replace("n", ""));
			Node tmpA = new Node(i);
			tmpA.includeInSubGraph((int) Double.parseDouble(vertex.getProperty("comunidad").toString()));
			nodes.add(tmpA);
		}
		// System.out.println("Edges of " + graph);
		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// System.out.println(edge.getLabel());
			// System.out.println(edge.getVertex(Direction.IN).getId());

			Vertex source = edge.getVertex(Direction.IN);
			int idSOurce = Integer.parseInt(source.getId().toString().replace("n", ""));
			Node tmpA = new Node(idSOurce);
			tmpA.includeInSubGraph((int) Double.parseDouble(source.getProperty("comunidad").toString()));

			Vertex target = edge.getVertex(Direction.OUT);
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
			Node tmpB = new Node(idTarget);
			tmpB.includeInSubGraph((int) Double.parseDouble(target.getProperty("comunidad").toString()));

			// Aqui existe el problema que el nodo source y target usados para
			// crear la instancia de edge no son los mismos nodos que hay en la
			// colección de nodos.
			graphElements.Edge e = new graphElements.Edge(tmpA, tmpB, true);
			edges.add(e);
		}

		rtn = new graphElements.Graph(nodes, edges);
		return rtn;

	}

	public graphElements.Graph getGraphJuan() {
		graphElements.Graph rtn = new graphElements.Graph();
		ArrayList<graphElements.Edge> edges = new ArrayList<graphElements.Edge>();
		ArrayList<Node> nodes = new ArrayList<Node>();

		for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
			// From each edge retrieve the source and target vertex
			Vertex source = edge.getVertex(Direction.IN);
			Vertex target = edge.getVertex(Direction.OUT);
			// Get their ID
			int idSource = Integer.parseInt(source.getId().toString().replace("n", ""));
			int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
			// Instantiate Nodes
			Node sourceNode = new Node(idSource);
			Node targetNode = new Node(idTarget);
			// Classify in communities
			sourceNode.includeInSubGraph((int) Double.parseDouble(source.getProperty("comunidad").toString()));
			targetNode.includeInSubGraph((int) Double.parseDouble(target.getProperty("comunidad").toString()));
			// add nodes to collection.
			int index = nodes.indexOf(sourceNode);
			// If sourceNode not in the list yet
			if (index == -1) {
				// Add to the list
				nodes.add(sourceNode);
			} else {
				// Get from the list
				sourceNode = nodes.get(index);
			}
			// If targetNode not in the list yet
			index = nodes.indexOf(targetNode);
			if (index == -1) {
				nodes.add(targetNode);
			} else {
	
				targetNode = nodes.get(index);
			}
			// Generate graphElements.Edge
			graphElements.Edge e = new graphElements.Edge(sourceNode, targetNode, true);
			// Add graphElements.Edge to collection
			edges.add(e);
		}

		// This needs a better solution. The ideal solution is to rework the
		// entire code replacing the nodes ArrayList for a TreeSet. It might be
		// simpler instead when it is needed to retrieve a particular instance
		// from the set.
		// That is the case of some methods in Container and RandomGraphFactory
		ArrayList<Node> listNodes = new ArrayList<Node>(nodes);
		rtn = new graphElements.Graph(listNodes, edges);
		return rtn;

	}

}