package utilities.visualArrangements;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import visualElements.interactive.VisualAtom;

public class CircularArrangement implements Arrangement {

	public PApplet app;
	private String name;

	public CircularArrangement(PApplet app, String name) {
		this.app = app;
		this.name = name;
	}

	public void circularLayout(float radius,
			ArrayList<VisualAtom> visualElements) {
		// Organize nodes on a circle
		float step = PConstants.TWO_PI / visualElements.size();
		Iterator<VisualAtom> itrVNode = visualElements.iterator();
		int count = 0;
		while (itrVNode.hasNext()) {
			VisualAtom tmp = itrVNode.next();
			// calculated the angle rotated to the top quadrant
			float angle = calcAngle(step, count) - PConstants.HALF_PI;
			// get XY
			PVector XY = getXY(angle);
			// multiply by radius
			XY.mult(radius);
			// set new XY
			tmp.pos.set(XY);
			count++;
		}
	}

	private PVector getXY(float angle) {
		PVector rtn = new PVector(PApplet.cos(angle), PApplet.sin(angle));
		return rtn;
	}

	public float calcAngle(float step, int index) {
		float rtnAngle = (step * index);
		return rtnAngle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean hasName(String str) {
		if (str.equals(name))
			return true;
		else
			return false;
	}
}
