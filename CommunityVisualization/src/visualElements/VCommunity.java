package visualElements;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PConstants;
import utilities.visualArrangements.Container;
import visualElements.interactive.VisualAtom;

import java.awt.event.MouseEvent;

import comparators.OutDegreeComparator;

/**
 * A community is defined as a subset of nodes linked to other nodes within the
 * same subset. This class visualizes only edges within a visual network
 * VNetwork. External links to or from nodes of other communities are not
 * displayed, yet.
 * 
 * @author jsalam
 *
 */
public class VCommunity extends VisualAtom {
	private float minCommunityDiam, maxCommunityDiam;
	private int minCommunitySize, maxCommunitySize;
	private float angle = PConstants.TWO_PI / 360;
	private float angle2;
	private boolean open, unlocked;
	private int i, increment;
	public Container container;

	public VCommunity(PApplet app, Container container, float posX, float posY, float diam) {
		super(app, posX, posY, diam);
		this.container = container;
		open = false;
		unlocked = false;
		i = 0;
		increment = 10;
		setLayoutParameters();
		communityLayout();
	}

	private void setLayoutParameters() {
		// Calculate the community diameter
		minCommunityDiam = 70;
		maxCommunityDiam = 200;
		minCommunitySize = 1;
		maxCommunitySize = 1000;
		diam = PApplet.map(container.size(), minCommunitySize, maxCommunitySize, minCommunityDiam, maxCommunityDiam);
	}

	private void communityLayout() {
		// **SORTERS
		container.sort(new OutDegreeComparator());
		// ** CIRCULAR LAYOUT
		container.arrangeBy("circular");
		// ** LINEAR LAYOUT
		//container.arrangeBy("linear");
		container.recenter(pos);

	}

	public void show() {
		// Switch control
		app.fill(250,200);
		// Community Name
		app.text(container.getID(), pos.x, pos.y);
		app.text("Com. size: "+container.getGraph().size(), pos.x, pos.y+20);
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
		
		container.show(visualizeNodes, visualizeEdges, showInvolute);

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
	public void setVNetwork(Container net) {
		container = net;
	}

	public int getID() {
		return container.getID();
	}

	// ***** Events
	public void mouseClicked(MouseEvent e) {
		if (isMouseOver()) {
			unlocked = !unlocked;
		}
	}

}
