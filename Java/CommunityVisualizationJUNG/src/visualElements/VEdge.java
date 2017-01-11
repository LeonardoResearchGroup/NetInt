package visualElements;

import utilities.mapping.Mapper;
import visualElements.gui.VisibilitySettings;
import visualElements.primitives.VisualAtom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import graphElements.Edge;

public class VEdge implements Serializable {
	private Edge edge;
	private boolean aboveArc, visibility;
	private VNode source, target;
	private BezierBeta bezier;
	// Visual Attributes
	private float thickness;
	private int maxThickness = 3;

	public VEdge(Edge edge) {
		this.edge = edge;
		aboveArc = true;
		thickness = 1; // (int) (Mapper.getInstance().convert(Mapper.LINEAR,
						// edge.getWeight(), 1, Mapper.EDGE_WEIGHT));
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
		bezier = new BezierBeta();
		int alpha = 100; // (int) (Mapper.getInstance().convert(Mapper.LINEAR,
							// edge.getWeight(), 255, Mapper.EDGE_WEIGHT));
		bezier.setAlpha(alpha);
	}

	public void show() {
		int alpha = 150;
		if (source.isVisible() && target.isVisible()) {
			if (visibility) {
				// Set thickness
				Canvas.app.strokeWeight(thickness);
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
				// Update source and target
				if (!VisibilitySettings.getInstance().getOnlyPropagation()) {
					bezier.setSourceAndTarget(source.pos, target.pos);
					// Edge mode: normal, head, tail or both
					if (source.isPropagated()) {
						bezier.drawBezier2D(Canvas.app, 2f);
						bezier.drawHeadBezier2D(Canvas.app, 2, alpha);
					} else {
						bezier.drawBezier2D(Canvas.app, 1f);
						bezier.drawHeadBezier2D(Canvas.app, thickness, alpha);
					}
				} else {
					if (source.isPropagated()) {
						bezier.setSourceAndTarget(source.pos, target.pos);
						// Edge mode: normal, head, tail or both
						bezier.drawBezier2D(Canvas.app, 2f);
						bezier.drawHeadBezier2D(Canvas.app, thickness, alpha);
					}
				}
			}
		}
		// **** Thickness mapping
		// If there is an event in the control panel update the thickness
		VisibilitySettings.getInstance();
		if (VisibilitySettings.eventOnVSettings && edge.getAttributeSize() > 0) {
			Object[] keys = edge.getAttributeKeys();
			// Set the Visibility with the first Attribute of Edge Import: "Body
			// Thickness"
			// System.out.println("VEDGE > "+ (String) keys[0]);
			try {
				float weight = edge.getFloatAttribute((String) keys[0]);
				// Set transparency
				alpha = (int) (Mapper.getInstance().convert(Mapper.LINEAR, weight, 155, Mapper.EDGE_WEIGHT));
				if (VisibilitySettings.getInstance().getFiltrosVinculo() != null) {

					switch (VisibilitySettings.getInstance().getFiltrosVinculo()) {
					case "Radial":
						thickness = Mapper.getInstance().convert(Mapper.RADIAL, weight, maxThickness,
								Mapper.EDGE_WEIGHT);
						break;
					case "Lineal":
						thickness = Mapper.getInstance().convert(Mapper.LINEAR, weight, maxThickness,
								Mapper.EDGE_WEIGHT);
						break;
					case "Logarithmic":
						thickness = Mapper.getInstance().convert(Mapper.LOGARITMIC, weight, maxThickness,
								Mapper.EDGE_WEIGHT);
						break;
					case "Sinusoidal":
						thickness = Mapper.getInstance().convert(Mapper.SINUSOIDAL, weight, maxThickness,
								Mapper.EDGE_WEIGHT);
						break;
					case "Sigmoid":
						thickness = Mapper.getInstance().convert(Mapper.SIGMOID, weight, maxThickness,
								Mapper.EDGE_WEIGHT);
					}
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
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
		Object[] keys = edge.getAttributeKeys();
		// Set the Visibility with the first Attribute of Edge Import: "Body
		// Thickness"
		try {

			if (edgeVisibilityThreshold > edge.getFloatAttribute((String) keys[0])) {
				visibility = false;
			} else {
				visibility = true;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}
}
