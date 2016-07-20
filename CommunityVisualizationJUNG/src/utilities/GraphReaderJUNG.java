package utilities;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import graphElements.Edge;
import graphElements.Node;

public class GraphReaderJUNG {
	
	Reader reader;
	GraphMLReader2<DirectedSparseMultigraph<Node, Edge>, Node, Edge> mlReader;
	DirectedSparseMultigraph<Node, Edge> graph;
	int cont = 0;
	
	public GraphReaderJUNG(String file){
		try {
			reader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Graph
		Transformer<GraphMetadata, DirectedSparseMultigraph<Node, Edge>> graphTransformer;
		graphTransformer = new Transformer<GraphMetadata, DirectedSparseMultigraph<Node, Edge>>() {
			public DirectedSparseMultigraph<Node, Edge> transform(GraphMetadata gmd) {
				return new DirectedSparseMultigraph<Node, Edge>();
			}
		};
		// Vertex
		Transformer<NodeMetadata, Node> vertexTransformer;
		vertexTransformer = new Transformer<NodeMetadata, Node>() {
			public Node transform(NodeMetadata nmd) {
				Node v = new Node();
				v.setName(nmd.getProperty("label"));
				v.setCommunity(nmd.getProperty("continent"));
				v.setId(String.valueOf(cont));
				cont++;
				return v;
			}
		};
		// Edge
		Transformer<EdgeMetadata, Edge> edgeTransformer;
		edgeTransformer = new Transformer<EdgeMetadata, Edge>() {
			public Edge transform(EdgeMetadata emd) {
				Edge e = new Edge();
				e.setName(emd.getProperty("edgelabel"));
				e.setDirected(true);
				try{
				e.setWeight(Float.parseFloat(emd.getProperty("weight")));
				}catch (NumberFormatException nE){
					nE.printStackTrace();
				}
				return e;
			}
		};
		// HyperEdge
		Transformer<HyperEdgeMetadata, Edge> hyperEdgeTransformer;
		hyperEdgeTransformer = new Transformer<HyperEdgeMetadata, Edge>() {
			public Edge transform(HyperEdgeMetadata emd) {
				Edge e = new Edge();
				System.out.println("emd2 " + emd);
				return e;
			}
		};

		mlReader = new GraphMLReader2<DirectedSparseMultigraph<Node, Edge>, Node, Edge>(reader, graphTransformer,
				vertexTransformer, edgeTransformer, hyperEdgeTransformer);
		
		// Read Graph
		try {
			graph = mlReader.readGraph();
		} catch (GraphIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DirectedSparseMultigraph<Node, Edge> getDirectedGraph(){
		return graph;
	}

}
