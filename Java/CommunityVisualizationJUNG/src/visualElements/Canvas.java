package visualElements;

import java.util.Timer;
import java.util.TimerTask;

import processing.core.*;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import visualElements.gui.VisibilitySettings;

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
	// Transformation control
	private boolean shiftDown;
	private boolean canvasBeingTransformed;
	public boolean canvasBeingZoomed = false;

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
		app.text("Canvas mouse: " + canvasMouse, pos.x, pos.y + 15);
		app.text("Zoom: " + zoom, pos.x, pos.y + 25);
		app.text("Offset: " + offset, pos.x, pos.y + 35);
		app.text("startOffset: " + startOffset, pos.x, pos.y + 45);
		app.text("endOffset: " + endOffset, pos.x, pos.y + 55);
		app.textAlign(PConstants.CENTER);
	}

	public void showLegend(PVector pos) {
		app.fill(255, 90);
		app.textAlign(PConstants.RIGHT);
		app.text("Hold SHIFT and right mouse button to pan", pos.x, pos.y);
		app.text("use 'a' to zoom in, 'z' to zoom  out, and 'c' to close a community", pos.x, pos.y+ 10);
		app.text("Press r to restore zoom and pan to default values", pos.x, pos.y + 20);
		app.textAlign(PConstants.CENTER);
	}

	public void showControlPanelMessages(PVector pos) {
		app.fill(255, 90);
		app.textAlign(PConstants.LEFT);
		if (VisibilitySettings.getInstance().getIdBuscador() != null) {
			app.text("Searching for node: " + VisibilitySettings.getInstance().getIdBuscador(), pos.x, pos.y);
		}

		app.textAlign(PConstants.CENTER);
	}

	public void originCrossHair() {
		app.stroke(255);
		app.strokeWeight(0.5F);
		app.line(0, -app.height, 0, app.height);
		app.line(-app.width, 0, app.width, 0);
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
		Timer timer = new Timer();

		// Control of zoom with keyboard
		if (k.getAction() == KeyEvent.PRESS) {
			if (k.getKey() == 'a') {
				// canvas will being zoomed for 4 minutes
				if (!canvasBeingZoomed) {
					canvasBeingZoomed = true;
					timer.schedule(new RemindTask(this), 4 * 1000);
				}
				in(0.1f);
			} else if (k.getKey() == 'z') {
				// canvas will being zoomed for 4 minutes
				if (!canvasBeingZoomed) {
					canvasBeingZoomed = true;
					timer.schedule(new RemindTask(this), 4 * 1000);
				}
				out(0.1f);
			} else if (k.getKey() == 'r') {
				reset();
			} else if (k.getKeyCode() == 16) {
				shiftDown = k.isShiftDown();
				System.out.println("Pressed " + shiftDown);
			}
		}
	}

	/**
	 * It indicates when the canvas will not being zoomed anymore.
	 * 
	 * @author Loaiza Quintana
	 *
	 */
	class RemindTask extends TimerTask {
		private Canvas c;

		public RemindTask(Canvas c) {
			this.c = c;
		}

		public void run() {
			c.canvasBeingZoomed = false;
			System.out.println("Time's up!");
			// timer.cancel(); //Terminate the timer thread
		}
	}

	private void kReleased(KeyEvent k) {
		if (k.getKeyCode() == 16 && k.getAction() == KeyEvent.RELEASE) {
			shiftDown = false;
			System.out.println("Released " + shiftDown);
			canvasBeingTransformed = false;
		}
	}
}
