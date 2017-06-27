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
package netInt.canvas;

import java.util.Timer;
import java.util.TimerTask;

import netInt.GraphPad;
import netInt.visualElements.gui.UserSettings;
import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class Canvas {

	// The scale of our world
	private float zoom;
	// A vector to store the offset
	private PVector offset;
	// A vector to store the start offset
	private PVector startOffset;
	// The previous offset
	private PVector endOffset;
	// A vector for the mouse position
	private static PVector canvasMouse;
	// A Vector for the canvas center
	private PVector newCenter;
	// An Event to inform if there was an event on the canvas
	public static boolean mouseEventOnCanvas;
	// Transformation control
	private boolean shiftDown;
	public static boolean canvasBeingTransformed = false;
	public static boolean canvasBeingZoomed = false;

	public static PApplet app;

	public Canvas(PApplet app) {
		Canvas.app = app;
		zoom = 1;
		offset = new PVector(0, 0);
		startOffset = new PVector(0, 0);
		endOffset = new PVector(0, 0);
		canvasMouse = new PVector(0, 0);
		newCenter = new PVector(0, 0);
		myRegister();
	}

	public Canvas() {
		zoom = 1;
		offset = new PVector(0, 0);
		startOffset = new PVector(0, 0);
		endOffset = new PVector(0, 0);
		canvasMouse = new PVector(0, 0);
		newCenter = new PVector(0, 0);
	}

	public void setPApplet(PApplet app) {
		Canvas.app = app;
	}

	/**
	 * This method MUST be invoked iteratively to get a fresh mouseCoordinate.
	 * Ideally within PApplet.draw()
	 */
	public void transform() {
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
	private void zoomIn(float val) {
		zoom += val;
		// The values of this substraction factor need to be revised
		offset.sub(new PVector(100, 80));
	}

	/**
	 * Zoom out keyboard
	 * 
	 * @param val
	 */
	private void zoomOut(float val) {
		zoom -= val;
		if (zoom < 0.1) {
			zoom = 0.1f;
		}
		// The values of this addition factor need to be revised
		offset.add(new PVector(100, 80));
	}

	/**
	 * Returns the current zoom value
	 * 
	 * @return current zoom value
	 */
	public float getZoomValue() {
		return zoom;
	}

	/**
	 * Returns the current mouse coordinates in the transformed canvas
	 * 
	 * @return current mouse coordinates in the transformed canvas
	 */
	public static PVector getCanvasMouse() {
		return canvasMouse;
	}

	public void translateCenter(float x, float y) {
		newCenter = new PVector(x, y);
	}

	public void displayValues(PVector pos) {
		// **** Legends
		app.fill(255, 90);
		app.textAlign(PConstants.RIGHT);
		app.text("Graph file: " + GraphPad.getFile().getName(), pos.x, pos.y + 15);
		app.text("Mouse on canvas: " + canvasMouse, pos.x, pos.y + 25);
		app.text("Zoom: " + zoom, pos.x, pos.y + 35);
		// app.text("Offset: " + offset, pos.x, pos.y + 45);
		// app.text("startOffset: " + startOffset, pos.x, pos.y + 55);
		// app.text("endOffset: " + endOffset, pos.x, pos.y + 65);
		// app.textAlign(PConstants.CENTER);
	}

	public void showLegend(PVector pos) {
		app.fill(255, 90);
		app.textAlign(PConstants.RIGHT);
		app.text("Hold SHIFT and right mouse button to pan", pos.x, pos.y);
		app.text("use 'a' to zoom in, 'z' to zoom  out, and 'c' to close a community", pos.x, pos.y + 10);
		app.text("Press r to restore zoom and pan to default values", pos.x, pos.y + 20);
		app.textAlign(PConstants.CENTER);
	}

	public void showControlPanelMessages(PVector pos) {
		app.fill(255, 90);
		app.textAlign(PConstants.LEFT);
		if (UserSettings.getInstance().getIdSearch() != null) {
			app.text("Searching for node: " + UserSettings.getInstance().getIdSearch(), pos.x, pos.y);
		}

		app.textAlign(PConstants.CENTER);
	}

	public void originCrossHair() {
		app.stroke(255);
		app.strokeWeight(0.5F);
		app.line(0, -app.height, 0, app.height);
		app.line(-app.width, 0, app.width, 0);
	}

	public static boolean isCanvasInTransformation() {
		return canvasBeingTransformed;
	}

	// *** Events registration P3
	public void myRegister() {
		app.registerMethod("mouseEvent", this);
		app.registerMethod("keyEvent", this);
	}

	public void keyEvent(KeyEvent k) {
		kPressed(k);
		kReleased(k);
		setEventOnCanvas(true);
	}

	public void mouseEvent(MouseEvent e) {
		if (e.getAction() == MouseEvent.RELEASE) {
			mReleased(e);
			setEventOnCanvas(true);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mPressed(e);
			setEventOnCanvas(true);
		}
		if (e.getAction() == MouseEvent.DRAG) {
			mDragged(e);
			setEventOnCanvas(true);
		}

	}

	// *** Event related methods
	private void mPressed(MouseEvent arg0) {
		startOffset.set(app.mouseX, app.mouseY, 0);
	}

	private void mReleased(MouseEvent arg0) {
		canvasBeingTransformed = false;
	}

	private void mDragged(MouseEvent e) {
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

	private void kPressed(KeyEvent k) {
		Timer timer = new Timer();

		// Control of zoom with keyboard
		if (k.getAction() == KeyEvent.PRESS) {
			int n = 1;
			if (k.getKey() == 'a') {
				// canvas will being zoomed for n minutes
				if (!canvasBeingZoomed) {
					canvasBeingZoomed = true;
					timer.schedule(new RemindTask(), n * 1000);
				}
				zoomIn(0.1f);
			} else if (k.getKey() == 'z') {
				// canvas will being zoomed for n minutes
				if (!canvasBeingZoomed) {
					canvasBeingZoomed = true;
					timer.schedule(new RemindTask(), n * 1000);
				}
				zoomOut(0.1f);
			} else if (k.getKey() == 'r') {
				reset();
			} else if (k.getKeyCode() == 16) {
				shiftDown = k.isShiftDown();
			}
		}
	}

	private void kReleased(KeyEvent k) {
		if (k.getKeyCode() == 16 && k.getAction() == KeyEvent.RELEASE) {
			shiftDown = false;
			canvasBeingTransformed = false;
		}
	}

	public static void setEventOnCanvas(boolean eventOnCanvas) {
		Canvas.mouseEventOnCanvas = eventOnCanvas;
	}

}

// *** Complementary classes
/**
 * It indicates when the canvas is not being zoomed anymore.
 * 
 * @author Loaiza Quintana
 *
 */
class RemindTask extends TimerTask {

	public RemindTask() {

	}

	public void run() {
		Canvas.canvasBeingZoomed = false;
		System.out.println("Canvas> RemindTask> Time's up!");
	}
}
