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
	//
	private boolean notOpened;
	public boolean itOpens;
	public boolean despuesOpens = false;
	
	private int i, increment, count, containerIterations;
	public Container container;
	private PVector lastPosition;

	public VCommunity(PApplet app, Node node, Container container) {
		super(app, node, (float) container.dimension.width / 2, (float) container.dimension.height / 2, 0);
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
		
		notOpened = true;
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

	public void show() {
		itOpens = false;
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
		
		//check if occurs the first community opening
		if( notOpened && communityIsOpen){
			itOpens = true;
			notOpened = false;
			// Initialize community
			container.initialize(communityIsOpen);
		}

		
		//System.out.println(communityIsOpen);

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
		boolean visualizeNodes = isMouseOver();
		boolean visualizeEdges = unlocked && communityIsOpen;
		boolean showInvolute = unlocked && communityIsOpen;
		if(!container.getName().equals("SA") || despuesOpens)
			showCommunity(visualizeNodes, visualizeEdges, showInvolute);
		if( itOpens ){
			despuesOpens = true;
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

	public void showCommunity(boolean showNodes, boolean showEdges, boolean networkVisible) {
		if (showNodes || networkVisible) {
			for (VisualAtom vA : container.getVNodes()) {
				VNode vN = (VNode) vA;
				vN.setDiam(vN.getNode().getOutDegree(0) + 5);
				vN.show(showNodes, networkVisible);
				//System.out.println("PintaNodos");
			}
		}
		if (showEdges || networkVisible) {
			for (VEdge vE : container.getVEdges()) {
				vE.show(app);
				//System.out.println("PintaArsitas");
			}
			for (VEdge vEE : container.getVExtEdges()) {
				vEE.show(app);
				//System.out.println("PintaExt");
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

	public void showCoverLable() {
		app.fill(250, 200);
		app.text(container.getName(), pos.x, pos.y);
		app.noFill();
		app.stroke(180);
		//app.rect(0, 0, container.dimension.width, container.dimension.height);
		app.text("Nodes: " + container.getGraph().getVertexCount(), pos.x, pos.y + 20);
	}

	// ***** Setters
	public void setContainer(Container nodesAndEdges) {
		container = nodesAndEdges;
	}

	// ***** Events

}
