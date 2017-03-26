/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 *
 * It makes extensive use of free libraries such as Processing, Jung, ControlP5, JOGL, 
 * Tinkerpop and many others. For details see the copyrights folder. 
 *
 * Contributors:
 * 	Juan Salamanca, Cesar Loaiza, Luis Felipe Rivera, Javier Diaz
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *
 * version alpha
 *******************************************************************************/
package netInt.visualElements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import netInt.graphElements.Node;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.gui.UserSettings;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.*;

public class VNode extends VisualAtom implements Serializable {

	private static final long serialVersionUID = 1L;
	private Node node;
	
	// This variable is used to control if a given attribute is below or above a
	// visibility threshold
	private boolean visible;
	
	// propagation attributes
	private boolean propagationSource, inPropagationChain, propagated = false;
	private ArrayList<VNode> successors;
	
	// The collection of propagation indexes. Each index represents the position
	// of this VNode in a propagation chain
	private ArrayList<Integer> propIndex;
	public int propagationSteps;
	private VNodeDescription description;
	
	// attribute used to map node diameter. It gets its value from the
	// UserSettings
	private String attributeName = "no_attribute";
	
	// converter used to map node visual attributes. It gets its value from the
	// UserSettings
	private String converterName = Mapper.LINEAR;

	public VNode(Node node, float x, float y) {
		super(x, y);
		this.node = node;
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 0;
		description = new VNodeDescription();
		visible = false;
		// Register mouse, touch or key events triggered on this object in the
		// context of the canvas
		registerEvents();
	}

	public VNode(VNode vNode) {
		super(vNode.getX(), vNode.getY());
		this.node = vNode.getNode();
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 0;
		description = new VNodeDescription();
		visible = false;
		registerEvents();
	}

	// *** PROPAGATION
	/**
	 * Set the collection of VNode successors of this node
	 * 
	 * @param successors
	 */
	public void setVNodeSuccessors(Collection<VNode> successors) {
		this.successors.addAll(successors);
	}

	private void propagate(int sequence) {
		// Controls iterative propagation
		if (!propagated) {
			// Successors
			int next = propagationCount(sequence);
			for (VNode vN : successors) {
				vN.propagateSuccessor(next);
				// Mark the next vNode as part of the propagation chain. This is
				// important because vEdges are displayed as part of the
				// propagation chain only if source and target nodes are marked
				// as propagated
				vN.inPropagationChain = true;
			}
			inPropagationChain = true;
			propagated = true;
		}
		// this logic gate serves to update the propagation visualization
		// every time the user changes the propagation setting in the control
		// panel
		if (propagationSteps != (int) UserSettings.getInstance().getPropagation()) {
			reclaim();
			propagationSteps = (int) UserSettings.getInstance().getPropagation();
		}
	}

	/**
	 * Activates the method that mark this VNode as a link in the propagation
	 * chain: propagationCount() and propagates the sequence in its collection
	 * of successors
	 * 
	 * @param parentSequence
	 */
	private void propagateSuccessor(int parentSequence) {
		int next = this.propagationCount(parentSequence);
		// Controls iterative propagation
		if (next >= 0) {
			for (VNode vN : this.successors) {
				vN.propagateSuccessor(next);
				vN.inPropagationChain = true;
			}
			propagated = true;
		}
	}

	/**
	 * If this VNode is a link in a propagation chain, propagationChain is set
	 * to true and the number of next steps in the chain is returned
	 * 
	 * @param sequence
	 * @return
	 */
	public int propagationCount(int sequence) {
		// this creates an arrayList indicating the position of this node in all
		// propagation chains
		propIndex.add((int) UserSettings.getInstance().getPropagation() - sequence);
		if (sequence - 1 >= 0) {
			inPropagationChain = true;
		}
		return sequence - 1;
	}

	public void reclaim() {
		if (inPropagationChain) {
			inPropagationChain = false;
			propIndex.clear();
			propagated = false;
			for (VNode vN : successors) {
				vN.reclaim();
			}
		}
	}

	// *** SHOW METHODS ***
	public void show() {

	}

