package visualElements;

import processing.core.*;
import visualElements.primitives.VisualAtom;

import java.util.ArrayList;
import graphElements.Edge;

public class VEdge {
	private Edge edge;
	private boolean aboveArc;
	private VNode source, target;
	private Bezier bezier;
	private float thickness;

	public VEdge(Edge edge) {
		this.edge = edge;
		aboveArc = true;
		thickness = 1f;
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
		// Set thickness
		app.strokeWeight(thickness);
		// Set color
		if (source.isPropagated()){
			bezier.color(Bezier.PROPAGATE);
			//setAlpha(90);
		}else{
			bezier.color(Bezier.NORMAL);
			//setAlpha(40);

		}
		bezier.brighter();
		// Update source and target
		bezier.setAndUpdateSourceAndTarget(source.pos, target.pos);
		bezier.setControl((source.pos.x - target.pos.x) / 2);
		// Edge mode: simple, head, tail or both
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

	public void setSource(VNode source) {
		this.source = source;
	}

	public VisualAtom getTarget() {
		return target;
	}

	public void setTarget(VNode target) {
		this.target = target;
	}

	public Edge getEdge() {
		return edge;
	}

	public float getThickness() {
		return thickness;
	}

	public void setThickness(float thickness) {
		this.thickness = thickness;
	}
	
	public void setAlpha(int alpha){
		bezier.setAlpha(alpha);
	}
	
	public boolean equals(Object obj) {
		VEdge vEdge = (VEdge) obj;
		boolean sourceIsEqual = vEdge.getSource().equals(this.getSource());
		boolean targetIsEqual = vEdge.getTarget().equals(this.getTarget());
		return sourceIsEqual && targetIsEqual;
	}
}
