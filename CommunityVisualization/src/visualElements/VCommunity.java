package visualElements;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PConstants;
import java.awt.event.MouseEvent;
import interactiveElements.Button;

/**
 * A community is defined as a subset of nodes linked to other nodes within the
 * same subset. This class visualizes only edges within the community. External
 * links to or from nodes of other communities are not displayed
 * 
 * @author jsalam
 *
 */
public class VCommunity extends Button {
	private float minCommunityDiam, maxCommunityDiam;
	private int minCommunitySize, maxCommunitySize;
	private float angle = PConstants.TWO_PI / 360;
	private float angle2;
	private boolean open, unlocked;
	private PVector intersect, involute, extendedCordOrigin, extendedCordEnd;
	int i, increment;
	public VNetwork vNet;

	public VCommunity(PApplet app, VNetwork vNet, float posX, float posY, float diam) {
		super(app, posX, posY, diam);
		this.vNet = vNet;
		open = false;
		unlocked = false;
		i = 0;
		increment = 4;
		setCommunityParameters();
		setNetworkParameters();
	}

	private void setCommunityParameters() {
		// Calculate the community diameter
		minCommunityDiam = 50;
		maxCommunityDiam = 300;
		minCommunitySize = 1;
		maxCommunitySize = 1000;
		diam = PApplet.map(vNet.size(), minCommunitySize, maxCommunitySize, minCommunityDiam, maxCommunityDiam);
		// determine the origin and end PVectors for the linear visualization
		extendedCordOrigin = new PVector(pos.x - getLength(360f, diam / 2), pos.y + diam / 2);
		extendedCordEnd = new PVector(pos.x + getLength(360f, diam / 2), pos.y + diam / 2);
	}

	private void setNetworkParameters() {
		vNet.sortInDegree();
		vNet.sortOutDegree();
		//vNet.linearLayout(app, extendedCordOrigin, extendedCordEnd);
		vNet.circularLayout(app, pos, diam / 2);
	}

	public void show() {
		// DetectMouse is necessary to detect mouse events
		detectMouse();
		// Switch control
		if (unlocked) {
			if (!open) {
				if (i < 180) {
					i += increment;
				} else {
					open = true;
				}
			} else {
				// Show nodes
				vNet.show(app, true, false);
			}
		} else {
			if (i > 0) {
				i -= increment;
			}
			if (i == 0) {
				open = false;
			}
		}
		// Open or close the community
		showInvolute();
	}
	
	private void involuteNetwork(){
	//	vNet.setNodesXY();
	}
	
	private void showInvolute() {
		// *** DRAWS RIGHT HALF
		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		intersect = getXY(angle2);

		// *** Arc right half
		app.stroke(100);
		app.strokeWeight(0);
		app.fill(255, 30);
		app.arc(pos.x, pos.y, diam, diam, angle2, PConstants.TWO_PI + PConstants.HALF_PI);

		// *** Involute right half
		involute = getXY(PConstants.PI + (angle2 + PConstants.HALF_PI));
		app.stroke(100);
		app.strokeWeight(0);
		app.ellipse( pos.x + intersect.x * (diam / 2) + involute.x * getLength(i, diam),
				pos.y + intersect.y * (diam / 2) + involute.y * getLength(i, diam),5,5);
		app.line(pos.x + intersect.x * (diam / 2), pos.y + intersect.y * (diam / 2),
				pos.x + intersect.x * (diam / 2) + involute.x * getLength(i, diam),
				pos.y + intersect.y * (diam / 2) + involute.y * getLength(i, diam));
	
		// *** DRAWS LEFT HALF
		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		intersect = getXY(angle2);

		// *** Arc left half
		app.stroke(100);
		app.strokeWeight(0);
		app.arc(pos.x, pos.y, diam, diam, PConstants.HALF_PI, angle2);

		// *** Involute left half
		involute = getXY(PConstants.PI + (angle2 + PConstants.HALF_PI));
		app.stroke(100);
		app.strokeWeight(0);
		app.line(pos.x + intersect.x * (diam / 2), pos.y + intersect.y * (diam / 2),
				pos.x + intersect.x * (diam / 2) - involute.x * getLength(i, diam),
				pos.y + intersect.y * (diam / 2) - involute.y * getLength(i, diam));
		involute = getXY(PConstants.PI + (angle2 + PConstants.HALF_PI));
	}

	// ***** Getters
	/**
	 * Gets the XY PVector for a given angle for a circumference of radius 1
	 * 
	 * @param angle
	 * @return
	 */
	private PVector getXY(float angle) {
		PVector rtn = new PVector(PApplet.cos(angle), PApplet.sin(angle));
		return rtn;
	}

	/**
	 * Gets the XY PVector on the circumference of radius 1 for a given angle
	 * 
	 * @param angle
	 * @return
	 */
	private PVector getXYInvolute(float angle) {
		PVector rtn = new PVector((PApplet.cos(angle) + angle * PApplet.sin(angle)),
				(PApplet.sin(angle) - angle * PApplet.cos(angle)));
		return rtn;
	}

	/**
	 * Calculates the length of a cord (circumference section)
	 * 
	 * @param i
	 *            The angle of the cord
	 * @param rad
	 *            The radius of the circumference
	 * @return
	 */
	private float getLength(float i, float rad) {
		float rtn = ((PConstants.TWO_PI * rad) / 360) * i;
		return rtn / 2;
	}

	// ***** Setters
	public void setVNetwork(VNetwork net) {
		vNet = net;
	}

	// ***** Events
	public void mouseClicked(MouseEvent e) {
		if (detectMouse()) {
			unlocked = !unlocked;
			PApplet.println(this.getClass() + " mouseClicked");
		}
	}

}