	/**
	 * @param communityDisplayed
	 */
	public void show(boolean communityDisplayed) {
		// Hide or show the vNode depending of the degrees threshold
		setVisibility(UserSettings.getInstance().getDegreeThreshold());

		// Set the vNode diameter
		try {
			// determine the diameter based on the user selected attribute name
			if (!attributeName.equals(UserSettings.getInstance().getNodeFilters())) {
				if (UserSettings.getInstance().getNodeFilters() != null)
					attributeName = UserSettings.getInstance().getNodeFilters();
				float value = node.getFloatAttribute(attributeName);
				float tmp = Mapper.getInstance().convert(converterName, value, 60, Mapper.NODE, attributeName);
				setDiameter(tmp);
			}
			// determine the diameter based on the user selected converter name
			if (!converterName.equals(UserSettings.getInstance().getConverterNode())) {
				if (UserSettings.getInstance().getConverterNode() != null)
					converterName = UserSettings.getInstance().getConverterNode();
				float value = node.getFloatAttribute(attributeName);
				float tmp = Mapper.getInstance().convert(converterName, value, 60, Mapper.NODE, attributeName);
				setDiameter(tmp);
			}
		} catch (NullPointerException npe) {
			// npe.printStackTrace();
			setDiameter(5);
		}

		if (UserSettings.getInstance().getOnlyPropagation()) {
			if (propagationSource || inPropagationChain) {
				communityDisplayed = true;
			} else {
				communityDisplayed = false;
			}
		} else {
			communityDisplayed = true;
		}

		if (communityDisplayed && visible) {

			if (node.isFound()) {
				Canvas.app.fill(255, 0, 0);
				Canvas.app.ellipse(pos.x, pos.y, getDiameter() + 2, getDiameter() + 2);
			}

			if (!UserSettings.getInstance().getOnlyPropagation()) {
				Canvas.app.fill(getColorRGB());
				Canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
			}

			// if this node is in the propagation chain
			if (inPropagationChain) {
				setAlpha(195);
				Canvas.app.fill(getColorRGB());
				Canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
				if (UserSettings.getInstance().showName()) {
					Canvas.app.fill(200, 200, 200);
					Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
					Canvas.app.text(propIndex.toString(), pos.x + 5, pos.y + 15);
				}
			} else {
				// UserSettings contains all the visibility settings
				// defined by the user in the control panel
				if (UserSettings.getInstance().showName()) {
					if (!UserSettings.getInstance().getOnlyPropagation()) {
						// Canvas.app.fill(getColorRGB());
						Canvas.app.fill(200, 200, 200);
						Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
					}
				}
				setAlpha(150);
			}
			// Show propagation and source halo permanently
			if (leftClicked) {
				propagationSource = true;
				// Show propagation
				propagate((int) UserSettings.getInstance().getPropagation());
				// propagate(propagationSteps);
				Canvas.app.stroke(225, 0, 0);
				Canvas.app.strokeWeight(1.5f);
				Canvas.app.fill(getColorRGB());
				Canvas.app.ellipse(pos.x, pos.y, getDiameter() + 3, getDiameter() + 3);
				Canvas.app.fill(225, 225, 225);
				Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);

			} else {
				if (propagationSource) {
					reclaim();
					propagationSource = false;
				}
			}

			if (isMouseOver) {
				// canvas.app.fill(setColor(200, 0, 0, 120));
				// canvas.app.noFill();
				Canvas.app.strokeWeight(1);
				Canvas.app.fill(brighter());
				Canvas.app.stroke(225, 0, 0);
				Canvas.app.ellipse(pos.x, pos.y, getDiameter() + 2, getDiameter() + 2);
				// Show comments
				description.show(this);
			} else {
				Canvas.app.noStroke();
			}
		}
	}

	public boolean hasNode(Node node) {
		return this.node.equals(node);
	}

	// *** equals
	public boolean equals(Object obj) {
		VNode vN = (VNode) obj;
		boolean rtn = vN.getNode().equals(this.node);
		return rtn;
	}

	public int hashCode() {
		return node.getId().hashCode();
	}

	// *** GETTERS AND SETTERS

	public void absoluteToRelative(PVector center) {
		pos = PVector.sub(pos, center);
	}

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public float getZ() {
		return pos.z;
	}

	public Node getNode() {
		return node;
	}

	public boolean isPropagated() {
		return propagated;
	}

	public boolean isInPropagationChain() {
		return inPropagationChain;
	}

	public int getPropagationSteps() {
		return propagationSteps;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setVertex(Node vertex) {
		this.node = vertex;
	}

	public void setVisibility(float visibilityThreshold) {
		if (visibilityThreshold > getNode().getOutDegree(1)) {
			setVisibility(false);
		} else {
			setVisibility(true);
		}
	}

	// ***** Events

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

}
