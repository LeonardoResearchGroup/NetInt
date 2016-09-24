package visualElements.primitives;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.event.MouseEvent;
import visualElements.Canvas;
import visualElements.VCommunity;
import visualElements.VNode;

public abstract class VisualAtom {
	public Canvas canvas;
	public PVector mouse;
	private int wdth, hght;
	private float diameter;


	public PVector pos;
	public boolean isMouseOver;
	public boolean leftClicked, rightClicked, centerClicked;
	public boolean leftPressed, rightPressed, centerPressed;
	private boolean visible;
	protected Color color;

	public VisualAtom(float x, float y, float diam) {
		// this.canvas = canvas;
		this.diameter = diam;
		pos = new PVector(x, y);
		leftClicked = false;
		canvas = null;
		color = new Color(255, 255, 255, 90);
		visible = true;
	}

	public VisualAtom(float x, float y, int wdth, int hght) {
		// this.canvas = canvas;
		this.wdth = wdth;
		this.hght = hght;
		pos = new PVector(x, y);
		leftClicked = false;
		canvas = null;
		color = new Color(0, 100, 100, 90);
		visible = true;
	}

	public abstract void show(Canvas canvas);

	/**
	 * WARNING: Every instance of this class MUST invoke this method within
	 * show()
	 * 
	 * @param canvas
	 */
	protected void registerEvents(Canvas canvas) {
		if (this.canvas == null) {
			this.canvas = canvas;
			eventRegister(this.canvas.app);
		} else if (!this.canvas.equals(canvas)) {
			this.canvas = canvas;
			eventRegister(this.canvas.app);
			// System.out.println("VAtom> registerCanvas: Atom registered");
		}
	}

	/**
	 * WARNING: Every instance of this class MUST invoke this method within
	 * show()
	 * 
	 * @param mouse
	 */
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
			if (PApplet.dist(mouse.x, mouse.y, pos.x, pos.y) <= diameter / 2) {
				isMouseOver = true;
			}
		}
	}

	// *** Getters and Setters

	public void setX(float x) {
		pos.x = x;
	}

	public void setY(float y) {
		pos.y = y;
	}

	public void setDiameter(float d) {
		diameter = d;
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
	
	public boolean isVisible(){
		return visible;
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
	
	 protected void setVisibility(boolean visible){
		this.visible = visible;
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

	// public void eventRegister(PApplet theApp) {
	// theApp.registerMethod("mouseEvent", this);
	// }

	public void mouseEvent(MouseEvent e) {
		VCommunity tmpCommunity = null;
		VNode tmpNode = null;
		try {
			tmpCommunity = (VCommunity) this;
		} catch (java.lang.RuntimeException exCommunity) {
			try {
				tmpNode = (VNode) this;
			} catch (java.lang.RuntimeException exNode) {
				exNode.printStackTrace();
			}
		}
		if (tmpNode != null) {
			vNodeEvent(tmpNode, e);
		} else if (tmpCommunity != null) {
			vCommunityEvent(tmpCommunity, e);
		}
//		canvas.app.redraw();

	}

	private void vCommunityEvent(VCommunity vComm, MouseEvent e) {
		if (e.getAction() == MouseEvent.CLICK) {
			if(vComm.container.getName().equals("SubSubcommunities")){
				System.out.println("pintando externas");
				vComm.buildExternalEdges();
			}
			if (vComm != null && isMouseOver) {
				System.out.println("Visual Atom> event: " + vComm.container.getName());
			}
			mouseClicked(e);
		} else if (e.getAction() == MouseEvent.RELEASE) {
			mouseReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mousePressed(e);
		}
	}

	private void vNodeEvent(VNode vNode, MouseEvent e) {
		if (e.getAction() == MouseEvent.CLICK) {
			mouseClicked(e);
		} else if (e.getAction() == MouseEvent.RELEASE) {
			mouseReleased(e);
		} else if (e.getAction() == MouseEvent.PRESS) {
			mousePressed(e);
		}
	}

}
