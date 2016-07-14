package visualElements;

import java.util.Iterator;

import graphElements.Node;
import processing.core.*;
import utilities.geometry.Involute;
import visualElements.interactive.VisualAtom;

public class VNode extends VisualAtom {

	private Node node;
	private int alpha = 70;
	// For involute
	private PVector center;
	private Involute inv;
	private int sections, index;
	private float radius;

	public VNode(PApplet app, Node node, float x, float y, float diam) {
		super(app, x, y, diam);
		this.node = node;
	}

	public VNode(VNode vNode) {
		super(vNode.app, vNode.getX(), vNode.getY(), vNode.diam);
		this.node = vNode.getNode();
	}

	public void setup() {
		inv = new Involute(app, radius, sections, index);
		pos = inv.getInvoluteCoords(center);
	}

	// *** SHOW METHODS ***
	public void show() {
		app.noStroke();
		app.ellipse(pos.x, pos.y, diam, diam);
	}

	// showNodes, networkVisible
	public void show(boolean showNode, boolean visible) {
		if (visible) {
			app.fill(200, alpha);
			app.noStroke();
			app.ellipse(pos.x, pos.y, diam, diam);

			if (isMouseOver()) {
				app.fill(200, 0, 0, alpha);
				app.noStroke();
				app.ellipse(pos.x, pos.y, diam, diam);
				// Show comments
				verbose();
				// If community unlocked
				if (inv != null) {
					inv.runInvolute(90, 10, visible);
					pos = inv.getInvoluteCoords(center);
					// inv.show(center);
				}
			} else {
				alpha = 70;
			}
		}
	}

	private void verbose() {
		app.textAlign(PConstants.LEFT);
		if (isMouseOver()) {
			alpha = 200;
			app.fill(0, alpha);
			app.rect(pos.x - 5, pos.y - 3, 60, -53);
			app.fill(200, alpha);
			// Identification Data
			app.text("Name: " + node.getName(), pos.x + 5, pos.y - 5);
			app.text("ID: " + node.getId(), pos.x + 5, pos.y - 15);
			// Communities data
			Iterator<Integer> itr = node.getMetadataKeys().iterator();
			int count = 0;
			while (itr.hasNext()) {
				int key = itr.next();
				int shift = count * 35;
				app.text("Com: " + node.getCommunity(key), pos.x + 5, pos.y - 45 - shift);
				app.text("in: " + node.getInDegree(key), pos.x + 5, pos.y - 35 - shift);
				app.text("out: " + node.getOutDegree(key), pos.x + 5, pos.y - 25 - shift);
				count++;
			}
		} else {
			alpha = 90;
		}
		app.textAlign(PConstants.CENTER, PConstants.CENTER);
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
		return node.getId();
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

	public int getAlpha() {
		return alpha;
	}

	public Node getNode() {
		return node;
	}

	public void setNode(Node node) {
		this.node = node;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
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

}
