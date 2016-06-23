package utilities.visualArrangements;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PVector;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

public class LinearArrangement implements Arrangement {
	public PApplet app;
	private String name;

	public LinearArrangement(PApplet app, String name) {
		this.app = app;
		this.name = name;
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
