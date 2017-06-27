package netInt.containers;

import java.util.ArrayList;
import java.util.Iterator;

import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

public class CircularArrangement extends Arrangement {
	public PApplet app;

	public CircularArrangement(PApplet app, String name) {
		super();
		this.app = app;
	}

	public void layout(PVector center, float radius, ArrayList<VisualAtom> visualElements) {
		// clear previous layout
		clearLayout(visualElements);
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
			// center XY
			XY.add(center);
			// set new XY
			tmp.getPos().set(XY);
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
}
