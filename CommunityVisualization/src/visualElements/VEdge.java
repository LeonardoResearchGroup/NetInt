package visualElements;

import processing.core.*;

import java.util.ArrayList;

import graphElements.Edge;
import graphElements.Node;

public class VEdge {
	Edge edge;
	boolean aboveArc;
	PVector orig, dest;
	int color;
	Bezier bezier;

	public VEdge(Edge edge) {
		this.edge = edge;
		aboveArc = true;
	}

	public void layout(ArrayList<VNode> vNodes, ArrayList<Node> nodes) {
		// look in the collection of nodes where is the edge's source
		int indxOrg = nodes.indexOf(edge.getSource());
		if (indxOrg > -1) {
			// ask for its coordinates
			orig = vNodes.get(indxOrg).pos;
		}

		// look in the collection of nodes where is the edge's target
		int indxDes = nodes.indexOf(edge.getTarget());
		if (indxDes > -1) {
			// ask for its coordinates
			dest = vNodes.get(indxDes).pos;
		}
		setDirection(indxOrg, indxDes);
	}

	public void makeBezier() {
		try {
			// if not loop
			if (!edge.isLoop()) {
				float control = (dest.x - orig.x) / 2;
				if (!edge.isDirected())
					control = Math.abs(control);
				bezier = new Bezier(orig.x, orig.y, orig.x, orig.y - control, dest.x, dest.y - control, dest.x, dest.y);

			} else {
				bezier = new Bezier(orig.x, orig.y, orig.x - 20, orig.y - 20, orig.x + 20, orig.y - 20, dest.x, dest.y);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void show(PApplet app) {
		bezier.drawBezier2D(app);
		bezier.drawHeadBezier2D(app);
	}

	public void showSourceToTarget(PApplet app) {
		try {
			app.noFill();
			app.stroke(200, 30);
			// if not loop
			if (!edge.isLoop()) {
				float control = (dest.x - orig.x) / 2;
				if (!edge.isDirected())
					control = Math.abs(control);
				app.bezier(orig.x, orig.y, orig.x, orig.y - control, dest.x, dest.y - control, dest.x, dest.y);

			} else {
				app.bezier(orig.x, orig.y, orig.x - 20, orig.y - 20, orig.x + 20, orig.y - 20, dest.x, dest.y);
			}
			// app.line(dest.x, dest.y, dest.x+10, dest.y-10);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setDirection(int indxS, int indxT) {
		if (edge.isDirected()) {
			if (indxS - indxT < 0)
				aboveArc = true;
			else
				aboveArc = false;
		}
	}

	// getters and setters
	public boolean isAboveArc() {
		return aboveArc;
	}
}
