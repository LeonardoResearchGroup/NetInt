package interactiveElements;

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import processing.core.*;

public abstract class Button implements MouseListener {

	public PApplet app;
	private int wdth, hght;
	public float diam;
	public PVector pos;

	public Button(PApplet app, float x, float y, float diam) {
		this.app = app;
		this.diam = diam;
		pos = new PVector(x, y);
		app.addMouseListener(this);
	}

	public Button(PApplet app, float x, float y, int wdth, int hght) {
		this.app = app;
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
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
	
	public void setX(float x){
		pos.x = x;
	}
	
	public void setY(float y){
		pos.y = y;
	}
	
	public void setDiam(float d){
		diam = d;
	}

	// ---------------- implementing MouseEvent methods ----------------
	public void mousePressed(MouseEvent e) {
		//if (isMouseOver())
			//PApplet.println(this.getClass() + " mousePressed");
	}

	public void mouseReleased(MouseEvent e) {
		//if (isMouseOver())
			//PApplet.println(this.getClass() + " mouseReleased");
	}

	public void mouseExited(MouseEvent e) {
		// PApplet.println("mouse exited");
	}

	public void mouseEntered(MouseEvent e) {
		// PApplet.println("mouse entered");
	}

	public void mouseClicked(MouseEvent e) {
		//if (isMouseOver())
			//PApplet.println(this.getClass() + " mouse clicked");
	}
}