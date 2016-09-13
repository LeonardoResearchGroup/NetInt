package visualElements;

import java.util.Iterator;

import graphElements.Node;
import processing.core.*;
import utilities.geometry.Involute;
import visualElements.interactive.VisualAtom;

public class VNode extends VisualAtom {

	private Node node;
	// For involute
	private PVector center;
	// private Involute inv;
	private int sections, index;
	private float radius;

	public VNode(Node node, float x, float y, float diam) {
		super(x, y, diam);
		this.node = node;
	}

	public VNode(VNode vNode) {
		super(vNode.getX(), vNode.getY(), vNode.diam);
		this.node = vNode.getNode();
	}

	// public void setup(Canvas canvas) {
	// inv = new Involute(radius, sections, index);
	// pos = inv.getInvoluteCoords(center);
	// }

	// *** SHOW METHODS ***
	public void show(Canvas canvas) {
		// Register mouse, touch or key events triggered on this object in the
		// context of the canvas
		registerEvents(canvas);
		// retrieve mouse coordinates
		detectMouseOver(canvas.getCanvasMouse());
		canvas.app.noStroke();
		canvas.app.fill(200, 0, 200, alpha);
		if (isMouseOver) {
			canvas.app.fill(200, 200, 0, 30);
			// Show comments
			verbose(canvas);
		} else {
			setAlpha(90);
		}
		canvas.app.ellipse(pos.x, pos.y, diam, diam);
	}

	// showNodes, networkVisible
	public void show(Canvas canvas, boolean visible) {
		// Register mouse, touch or key events triggered on this object in the
		// context of the canvas
		registerEvents(canvas);
		// retrieve mouse coordinates
		detectMouseOver(canvas.getCanvasMouse());

		if (visible) {
			canvas.app.fill(200, alpha);
			canvas.app.noStroke();
			canvas.app.ellipse(pos.x, pos.y, diam, diam);

			if (isMouseOver) {
				canvas.app.fill(200, 0, 0, alpha);
				canvas.app.noStroke();
				canvas.app.ellipse(pos.x, pos.y, diam, diam);
				// Show comments
				verbose(canvas);
				// If community unlocked
				// if (inv != null) {
				// inv.runInvolute(90, 10, visible);
				// pos = inv.getInvoluteCoords(center);
				// // inv.show(center);
				// }
			} else {
				setAlpha(90);
			}
		}
	}

	private void verbose(Canvas canvas) {
		canvas.app.textAlign(PConstants.LEFT);
		if (isMouseOver) {
			setAlpha(200);
			canvas.app.fill(0, alpha);
			canvas.app.rect(pos.x - 5, pos.y - 3, 60, -53);
			canvas.app.fill(200, alpha);
			// Identification Data
			canvas.app.text("Name: " + node.getName(), pos.x + 5, pos.y - 5);
			canvas.app.text("ID: " + node.getId(), pos.x + 5, pos.y - 15);
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
		} else {
			setAlpha(90);
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

	public void setNode(Node node) {
		this.node = node;
	}

	public void setVertex(Node vertex) {
		this.node = vertex;
	}

	public void setCenter(PVector center) {
		this.center = center;
	}

	public void setNetworkRadius(float radius) {
		this.radius = radius;

	}

	public void setArcSections(int size) {
		this.sections = size;

	}

	public void setIndex(int index) {
		this.index = index;
	}

	// ***** Events

	public void eventRegister(PApplet theApp) {
//		theApp.registerMethod("mouseEvent", this);
//		System.out.println("VNode> event");
	}
}
