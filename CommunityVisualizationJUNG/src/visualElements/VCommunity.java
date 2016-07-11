package visualElements;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PConstants;
import utilities.visualArrangements.Container;
import visualElements.interactive.VisualAtom;

import java.awt.event.MouseEvent;
import graphElements.Node;

/**
 * A community is defined as a subset of nodes linked to other nodes within the
 * same subset.
 * 
 * @author jsalam
 *
 */
public class VCommunity extends VNode {
	private float minCommunityDiam, maxCommunityDiam;
	private int minCommunitySize, maxCommunitySize;
	private float angle = PConstants.TWO_PI / 360;
	private float angle2;
	private boolean unlocked, iterationCompleted;
	private int i, increment, containerIterations;
	public Container container;
	private PVector lastPosition;
	private int count = 0;

	public VCommunity(PApplet app, Node node, Container container) {
		super(app, node, (float) container.layout.getSize().getWidth() / 2,
				(float) container.layout.getSize().getHeight() / 2, 0);
		this.container = container;
		unlocked = false;
		i = 0;
		increment = 10;
		setLayoutParameters();
		lastPosition = pos;
		containerIterations = 100;
		// Move vNodes relative to the vCommnity center
		updateContainer(true);
	}

	public VCommunity(VNode vNode, Container container) {
		super(vNode);
		this.container = container;
		unlocked = false;
		i = 0;
		increment = 10;
		lastPosition = pos;
		containerIterations = 100;
		setLayoutParameters();
	}

	private void setLayoutParameters() {
		// Calculate the community diameter
		minCommunityDiam = 70;
		maxCommunityDiam = 200;
		minCommunitySize = 1;
		maxCommunitySize = 1000;
		diam = PApplet.map(container.size(), minCommunitySize, maxCommunitySize, minCommunityDiam, maxCommunityDiam);
	}

	public void show() {
		// mouse interaction
		unlocked = leftClicked;

		// Move vCommunity to mouse position
		if (rightPressed) {
			setCommunityCenter(new PVector(app.mouseX, app.mouseY));
		}
		// Community cover data
		showCoverLable();
		// Open or close the community
		boolean communityIsOpen = showCommunityCover();
		// Layout iterations
		if (communityIsOpen && !iterationCompleted) {
			if (count < containerIterations) {
				container.stepIterativeLayout(pos);
				count++;
			} else {
				iterationCompleted = true;
				System.out.println("VCommunity > Layout iteration completed");
			}
		}
		// Update position of each visualElement in the container relative to
		// current vCommunity center
		updateContainer(communityIsOpen);

		// Visualize nodes & edges in container
		boolean visualizeNodes = isMouseOver();
		boolean visualizeEdges = unlocked && communityIsOpen;
		boolean showInvolute = unlocked && communityIsOpen;
		showCommunity(visualizeNodes, visualizeEdges, showInvolute);

	}

	public void showCommunity(boolean showNodes, boolean showEdges, boolean networkVisible) {
		if (showNodes || networkVisible) {
			for (VisualAtom vA : container.getVNodes()) {
				VNode vN = (VNode) vA;
				vN.setDiam(vN.getNode().getOutDegree() + 5);
				vN.show(showNodes, networkVisible);
			}
		}
		if (showEdges || networkVisible) {
			for (VEdge vE : container.getVEdges()) {
				vE.show(app);
			}
		}
	}

	private boolean showCommunityCover() {
		boolean open = false;
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
		// Visualize community cover
		app.stroke(100);
		app.strokeWeight(0);
		app.fill(255, 30);
		// *** DRAWS RIGHT HALF INVOLUTE
		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		// PVector intersect = getXY(angle2);
		// *** Arc right half
		app.arc(pos.x, pos.y, diam, diam, angle2, PConstants.TWO_PI + PConstants.HALF_PI);

		// *** DRAWS LEFT HALF INVOLUTE
		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		// intersect = getXY(angle2);
		// *** Arc left half
		app.arc(pos.x, pos.y, diam, diam, PConstants.HALF_PI, angle2);
		return open;
	}

	public void updateContainer(boolean update) {
		if (update) {
			if (!lastPosition.equals(pos)) {
				// Get the difference of centers
				PVector diffPos = lastPosition;
				diffPos.sub(pos);
				// set new vNodes coordinates
				container.updateVNodesCoordinates(diffPos);
				lastPosition = pos;
			}
		}
	}

	public void setCommunityCenter(PVector newPos) {
		this.pos = newPos;
	}

	public void showCoverLable() {
		app.fill(250, 200);
		app.text(container.getName(), pos.x, pos.y);
		app.noFill();
		app.stroke(180);
		app.rect(0, 0, container.layout.getSize().width, container.layout.getSize().height);
		app.text("Nodes: " + container.getGraph().getVertexCount(), pos.x, pos.y + 20);
	}

	// ***** Setters
	public void setContainer(Container nodesAndedges) {
		container = nodesAndedges;
	}

	// ***** Events
	// public void mouseClicked(MouseEvent e) {
	// if (isMouseOver()) {
	// System.out.println(leftClicked);
	// unlocked = !unlocked;
	// }
	// }

}
