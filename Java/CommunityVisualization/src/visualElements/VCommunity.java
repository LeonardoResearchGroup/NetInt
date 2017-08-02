package visualElements;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PConstants;
import java.awt.event.MouseEvent;
import interactiveElements.Button;

/**
 * A community is defined as a subset of nodes linked to other nodes within the
 * same subset. This class visualizes only edges within a visual network
 * VNetwork. External links to or from nodes of other communities are not
 * displayed, yet.
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
	private int i, increment;
	public VNetwork vNet;
	private int Id;

	public VCommunity(PApplet app, VNetwork vNet, float posX, float posY, float diam) {
		super(app, posX, posY, diam);
		this.vNet = vNet;
		open = false;
		unlocked = false;
		i = 0;
		increment = 10;
		setLayoutParameters();
		setNetworkParameters();
	}

	private void setLayoutParameters() {
		// Calculate the community diameter
		minCommunityDiam = 70;
		maxCommunityDiam = 200;
		minCommunitySize = 1;
		maxCommunitySize = 1000;
		diam = PApplet.map(vNet.size(), minCommunitySize, maxCommunitySize, minCommunityDiam, maxCommunityDiam);
	}

	private void setNetworkParameters() {
		// **SORTERS
		//vNet.sortInDegree();
		vNet.sortOutDegree();

		// ** CIRCULAR LAYOUT
		// vNet.circularLayout(app, pos, diam / 2);

		// ** LINEAR LAYOUT
		// determine the origin and end PVectors for the linear visualization
		// PVector extendedCordOrigin = new PVector(pos.x -
		// getLength(PConstants.TWO_PI, diam / 4), pos.y + diam / 2);
		// PVector extendedCordEnd = new PVector(pos.x +
		// getLength(PConstants.TWO_PI, diam / 4), pos.y + diam / 2);

		PVector extendedCordOrigin = new PVector(50, pos.y + diam / 2);
		PVector extendedCordEnd = new PVector(app.width - 50, pos.y + diam / 2);

		vNet.linearLayout(app, extendedCordOrigin, extendedCordEnd);
	}

	public void show() {
		// Switch control
		app.text("COMMUNITY X", pos.x, pos.y);
		if (unlocked) {
			if (!open) {

				if (i < 180) {
					i += increment;
				} else {
					open = true;
				}
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
		showSimpleCommunityInvolute();
	}

	private void showSimpleCommunityInvolute() {
		// Visualize nodes & edges
		boolean visualizeNodes = isMouseOver();
		boolean visualizeEdges = unlocked;
		boolean showInvolute = unlocked;
		vNet.show(app, visualizeNodes, visualizeEdges, showInvolute);

		// Visualize community cover
		app.stroke(100);
		app.strokeWeight(0);
		app.fill(255, 30);
		// *** DRAWS RIGHT HALF
		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		PVector intersect = getXY(angle2);
		// *** Arc right half
		app.arc(pos.x, pos.y, diam, diam, angle2, PConstants.TWO_PI + PConstants.HALF_PI);

		// *** DRAWS LEFT HALF
		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		intersect = getXY(angle2);
		// *** Arc left half
		app.arc(pos.x, pos.y, diam, diam, PConstants.HALF_PI, angle2);
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

	private float getLength(float angle, float radius) {
		float rtn = angle * radius;
		return rtn;
	}

	// ***** Setters
	public void setVNetwork(VNetwork net) {
		vNet = net;
	}

	// ***** Events
	public void mouseClicked(MouseEvent e) {
		if (isMouseOver()) {
			unlocked = !unlocked;
			PApplet.println(this.getClass() + " mouseClicked");
		}
	}

}