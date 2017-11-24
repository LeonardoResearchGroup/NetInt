/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.visualElements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import jViridis.ColorMap;
import netInt.canvas.Canvas;
import netInt.canvas.MouseHook;
import netInt.graphElements.Edge;
import netInt.gui.UserSettings;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

/**
 * The visual representation of a grahElement.Edge. Each VEdge has a source and
 * target instances of VNodes
 * 
 * @author jsalam
 *
 */
public class VEdge implements Serializable {

	private static final long serialVersionUID = 1L;

	// The edge
	private Edge edge;

	// Visibility attributes
	private boolean aboveVisibilityThreshold = true;
	private boolean hidden = false;

	// Depends on degree of the nodes
	private boolean sourceTargetVisibile = true;

	// Source and target nodes
	private VNode vSource, vTarget;

	// The curve linking the nodes
	private Bezier bezier;

	// Visual Attributes
	private float thickness = 1;
	private float scaleFactor = 3;
	private int alpha = 150;

	// The float value of the current user selected edge attribute
	private float attributeValue;

	// UserSettings
	private String thicknessAttributeName = "no_attribute";
	private String colorAttributeName = "no_attribute";
	
	// Converters
	private String converterThicknessName = Mapper.LINEAR;
	private String converterColorName = Mapper.LINEAR;

	public VEdge(Edge edge) {
		this.edge = edge;
		// Canvas.app.registerMethod("mouseEvent", this);
	}

