package executable;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collection;

import containers.Container;
import containers.RootContainer;
import containers.SubContainer;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import graphElements.Edge;
import graphElements.Node;
import visualElements.VCommunity;
import processing.core.*;

public class Logica {
	// Interaction elements
	public PVector mousePos;

	// Graph Elements
	private GraphLoader rootGraph;
	private ArrayList<DirectedSparseMultigraph<Node, Edge>> subGraphs;

	// Visual Elements
	private ArrayList<SubContainer> containers;
	private ArrayList<VCommunity> vCommunities;
	private VCommunity vMainCommunity;

	public Logica(PApplet app) {
		String XML_FILE = "../data/graphs/Risk.graphml";

		// ***** ROOT GRAPH*****
		rootGraph = new GraphLoader(XML_FILE, "Continent", "label");
		// Container of rootGraph
		RootContainer mainCommunity = new RootContainer(app, rootGraph.jungGraph, RootContainer.CIRCULAR,
				new Dimension(250, 250));
		mainCommunity.setName("Root");
		// Root Community
		vMainCommunity = new VCommunity(app, new Node(0), mainCommunity);

		containers = new ArrayList<SubContainer>();
		vCommunities = new ArrayList<VCommunity>();
		// ***** SUBGRAPHS & CONTAINERS *****
		subGraphs = new ArrayList<DirectedSparseMultigraph<Node, Edge>>();
		int cont = 0;
		for (String communityName : rootGraph.getCommunityNames()) {
			// SubGraphs
			DirectedSparseMultigraph<Node, Edge> graphTemp = GraphLoader.filterByCommunity(rootGraph.jungGraph,
					communityName);
			// SubContainers
			SubContainer containerTemp = new SubContainer(graphTemp, mainCommunity, Container.FRUCHTERMAN_REINGOLD ,
					new Dimension(100 + (cont * 30), 100 + (cont * 30)));
			containerTemp.setName(communityName);
			// Visualizers
			VCommunity communityTemp = new VCommunity(app, new Node(0), containerTemp);
			subGraphs.add(graphTemp);
			containers.add(containerTemp);
			vCommunities.add(communityTemp);
			cont++;
		}
	}

	public void show(PApplet app) {
		//vMainCommunity.show();
		for (VCommunity vC : vCommunities) {
			vC.show();
			if(vC.itOpens){
				for (VCommunity vC2 : vCommunities) {
					if (vC2.container.getName().equals("AF")){
						System.out.println("Abre");
						System.out.println("\n");
						System.out.println("\n");
						System.out.println("\n");
						System.out.println("\n");
						System.out.println("\n");
						System.out.println("Nueva Ronda");
						Node nodo = new Node(-1);
						for( Edge edge : vC.container.getGraph().getEdges() ){
							System.out.println(edge.getSource().getName());
							System.out.println(edge.getTarget().getName());
							System.out.println("Son iguales los braziles");
							if(edge.getSource().getName().equals("Brazil")){
								System.out.println("Entró");
								System.out.println(edge.getSource().getName());
								System.out.println(nodo.getName());
								System.out.println(edge.getSource() == nodo);
								nodo = edge.getSource();
							}
							
							
						}
//						System.out.println("Despues del filtro");
//						for( Edge edge : vC.container.getExternalEdges(rootGraph.jungGraph, vC2.container.getName(), vC2.container) ){
//							System.out.println(edge.getSource().getName());
//							System.out.println(edge.getTarget().getName());
//							for( Edge edge2 : vC.container.getGraph().getEdges() ){
//								System.out.println("source comunidad");
//								System.out.println(edge2.getSource().getName());
//								System.out.println("Target Comunidad");
//								System.out.println(edge2.getTarget().getName());
//								System.out.println("source Externo");
//								System.out.println(edge.getSource().getName());
//								System.out.println("Target Externo");
//								System.out.println(edge.getTarget().getName());
//								System.out.println(edge.getSource() == edge2.getSource());
//								System.out.println(edge.getTarget() == edge2.getSource());
//								System.out.println(edge.getSource() == edge2.getTarget());
//								System.out.println(edge.getTarget() == edge2.getTarget());
//							}
//						}
					}
				}
			}
		}
	}
}
