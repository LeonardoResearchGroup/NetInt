package utilities;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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
  
  public GraphReader(String xmlFile){
	graph = new TinkerGraph();
    GraphMLReader reader = new GraphMLReader(graph);
 
    InputStream is;
	try {
		is = new BufferedInputStream(new FileInputStream(xmlFile));
		reader.inputGraph(is);
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
  
public graphElements.Graph getGraph(){
	graphElements.Graph rtn = new graphElements.Graph();
	ArrayList<graphElements.Edge> edges = new ArrayList<graphElements.Edge>();
	ArrayList<Node> nodes = new ArrayList<Node>();

	System.out.println("Vertices of " + graph);
	for (Vertex vertex : graph.getVertices()) {
	  System.out.println(vertex.getId().toString().replace("n", "") + " está en el comunidad " + vertex.getProperty("comunidad"));
	  int i = Integer.parseInt(vertex.getId().toString().replace("n", ""));
	  Node tmpA = new Node(i);
	  tmpA.includeInSubGraph((int)Double.parseDouble(vertex.getProperty("comunidad").toString()));
	  nodes.add(tmpA);
	}
	System.out.println("Edges of " + graph);
	for (com.tinkerpop.blueprints.Edge edge : graph.getEdges()) {
	  //System.out.println(edge.getLabel());
	  //System.out.println(edge.getVertex(Direction.IN).getId());
	  Vertex source = edge.getVertex(Direction.IN);
	  int idSOurce = Integer.parseInt(source.getId().toString().replace("n", ""));
	  Node tmpA = new Node(idSOurce);
	  tmpA.includeInSubGraph((int)Double.parseDouble(source.getProperty("comunidad").toString()));
	  Vertex target = edge.getVertex(Direction.OUT);
	  int idTarget = Integer.parseInt(target.getId().toString().replace("n", ""));
	  Node tmpB = new Node(idTarget);
	  tmpB.includeInSubGraph((int)Double.parseDouble(target.getProperty("comunidad").toString()));
	  
	  graphElements.Edge e = new graphElements.Edge(tmpA, tmpB, true);
	  edges.add(e);
  
	 }  
	
	rtn = new graphElements.Graph(nodes, edges);
	return rtn;
	  
  }
  
  
}