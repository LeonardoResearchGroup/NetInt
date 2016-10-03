package visualElements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jcolorbrewer.ColorBrewer;

import graphElements.Node;
import processing.core.*;
import utilities.mapping.Mapper;
import visualElements.gui.VisibilitySettings;
import visualElements.primitives.VisualAtom;

public class VNode extends VisualAtom {
	private Node node;
	private boolean propagationSource, inPropagationChain, propagated = false;
	private ArrayList<VNode> successors;
	private ArrayList<Integer> propIndex;
	public int propagationSteps;
	private String currentMapper;


	public VNode(Node node, float x, float y) {
		super(x, y, 0);
		this.node = node;
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 0;
	}

	public VNode(VNode vNode) {
		super(vNode.getX(), vNode.getY(), vNode.getDiameter());
		this.node = vNode.getNode();
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 0;
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
				// System.out.println("1: " + vN.getNode().getName() + ": " +
				// vN.propIndex.toString());
			}
			propagated = true;
		}
		// this logic gate serves to update the propagation visualization
		// every time the user changes the propagation setting in the control
		// panel
		if (propagationSteps != (int) VisibilitySettings.getInstance().getPropagacion()) {
			reclaim();
			propagationSteps = (int) VisibilitySettings.getInstance().getPropagacion();
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
		propIndex.add((int) VisibilitySettings.getInstance().getPropagacion() - sequence);
		if (sequence - 1 >= 0) {
			inPropagationChain = true;
		}
		return sequence - 1;
	}

	public void reclaim() {
		if (propagated && inPropagationChain) {
			inPropagationChain = false;
			for (VNode vN : successors) {
				vN.reclaim();
				vN.propIndex.clear();
				vN.propagated = false;
			}
			propIndex.clear();
			propagated = false;
		}

	}

	// *** SHOW METHODS ***

	public void show(Canvas canvas) {
		canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
	}

	/**
	 * @param canvas
	 * @param communityOpen
	 */
	public void show(Canvas canvas, boolean communityOpen) {
		// Register mouse, touch or key events triggered on this object in the
		// context of the canvas
		registerEvents(canvas);
		// retrieve mouse coordinates
		detectMouseOver(canvas.getCanvasMouse());

		// **** Diameter mapping
		if (VisibilitySettings.getInstance().getFiltrosNodo() != null
				&& VisibilitySettings.getInstance().getFiltrosNodo() != currentMapper) {
			switch (VisibilitySettings.getInstance().getFiltrosNodo()) {
			case "Radial":
				setDiameter(
						Mapper.getInstance().convert(Mapper.RADIAL, node.getOutDegree(1), 150, Mapper.COMUNITY_SIZE));
				break;
			case "Lineal":
				setDiameter(
						Mapper.getInstance().convert(Mapper.LINEAR, node.getOutDegree(1), 150, Mapper.COMUNITY_SIZE));
				break;
			case "Logarithmic":
				setDiameter(Mapper.getInstance().convert(Mapper.LOGARITMIC, node.getOutDegree(1), 150,
						Mapper.COMUNITY_SIZE));
				break;
			case "Sinusoidal":
				setDiameter(Mapper.getInstance().convert(Mapper.SINUSOIDAL, node.getOutDegree(1), 150,
						Mapper.COMUNITY_SIZE));
				break;
			case "Sigmoid":
				setDiameter(
						Mapper.getInstance().convert(Mapper.SIGMOID, node.getOutDegree(1), 150, Mapper.COMUNITY_SIZE));
				break;

			}
			// give a minimal interaction area to every vNode
			setDiameter(getDiameter() + 5);
			currentMapper = VisibilitySettings.getInstance().getFiltrosNodo();
		}

		if (communityOpen) {
			// if this node is in the propagation chain
			if (inPropagationChain) {
				setAlpha(175);
				if (VisibilitySettings.getInstance().isMostrarNombre()) {
					canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
					canvas.app.text(propIndex.toString(), pos.x + 5, pos.y + 15);
				}
			} else {
				// regular color
				// canvas.app.fill(getColorRGB());
				// VisibilitySettings contains all the visibility settings
				// defined by the user in the control panel
				if (VisibilitySettings.getInstance().isMostrarNombre()) {
					canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);
				}
				setAlpha(150);
			}
			// Show propagation and source halo permanently
			if (leftClicked) {
				propagationSource = true;
				// Show propagation
				propagate((int) VisibilitySettings.getInstance().getPropagacion());
				// propagate(propagationSteps);
				canvas.app.stroke(225, 0, 0);
				canvas.app.strokeWeight(1.5f);
				canvas.app.ellipse(pos.x, pos.y, getDiameter() + 3, getDiameter() + 3);
				canvas.app.fill(255, 0, 0);
				canvas.app.text(node.getName(), pos.x + 5, pos.y + 5);

			} else {
				if (propagationSource) {
					reclaim();
					propagationSource = false;
				}
			}

			if (isMouseOver) {
				// canvas.app.fill(setColor(200, 0, 0, 120));
				// canvas.app.noFill();
				canvas.app.fill(brighter());
				canvas.app.stroke(225, 0, 0);
				canvas.app.ellipse(pos.x, pos.y, getDiameter() + 2, getDiameter() + 2);
				// Show comments
				verbose(canvas);
			} else {
				canvas.app.noStroke();
			}
			
			if(node.isFound()){
				canvas.app.fill(255,0,0);
				canvas.app.ellipse(pos.x, pos.y, getDiameter() + 2, getDiameter() + 2);
			}

			canvas.app.fill(getColorRGB());
			canvas.app.ellipse(pos.x, pos.y, getDiameter(), getDiameter());
		}

	}

	private void verbose(Canvas canvas) {
		canvas.app.textAlign(PConstants.LEFT);
		if (isMouseOver) {
			canvas.app.noStroke();
			// canvas.app.fill(setColor(50, 150));
			canvas.app.fill(new Color(50, 50, 50, 150).getRGB());
			canvas.app.rect(pos.x - 5, pos.y - 3, 160, -103);
			// canvas.app.fill(setColor(200, 170));
			canvas.app.fill(new Color(200, 200, 200, 170).getRGB());

			// Identification Data
			canvas.app.text("Name: " + node.getName(), pos.x + 5, pos.y - 5);
			// canvas.app.text("ID: " + node.getId(), pos.x + 5, pos.y - 15);
			canvas.app.text("Sector: " + node.getSector(), pos.x + 5, pos.y - 15);
			// Communities data
			Iterator<Integer> itr = node.getMetadataKeys().iterator();
			int count = 0;
			while (itr.hasNext()) {
				int key = itr.next();
				int shift = count * 35;
				canvas.app.text("Com: " + node.getCommunity(key), pos.x + 5, pos.y - 45 - shift);
				canvas.app.text("in: " + node.getInDegree(key), pos.x + 5, pos.y - 35 - shift);
				canvas.app.text("out: " + node.getOutDegree(key), pos.x + 5, pos.y - 25 - shift);
				count++;
			}
		}
		canvas.app.textAlign(PConstants.CENTER, PConstants.TOP);
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
	@Override
	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

}
