package utilities.containers;

import graphElements.Edge;

import java.util.ArrayList;

import processing.core.PApplet;
import visualElements.VEdge;
import visualElements.interactive.VisualAtom;

public abstract class Container {

	public PApplet app;
	public ArrayList<VisualAtom> visualElements;


	public Container(PApplet app) {
		this.app = app;
	}
	
	public void addElements(ArrayList<VisualAtom> collection){
		visualElements = collection;
	}
	
	public abstract void updateContainer();

	public void show(ArrayList<Edge> edges){
	
	for (int i = 0; i < graph.getEdges().size(); i++) {
		Edge e = graph.getEdges().get(i);
		VEdge tmp = new VEdge(e);
		tmp.layout(VAtoms, graph.getVertices());
		tmp.makeBezier();
		VEdges.add(tmp);
	}}
}
