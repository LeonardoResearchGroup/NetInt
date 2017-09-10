/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.visualElements;

import processing.core.PVector;
import processing.event.KeyEvent;
import processing.core.PApplet;

import java.awt.Color;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import netInt.canvas.Canvas;
import netInt.containers.Container;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.filters.Filters;
import netInt.utilities.mapping.Mapper;
import netInt.visualElements.gui.UserSettings;
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
	private PVector lastPosition, deltaMouse;
	// This lock is used to control iterative behavior in
	// showCommunityContents()
	private boolean lock = false;

	// Search tool
	protected Node nodeFound;
	protected VCommunity nodeFoundInSuperCommunity;
	private String idSearch = null;
	protected boolean containsSearchedNode = false;

	private boolean vNodesCentered = true;

	public VCommunity(Node node, Container container) {
		super(node, (float) container.getDimension().width / 2, (float) container.getDimension().height / 2);
		this.container = container;
		lastPosition = pos;
		comCover = new VCommunityCover(this);
		node.setAttribute("Community size", container.size());
		Mapper.getInstance().setMaxMinNodeAttributes(node);
		// Move vNodes relative to the vCommnity center
		updateContainer(true);
	}

	public void init() {
		setDiameter(Mapper.getInstance().convert(Mapper.LINEAR, container.size(), 150, Mapper.NODE, "Community size"));
		if (this.getDiameter() < 20)
			setDiameter(20);
		comCover.setStrokeThickness(
				(int) Mapper.getInstance().convert(Mapper.LINEAR, container.size(), 10, Mapper.NODE, "Community size"));
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
				for (VCommunity vC : container.getVCommunities()) {
					if (vC.comCover.isDeployed()) {
						// build external edges
						vC.container.buildExternalEdges(container.getVCommunities(), (DirectedSparseMultigraph<Node, Edge>)this.container.getGraph());
					}
				}
			} else {
				container.initialize();
				System.out.println(this.getClass().getName() + " "+container.getName()+ " initialize");
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
		}

		// Move vCommunity to mouse position if right button is pressed
		if (isMouseOver && rightPressed) {
			if (deltaMouse == null) {
				deltaMouse = new PVector(Canvas.getCanvasMouse().x - pos.x, Canvas.getCanvasMouse().y - pos.y);
			}
			moveCommunityCenterTo(Canvas.getCanvasMouse().sub(deltaMouse));
		} else {
			deltaMouse = null;
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
	 * @param showEdges
	 */

	public void showCommunityContents(boolean showNodes, boolean showEdges) {
		// ** Display VEdges
		
		// Internal Edges
		if (UserSettings.getInstance().showInternalEdges()) {

			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !rightPressed && !Canvas.canvasBeingZoomed) {

				// If the container Layout iterates to distribute nodes
				if (container.isLayoutIterative()) {
					if (container.stepIterativeLayout(pos).done() || container.isDone()) {
						// Show internal edges
						for (VEdge vE : container.getVEdges()) {
							
							// If the edge has any attribute
							if (vE.getEdge().getAttributeSize() > 0) {
								vE.setVisibility(UserSettings.getInstance().getTransactionVolume());
							}
							if (container.currentLayout == Container.CIRCULAR) {
								vE.setLayoutAndCenter(container.currentLayout, this.pos);
							}
							vE.show();
						}
					}
				} else {
					// Show internal edges
					for (VEdge vE : container.getVEdges()) {
						
						// If the edge has any attribute
						if (vE.getEdge().getAttributeSize() > 0) {
							vE.setVisibility(UserSettings.getInstance().getTransactionVolume());
						}
						if (container.currentLayout == Container.CIRCULAR) {
							vE.setLayoutAndCenter(container.currentLayout, this.pos);
						}
						vE.show();
					}
				}
			}
		}
		
		
		// External edges
		if (UserSettings.getInstance().showExternalEdges()) {
			
			// VCommunity open and it is not being modified by the user
			if (showEdges && !Canvas.canvasBeingTransformed && !rightPressed && !Canvas.canvasBeingZoomed) {
				
				// Show external edges
				for (VEdge vEE : container.getVExtEdges()) {
					
					// If the edge has any attribute
					if (vEE.getEdge().getAttributeSize() > 0) {
						vEE.setVisibility(UserSettings.getInstance().getTransactionVolume());
					}
					if (container.currentLayout == Container.CIRCULAR) {
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
		if (UserSettings.getInstance().showNodes()) {
			
			// VCommunity open
			if (showNodes) {
				
				// VCommunities
				for (VCommunity vC : container.getVCommunities()) {
					vC.setVisibility(true);
					vC.show();
					if (vC.comCover.isUnlocked() && !vC.lock) {
//						container.setIncidentEdgesVisibility(vC.getNode(), false);
						vC.lock = true;
					}
					if (!vC.comCover.isUnlocked() && vC.lock) {
//						container.setIncidentEdgesVisibility(vC.getNode(), true);
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
					// System.out.println(vN.isDisplayed());
					if (!vNodesCentered) {
						vN.show(vN.isDisplayed());
						vN.setDisplayed(true);
					}
				}
				vNodesCentered = false;
			} else {
				
				// This block centers all the elements in the container
				for (VisualAtom vA : container.getVNodes()) {
				
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

	public VCommunityCover getComCover() {
		return comCover;
	}

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
