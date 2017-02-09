package visualElements.primitives;

import java.awt.Color;
import java.io.Serializable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;
import visualElements.Canvas;

/**
 * Default values: diameter = 5, color = Color(255, 255, 255, 90)
 * 
 * @author jsalam
 *
 */
public abstract class VisualAtom implements Serializable {

	private static final long serialVersionUID = 1L;
	public PVector mouse;
	private int wdth, hght;
	private float diameter;

	public PVector pos;
	public boolean isMouseOver;
	public boolean leftClicked, rightClicked, centerClicked;
	public boolean leftPressed, rightPressed, centerPressed;

	protected Color color;
	// Events
	protected boolean eventsRegistered;

	public VisualAtom(float x, float y) {
		setDiameter(5);
		pos = new PVector(x, y);
		leftClicked = false;
		color = new Color(255, 255, 255, 90);
	}

	public VisualAtom(float x, float y, int wdth, int hght) {
		setDiameter(5);
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
		leftClicked = false;
		color = new Color(255, 255, 255, 90);
	}

	public abstract void show();

	/**
	 * Register mouse, touch or key events triggered on this object in the
	 * context of the canvas' PApplet
	 * 
	 */
	protected void registerEvents() {
		eventRegister(Canvas.app);
	}

	/**
	 * This method is invoked every time there is a mouse event. See
	 * mouseEvent(MouseEvent e)
	 * 
	 * @param mouse
	 */
	private boolean detectMouseOver(PVector mouse) {
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
			float dist = PApplet.dist(mouse.x, mouse.y, pos.x, pos.y);
			if (dist <= diameter / 2) {
				isMouseOver = true;
			}
		}
		return isMouseOver;
	}

	// *** Getters and Setters

	public void setX(float x) {
		pos.x = x;
	}

	public void setY(float y) {
		pos.y = y;
	}

	public void setDiameter(float d) {
		this.diameter = d;
	}

	public float getDiameter() {
		return diameter;
	}
	// *** Color Methods ***

	public void setAlpha(int alpha) {
		color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	public int getAlpha() {
		return color.getAlpha();
	}

	public Color getColor() {
		return color;
	}

	public int getColorRGB() {
		return color.getRGB();
	}

	public int setColor(Color color) {
		this.color = color;
		return this.color.getRGB();
	}

	public int setColor(int red, int green, int blue) {
		this.color = new Color(red, green, blue);
		return this.color.getRGB();
	}

	public int setColor(int red, int green, int blue, int alpha) {
		this.color = new Color(red, green, blue, alpha);
		return this.color.getRGB();
	}

	public int setColor(int brightness, int alpha) {
		this.color = new Color(brightness, brightness, brightness, alpha);
		return this.color.getRGB();
	}

	public int darker() {
		return this.color.darker().getRGB();
	}

	public int brighter() {
		return this.color.brighter().getRGB();
	}

	// ---------------- MouseEvent methods ----------------
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
	public abstract void eventRegister(PApplet theApp);

	public void mouseEvent(MouseEvent e) {
		detectMouseOver(Canvas.getCanvasMouse());
		if (e.getAction() == MouseEvent.CLICK) {
			mouseClicked(e);
		} else if (e.getAction() == MouseEvent.RELEASE) {
			mouseReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mousePressed(e);
		}
	}
}
