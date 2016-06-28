package visualElements;

import processing.core.*;
import visualElements.interactive.VisualAtom;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Node;

public class VEdge {
	Edge edge;
	boolean aboveArc;
	VisualAtom source, target;
	// int color;
	Bezier bezier;

	public VEdge(Edge edge) {
		this.edge = edge;
		aboveArc = true;
	}

	public void setSourceAndTarget(ArrayList<VisualAtom> visualAtoms, ArrayList<Node> nodes) {
		// look for the edge source's index in the collection of nodes 
		int indxSource = nodes.indexOf(edge.getSource());
		if (indxSource > -1) {
			// ask for its coordinates
			source = visualAtoms.get(indxSource);
		}
		// look in the collection of nodes where is the edge's target
		int indxTarget = nodes.indexOf(edge.getTarget());
		// System.out.println("VEdge>setSourceAndTarget: index Target:" +indxTarget);
		if (indxTarget > -1) {
			// ask for its coordinates
			target = visualAtoms.get(indxTarget);
		}
		if (source != null && target != null) {
			setDirection(source.pos.x, target.pos.x);
		}
	}

	public void makeBezier() {
		bezier = new Bezier(aboveArc);
	}

	public void show(PApplet app) {

		switch (edge.getMark()) {
		case 0:
			bezier.setColor(app.color(255, 100, 255));
			bezier.setAlpha(40);
			break;
		case 1:
			bezier.setColor(app.color(255, 0, 0));
			bezier.setAlpha(40);
			break;
		case 2:
			// propagation chain
			bezier.setColor(app.color(0, 255, 0));
			bezier.setAlpha(90);
			break;
		case 3:
			bezier.setColor(app.color(255, 255, 0));
			bezier.setAlpha(90);
			break;
		}
		bezier.setAndUpdateSourceAndTarget(source.pos, target.pos);
		bezier.setControl((source.pos.x - target.pos.x) / 2);
		bezier.drawBezier2D(app);
		bezier.drawHeadBezier2D(app);
	}

	private void setDirection(float posXOrg, float posXDes) {
		if (edge.isDirected()) {
			if (posXOrg - posXDes < 0)
				aboveArc = true;
			else
				aboveArc = false;
		}
	}

	// getters and setters
	public boolean isAboveArc() {
		return aboveArc;
	}

	public void setColor(int color) {
		bezier.setColor(color);
	}

	public VisualAtom getSource() {
		return source;
	}

	public void setSource(VisualAtom source) {
		this.source = source;
	}

	public VisualAtom getTarget() {
		return target;
	}

	public void setTarget(VisualAtom target) {
		this.target = target;
	}

	public Edge getEdge() {
		return edge;
	}
}
