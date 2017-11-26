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

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;

import jViridis.ColorMap;
import netInt.canvas.Canvas;
import netInt.graphElements.Node;
import netInt.gui.UserSettings;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.*;
import processing.event.MouseEvent;

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
	private String sizeAttributeName = "no_attribute";
	private String colorAttributeName = "no_attribute";

	// converter used to map node visual attributes. It gets its value from the
	// UserSettings
	private String converterSizeName = Mapper.LINEAR;
	private String converterColorName = Mapper.LINEAR;
	private int sizeFactor = 10;
	private int minSize = 3;

	// Visual attributes
	int textColor = new Color(200, 200, 200).getRGB();
	int haloOffset = 0;

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
	 *            Collection of successors
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
		if (propagationSteps != (int) UserSettings.getInstance().getPropagationThreshold()) {

			reclaim();

			propagationSteps = (int) UserSettings.getInstance().getPropagationThreshold();
		}
	}

	/**
	 * This method is used to clear propagation from Control Panel user
	 * selection
	 * 
	 * @param val
	 */
	protected void clearPropagation() {
		if (UserSettings.getInstance().getClearPropagation()) {
			leftClicked = false;
			reclaim();
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
	 *            position in propagation chain
	 * @return number of next steps in the propagation chain
	 */
	public int propagationCount(int sequence) {
		// this creates an arrayList indicating the position of this node in all
		// propagation chains
		propIndex.add((int) UserSettings.getInstance().getPropagationThreshold() - sequence);
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

	/**
	 * @param communityDisplayed
	 *            true if show
	 */
	public void show() {
		// Hide or show the vNode according to the user defined degree threshold
		if (filterVisibility()) {

			// Reset stroke weight
			Canvas.app.noStroke();

			// *** Set the vNode size
			// If this node has the attribute selected by the user
			if (node.getAttributes().containsKey(UserSettings.getInstance().getNodeSize())) {

				// Set the size with current converter and new selected
				// attribute name
				setDiameter(calculateSize());

				// Reset the size if user selects a new converter
				convertSize();
			}

			// *** Set vNode color
			// If this node has the attribute selected by the user
			if (node.getAttributes().containsKey(UserSettings.getInstance().getNodeColor())) {

				// Set the color with current converter and new selected
				// attribute name
				setColor(calculateColor());
				setAlpha(150);
			}

			// Draw node following propagation variables
			drawNode();

			showDescription();
		}
	}

	private float calculateSize() {

		float rtn = getDiameter();

		// user selected attribute name
		String userSelectedFilter = UserSettings.getInstance().getNodeSize();

		// For any change of size settings in user settings. Logical gate to
		// prevent unnecessary looping
		if (userSelectedFilter != null && !sizeAttributeName.equals(userSelectedFilter)) {

			sizeAttributeName = userSelectedFilter;

			float value = node.getFloatAttribute(sizeAttributeName);

			rtn = Mapper.getInstance().convert(converterSizeName, value, Mapper.NODE, sizeAttributeName);

			rtn = (rtn * sizeFactor) + minSize;

		}

		return rtn;
	}

	private void convertSize() {

		// user selected converter name
		String userSelectedConverter = UserSettings.getInstance().getConverterNode();

		// logical gate to prevent unnecessary looping
		if (userSelectedConverter != null && !converterSizeName.equals(userSelectedConverter)) {

			// Update converter name
			converterSizeName = userSelectedConverter;

			if (UserSettings.getInstance().getNodeSize() != null) {

				sizeAttributeName = UserSettings.getInstance().getNodeSize();

				float value = node.getFloatAttribute(sizeAttributeName);

				// Map input with updated converter
				float tmp = Mapper.getInstance().convert(converterSizeName, value, Mapper.NODE, sizeAttributeName);

				tmp *= sizeFactor;

				tmp += minSize;

				setDiameter(tmp);
			}
		}
	}

	private int calculateColor() {

		int rtn = getColor().getRGB();

		// user selected attribute name
		String userSelectedFilter = UserSettings.getInstance().getNodeColor();

		// logical gate to prevent unnecessary looping
		if (userSelectedFilter != null && !colorAttributeName.equals(userSelectedFilter)) {

			colorAttributeName = userSelectedFilter;

			float value = node.getFloatAttribute(colorAttributeName);

			float tmp = Mapper.getInstance().convert(converterColorName, value, Mapper.NODE, colorAttributeName);

			rtn = ColorMap.getInstance().getColorRGB(tmp);
		}

		// For any change of user settings
		if (UserSettings.eventOnVSettings && !colorAttributeName.equals("no_attribute")) {

			float value = node.getFloatAttribute(colorAttributeName);

			float tmp = Mapper.getInstance().convert(converterColorName, value, Mapper.NODE, colorAttributeName);

			rtn = ColorMap.getInstance().getColorRGB(tmp);
		}

		return rtn;
	}

	/**
	 * @param value
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private void convertColor(float value) {

		// user selected converter name
		String userSelectedConverter = UserSettings.getInstance().getConverterNode();

		// logical gate to prevent unnecessary looping
		if (userSelectedConverter != null && !converterColorName.equals(userSelectedConverter)) {

			// Update converter name
			converterColorName = userSelectedConverter;

			// Map input with updated converter
			float tmpColor = Mapper.getInstance().convert(converterColorName, value, Mapper.NODE, colorAttributeName);

			int mappedColor = ColorMap.getInstance().getColorRGB(tmpColor);

			setColor(mappedColor);
		}
	}

	private void drawNode() {
		
		clearPropagation();

		// Show Propagation Halo
		propagationSourceHalo(leftClicked);

		if (propagationSource) {

			// Set fill
			Canvas.app.fill(propagatedColor.getRGB());

			// Set stroke
			Canvas.app.stroke(propagatedColor.getRGB());

			// Set outline thickness
			Canvas.app.strokeWeight(1f);

			// Set offset
			haloOffset = 3;

		} else if (inPropagationChain) {

			// Set fill
			Canvas.app.fill(getColor().getRGB());

			// Set stroke
			Canvas.app.stroke(propagatedColor.getRGB());

			// Set outline thickness
			Canvas.app.strokeWeight(1f);

			// Set offset
			haloOffset = 0;

		} else {

			// If the user selects "propagation filter" button in Control Panel
			if (UserSettings.getInstance().filterPropagation()) {

				// Set fill
				Canvas.app.noFill();

				// Set stroke
				Canvas.app.noStroke();

				// Set outline thickness
				Canvas.app.strokeWeight(1f);

			}

			else {

				// Set fill
				Canvas.app.fill(getColorRGB());

				// Set stroke
				Canvas.app.noStroke();

				// Set outline thickness
				Canvas.app.strokeWeight(1f);
			}

			// Set offset
			haloOffset = 0;

		}

		if (node.isFound()) {
			Canvas.app.fill(propagatedColor.getRGB());
			haloOffset = 2;

		}

		Canvas.app.ellipse(pos.x, pos.y, getDiameter() + haloOffset, getDiameter() + haloOffset);
	}

	private void propagationSourceHalo(boolean selected) {

		// Show propagation and source halo permanently
		if (selected) {

			propagationSource = true;

			// Show propagation
			propagate((int) UserSettings.getInstance().getPropagationThreshold());

		} else {

			if (propagationSource) {

				reclaim();

				propagationSource = false;
			}
		}
	}

	private void showDescription() {

		// If show names enabled
		if (UserSettings.getInstance().showName()) {

			if (UserSettings.getInstance().filterPropagation()) {

				if (inPropagationChain || propagationSource) {
					Canvas.app.fill(textColor);
					Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
					Canvas.app.text(propIndex.toString(), pos.x + 5, pos.y + 15);
				}

			} else {
				Canvas.app.fill(textColor);
				Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
			}

		}

		if (isMouseOver) {
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

	// *** EQUALS
	public boolean equals(Object obj) {
		VNode vN = (VNode) obj;
		boolean rtn = vN.getNode().equals(this.node);
		return rtn;
	}

	public int hashCode() {
		return node.getId().hashCode();
	}

	// *** GETTERS AND SETTERS

	// *** GETTERS AND SETTERS

	public boolean hasNode(Node node) {
		return this.node.equals(node);
	}

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

	public void setNode(Node node) {
		this.node = node;
	}

	public void setVertex(Node vertex) {
		this.node = vertex;
	}

	private boolean filterVisibility() {

		float visibilityThreshold = UserSettings.getInstance().getDegreeThreshold();

		if (visibilityThreshold > getNode().getOutDegree(1)) {
			visible = false;
		} else {
			visible = true;
		}

		return visible;
	}

	// ***** Events

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

}
