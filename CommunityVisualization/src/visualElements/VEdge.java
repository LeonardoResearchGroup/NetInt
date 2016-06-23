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

	public void setCoordinates(ArrayList<VisualAtom> vAtom, ArrayList<Node> nodes) {
		// look in the collection of nodes where is the edge's source
		int indxOrg = nodes.indexOf(edge.getSource());
		if (indxOrg > -1) {
			// ask for its coordinates
			source = vAtom.get(indxOrg);
		}
		// look in the collection of nodes where is the edge's target
		int indxDes = nodes.indexOf(edge.getTarget());
		if (indxDes > -1) {
			// ask for its coordinates
			target = vAtom.get(indxDes);
		}
		// This works fine in linear arrays
		setDirection(source.pos.x, target.pos.x);
	}

	public void makeBezier() {
		bezier = new Bezier(aboveArc);
	}

	public void show(PApplet app) {

		switch (edge.getMark()) {
		case 0:
			bezier.setColor(app.color(255, 100, 255));
			bezier.setAlpha(20);
			break;
		case 1:
			bezier.setColor(app.color(255, 0, 0));
			bezier.setAlpha(20);
			break;
		case 2:
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
}
