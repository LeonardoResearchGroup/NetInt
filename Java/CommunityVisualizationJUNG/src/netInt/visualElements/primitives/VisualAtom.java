/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.visualElements.primitives;

import java.awt.Color;
import java.io.Serializable;

import netInt.canvas.Canvas;
import netInt.canvas.MouseHook;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;

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

	protected PVector pos;

	// The difference between the
	protected PVector deltaMouse;

	public boolean isMouseOver, isDragged;
	protected boolean leftClicked, rightClicked, centerClicked;
	protected boolean leftPressed, rightPressed, centerPressed;
	
	// This variable is used to control that the vElement is displayed only
	// once. It is useful to prevent that edges with the same source or target
	// vNode display the nodes more than once
	protected boolean displayed;

	protected Color color;
	protected Color propagatedColor = new Color(255,0,0);
	
	// Events
	protected boolean eventsRegistered;

	public VisualAtom(float x, float y) {
		setDiameter(5);
		pos = new PVector(x, y);
		leftClicked = false;
		color = new Color(255, 255, 255, 90);
		displayed = true;
	}

	public VisualAtom(float x, float y, int wdth, int hght) {
		setDiameter(5);
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
		leftClicked = false;
		color = new Color(255, 255, 255, 90);
		displayed = true;
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
	protected boolean detectMouseOver(PVector mouse) {
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

	public PVector getPos() {
		return pos;
	}

	public boolean isDisplayed() {
		return displayed;
	}

	public void setDisplayed(boolean displayed) {
		this.displayed = displayed;
	}

	public int setColor(Color color) {
		this.color = color;
		return this.color.getRGB();
	}

	public int setColor(int rgbColor) {
		this.color = new Color(rgbColor);
		return rgbColor;
	}

	public int setColor(int rgbColor, int alpha) {
		this.color = new Color(rgbColor);
		this.color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
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

	public int darker() {
		return this.color.darker().getRGB();
	}

	public int brighter() {
		return this.color.brighter().getRGB();
	}

	// ---------------- MouseEvent methods ----------------
	protected void mousePressed(MouseEvent e) {
		if (isMouseOver && displayed) {
			switch (e.getButton()) {
			case PConstants.LEFT:
				leftPressed = true;
				
				// hooks this vAtom
				MouseHook.getInstance().hook(this);

				// stores the mousePosition
				if (deltaMouse == null) {
					deltaMouse = new PVector(Canvas.getCanvasMouse().x - pos.x, Canvas.getCanvasMouse().y - pos.y);
				}
				
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

	protected void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case PConstants.LEFT:
			leftPressed = false;
			isDragged = false;
			deltaMouse = null;
			MouseHook.getInstance().release();
			break;
		case PConstants.CENTER:
			centerPressed = false;
			break;
		case PConstants.RIGHT:
			rightPressed = false;
			break;
		}
	}

	protected void mouseClicked(MouseEvent e) {
		
		if (isMouseOver && displayed) {
			// change booleans
			switch (e.getButton()) {
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

	protected void mouseDragged(MouseEvent e) {
		
		// Move VAtom
		if (MouseHook.getInstance().isHooked(this) && displayed) {
			pos = Canvas.getCanvasMouse().sub(deltaMouse);
			isDragged = true;
		}
	}

	// P3
	public abstract void eventRegister(PApplet theApp);

	public void mouseEvent(MouseEvent e) {
		if (displayed)
			detectMouseOver(Canvas.getCanvasMouse());
		if (e.getAction() == MouseEvent.CLICK) {
			mouseClicked(e);
		} else if (e.getAction() == MouseEvent.RELEASE) {
			mouseReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mousePressed(e);
		} else if (e.getAction() == MouseEvent.DRAG) {
			mouseDragged(e);
		}
	}

}
