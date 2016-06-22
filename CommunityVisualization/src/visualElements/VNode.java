package visualElements;

import graphElements.Node;
import interactiveElements.Button;
import processing.core.*;
import utilities.HopMessenger;
import utilities.geometry.Involute;

public class VNode extends Button {

	private Node vertex;
	private int alpha = 30;
	// For involute
	private PVector center;
	private Involute inv;
	private int sections, index;
	private float radius;

	private boolean notifiedIn;
	private boolean notifiedOut;

	public VNode(PApplet app, Node vertex, float x, float y, float diam) {
		super(app, x, y, diam);
		this.vertex = vertex;
		notifiedIn = false;
		notifiedOut = false;
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
		if (isMouseOver()) {
			app.fill(200, 0, 0,alpha);

			// Observer
			if (!notifiedIn) {
				HopMessenger heap = new HopMessenger();
				heap.setMessage("selected");
				// This parameter defines the length of the propagation sequence
				heap.setHops(15);
				heap.setCurrentHope(1);
				vertex.change();
				vertex.notifyObservers(heap);
				notifiedIn = true;
				notifiedOut = false;
			}

		} else {
			app.fill(200, alpha);

			// Observer
			if (!notifiedOut) {
				HopMessenger heap = new HopMessenger();
				heap.setMessage("free");
				// This parameter defines the length of the propagation sequence
				heap.setHops(15);
				heap.setCurrentHope(1);
				vertex.change();
				vertex.notifyObservers(heap);
				notifiedOut = true;
				notifiedIn = false;
			}
		}
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
	}

	private void verbose() {
		app.textAlign(PConstants.LEFT);
		if (isMouseOver()) {
			alpha = 200;
			app.fill(0, alpha);
			app.rect(pos.x - 5, pos.y - 3, 60, -43);
			app.fill(200, alpha);
			app.text("ID: " + vertex.getId(), pos.x + 5, pos.y - 35);
			app.text("degree: " + vertex.getDegree(), pos.x + 5, pos.y - 25);
			app.text("in: " + vertex.getInDegree(), pos.x + 5, pos.y - 15);
			app.text("out: " + vertex.getOutDegree(), pos.x + 5, pos.y - 5);

		} else {
			alpha = 90;
		}
		app.textAlign(PConstants.CENTER);
	}

	// *** GETTERS AND SETTERS
	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public float getZ() {
		return pos.z;
	}

	public Node getVertex() {
		return vertex;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public void setVertex(Node vertex) {
		this.vertex = vertex;
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
