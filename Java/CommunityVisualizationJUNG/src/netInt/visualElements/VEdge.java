/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.visualElements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import jViridis.ColorMap;
import netInt.canvas.Canvas;
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
	//Depends on degree of the nodes
	private boolean anotherVisibility = false;
	// Source and target nodes
	private VNode vSource, vTarget;
	// The curve linking the nodes
	private Bezier bezier;
	// Visual Attributes
	private float thickness = 1;
	private float scaleFactor = 3;
	// UserSettings
	private String attributeName = "no_attribute";
	private String converterName = Mapper.LINEAR;

	public VEdge(Edge edge) {
		this.edge = edge;
	}

	public VEdge(VNode source, VNode target) {
		this.edge = new Edge(source.getNode(), target.getNode(), true);
		this.vSource = source;
		this.vTarget = target;
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
	public boolean setSourceAndTarget(HashMap<String,VNode> visualNodes) {
		boolean sourceDone = false;
		boolean targetDone = false;
		VNode vSourceTmp = visualNodes.get(edge.getSource().getId());
		VNode vTargetTmp = visualNodes.get(edge.getTarget().getId());
		if(vSourceTmp != null){
			vSource	= vSourceTmp;
			sourceDone = true;
		}
		if(vTargetTmp != null){
			vTarget	= vTargetTmp;
			targetDone = true;
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
		// vTarget.show(vTarget.isDisplayed());
		if (vSource.isVisible() && vTarget.isVisible() && vSource.isDisplayed() && vTarget.isDisplayed()) {

			// This visibility is determined by a threshold parameter set at the
			// Control Panel
			if (visibility && anotherVisibility) {

				// Set thickness
				try {
					// determine the thickness based on the user selected
					// attribute name
					if (UserSettings.getInstance().getEdgeFilters() != null) {
						if (!attributeName.equals(UserSettings.getInstance().getEdgeFilters())) {
							attributeName = UserSettings.getInstance().getEdgeFilters();
							float value = edge.getFloatAttribute(attributeName);
							float tmp = Mapper.getInstance().convert(converterName, value, Mapper.EDGE, attributeName);
							// System.out.println("VEdge > value of " +
							// attributeName + " :" + value + ", mapped to: " +
							// tmp);
							setThickness(scaleFactor * tmp);

							/// COLOR TEMPORARY
							float mappedVal = Mapper.getInstance().convert(converterName, value, Mapper.EDGE,
									attributeName);
							int mappedColor = ColorMap.getInstance(ColorMap.PLASMA).getColorRGB(mappedVal);
							bezier.setColor(mappedColor);
						}

						// determine the thickness based on the user selected
						// converter name
						if (!converterName.equals(UserSettings.getInstance().getConverterEdge())) {
							if (UserSettings.getInstance().getConverterEdge() != null)
								converterName = UserSettings.getInstance().getConverterEdge();
							float value = edge.getFloatAttribute(attributeName);
							float tmp = Mapper.getInstance().convert(converterName, value, Mapper.EDGE, attributeName);
							setThickness(scaleFactor * tmp);
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
					// if (!vSource.getPos().equals(vTarget.getPos()) &&
					// Canvas.mouseEventOnCanvas)
					bezier.setSourceAndTarget(vSource.getPos(), vTarget.getPos());
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
						// if (!vSource.getPos().equals(vTarget.getPos()))
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
		// Set the Visibility with the second Attribute of Edge Import: "Body
		// Thickness"
		try {
			if (edge.getAttribute((String) keys[1]) instanceof Float) {
				
				float value = edge.getFloatAttribute((String) keys[1]);

				if (edgeVisibilityThreshold > value || hidden) {
					visibility = false;
				} else {
					visibility = true;
				}
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
	
	public void setAnotherVisibility(boolean anotherVisibility) {
		this.anotherVisibility = anotherVisibility;
	}

}
