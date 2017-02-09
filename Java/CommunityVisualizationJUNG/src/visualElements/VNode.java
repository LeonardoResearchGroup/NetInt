package visualElements;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import graphElements.Node;
import processing.core.*;
import visualElements.gui.UserSettings;
import visualElements.primitives.VisualAtom;

public class VNode extends VisualAtom implements Serializable {

	private static final long serialVersionUID = 1L;
	private Node node;
	// This variable is used to control if a given attribute is below or above a
	// visibility threshold
	private boolean visible;
	// This variable is used to control that the vElement is displayed only
	// once. It is useful to prevent that edges with the same source or target
	// vNode display the nodes more than once
	private boolean displayed;
	// propagation attributes
	private boolean propagationSource, inPropagationChain, propagated = false;
	private ArrayList<VNode> successors;
	// The collection of propagation indexes. Each index represents the position
	// of this VNode in a propagation chain
	private ArrayList<Integer> propIndex;
	public int propagationSteps;
	private VNodeDescription description;

	public VNode(Node node, float x, float y) {
		super(x, y);
		this.node = node;
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 0;
		description = new VNodeDescription();
		visible = false;
		setDisplayed(false);
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
		setDisplayed(false);
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
			propagated = true;
		}
		// this logic gate serves to update the propagation visualization
		// every time the user changes the propagation setting in the control
		// panel
		if (propagationSteps != (int) UserSettings.getInstance().getPropagacion()) {
			reclaim();
			propagationSteps = (int) UserSettings.getInstance().getPropagacion();
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
				// System.out.println(" 2: " + getNode().getName() + ": " +
				// prompted + ": " + vN.propIndex.toString());
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
		propIndex.add((int) UserSettings.getInstance().getPropagacion() - sequence);
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
	 * @param canvas
	 * @param communityOpen
	 */
	public void show(boolean displayed) {
		setVisibility(UserSettings.getInstance().getUmbralGrados());
		if (getDiameter() > 50) {
			setDiameter(60);
		}
		if (displayed && visible) {
			// if this node is in the propagation chain
			if (inPropagationChain) {
				setAlpha(195);
				Canvas.app.fill(getColorRGB());
				Canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
				if (UserSettings.getInstance().mostrarNombre()) {
					Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
					Canvas.app.text(propIndex.toString(), pos.x + 5, pos.y + 15);
				}
			} else {
				// UserSettings contains all the visibility settings
				// defined by the user in the control panel
				if (UserSettings.getInstance().mostrarNombre()) {
					if (!UserSettings.getInstance().getOnlyPropagation()) {
						Canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
					}
				}
				setAlpha(150);
			}
			// Show propagation and source halo permanently
			if (leftClicked) {
				propagationSource = true;
				// Show propagation
				propagate((int) UserSettings.getInstance().getPropagacion());
				// propagate(propagationSteps);
				Canvas.app.stroke(225, 0, 0);
				Canvas.app.strokeWeight(1.5f);
				Canvas.app.ellipse(pos.x, pos.y, getDiameter() + 3, getDiameter() + 3);
				Canvas.app.fill(255, 0, 0);
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

			if (node.isFound()) {
				Canvas.app.fill(255, 0, 0);
				Canvas.app.ellipse(pos.x, pos.y, getDiameter() + 2, getDiameter() + 2);
			}

			if (!UserSettings.getInstance().getOnlyPropagation()) {
				Canvas.app.fill(getColorRGB());
				Canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
			}
		}
		if (!displayed) {
			setDisplayed(true);
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

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
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
