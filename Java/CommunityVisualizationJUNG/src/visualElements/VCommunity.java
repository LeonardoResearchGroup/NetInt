package visualElements;

import processing.core.PVector;
import processing.event.KeyEvent;
import visualElements.gui.VisibilitySettings;
import visualElements.primitives.VisualAtom;
import processing.core.PApplet;

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

	private static final long serialVersionUID = 1L;
	private float minCommunityDiam, maxCommunityDiam;
	private int minCommunitySize, maxCommunitySize;
	protected boolean hasNodeFound = false;

	public Container container;
	private VCommunityCover comCover;
	private PVector lastPosition;

	// Search tool
	protected Node nodeFound;
	protected VCommunity nodeFoundInSuperCommunity;
	private String idSearch = null;

	public VCommunity(Node node, Container container) {
		super(node, (float) container.dimension.width / 2, (float) container.dimension.height / 2);
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

	boolean arrangementIterationsDone = false;

	public void show() {
		// Display the community cover
		comCover.show(container, this, hasNodeFound);

		// Check if community cover is completely deployed
		if (comCover.isUnlockedAndDeployed()) {
			// If the layout is iterative
			if (container.isCurrentLayoutIterative()) {
				// Show only nodes if layout is still organizing elements
				showCommunityContents(comCover.isUnlocked(), container.stepIterativeLayout(pos).done() || container.isDone());
			} else {
				// If layout not iterative show nodes and edges
				showCommunityContents(comCover.isUnlocked(), comCover.isDeployed());
			}
		}
		// Move vCommunity to mouse position if right button is pressed
		if (isMouseOver && rightPressed) {
			setCommunityCenter(Canvas.getCanvasMouse());
		}
		// Update position of each visualElement in the container relative to
		// current vCommunity center. This is needed to reposition deployed and
		// collapsed VCommunities with the mouse
		updateContainer(Canvas.eventOnCanvas);
	}

	public void showCommunityContents(boolean showNodes, boolean showEdges) {
		// ** Display VNodes
		// GUI
		if (VisibilitySettings.getInstance().mostrarNodos()) {
			// VCommunity open
			if (showNodes) {
				for (VisualAtom vA : container.getVNodes()) {
					// If vA is a VCommunity
					if (vA instanceof VCommunity) {
						VCommunity vC = (VCommunity) vA;
						// *** External Edges
						vC.show();
					} else {
						// If vA is a VNode
						VNode vN = (VNode) vA;
						vN.setVisibility(VisibilitySettings.getInstance().getUmbralGrados());
						// If vN is visible
						if (vN.isVisible()) {
							vN.show();
						}
					}
				}
			}
		}
		// ** Display VEdges
		// GUI
		if (VisibilitySettings.getInstance().mostrarVinculos()) {
			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !rightPressed && !Canvas.canvasBeingZoomed) {
				// Show internal edges
				for (VEdge vE : container.getVEdges()) {
					vE.setVisibility(VisibilitySettings.getInstance().getVolTransaccion());
					vE.show();
				}
				// Show external edges
				for (VEdge vEE : container.getVExtEdges()) {
					vEE.setVisibility(VisibilitySettings.getInstance().getVolTransaccion());
					vEE.show();
				}
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

	public void setCommunityCenter(PVector newPos) {
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

	/**
	 * Build all the external edges of a clicked community
	 */
	public void buildExternalEdges() {
		this.container.initialize(true);
		for (VisualAtom vA : container.getVNodes()) {
			// If vA is a VCommunity
			if (vA instanceof VCommunity) {
				VCommunity vC = (VCommunity) vA;
				if (vC.isMouseOver && !vC.comCover.isDeployed()) {
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
							if (internalVC.comCover.isDeployed() && !internalVC.equals(vC)) {
								vC.container.runExternalEdgeFactory(container.rootGraph, internalVC.container.getName(),
										internalVC.container);
								vC.container.retrieveExternalVNodeSuccessors(container.rootGraph, internalVC.container);
								internalVC.container.retrieveExternalVNodeSuccessors(container.rootGraph, vC.container);
							}
						}
					}
				} else if (vC.isMouseOver && vC.comCover.isDeployed()) {
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
		return hasNodeFound;
	}

	public void hasSoughtNode(boolean isSought) {
		this.hasNodeFound = isSought;
	}

	/**
	 * Reset the visual attributes of a Node that contains the node found by the
	 * search tool
	 */
	public void resetNodeFound() {
		if (nodeFound != null) {
			nodeFound.setFound(false);
			hasNodeFound = false;
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
