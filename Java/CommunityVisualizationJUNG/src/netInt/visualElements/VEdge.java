package netInt.visualElements;

import java.io.Serializable;
import java.util.ArrayList;

import netInt.graphElements.Edge;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.gui.UserSettings;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.PVector;

/**
 * The visual representation of a grahElement.Edge. Each VEdge has a source and
 * target instances of VNodes
 * 
 * @author jsalam
 *
 */
public class VEdge implements Serializable {
	private static final long serialVersionUID = 1L;
	private Edge edge;
	// Visibility attributes
	private boolean visibility, hidden;
	// Source and target nodes
	private VNode vSource, vTarget;
	// The curve linking the nodes
	private Bezier bezier;
	// Visual Attributes
	private float thickness;
	// UserSettings
	private String attributeName = "no_attribute";
	private String converterName = Mapper.LINEAR;

	public VEdge(Edge edge) {
		this.edge = edge;
		thickness = 1;
	}

	public VEdge(VNode source, VNode target) {
		this.edge = new Edge(source.getNode(), target.getNode(), true);
		this.vSource = source;
		this.vTarget = target;
		thickness = 1;
	}

	/**
	 * Retrieves the vNodes that are the visual representation of the source and
	 * target nodes associated to the edge.
	 * 
	 * @param visualNodes
	 *            The collection of visual nodes that contain edge's source and
	 *            target
	 * @return false if source and target are not found in the given collection
	 */
	public boolean setSourceAndTarget(ArrayList<VNode> visualNodes) {
		boolean sourceDone = false;
		boolean targetDone = false;
		for (VNode vNode : visualNodes) {
			// Source
			if (vNode.hasNode(edge.getSource())) {
				vSource = vNode;
				sourceDone = true;
			}
			// Target
			if (vNode.hasNode(edge.getTarget())) {
				vTarget = vNode;
				targetDone = true;
			}
		}
		return sourceDone && targetDone;
	}

	public void makeBezier() {
		bezier = new Bezier(vSource.getPos(), vTarget.getPos());
		int alpha = 100;
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
		// If both source and target are above a visibility threshold
		// source.show(source.isDisplayed());
		// target.show(target.isDisplayed());
		if (vSource.isVisible() && vTarget.isVisible()) {
			// This visibility is determined by a threshold parameter set at the
			// Control Panel
			if (visibility) {

				// Set thickness
				try {
					// determine the diameter based on the user selected
					// attribute name
					if (UserSettings.getInstance().getEdgeFilters() != null) {
						if (!attributeName.equals(UserSettings.getInstance().getEdgeFilters())) {
							attributeName = UserSettings.getInstance().getEdgeFilters();
							float value = edge.getFloatAttribute(attributeName);
							float tmp = Mapper.getInstance().convert(converterName, value, 5, Mapper.EDGE,
									attributeName);
							setThickness(tmp);
						}

						// determine the diameter based on the user selected
						// converter name
						if (!converterName.equals(UserSettings.getInstance().getConverterEdge())) {
							if (UserSettings.getInstance().getConverterEdge() != null)
								converterName = UserSettings.getInstance().getConverterEdge();
							float value = edge.getFloatAttribute(attributeName);
							float tmp = Mapper.getInstance().convert(converterName, value, 5, Mapper.EDGE,
									attributeName);
							setThickness(tmp);
						}
					}
				} catch (NullPointerException npe) {
					setThickness(1);
				}

				Canvas.app.strokeWeight(thickness);
				// Set color
				if (vSource.isPropagated()) {
					bezier.color(Bezier.PROPAGATE);
				} else {
					bezier.color(Bezier.NORMAL);
				}
				// If visualize the nodes and edges if not in propagation
				if (!UserSettings.getInstance().getOnlyPropagation()) {
				//	if (!vSource.getPos().equals(vTarget.getPos()) && Canvas.mouseEventOnCanvas)
						bezier.setSourceAndTarget(vSource.getPos(), vTarget.getPos());
					// If source and target nodes are in propagation
					// Edge mode: normal, head, tail or both
					if (vSource.isPropagated()) {
						bezier.drawBezier2D(Canvas.app, 2f);
						bezier.drawHeadBezier2D(Canvas.app, 2, alpha);
					} else {
						bezier.drawBezier2D(Canvas.app, 1f);
						bezier.drawHeadBezier2D(Canvas.app, thickness, alpha);
					}
				} else {
					// If solo propagation
					if (vSource.isPropagated()) {
					//	if (!vSource.getPos().equals(vTarget.getPos()))
							bezier.setSourceAndTarget(vSource.getPos(), vTarget.getPos());
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
		return vSource;
	}

	public void setSource(VNode source) {
		this.vSource = source;
	}

	public VisualAtom getTarget() {
		return vTarget;
	}

	public void setTarget(VNode target) {
		this.vTarget = target;
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
