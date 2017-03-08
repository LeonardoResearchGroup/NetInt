package containers;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PVector;
import visualElements.primitives.VisualAtom;

public class LinearLayout extends Arrangement  {
	public PApplet app;

	public LinearLayout(PApplet app) {
		super();
		this.app = app;
	}

	/**
	 * Assigns coordinates to each VisualAtom on an horizontal axis. The length
	 * of the axis is the width of the Applet minus 2* margin. Center is at
	 * (0,0)
	 * 
	 * @param margin
	 *            the gap between the Applet margin and each extreme of the axis
	 */
	public void linearLayout(float margin, ArrayList<VisualAtom> visualElements) {
		clearLayout(visualElements);
		float dist = app.width - (2 * margin);
		float xStep = (float) dist / (visualElements.size()-1);
		PVector left = new PVector(dist/-2, 0);
		int count = 0;
		
		// Organize nodes on a line
		Iterator<VisualAtom> itr = visualElements.iterator();
		while (itr.hasNext()) {
			VisualAtom tmp = itr.next();
			tmp.setX(left.x + (xStep * count));
			tmp.setY(left.y);
			count++;
		}
	}

}