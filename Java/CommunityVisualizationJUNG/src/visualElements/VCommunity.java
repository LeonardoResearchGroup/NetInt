package visualElements;

import processing.core.PVector;
import processing.event.KeyEvent;
import visualElements.gui.UserSettings;
import visualElements.primitives.VisualAtom;
import processing.core.PApplet;

import java.awt.Color;

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

	private static final long serialVersionUID = 1L;
	private float minCommunityDiam, maxCommunityDiam;
	private int minCommunitySize, maxCommunitySize;

	public Container container;
	private VCommunityCover comCover;
	private PVector lastPosition;
	// This lock is used to control iterative behavior in
	// showCommunityContents()
	private boolean lock = false;

	// Search tool
	protected Node nodeFound;
	protected VCommunity nodeFoundInSuperCommunity;
	private String idSearch = null;
	protected boolean containsSearchedNode = false;

	private boolean vNodesCentered = true;
	private boolean externalEdgesBuilt = false;

	public VCommunity(Node node, Container container) {
		super(node, (float) container.getDimension().width / 2, (float) container.getDimension().height / 2);
		this.container = container;
		setLayoutParameters();
		lastPosition = pos;
		comCover = new VCommunityCover();
		// Move vNodes relative to the vCommnity center
		updateContainer(true);
	}

	private void setLayoutParameters() {
		// Calculate the community diameter
		minCommunityDiam = 70;
		maxCommunityDiam = 200;
		minCommunitySize = 1;
		maxCommunitySize = 5000;
		setDiameter(
				PApplet.map(container.size(), minCommunitySize, maxCommunitySize, minCommunityDiam, maxCommunityDiam));
	}

	public void show() {
		// Display the community cover
		comCover.show(container, this, containsSearchedNode);
		// Set once coordinates for all elements inside the container
		this.container.initialize();
		// Check if community cover is completely deployed
		if (comCover.isDeployed()) {
			// If the layout is iterative
			if (container.isCurrentLayoutIterative()) {
				// Show only nodes if layout is still organizing elements
				showCommunityContents(comCover.isUnlocked(),
						container.stepIterativeLayout(pos).done() || container.isDone());
			} else {
				// If layout not iterative show nodes and edges
				showCommunityContents(comCover.isUnlocked(), comCover.isDeployed());
			}
		}
		// Move vCommunity to mouse position if right button is pressed
		if (isMouseOver && rightPressed) {
			moveCommunityCenterTo(Canvas.getCanvasMouse());
		}
		// Update position of each visualElement in the container relative to
		// current vCommunity center. This is needed to reposition deployed and
		// collapsed VCommunities with the mouse
		updateContainer(Canvas.eventOnCanvas);
	}

	/**
	 * A community contains VisualAtoms that might be VNodes or VCommunities.
	 * Therefore a higher tier VCommunity may contain nested VCommunities. In
	 * order to display each VisualAtom correctly the class type is determined.
	 * 
	 * @param showNodes
	 * @param showEdges
	 */

	public void showCommunityContents(boolean showNodes, boolean showEdges) {
		// ** Display VEdges
		// GUI
		// Internal Edges
		if (UserSettings.getInstance().mostrarVinculosInt()) {
			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !rightPressed && !Canvas.canvasBeingZoomed) {
				// Show internal edges
				for (VEdge vE : container.getVEdges()) {
					// If the edge has any attribute
					if (vE.getEdge().getAttributeSize() > 0) {
						vE.setVisibility(UserSettings.getInstance().getVolTransaccion());
					}
					if (container.currentLayout == Container.CIRCULAR) {
						vE.setLayoutAndCenter(container.currentLayout, this.pos);
					}
					vE.show();
				}
			}
		}
		// External edges
		if (UserSettings.getInstance().mostrarVinculosExt()) {
			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !rightPressed && !Canvas.canvasBeingZoomed) {
				// Show external edges
				for (VEdge vEE : container.getVExtEdges()) {
					// If the edge has any attribute
					// These edges have no attributes and no source or
					// target.
					// It needs to be solved
					if (vEE.getEdge().getAttributeSize() > 0) {
						vEE.setVisibility(UserSettings.getInstance().getVolTransaccion());
					}
					if (container.currentLayout == Container.CIRCULAR) {
						vEE.setLayoutAndCenter(container.currentLayout, this.pos);
					}
					Color color = new Color(255, 100, 180);
					vEE.setColor(color.getRGB());
					vEE.show();
				}
			}

		}
		// ** Display VNodes
		if (UserSettings.getInstance().mostrarNodos()) {
			// VCommunity open
			if (showNodes) {
				// VCommunities
				for (VCommunity vC : container.getVCommunities()) {
					vC.setVisibility(true);
					vC.show();
					if (vC.comCover.isUnlocked() && !vC.lock) {
						container.setIncidentEdgesVisibility(vC.getNode(), false);
						// Create edges that connect VNodes of this community
						// with those of
						// other communities
						if (!vC.externalEdgesBuilt) {
							vC.container.buildExternalEdges(container.getVCommunities());
							// vC.container.buildExternalEdges(this.container.getVCommunities());
							vC.externalEdgesBuilt = true;
						}
						vC.lock = true;
					}
					if (!vC.comCover.isUnlocked() && vC.lock) {
						container.setIncidentEdgesVisibility(vC.getNode(), true);
						vC.lock = false;
					}
				}
				// VNodes
				for (VNode vN : container.getJustVNodes()) {
					vN.setVisibility(true);
					if (vNodesCentered) {
						// reset vNode coordinates to the coordinates
						// assigned in the container's layout
						PVector newOrigin = new PVector(container.getDimension().width / 2,
								container.getDimension().height / 2);
						container.translateVElementCoordinates(vN, PVector.sub(pos, newOrigin));
						vNodesCentered = true;
					}
					// If vN is visible and not centered
					if (!vNodesCentered) {
						vN.show(vN.isDisplayed());
					}
				}
				vNodesCentered = false;
			} else {
				for (VisualAtom vA : container.getVNodes()) {
					vA.pos.set(pos);
					// We have to known which nodes are visible.
					if (vA instanceof VNode) {
						VNode vN = (VNode) vA;
						vN.setDisplayed(false);
					}
				}
				vNodesCentered = true;
			}
		}
	}

	public void updateContainer(boolean update) {
		if (update) {
			if (!lastPosition.equals(pos)) {
				// Get the difference of centers
				PVector diffPos = lastPosition.sub(pos);
				// set new vNodes coordinates
				container.updateVNodesCoordinates(diffPos);
				lastPosition = pos;
			}
		}
	}

	public void moveCommunityCenterTo(PVector newPos) {
		pos = newPos;
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
				comCover.setEnableClosing(true);
			}
		} else {
			if (k.getAction() == KeyEvent.RELEASE) {
				if (k.getKey() == 'c' || k.getKey() == 'C') {
					comCover.setEnableClosing(true);
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
		if (UserSettings.getInstance().getIdBuscador() != null) {
			// If the search tool is looking for something new
			if (idSearch != UserSettings.getInstance().getIdBuscador()) {
				idSearch = UserSettings.getInstance().getIdBuscador();
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
		return containsSearchedNode;
	}

	public void hasSoughtNode(boolean isSought) {
		this.containsSearchedNode = isSought;
	}

	/**
	 * Reset the visual attributes of a Node that contains the node found by the
	 * search tool
	 */
	public void resetNodeFound() {
		if (nodeFound != null) {
			nodeFound.setFound(false);
			containsSearchedNode = false;
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
