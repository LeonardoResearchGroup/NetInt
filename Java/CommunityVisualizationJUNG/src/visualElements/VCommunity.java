package visualElements;

import processing.core.PVector;
import processing.event.KeyEvent;
import utilities.mapping.Mapper;
import visualElements.Canvas.RemindTask;
import visualElements.gui.VisibilitySettings;
import visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PConstants;

import java.util.ArrayList;
import java.util.Timer;

import containers.Container;
import edu.uci.ics.jung.algorithms.layout.Layout;
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
	private boolean enableClosing = false;
	// Controls nodes visibility
	// public float nodeVisibilityThreshold = 0;
	// public float edgeVisibilityThreshold = 0;

	private int i, increment, count, containerIterations;
	public Container container;
	private PVector lastPosition;

	// Search tool
	protected boolean hasFoundNode = false;
	protected Node nodeFound;
	protected VCommunity nodeFoundInSuperCommunity;
	private String idSearch = null;
	
	// visibility settings
	private int strokeThickness = 10;

	public VCommunity(Node node, Container container) {
		super(node, (float) container.dimension.width / 2, (float) container.dimension.height / 2);
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
		maxCommunitySize = 5000;
		setDiameter(
				PApplet.map(container.size(), minCommunitySize, maxCommunitySize, minCommunityDiam, maxCommunityDiam));
		// diam = Mapper.getInstance().radial(container.size())*100;
	}

	boolean arrangementIterationsDone = false;

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
		// if community not opened
		if (!unlocked) {
			// listen to the mouse
			unlocked = leftClicked;
			// ask if the C key is pressed
		} else if (enableClosing) {
			// listen to the mouse
			unlocked = leftClicked;
		}
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
		// container.initialize(communityIsOpen);
		// Layout iterations
		if (communityIsOpen && container.isCurrentLayoutIterative()) {
			// if (count < containerIterations) {

			if (!arrangementIterationsDone) {
				arrangementIterationsDone = container.stepIterativeLayout(pos);
				// container.stepIterativeLayout(pos);
				visualizeEdges = false;
				// count++;
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
		showCommunity(canvas, visualizeNodes, visualizeEdges);
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
		if (hasFoundNode && !communityIsOpen) {
			// canvas.app.fill(255, 0, 0);
			// canvas.app.ellipse(pos.x, pos.y, getDiameter() + 2, getDiameter()
			// + 2);
			canvas.app.fill(255, 0, 0, 100);
			canvas.app.arc(pos.x, pos.y, getDiameter()-10, getDiameter()-10,-PConstants.PI, PConstants.PI);
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
				canvas.app.stroke(255,20);
				canvas.app.strokeWeight(strokeThickness);
				canvas.app.fill(255,10);
				canvas.app.arc(pos.x, pos.y, getDiameter(), getDiameter(),-PConstants.PI, PConstants.PI);
			}
		} else {
			if (i > 0) {
				i -= increment;
			}
			if (i == 0) {
				open = false;
			}
			
			// Highlight the community cover if contains a found node
//			if (nodeFound != null) {
//				canvas.app.fill(255, 0, 0, 50);
//				canvas.app.arc(pos.x, pos.y, getDiameter(), getDiameter(), -PConstants.PI, PConstants.PI);
//			} else {
//				canvas.app.noFill();
//			}
		}
		// *** DRAWS RIGHT HALF INVOLUTE
		canvas.app.stroke(getColorRGB());
		canvas.app.strokeWeight(strokeThickness);
		canvas.app.noFill();
		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;
		// *** Arc right half
		canvas.app.arc(pos.x, pos.y, getDiameter(), getDiameter(), angle2, PConstants.TWO_PI + PConstants.HALF_PI);
		// *** DRAWS LEFT HALF INVOLUTE
		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;
		// *** Arc left half
		
		canvas.app.arc(pos.x, pos.y, getDiameter(), getDiameter(), PConstants.HALF_PI, angle2);
		return open;
	}

	public void showCommunity(Canvas canvas, boolean showNodes, boolean showEdges) {
		if (communityIsOpen) {
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
						vN.setVisibility(VisibilitySettings.getInstance().getUmbralGrados());
						/*
						 * The integer parameter for getOutDegree(int) is the
						 * number of the community to which that node belongs
						 * and from where you want to get its degree. int = 0 is
						 * the degree in the bottom tier (rootCommunity), int =
						 * 1 is the degree in tier 1 community, and so on.
						 */
						if (vN.isVisible()) {
							// vN.setDiam(vN.getNode().getOutDegree(1) + 5); //
							vN.show(canvas, communityIsOpen);
							// This is to rearrange the vNodes once the
							// community is opened
							if (itOpens) {
								// reset vNode coordinates to the coordinates
								// assigned in the container's layout
								PVector newOrigin = new PVector(container.dimension.width / 2,
										container.dimension.height / 2);
								container.translateVElementCoordinates(vN, PVector.sub(pos, newOrigin));
								// vNodesCentered = true;
							}
						}
					}
				}
				// vNodesCentered = false;
			}
			if (showEdges && !canvas.isCanvasInTransformation() && !rightPressed && !canvas.canvasBeingZoomed) {
				for (VEdge vE : container.getVEdges()) {
					vE.setVisibility(VisibilitySettings.getInstance().getVolTransaccion());
					vE.show(canvas.app);
				}
				for (VEdge vEE : container.getVExtEdges()) {
					vEE.setVisibility(VisibilitySettings.getInstance().getVolTransaccion());
					// vEE.setVisibility(1); // this is the edge minimal weight
					// to be visible
					vEE.show(canvas.app);
				}
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
		canvas.app.text("Edges: " + container.getGraph().getEdgeCount() , pos.x, pos.y + 35);
	}

	// ***** Setters
	public void setContainer(Container nodesAndEdges) {
		container = nodesAndEdges;
	}

	// ***** Events

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
		theApp.registerMethod("keyEvent", this);
	}

	public void keyEvent(KeyEvent k) {
		kPressed(k);
	}

	private void kPressed(KeyEvent k) {
		// Control closing communities
		if (k.getAction() == KeyEvent.PRESS) {
			if (k.getKey() == 'c' || k.getKey() == 'C') {
				enableClosing = true;
			}
		} else {
			if (k.getAction() == KeyEvent.RELEASE) {
				if (k.getKey() == 'c' || k.getKey() == 'C') {
					enableClosing = false;
				}
			}

		}
	}

	/**
	 * Build all the external edges of a clicked community
	 */
	public void buildExternalEdges() {
		this.container.initialize(true);
		for (VisualAtom vA : container.getVNodes()) {
			// If vA is a VCommunity
			if (vA instanceof VCommunity) {
				VCommunity vC = (VCommunity) vA;
				if (vC.isMouseOver && !vC.communityIsOpen) {
					// It clears the edges between communities of the opened
					// community
					container.getGraph().removeVertex(vC.getNode());
					container.getVEdges().clear();
					container.runEdgeFactory();
					vC.container.initialize(true);
					// Builds vEdges for all open communities
					for (VisualAtom internalVA : container.getVNodes()) {
						// If vA is a VCommunity
						if (internalVA instanceof VCommunity) {
							VCommunity internalVC = (VCommunity) internalVA;
							if (internalVC.communityIsOpen && !internalVC.equals(vC)) {
								vC.container.runExternalEdgeFactory(container.rootGraph, internalVC.container.getName(),
										internalVC.container);
								vC.container.retrieveExternalVNodeSuccessors(container.rootGraph, internalVC.container);
								internalVC.container.retrieveExternalVNodeSuccessors(container.rootGraph, vC.container);
							}
						}
					}
				} else if (vC.isMouseOver && vC.communityIsOpen) {
					vC.container.setvExtEdges(new ArrayList<VEdge>());
				}
			}
		}
	}

	// ***** Search Methods *******
	/**
	 * This method must be invoked ONLY by VCommunities that contain other
	 * VCommunities
	 */
	public void searchNode() {
		// Is the search tool is not null
		if (VisibilitySettings.getInstance().getIdBuscador() != null) {
			// If the search tool is looking for something new
			if (idSearch != VisibilitySettings.getInstance().getIdBuscador()) {
				idSearch = VisibilitySettings.getInstance().getIdBuscador();
				boolean rtn = searchNodeSuperCommunity(idSearch);
				System.out.println("VCommunity> searchNode: Cadena de busqueda encontro: " + rtn);
			}
		} else {
			// if the search tool is cleared
			if (idSearch != null) {
				resetNodeFoundSuperCommunity();
				idSearch = null;
				System.out.println("VCommunity> searchNode: Cadena de busqueda NULL: ");
			}
		}
	}

	/**
	 * Search for a Node using the ID parameter
	 * 
	 * @param id
	 * @return true if found
	 */
	public boolean searchNodeID(String id) {
		boolean rtn = false;
		for (Node node : container.getGraph().getVertices()) {
			if (node.getId().equals(id)) {
				node.setFound(true);
				hasSoughtNode(true);
				nodeFound = node;
				rtn = true;
				break;
			}
		}
		return rtn;
	}

	/**
	 * Search a vCommunity through the id of some node of his community and
	 * change the value of his hasFoundNode variable
	 * 
	 * @param id
	 * @return true if found
	 */
	public boolean searchNodeSuperCommunity(String id) {
		boolean rtn = false;
		for (VisualAtom vA : container.getVNodes()) {
			// If vA is a VCommunity
			if (vA instanceof VCommunity) {
				VCommunity vC = (VCommunity) vA;
				rtn = vC.searchNodeID(id);
				nodeFoundInSuperCommunity = vC;
				if (rtn)
					break;
			}
		}
		return rtn;
	}

	public boolean containsFoundNode() {
		return hasFoundNode;
	}

	public void hasSoughtNode(boolean isSought) {
		this.hasFoundNode = isSought;
	}

	/**
	 * Reset the visual attributes of a Node that contains the node found by the
	 * search tool
	 */
	public void resetNodeFound() {
		if (nodeFound != null) {
			nodeFound.setFound(false);
			hasFoundNode = false;
		}
	}

	/**
	 * Reset the visual attributes of a SubSubCommunity that contains a node
	 * found by the search tool
	 */
	public void resetNodeFoundSuperCommunity() {
		if (nodeFoundInSuperCommunity != null) {
			nodeFoundInSuperCommunity.resetNodeFound();
		}
	}
}
