package visualElements;

import processing.core.*;
import visualElements.interactive.VisualAtom;
import java.util.ArrayList;
import graphElements.Edge;

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

	public void setSourceAndTarget(ArrayList<VNode> visualNodes) {
		int cont = 0;
		for (VNode atm : visualNodes) {
			// Source
			if (atm.hasNode(edge.getSource())) {
				source = visualNodes.get(cont);
			}
			// Target
			if (atm.hasNode(edge.getTarget())) {
				target = visualNodes.get(cont);
			}
			cont++;
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
		// System.out.println("VEdge>show(): source= " + source);
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
