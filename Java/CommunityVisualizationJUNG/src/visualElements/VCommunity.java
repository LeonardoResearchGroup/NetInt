package visualElements;

import processing.core.PVector;
import visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;

import containers.Container;
import graphElements.Node;

/**
 * A community is defined as a subset of nodes linked to other nodes within the
 * same subset.
 * 
 * @author jsalam
 *
 */
public class VCommunity extends VNode implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float minCommunityDiam, maxCommunityDiam;
	private int minCommunitySize, maxCommunitySize;
	private float angle = PConstants.TWO_PI / 360;
	private float angle2;
	private boolean unlocked;
	// Booleanos de Cesar
	public boolean notOpened;
	public boolean itOpens;
	boolean vNodesCentered = false;
	// public boolean despuesOpens = false;
	public boolean communityIsOpen = false;

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
		containerIterations = 50;
		// Move vNodes relative to the vCommnity center
		updateContainer(true);
		// *CESAR
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

	public void show(Canvas canvas) {
		// Visualize nodes & edges in container
		boolean visualizeNodes = false;
		boolean visualizeEdges = false;
		// Register mouse, touch or key events triggered on this object in the
		// context of the canvas
		registerEvents(canvas);
		// retrieve mouse coordinates
		detectMouseOver(canvas.getCanvasMouse());
		// *CESAR
		itOpens = false;
		// mouse interaction
		unlocked = leftClicked;
		// Move vCommunity to mouse position
		if (rightPressed) {
			setCommunityCenter(new PVector(canvas.getCanvasMouse().x, canvas.getCanvasMouse().y));
		}
		// Open or close the community
		communityIsOpen = showCommunityCover(canvas);
		// *CESAR
		// check if occurs the first community opening
		if (notOpened && communityIsOpen) {
			itOpens = true;
			notOpened = false;
		}
		// Initialize community: building vNodes and vEdges
//		container.initialize(communityIsOpen);
		// Layout iterations
		if (communityIsOpen && container.isCurrentLayoutIterative()) {
			if (count < containerIterations) {
				container.stepIterativeLayout(pos);
				visualizeEdges = false;
				count++;
			} else {
				visualizeEdges = unlocked && communityIsOpen;
			}
		} else {
			visualizeEdges = unlocked && communityIsOpen;
		}
		// Update position of each visualElement in the container relative to
		// current vCommunity center
		updateContainer(communityIsOpen);
		// Visualize nodes & edges in container
		visualizeNodes = unlocked;
		showCommunity(canvas, visualizeNodes, visualizeEdges, communityIsOpen);
		// Circular mask of Community cover data
		showCoverLable(canvas);
	}

	private boolean showCommunityCover(Canvas canvas) {
		// Colors
		canvas.app.stroke(100);
		canvas.app.strokeWeight(0);
		canvas.app.fill(getColorRGB());
		if (isMouseOver) {
			canvas.app.fill(darker());
		}
		// Logics
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
		// *** DRAWS RIGHT HALF INVOLUTE
		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;
		// *** Arc right half
		canvas.app.arc(pos.x, pos.y, diam, diam, angle2, PConstants.TWO_PI + PConstants.HALF_PI);
		// *** DRAWS LEFT HALF INVOLUTE
		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;
		// *** Arc left half
		canvas.app.arc(pos.x, pos.y, diam, diam, PConstants.HALF_PI, angle2);
		return open;
	}

	public void showCommunity(Canvas canvas, boolean showNodes, boolean showEdges, boolean networkVisible) {
		if (networkVisible) {
			if (showEdges) {
				for (

				VEdge vE : container.getVEdges()) {
					vE.show(canvas.app);
				}
				// System.out.println("PintaArsitas");
				for (VEdge vEE : container.getVExtEdges()) {
					vEE.show(canvas.app);
					// System.out.println("PintaExt");
				}
			}
			if (showNodes) {
				for (VisualAtom vA : container.getVNodes()) {
					// If vA is a VCommunity
					if (vA instanceof VCommunity) {
						VCommunity vC = (VCommunity) vA;
						// *** External Edges
						vC.show(canvas);
					} else {
						// If vA is a VNode
						VNode vN = (VNode) vA;
						/*
						 * outDegree 0= degree in rootCommunity, outDegree 1 =
						 * degree in this community
						 */
						vN.setDiam(vN.getNode().getOutDegree(1) + 5); //
						vN.show(canvas, networkVisible);
						// This is to rearrange the vNodes once the community is
						// opened
						if (vNodesCentered) {
							// reset vNode coordinates to the coordinates
							// assigned in the container's layout
							PVector newOrigin = new PVector(container.dimension.width / 2,
									container.dimension.height / 2);
							container.translateVElementCoordinates(vN, PVector.sub(pos, newOrigin));
						}
					}
				}
				vNodesCentered = false;
			}
		} // If network not visible
		else {
			// If network never opened
			if (!notOpened && !vNodesCentered) {
				// Center all the VNodes in this community to the coordinates of
				// the vNode of this community when the cover is closed
				for (VisualAtom vA : container.getVNodes()) {
					vA.pos.set(pos);
				}
				vNodesCentered = true;
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
		// canvas.app.rect(0, 0, container.dimension.width,
		// container.dimension.height);
		canvas.app.text("Nodes: " + container.getGraph().getVertexCount(), pos.x, pos.y + 20);
	}

	// ***** Setters
	public void setContainer(Container nodesAndEdges) {
		container = nodesAndEdges;
	}

	// *** Events

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}
	
	/**
	 * Build all the external edges of a clicked community
	 */
	public void buildExternalEdges(){
		this.container.initialize(true);
		for (VisualAtom vA : container.getVNodes()) {
			// If vA is a VCommunity
			if (vA instanceof VCommunity) {
				VCommunity vC = (VCommunity) vA;
				if (vC.isMouseOver && !vC.communityIsOpen) {
					vC.container.initialize(true);
					// Builds vEdges for all open communities
					for (VisualAtom internalVA : container.getVNodes()) {
						// If vA is a VCommunity
						if (internalVA instanceof VCommunity) {
							VCommunity internalVC = (VCommunity) internalVA;
							if (internalVC.communityIsOpen && !internalVC.equals(vC)) {
								vC.container.runExternalEdgeFactory(container.rootGraph,
										internalVC.container.getName(), internalVC.container);
								vC.container.retrieveExternalVNodeSuccessors(container.rootGraph,internalVC.container);
							}
						}
					}
				} else if(vC.isMouseOver && vC.communityIsOpen){
					vC.container.setvExtEdges(new ArrayList<VEdge>());
				}
			}
		}
	}
}