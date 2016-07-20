package visualElements;

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
	private PVector canvasMouse;
	// A Vector for the canvas center
	private PVector newCenter;
	// Transformation control
	boolean shiftDown;
	boolean canvasBeingTransformed;
	public PApplet app;

	public Canvas(PApplet app) {
		this.app = app;
		zoom = 1;
		offset = new PVector(0, 0);
		startOffset = new PVector(0, 0);
		endOffset = new PVector(0, 0);
		canvasMouse = new PVector(0, 0);
		newCenter = new PVector(0, 0);
		myRegister();
	}

	/**
	 * This method MUST be invoked iteratively to get a fresh mouseCoordinate. Ideally within PApplet.draw()
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
		// System.out.println(this);
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
	private void in(float val) {
		zoom += val;
	}

	/**
	 * Zoom out keyboard
	 * 
	 * @param val
	 */
	private void out(float val) {
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
		app.fill(255, 90);
		app.textAlign(PConstants.RIGHT);
		app.text("Canvas mouse: " + canvasMouse, pos.x, pos.y + 10);
		app.text("Zoom: " + zoom, pos.x, pos.y + 20);
		app.text("Offset: " + offset, pos.x, pos.y + 30);
		app.text("startOffset: " + startOffset, pos.x, pos.y + 40);
		app.text("endOffset: " + endOffset, pos.x, pos.y + 50);
		app.textAlign(PConstants.CENTER);
	}

	public void showLegend(PVector pos) {
		app.fill(255, 90);
		app.textAlign(PConstants.RIGHT);
		app.text("Hold SHIFT and the left mouse button to zoom and pan", pos.x, pos.y);
		app.text("Press r to restore zoom and pan to default values", pos.x, pos.y + 10);
		app.textAlign(PConstants.CENTER);
	}

	public boolean isCanvasInTransformation() {
		return canvasBeingTransformed;
	}

	// *** P3
	public void myRegister() {
		app.registerMethod("mouseEvent", this);
		app.registerMethod("keyEvent", this);
	}

	public void keyEvent(KeyEvent k) {
		kPressed(k);
		kReleased(k);
	}

	public void mouseEvent(MouseEvent e) {
		if (e.getAction() == MouseEvent.RELEASE) {
			mReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mPressed(e);
		}
		if (e.getAction() == MouseEvent.DRAG) {
			mDragged(e);
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
		// Control of zoom with keyboard
		if (k.getAction() == KeyEvent.PRESS) {
			if (k.getKey() == 'a') {
				in(0.1f);
			} else if (k.getKey() == 'z') {
				out(0.1f);
			} else if (k.getKey() == 'r') {
				reset();
			}
		}
		shiftDown = k.isShiftDown();
	}

	private void kReleased(KeyEvent k) {
		shiftDown = k.isShiftDown();
		canvasBeingTransformed = k.isShiftDown();
	}

}
