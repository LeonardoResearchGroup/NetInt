package visualElements.interactive;

//import java.awt.event.MouseListener;
//import java.awt.event.MouseEvent;

import processing.core.*;
import processing.event.MouseEvent;

public abstract class VisualAtom {
	// public abstract class VisualAtom implements MouseListener {

	public PApplet app;
	private int wdth, hght;
	public float diam;
	public PVector pos;
	public boolean leftClicked, rightClicked, centerClicked;
	public boolean leftPressed, rightPressed, centerPressed;
	protected int alpha = 90;

	public VisualAtom(PApplet app, float x, float y, float diam) {
		this.app = app;
		this.diam = diam;
		pos = new PVector(x, y);
		leftClicked = false;
		myRegister(app);
		// app.addMouseListener(this);
	}

	public VisualAtom(PApplet app, float x, float y, int wdth, int hght) {
		this.app = app;
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
		leftClicked = false;
		myRegister(app);
		// app.addMouseListener(this);
	}

	public abstract void show();

	public boolean isMouseOver() {
		boolean temp = false;
		// If the button is a rectangle
		if (wdth != 0) {
			// verifies the x range
			if (app.mouseX > pos.x && app.mouseX < (pos.x + wdth)) {
				// verifies the y range
				if (app.mouseY > pos.y && app.mouseY < (pos.y + hght)) {
					temp = true;
				}
			}
		}
		// if the button is an ellipse
		else {
			if (PApplet.dist(app.mouseX, app.mouseY, pos.x, pos.y) <= diam / 2) {
				temp = true;
			}
		}
		return temp;
	}

	public void setX(float x) {
		pos.x = x;
	}

	public void setY(float y) {
		pos.y = y;
	}

	public void setDiam(float d) {
		diam = d;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public int getAlpha() {
		return alpha;
	}

	// ---------------- implementing MouseEvent methods ----------------
	private void mousePressed(MouseEvent e) {
		if (isMouseOver()) {
			switch (e.getButton()) {
			case PConstants.LEFT:
				leftPressed = true;
				break;
			case PConstants.CENTER:
				centerPressed = true;
				break;
			case PConstants.RIGHT:
				rightPressed = true;
				break;
			}
		}
	}

	private void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case PConstants.LEFT:
			leftPressed = false;
			break;
		case PConstants.CENTER:
			centerPressed = false;
			break;
		case PConstants.RIGHT:
			rightPressed = false;
			break;
		}

	}

	private void mouseExited(MouseEvent e) {
		// PApplet.println("mouse exited");
	}

	private void mouseEntered(MouseEvent e) {
		// PApplet.println("mouse entered");
	}

	private void mouseClicked(MouseEvent e) {
		if (isMouseOver()) {
			switch (e.getButton()) {
			// switch (e.getButton()) {
			case PConstants.LEFT:
				leftClicked = !leftClicked;
				break;
			case PConstants.CENTER:
				centerClicked = !centerClicked;
				break;
			case PConstants.RIGHT:
				rightClicked = !rightClicked;
				break;
			}
		}
	}

	// P3
	public void myRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

	public void mouseEvent(MouseEvent e) {
		if (e.getAction() == MouseEvent.CLICK) {
			mouseClicked(e);
		} else if (e.getAction() == MouseEvent.RELEASE) {
			mouseReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mousePressed(e);
		}
	}
}
