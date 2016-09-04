package visualElements;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jcolorbrewer.ColorBrewer;

import graphElements.Node;
import processing.core.*;
import visualElements.primitives.VisualAtom;

public class VNode extends VisualAtom {
	private Node node;
	private boolean propagationSource, inPropagationChain, propagated = false;
	private ArrayList<VNode> successors;
	private ArrayList<Integer> propIndex;
	public int propagationSteps;

	public VNode(Node node, float x, float y, float diam) {
		super(x, y, diam);
		this.node = node;
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 4;
	}

	public VNode(VNode vNode) {
		super(vNode.getX(), vNode.getY(), vNode.diam);
		this.node = vNode.getNode();
		successors = new ArrayList<VNode>();
		propIndex = new ArrayList<Integer>();
		propagationSteps = 4;
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
	 * Is this VNode is a link in a propagation chain, propagationChain is set
	 * to true and the number of next steps in the chain is returned
	 * 
	 * @param sequence
	 * @return
	 */
	public int propagationCount(int sequence) {
		propIndex.add(propagationSteps-sequence);
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
		canvas.app.ellipse(pos.x, pos.y, diam, diam);
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

		if (communityOpen) {
			// if this node is in the propagation chain
			if (inPropagationChain) {
				setAlpha(125);
				canvas.app.text(propIndex.toString(), pos.x + 5, pos.y + 5);
			} else {
				// regular color
				// canvas.app.fill(getColorRGB());
				setAlpha(90);
			}
			// Show propagation and source halo permanently
			if (leftClicked) {
				propagationSource = true;
				// Show propagation
				propagate(propagationSteps);
				canvas.app.stroke(225, 0, 0);
				canvas.app.ellipse(pos.x, pos.y, diam + 3, diam + 3);
			} else {
				if (propagationSource) {
					reclaim();
					propagationSource = false;
				}
			}

			if (isMouseOver) {
//				canvas.app.fill(setColor(200, 0, 0, 120));
				canvas.app.noFill();
				canvas.app.stroke(225, 0, 0);
				canvas.app.ellipse(pos.x, pos.y, diam + 2, diam + 2);
				// Show comments
				verbose(canvas);
			} else {
				canvas.app.noStroke();
			}
			
			boolean colorBlindSave = true;
			ColorBrewer[] sequentialPalettes = ColorBrewer.getSequentialColorPalettes(colorBlindSave);	


			ColorBrewer myBrewer = sequentialPalettes[7];
			

			System.out.println( "Name of this color brewer: " + myBrewer);

			// I want a gradient of 8 colors:
			Color[] myGradient = ColorBrewer.BuGn.getColorPalette(8);
			
//			canvas.app.fill(myGradient[5].getRGB());
			canvas.app.fill(getColorRGB());
			canvas.app.ellipse(pos.x, pos.y, diam, diam);
		}
	}

	private void verbose(Canvas canvas) {
		canvas.app.textAlign(PConstants.LEFT);
		if (isMouseOver) {
			canvas.app.noStroke();
			canvas.app.fill(setColor(50, 150));
			canvas.app.rect(pos.x - 5, pos.y - 3, 160, -103);
			canvas.app.fill(setColor(200, 170));
			// Identification Data
			canvas.app.text("Name: " + node.getName(), pos.x + 5, pos.y - 5);
			//canvas.app.text("ID: " + node.getId(), pos.x + 5, pos.y - 15);
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

	public int getPropagationSteps() {
		return propagationSteps;
	}

	public void setPropagationSteps(int propagationSteps) {
		this.propagationSteps = propagationSteps;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setVertex(Node vertex) {
		this.node = vertex;
	}
	
	public void setVisibility(float visibilityThreshold){
		if (visibilityThreshold > getNode().getOutDegree(1)){
			setVisibility(false);
		}else{
			setVisibility(true);
		}
	}

	// ***** Events
	@Override
	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

}
