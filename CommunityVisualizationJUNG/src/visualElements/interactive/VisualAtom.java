package visualElements.interactive;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import processing.core.*;

public abstract class VisualAtom implements MouseListener {

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
		app.addMouseListener(this);
	}

	public VisualAtom(PApplet app, float x, float y, int wdth, int hght) {
		this.app = app;
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
		leftClicked = false;
		app.addMouseListener(this);
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
	public void mousePressed(MouseEvent e) {
		if (isMouseOver()) {
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				leftPressed = true;
				break;
			case MouseEvent.BUTTON2:
				centerPressed = true;
				break;
			case MouseEvent.BUTTON3:
				rightPressed = true;
				break;
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			leftPressed = false;
			break;
		case MouseEvent.BUTTON2:
			centerPressed = false;
			break;
		case MouseEvent.BUTTON3:
			rightPressed = false;
			break;
		}

	}

	public void mouseExited(MouseEvent e) {
		// PApplet.println("mouse exited");
	}

	public void mouseEntered(MouseEvent e) {
		// PApplet.println("mouse entered");
	}

	public void mouseClicked(MouseEvent e) {
		if (isMouseOver()) {
			switch (e.getButton()) {
			case MouseEvent.BUTTON1:
				leftClicked = !leftClicked;
				break;
			case MouseEvent.BUTTON2:
				centerClicked = !centerClicked;
				break;
			case MouseEvent.BUTTON3:
				rightClicked = !rightClicked;
				break;
			}
		}
	}
}
