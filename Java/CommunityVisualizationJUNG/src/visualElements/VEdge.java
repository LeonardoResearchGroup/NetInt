package visualElements;

import processing.core.*;
import utilities.mapping.Mapper;
import visualElements.gui.VisibilitySettings;
import visualElements.primitives.VisualAtom;

import java.util.ArrayList;
import graphElements.Edge;

public class VEdge {
	private Edge edge;
	private boolean aboveArc, visibility;
	private VNode source, target;
	private Bezier bezier;
	// Visual Attributes
	private float thickness;

	public VEdge(Edge edge) {
		this.edge = edge;
		aboveArc = true;
		thickness = 1; //(int) (Mapper.getInstance().convert(Mapper.LINEAR, edge.getWeight(), 1, Mapper.EDGE_WEIGHT));
		if (thickness < 1) {
			thickness = 1;
		}
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
		int alpha = 100; //(int) (Mapper.getInstance().convert(Mapper.LINEAR, edge.getWeight(), 255, Mapper.EDGE_WEIGHT));
		bezier.setAlpha(alpha);
	}

	public void show(PApplet app) {
		if (source.isVisible() && target.isVisible()) {
			if (visibility) {
				// Set thickness
				app.strokeWeight(thickness);
				// Set color
				if (source.isPropagated()) {
					bezier.color(Bezier.PROPAGATE);
					// bezier.setAlpha((int)alpha);
					// setAlpha(90);
				} else {
					bezier.color(Bezier.NORMAL);
					// bezier.setAlpha((int)alpha);
					// setAlpha(40);
				}
				// bezier.brighter();
				// Update source and target
				bezier.setAndUpdateSourceAndTarget(source.pos, target.pos);
				bezier.setControl((source.pos.x - target.pos.x) / 2);
				// Edge mode: normal, head, tail or both
				bezier.drawBezier2D(app, 1f);
				int alpha = 100; //(int) (Mapper.getInstance().convert(Mapper.LINEAR, edge.getWeight(), 255, Mapper.EDGE_WEIGHT));
				bezier.drawHeadBezier2D(app, thickness, alpha);
			}
		}
		// **** Thickness mapping
		if (VisibilitySettings.getInstance().getFiltrosVinculo() != null) {
			switch (VisibilitySettings.getInstance().getFiltrosVinculo()) {
			case "Radial":
				thickness = Mapper.getInstance().convert(Mapper.RADIAL, edge.getWeight(), 3, Mapper.EDGE_WEIGHT);
				break;
			case "Lineal":
				thickness = Mapper.getInstance().convert(Mapper.LINEAR, edge.getWeight(), 3, Mapper.EDGE_WEIGHT);
				break;
			case "Logarithmic":
				thickness = Mapper.getInstance().convert(Mapper.LOGARITMIC, edge.getWeight(), 3, Mapper.EDGE_WEIGHT);
				break;
			case "Sinusoidal":
				thickness = Mapper.getInstance().convert(Mapper.SINUSOIDAL, edge.getWeight(), 3, Mapper.EDGE_WEIGHT);
				break;
			case "Sigmoid":
				thickness = Mapper.getInstance().convert(Mapper.SIGMOID, edge.getWeight(), 3, Mapper.EDGE_WEIGHT);
			}
		}
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

	public boolean equals(Object obj) {
		VEdge vEdge = (VEdge) obj;
		boolean sourceIsEqual = vEdge.getSource().equals(this.getSource());
		boolean targetIsEqual = vEdge.getTarget().equals(this.getTarget());
		return sourceIsEqual && targetIsEqual;
	}

	public void setVisibility(float edgeVisibilityThreshold) {
		if (edgeVisibilityThreshold > edge.getWeight()) {
			visibility = false;
		} else {
			visibility = true;
		}

	}
}
