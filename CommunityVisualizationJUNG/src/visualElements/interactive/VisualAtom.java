package visualElements.interactive;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;
import visualElements.Canvas;

public abstract class VisualAtom {
	public Canvas canvas;
	public PVector mouse;
	private int wdth, hght;
	public float diam;
	public PVector pos;
	public boolean isMouseOver;
	public boolean leftClicked, rightClicked, centerClicked;
	public boolean leftPressed, rightPressed, centerPressed;
	protected int alpha = 90;

	public VisualAtom(float x, float y, float diam) {
		// this.canvas = canvas;
		this.diam = diam;
		pos = new PVector(x, y);
		leftClicked = false;
		canvas = null;
	}

	public VisualAtom(float x, float y, int wdth, int hght) {
		// this.canvas = canvas;
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
		leftClicked = false;
		canvas = null;
	}

	public abstract void show(Canvas canvas);

	protected void registerEvents(Canvas canvas) {
		if (this.canvas == null) {
			this.canvas = canvas;
			eventRegister(this.canvas.app);
		} else if (!this.canvas.equals(canvas)) {
			this.canvas = canvas;
			eventRegister(this.canvas.app);
			//System.out.println("VAtom> registerCanvas:  Atom registered");
		}
	}

	public void detectMouseOver(PVector mouse) {
		isMouseOver = false;
		// If the button is a rectangle
		if (wdth != 0) {
			// verifies the x range
			if (mouse.x > pos.x && mouse.x < (pos.x + wdth)) {
				// verifies the y range
				if (mouse.y > pos.y && mouse.y < (pos.y + hght)) {
					isMouseOver = true;
				}
			}
		}
		// if the button is an ellipse
		else {
			if (PApplet.dist(mouse.x, mouse.y, pos.x, pos.y) <= diam / 2) {
				isMouseOver = true;
			}
		}
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
		if (isMouseOver) {
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

	private void mouseClicked(MouseEvent e) {
		if (isMouseOver) {
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
	public void eventRegister(PApplet theApp) {
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
