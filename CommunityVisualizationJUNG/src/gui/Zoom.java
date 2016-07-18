package gui;

//import java.awt.event.KeyEvent;
//import java.awt.event.KeyListener;
//import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
//import java.awt.event.MouseMotionListener;

import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Zoom {

	// The scale of our world
	private float zoom;
	// A vector to store the offset
	private PVector offset;
	// A vector to store the start offset
	private PVector startOffset;
	// The previous offset
	private PVector endOffset;
	// A vector for the mouse position
	private PVector canvasMouse;
	// A Vector for the canvas center
	private PVector newCenter;
	// The PApplet
	public PApplet app;

	// transformation control
	boolean shiftDown;
	boolean canvasBeingTransformed;

	public Zoom(PApplet app) {
		this.app = app;
		zoom = 1;
		offset = new PVector(0, 0);
		startOffset = new PVector(0, 0);
		endOffset = new PVector(0, 0);
		canvasMouse = new PVector(0, 0);
		newCenter = new PVector(0, 0);
//		app.addMouseListener(this);
//		app.addMouseMotionListener(this);
//		app.addKeyListener(this);
		myRegister(app);
	}

	public void active() {
		// **** Convert screenMouse into canvasMouse
		canvasMouse = new PVector(app.mouseX, app.mouseY);
		// translate canvasMouse
		canvasMouse.sub(newCenter);
		// Zoom
		canvasMouse.div(zoom);
		// Pan
		canvasMouse.sub(offset);

		// **** Transformation of canvas
		app.translate(newCenter.x, newCenter.y);
		// Use scale for 2D "zoom"
		app.scale(zoom);
		// The offset
		app.translate(offset.x, offset.y);
	}

	/**
	 * Reset zoom and pan to original values
	 */
	public void reset() {
		zoom = 1;
		offset.set(0.0f, 0.0f, 0);
	}

	/**
	 * Zoom in keyboard
	 * 
	 * @param val
	 */
	public void in(float val) {
		zoom += val;
	}

	/**
	 * Zoom out keyboard
	 * 
	 * @param val
	 */
	public void out(float val) {
		zoom -= val;
		if (zoom < 0.1) {
			zoom = 0.1f;
		}
	}

	/**
	 * Returns the current zoom value
	 * 
	 * @return
	 */
	public float getZoomValue() {
		return zoom;
	}

	/**
	 * Returns the current mouse coordinates in the transformed canvas
	 * 
	 * @return
	 */
	public PVector getCanvasMouse() {
		return canvasMouse;
	}

	public void translateCenter(float x, float y) {
		newCenter = new PVector(x, y);
	}

	public void displayValues(PVector pos) {
		// **** Legends
		app.textAlign(PConstants.RIGHT);
		app.text("Canvas mouse: " + canvasMouse, pos.x, pos.y + 10);
		app.text("Zoom: " + zoom, pos.x, pos.y + 20);
		app.text("Offset: " + offset, pos.x, pos.y + 30);
		app.text("startOffset: " + startOffset, pos.x, pos.y + 40);
		app.text("endOffset: " + endOffset, pos.x, pos.y + 50);
		app.textAlign(PConstants.CENTER);
	}

	public void showLegend(PVector pos) {
		app.textAlign(PConstants.RIGHT);
		app.text("Hold SHIFT and the left mouse button to zoom and pan", pos.x, pos.y);
		app.text("Press r to restore zoom and pan to default values", pos.x, pos.y + 10);
		app.textAlign(PConstants.CENTER);
	}

	public boolean isCanvasInTransformation() {
		return canvasBeingTransformed;
	}

	// *** Overwritten methods from implemented interfaces
	public void mousePressed(MouseEvent arg0) {
		startOffset.set(app.mouseX, app.mouseY, 0);
		System.out.println("Zoom > pressed");
	}

	public void mouseReleased(MouseEvent arg0) {
		canvasBeingTransformed = false;
	}

	public void mouseDragged(MouseEvent e) {
		if (shiftDown) {
			// set end for current drag iteration
			endOffset.set(app.mouseX, app.mouseY, 0);
			// set the difference
			offset.add(PVector.sub(endOffset, startOffset));
			// reset start for next drag iteration
			startOffset.set(app.mouseX, app.mouseY, 0);
			canvasBeingTransformed = true;
		} else {
			canvasBeingTransformed = false;
		}
	}

	public void keyPressed(KeyEvent arg0) {
		// Control of zoom by keyboard
		if (arg0.getKey() == 'a') {
			in(0.1f);
		} else if (arg0.getKey() == 'z') {
			out(0.1f);
		} else if (arg0.getKey() == 'r') {
			reset();
		}
		shiftDown = arg0.isShiftDown();
	}

	public void keyReleased(KeyEvent arg0) {
		shiftDown = arg0.isShiftDown();
		canvasBeingTransformed = arg0.isShiftDown();
	}

	public void keyTyped(KeyEvent arg0) {

	}

	// P3
	public void myRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
		theApp.registerMethod("keyEvent", this);
	}

	public void keyEvent(KeyEvent k) {
		keyPressed(k);
	}

	public void mouseEvent(MouseEvent e) {
		if (e.getAction() == MouseEvent.RELEASE) {
			mouseReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mousePressed(e);
		}
		if (e.getAction() == MouseEvent.DRAG) {
			mouseDragged(e);
		}
	}

}
