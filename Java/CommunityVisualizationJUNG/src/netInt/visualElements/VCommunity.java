/*******************************************************************************
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *  
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Copyright (C) 2017 Universidad Icesi & Bancolombia
 ******************************************************************************/
package netInt.visualElements;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import processing.core.PApplet;

import java.awt.Color;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import jViridis.ColorMap;
import netInt.canvas.Canvas;
import netInt.canvas.MouseHook;
import netInt.containers.Container;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.gui.UserSettings;
import netInt.utilities.filters.Filters;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.primitives.VisualAtom;

/**
 * A community is defined as a subset of nodes linked to other nodes within the
 * same subset.
 * 
 * @author jsalam
 *
 */
public class VCommunity extends VNode implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

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

	private boolean vNodesCentered = false;
	private boolean vCommunitiesCentered = false;

	private VNodeDescription description;

	public VCommunity(Node node, Container container) {
		super(node, (float) container.getDimension().width / 2, (float) container.getDimension().height / 2);
		this.container = container;
		lastPosition = pos;
		comCover = new VCommunityCover(this);
		node.setAbsoluteAttribute("Community size", container.size());
		// Move vNodes relative to the vCommnity center
		updateContainer(true);
		description = new VNodeDescription();
		setDiameter(30);
	}

	public void init() {
		float temp = Mapper.getInstance().convert(Mapper.LINEAR, container.size(), Mapper.NODE, "Community size");

		if ((temp * 50) < 10) {
			setDiameter(10);
		} else {
			setDiameter(temp * 50);
		}

		// if ((temp * 10) < 2) {
		// comCover.setStrokeThickness(1);
		// } else {
		// comCover.setStrokeThickness((int) (temp * 10));
		// }
		// try {
		// float minMax[] = Mapper.getInstance().getMinMaxForNodes("degree");
		// getNode().printAbsoluteAttributes();
		// float val = getNode().getFloatAttribute("degree");
		// System.out.println("min: " + minMax[0] + " max:" + minMax[1] + "
		// node:" + val);
		//
		// setColor(ColorMap.getInstance(ColorMap.PLASMA).getMappedColorRGB(minMax[0],
		// minMax[1],
		// getNode().getFloatAttribute("degree")), 100);
		// } catch (NullPointerException n) {
		// System.out.println(this.getClass().getName() + " " + n.toString());
		// //setColor(new Color(0));
		// }
	}

	public void show() {
		// Display the community cover
		comCover.show(container, containsSearchedNode);
		// Check if community cover is completely deployed
		if (comCover.isDeployed()) {
			setDisplayed(true);
			// if coordinates for all elements inside the container are set.
			// container.isInitializationComplete() is a boolean gate that
			// controls iteration
			if (container.isInitializationComplete()) {
				// Build external Edges of VCommunities included in this
				// VCommunity's container
				// int contador = 0;
				for (VCommunity vC : container.getVCommunities()) {
					if (vC.comCover.isDeployed()) {
						// System.out.println(this.getClass().getName() + ": "+
						// "VEZONAS " + this.container.getName());
						// System.out.println(this.getClass().getName() + ": "+
						// "VEZONAS " + contador);
						// build external edges
						vC.container.buildExternalEdges(container.getVCommunities());
						// contador++;
					}
				}
			} else {
				container.initialize();
			}

			// If the layout is iterative
			if (container.isLayoutIterative()) {
				// Show only nodes if layout is still organizing elements
				showCommunityContents(comCover.isUnlocked(), comCover.isUnlocked());
			} else {
				// If layout not iterative show nodes and edges
				showCommunityContents(comCover.isUnlocked(), comCover.isDeployed());
			}
		} else {
			setDisplayed(true);
			description.show(this);

			// Show community overall description on mouse over
			if (isMouseOver) {
				Canvas.app.text("Nodes: " + container.getGraph().getVertexCount(), getPos().x, getPos().y + 20);
				Canvas.app.text("Edges: " + container.getGraph().getEdgeCount(), getPos().x, getPos().y + 35);
			}
		}

		// Update position of each visualElement in the container relative to
		// current vCommunity center. This is needed to reposition deployed and
		// collapsed VCommunities with the mouse
		updateContainer(Canvas.mouseEventOnCanvas);
	}

	/**
	 * A community contains VisualAtoms that might be VNodes or VCommunities.
	 * Therefore a higher tier VCommunity may contain nested VCommunities. In
	 * order to display each VisualAtom correctly the class type is determined.
	 * 
	 * @param showNodes
	 *            true if show
	 * @param showEdges
	 *            true if show
	 */

	public void showCommunityContents(boolean showNodes, boolean showEdges) {
		// ** Display VEdges

		// Internal Edges

		// If the user chooses to turn on/off the internal edges
		if (UserSettings.getInstance().showInternalEdges()) {

			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !MouseHook.getInstance().isHooked(this)
					&& !Canvas.canvasBeingZoomed) {

				// If the container Layout iterates to distribute nodes
				// ****** Double check this boolean, it is doing nothing ****
				if (container.isDone()) {
					// Show internal edges
					for (VEdge vE : container.getVEdges()) {

						// If the edge has any attribute
						if (vE.getEdge().getAttributeSize() > 0) {
							vE.setVisibility(UserSettings.getInstance().getWeight());
						}

						if (container.currentLayout == Container.CONCENTRIC) {
							vE.setLayoutAndCenter(container.currentLayout, this.pos);
						}
						vE.show();
					}
				} else {

					// Show internal edges
					for (VEdge vE : container.getVEdges()) {

						// If the edge has any attribute
						if (vE.getEdge().getAttributeSize() > 0) {
							vE.setVisibility(UserSettings.getInstance().getWeight());
						}

						if (container.currentLayout == Container.CONCENTRIC) {
							vE.setLayoutAndCenter(container.currentLayout, this.pos);
						}
						vE.show();
					}
				}
			}
		}

		// External edges

		// If the user chooses to turn on/off the external edges
		if (UserSettings.getInstance().showExternalEdges()) {

			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !MouseHook.getInstance().isHooked(this)
					&& !Canvas.canvasBeingZoomed) {

				// Show external edges
				for (VEdge vEE : container.getVExtEdges()) {

					// If the edge has any attribute
					if (vEE.getEdge().getAttributeSize() > 0) {
						vEE.setVisibility(UserSettings.getInstance().getWeight());
					}

					if (container.currentLayout == Container.CONCENTRIC) {
						vEE.setLayoutAndCenter(container.currentLayout, this.pos);
					}

					// Set pink color
					Color color = new Color(255, 100, 180);
					vEE.setColor(color.getRGB());
					vEE.show();
				}
			}
		}

		// ** Display VNodes

		// If the user chooses to turn on/off the nodes
		if (UserSettings.getInstance().showNodes()) {

			// VCommunity open
			if (showNodes) {

				// VCommunities
				for (VCommunity vC : container.getVCommunities()) {

					vC.setVisibility(true);

					// set subCommunities coordinates relative to vCommunity
					// position
					if (!vCommunitiesCentered) {
						container.translateVElementCoordinates(vC, this.getPos());
					}

					vC.show();

					/*
					 * 
					 * if (vC.comCover.isUnlocked() && !vC.lock) {
					 * container.setIncidentEdgesVisibility(vC.getNode(),
					 * false); vC.lock = true; }
					 * 
					 * if (!vC.comCover.isUnlocked() && vC.lock) {
					 * container.setIncidentEdgesVisibility(vC.getNode(), true);
					 * vC.lock = false; }
					 * 
					 */

				}

				// This gate prevents vCommunity relocation in every draw() loop
				vCommunitiesCentered = true;

				// VNodes
				if (container.isLayoutIterative()) {

					container.stepIterativeLayout(pos).done();
				}

				// If the container does not contain vCommunities
				if (container.getVCommunities().size() == 0) {

					for (VNode vN : container.getVNodes()) {
						vN.setVisibility(true);

						// Center vNodes relative to a given position
						if (!vNodesCentered) {

							// set vNodes coordinates relative to vCommunity
							// position
							container.translateVElementCoordinates(vN, this.getPos());
						}

						// If vN is visible and not centered
						if (vNodesCentered) {
							vN.show(vN.isDisplayed());
							vN.setDisplayed(true);
						}
					}
					vNodesCentered = true;
				}
			} else {

				// This block centers all the elements in the container
				for (VisualAtom vA : container.getVNodes()) {

					// We have to known which nodes are visible.
					if (vA instanceof VNode) {
						VNode vN = (VNode) vA;
						vN.setDisplayed(false);
					}
				}
				vNodesCentered = false;
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

	// ***** Setters
	public void setContainer(Container nodesAndEdges) {
		container = nodesAndEdges;
	}

	// ***** Events

	public VCommunityCover getComCover() {
		return comCover;
	}

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

//	public void mouseEvent(MouseEvent e) {
//		if (displayed) {
//			detectMouseOver(Canvas.getCanvasMouse());
//		}
//		if (e.getAction() == MouseEvent.CLICK) {
//			mouseClicked(e);
//		} else if (e.getAction() == MouseEvent.RELEASE) {
//			mouseReleased(e);
//		} else if (e.getAction() == MouseEvent.PRESS) {
//			mousePressed(e);
//		} else if (e.getAction() == MouseEvent.DRAG) {
//			mouseDragged(e);
//		}
//	}

	// ***** Search Methods *******
	/**
	 * This method must be invoked ONLY by Higher Order Communities that contain
	 * Lower Order VCommunities
	 */
	public void searchNode() {
		// Is the search tool is not null
		if (UserSettings.getInstance().getIdSearch() != null) {
			// If the search tool is looking for something new
			if (idSearch != UserSettings.getInstance().getIdSearch()) {
				idSearch = UserSettings.getInstance().getIdSearch();
				boolean rtn = searchNodeSuperCommunity(idSearch);
				System.out.println("VCommunity> searchNode: Search chain found: " + rtn);
			}
		} else {
			// if the search tool is cleared
			if (idSearch != null) {
				resetNodeFoundSuperCommunity();
				idSearch = null;
				System.out.println("VCommunity> searchNode: Query didn't match any node");
			}
		}
	}

	/**
	 * Search for a Node using the ID parameter
	 * 
	 * @param id
	 *            Node id
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
	 *            Node id
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

	// ***** Between communities operations *****
	public DirectedSparseMultigraph<Node, Edge> detectLinkedCommunities(final VCommunity otherCommunity) {
		DirectedSparseMultigraph<Node, Edge> tmpGraph = new DirectedSparseMultigraph<Node, Edge>();
		// If not the same VCommunity
		if (!this.getNode().equals(otherCommunity.getNode())) {
			tmpGraph = Filters.filterAndRemoveEdgeLinkingCommunity(container.getName(),
					otherCommunity.container.getName());
		}
		return tmpGraph;
	}
}
