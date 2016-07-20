package visualElements;

import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PConstants;
import visualElements.interactive.VisualAtom;
import containers.Container;
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
	private boolean unlocked;
	private int i, increment, count, containerIterations;
	public Container container;
	private PVector lastPosition;

	public VCommunity(Node node, Container container) {
		super(node, (float) container.dimension.width / 2, (float) container.dimension.height / 2, 0);
		// super(app, node, (float) container.layout.getSize().getWidth() / 2,
		// (float) container.layout.getSize().getHeight() / 2, 0);
		this.container = container;
		unlocked = false;
		i = 0;
		increment = 10;
		count = 0;
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
		count = 0;
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

	public void show(Canvas canvas) {
		// Register mouse, touch or key events triggered on this object in the
		// context of the canvas
		registerEvents(canvas);
		// retrieve mouse coordinates
		detectMouseOver(canvas.getCanvasMouse());		
		// mouse interaction
		unlocked = leftClicked;

		// Move vCommunity to mouse position
		if (rightPressed) {
			setCommunityCenter(new PVector(canvas.getCanvasMouse().x, canvas.getCanvasMouse().y));
		}
		// Community cover data
		showCoverLable(canvas);

		// Open or close the community
		boolean communityIsOpen = showCommunityCover(canvas);

		// Initialize community
		container.initialize(communityIsOpen);

		// Layout iterations
		if (communityIsOpen && container.isCurrentLayoutIterative()) {
			if (count < containerIterations) {
				container.stepIterativeLayout(pos);
				count++;
			}
		}
		// Update position of each visualElement in the container relative to
		// current vCommunity center
		updateContainer(communityIsOpen);

		// Visualize nodes & edges in container
		boolean visualizeNodes = isMouseOver;
		boolean visualizeEdges = unlocked && communityIsOpen;
		boolean showInvolute = unlocked && communityIsOpen;
		showCommunity(canvas, visualizeNodes, visualizeEdges, showInvolute);
		
		if (isMouseOver){
			setAlpha(110);
		}else{
			setAlpha(90);
		}
	}

	private boolean showCommunityCover(Canvas canvas) {
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
		canvas.app.stroke(100);
		canvas.app.strokeWeight(0);
		canvas.app.fill(255, alpha);
		// *** DRAWS RIGHT HALF INVOLUTE
		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		// PVector intersect = getXY(angle2);
		// *** Arc right half
		canvas.app.arc(pos.x, pos.y, diam, diam, angle2, PConstants.TWO_PI + PConstants.HALF_PI);

		// *** DRAWS LEFT HALF INVOLUTE
		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;
		// Gets the PVector for angle2
		// intersect = getXY(angle2);
		// *** Arc left half
		canvas.app.arc(pos.x, pos.y, diam, diam, PConstants.HALF_PI, angle2);
		return open;
	}

	public void showCommunity(Canvas canvas, boolean showNodes, boolean showEdges, boolean networkVisible) {
		if (showNodes || networkVisible) {
			for (VisualAtom vA : container.getVNodes()) {
				VNode vN = (VNode) vA;
				vN.setDiam(vN.getNode().getOutDegree(0) + 5);
				vN.show(canvas, networkVisible);
			}
		}
		if (showEdges || networkVisible) {
			for (VEdge vE : container.getVEdges()) {
				vE.show(canvas.app);
			}
		}
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

	public void showCoverLable(Canvas canvas) {
		canvas.app.textAlign(PConstants.CENTER, PConstants.CENTER);
		canvas.app.fill(250, 200);
		canvas.app.text(container.getName(), pos.x, pos.y);
		canvas.app.noFill();
		canvas.app.stroke(180);
		//canvas.app.rect(0, 0, container.dimension.width, container.dimension.height);
		canvas.app.text("Nodes: " + container.getGraph().getVertexCount(), pos.x, pos.y + 20);
	}

	// ***** Setters
	public void setContainer(Container nodesAndEdges) {
		container = nodesAndEdges;
	}

}
