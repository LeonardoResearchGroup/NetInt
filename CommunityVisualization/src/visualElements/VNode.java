package visualElements;

import java.awt.event.MouseEvent;

import graphElements.Node;
import interactiveElements.Button;
import processing.core.*;

public class VNode extends Button{
	//PVector pos;
	private Node vertex;

//	public VNode(Node vertex) {
//		this.vertex = vertex;
//		//pos = new PVector(0, 0, 0);
//	}
	
	public VNode(PApplet app, Node vertex, float x, float y, float diam) {
		super (app,x,y,diam);
		this.vertex = vertex;
		//pos = new PVector(0, 0, 0);
	}
	

	public void show(float color) {
		app.fill(color);
		app.noStroke();
		//app.text(vertex.getOutDegree(), pos.x, pos.y + 10);
		app.ellipse(pos.x, pos.y, diam, diam);
	}

	public void show() {
		app.fill(200,90);
		app.noStroke();
		//app.text(vertex.getOutDegree(), pos.x, pos.y + 10);
		app.ellipse(pos.x, pos.y, diam, diam);
	}

	// Getter and setter

	public float getX() {
		return pos.x;
	}

	public float getY() {
		return pos.y;
	}

	public float getZ() {
		return pos.z;
	}

	public Node getVertex() {
		return vertex;
	}

	public void setVertex(Node vertex) {
		this.vertex = vertex;
	}
	
	// ***** Events
	public void mouseClicked(MouseEvent e) {
		if (detectMouse()) {
			PApplet.println(vertex.getId());
		}
	}
}
