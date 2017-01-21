package visualElements;

import utilities.mapping.Mapper;
import visualElements.gui.VisibilitySettings;
import visualElements.primitives.VisualAtom;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

import graphElements.Edge;
import processing.core.PVector;

/**
 * The visual representation of a grahElements.Edge. Each VEdge has a source and
 * target VNodes corresponding to the visual representation of the instances of
 * grahElements.Node associated to the grahElements.Edge
 * 
 * @author jsalam
 *
 */
public class VEdge implements Serializable {
	private Edge edge;
	// Visibility attributes
	private boolean visibility, hidden;
	// Source and target nodes
	private VNode source, target;
	// The curve linking the nodes
	private Bezier bezier;
	// Visual Attributes
	private float thickness;


	public VEdge(Edge edge) {
		this.edge = edge;
		thickness = 1; // (int) (Mapper.getInstance().convert(Mapper.LINEAR,
						// edge.getWeight(), 1, Mapper.EDGE_WEIGHT));
		if (thickness < 1) {
			thickness = 1;
		}
	}

	/**
	 * Retrieves the vNodes that are the visual representation of the source and
	 * target nodes associated to the edge.
	 * 
	 * @param visualNodes
	 */
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
	}

	public void makeBezier() {
		bezier = new Bezier(source.pos, target.pos);
		int alpha = 100; // (int) (Mapper.getInstance().convert(Mapper.LINEAR,
							// edge.getWeight(), 255, Mapper.EDGE_WEIGHT));
		bezier.setAlpha(alpha);
	}

	public void setLayoutAndCenter(int layout, PVector center) {
		bezier.setLayoutAndCenter(layout, center);
	}

	/**
	 * Visualize the VEdge on the Canvas
	 */
	public void show() {
		int alpha = 150;
		if (source.isVisible() && target.isVisible()) {
			if (visibility) {
				// Set thickness
				Canvas.app.strokeWeight(thickness);
				// Set color
				if (source.isPropagated()) {
					bezier.color(Bezier.PROPAGATE);
				} else {
					bezier.color(Bezier.NORMAL);
				}
				// If visualize the nodes and edges
				if (!VisibilitySettings.getInstance().getOnlyPropagation()) {
					bezier.setSourceAndTarget(source.pos, target.pos);
					// Edge mode: normal, head, tail or both
					if (source.isPropagated()) {
						bezier.drawBezier2D(Canvas.app, 2f);
						bezier.drawHeadBezier2D(Canvas.app, 2, alpha);
					} else {
						// ******
						// bezier.drawBezierAndControls(Canvas.app, thickness);
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
	}

	// getters and setters

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

			if (edgeVisibilityThreshold > edge.getFloatAttribute((String) keys[0]) || hidden) {
				visibility = false;
			} else {
				visibility = true;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;

	}
}