	public VEdge(VNode source, VNode target) {
		this.edge = new Edge(source.getNode(), target.getNode(), true);
		this.vSource = source;
		this.vTarget = target;
		// Canvas.app.registerMethod("mouseEvent", this);
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

	/**
	 * Retrieves the vNodes that are the visual representation of the source and
	 * target nodes associated to the edge.
	 * 
	 * @param visualNodes
	 *            The collection of visual nodes that contain edge's source and
	 *            target
	 * @return false if source and target are not found in the given collection
	 */
	public boolean setSourceAndTarget(HashMap<String, VNode> visualNodes) {
		boolean sourceDone = false;
		boolean targetDone = false;
		VNode vSourceTmp = visualNodes.get(edge.getSource().getId());
		VNode vTargetTmp = visualNodes.get(edge.getTarget().getId());
		if (vSourceTmp != null) {
			vSource = vSourceTmp;
			sourceDone = true;
		}
		if (vTargetTmp != null) {
			vTarget = vTargetTmp;
			targetDone = true;
		}

		return sourceDone && targetDone;
	}

	public void makeBezier() {
		bezier = new Bezier(vSource.getPos(), vTarget.getPos());
		int alpha = 100;
		bezier.setColor(getSource().getColor().darker().darker().getRGB());
		bezier.setAlpha(alpha);
	}

	public void setLayoutAndCenter(int layout, PVector center) {
		bezier.setLayoutAndCenter(layout, center);
	}

	/**
	 * Visualize the VEdge on the Canvas
	 */
	public void show() {

		if (checkVisibility()) {

			// This visibility is determined by a a threshold
			// parameter set at the Control Panel
			if (aboveVisibilityThreshold && sourceTargetVisibile) {

				// Set the thickness based on the user selected attribute name
				// in control panel
				if (UserSettings.eventOnVSettings) {

					// Set thickness
					setThickness(calculateEdgeThickness());

					// Set color
					setColor(calculateEdgeColor());

					// Convert thickness by current converter
					convertThickness();

					// Convert color by current converter
					convertColor();
				}

				// Set thickness on PApplet
				Canvas.app.strokeWeight(thickness);

				// Draw
				drawBezier();

			}
		}
	}

	private float calculateEdgeThickness() {
		float rtn = getThickness();

		if (UserSettings.getInstance().getEdgeThickness() != null) {

			if (!UserSettings.getInstance().getEdgeThickness().equals(thicknessAttributeName)) {

				thicknessAttributeName = UserSettings.getInstance().getEdgeThickness();

				if (edge.getAttributes().containsKey(thicknessAttributeName)) {

					attributeValue = edge.getFloatAttribute(thicknessAttributeName);

					rtn = Mapper.getInstance().convert(converterThicknessName, attributeValue, Mapper.EDGE,
							thicknessAttributeName);

				}
			}
		}
		return rtn;
	}

	private int calculateEdgeColor() {
		//int rtn = bezier.getBodyColor();
		int rtn = getSource().getColor().getRGB();

		if (UserSettings.getInstance().getEdgeColor() != null) {

			if (!UserSettings.getInstance().getEdgeColor().equals(colorAttributeName)) {

				colorAttributeName = UserSettings.getInstance().getEdgeColor();

				// if this edge has the attribute selected by the user
				if (edge.getAttributes().containsKey(colorAttributeName)) {

					attributeValue = edge.getFloatAttribute(colorAttributeName);

					float mappedVal = Mapper.getInstance().convert(converterColorName, attributeValue, Mapper.EDGE,
							colorAttributeName);

					rtn = ColorMap.getInstance().getColorRGB(mappedVal);

				}
			}
		}
		return rtn;
	}

	private void convertThickness() {
		// Set the thickness based on the user selected converter
		// name
		if (UserSettings.getInstance().getConverterEdge() != null) {

			if (!UserSettings.getInstance().getConverterEdge().equals(converterThicknessName)) {

				converterThicknessName = UserSettings.getInstance().getConverterEdge();

				float tmpThickness = Mapper.getInstance().convert(converterThicknessName, attributeValue, Mapper.EDGE,
						thicknessAttributeName);

				setThickness(scaleFactor * tmpThickness);
			}
		}
	}

	private void convertColor() {
		// Set the thickness based on the user selected converter
		// name
		if (UserSettings.getInstance().getConverterEdge() != null) {

			if (!UserSettings.getInstance().getConverterEdge().equals(converterColorName)) {

				converterColorName = UserSettings.getInstance().getConverterEdge();

				float tmpColor = Mapper.getInstance().convert(converterColorName, attributeValue, Mapper.EDGE,
						colorAttributeName);

				int mappedColor = ColorMap.getInstance().getColorRGB(tmpColor);

				setColor(mappedColor);

			}
		}
	}

	private void drawBezier() {
		// Set color
		if (vSource.isPropagated()) {

			bezier.color(Bezier.PROPAGATE);

		} else {

			bezier.color(Bezier.NORMAL);
		}

		if (Canvas.mouseEventOnCanvas) {
			bezier.setSourceAndTarget(vSource.getPos(), vTarget.getPos());
		}

		// Visualize the nodes and edges if not in propagation
		if (!UserSettings.getInstance().getOnlyPropagation())

		{

			// If source and target nodes are in propagation
			// Edge mode: normal, head, tail or both
			if (vSource.isPropagated()) {

				bezier.drawBezier2D(Canvas.app, 2f);

				bezier.drawHeadBezier2D(Canvas.app, 2, alpha);

			} else {

				bezier.drawBezier2D(Canvas.app, thickness);

				bezier.drawHeadBezier2D(Canvas.app, thickness, alpha);
			}
		} else {

			// If solo propagation
			if (vSource.isPropagated()) {

				// Edge mode: normal, head, tail or both
				bezier.drawBezier2D(Canvas.app, 2f);

				bezier.drawHeadBezier2D(Canvas.app, thickness, alpha);
			}
		}
	}

	// ****** getters and setters *******

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

	private void setThickness(float thickness) {
		this.thickness = thickness;
	}

	public boolean equals(Object obj) {
		VEdge vEdge = (VEdge) obj;
		boolean sourceIsEqual = vEdge.getSource().equals(this.getSource());
		boolean targetIsEqual = vEdge.getTarget().equals(this.getTarget());
		return sourceIsEqual && targetIsEqual;
	}

	public void setVisibility(float edgeVisibilityThreshold) {

		// if this edge has that attributeName
		if (edge.getAttributes().containsKey(UserSettings.getInstance().getEdgeWeightAttribute())) {

			// Set the Visibility
			try {

				float weight = edge.getFloatAttribute(UserSettings.getInstance().getEdgeWeightAttribute());

				if (edgeVisibilityThreshold > weight || hidden) {
					aboveVisibilityThreshold = false;

				} else {
					aboveVisibilityThreshold = true;
				}
			} catch (Exception e) {

				if (e instanceof NullPointerException) {

					/*
					 * IMPORTANT: edges belonging to tiers above tier 0 might
					 * not have the same attributes as the root edges, that is
					 * why NullPointerExceptions are ignored.
					 */

				} else {
					System.out.println(this.getClass().getName() + " " + e.getMessage());
				}
			}
		}
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;

	}

	public void setSourceTargetVisibility(boolean anotherVisibility) {
		this.sourceTargetVisibile = anotherVisibility;
	}

	public boolean checkVisibility() {

		boolean isVisible = vSource.isVisible() && vTarget.isVisible() && vSource.isDisplayed()
				&& vTarget.isDisplayed();

		if (vSource instanceof VCommunity) {

			VCommunity vCSource = (VCommunity) vSource;

			VCommunity vCTarget = (VCommunity) vTarget;

			isVisible = isVisible && !vCSource.getComCover().isUnlocked() && !vCTarget.getComCover().isUnlocked();
		}

		return isVisible;
	}

	// // **** Events
	//
	// public void mouseEvent(MouseEvent e) {
	// if (e.getAction() == MouseEvent.DRAG) {
	// if (MouseHook.getInstance().isHooked(vSource) ||
	// MouseHook.getInstance().isHooked(vTarget))
	// bezier.setSourceAndTarget(vSource.getPos(), vTarget.getPos());
	// }
	// }
}
