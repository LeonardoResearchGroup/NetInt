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

	// Search tool
	protected Node nodeFound;
	protected VCommunity nodeFoundInSuperCommunity;
	private String idSearch = null;
	protected boolean containsSearchedNode = false;
	private boolean vNodesCentered = false;
	private boolean vCommunitiesCentered = false;

	private String colorAttributeName = "no_attribute";
	private String sizeAttributeName = "no_attribute";

	// converter used to map node visual attributes. It gets its value from the
	// UserSettings
	private String sizeConverterName = Mapper.LINEAR;
	private String colorConverterName = Mapper.LINEAR;

	private float sizeFactor = 1;
	private float minSize = 5;

	private int tierSequence;

	private VNodeDescription description;

	public VCommunity(Node node, Container container) {

		super(node, (float) container.getDimension().width / 2, (float) container.getDimension().height / 2);

		this.container = container;

		lastPosition = pos;

		comCover = new VCommunityCover(this);

		if (container.size() != 0) {

			getNode().setAbsoluteAttribute("Community size", container.size());

		} else {

			getNode().setAbsoluteAttribute("Community size", 1);
		}

		// Move vNodes relative to the vCommnity center
		updateContainer();

		description = new VNodeDescription();

		setSize(minSize);

		sizeFactor = 30;
	}

	/**
	 * Sets the diameter relative to the size of other communities recorded in
	 * the Mapper set of minMax size values
	 */
	public void init() {

		sizeAttributeName = "Community size";

		float temp = Mapper.getInstance().convert(sizeConverterName, container.size(), Mapper.NODE, sizeAttributeName);

		setSize(temp);
	}

	private void setSize(float val) {

		float newSize = val * sizeFactor;

		if (newSize < minSize) {

			setDiameter(minSize);

		} else {

			setDiameter(newSize);
		}
	}

	private void convertSize() {

		boolean newConverter = false;
		boolean newAttribute = false;

		// user selected converter name
		String userSelectedConverter = UserSettings.getInstance().getCommunityConverter();

		// logical gate to prevent unnecessary looping
		if (userSelectedConverter != null && !sizeConverterName.equals(userSelectedConverter)) {
			newConverter = true;
			sizeConverterName = userSelectedConverter;
		}

		// user selected attribute name
		String userSelectedAttSize = UserSettings.getInstance().getCommunitySizeAtt();

		// logical gate to prevent unnecessary looping
		if (userSelectedAttSize != null && !sizeAttributeName.equals(userSelectedAttSize)) {
			newAttribute = true;
			sizeAttributeName = userSelectedAttSize;
		}

		if (newAttribute || newConverter) {
			
			//System.out.println(this.getClass().getName() + " " + getNode().getId() + " contains userAttribute: " + sizeAttributeName + " " + getNode().getAttributes().keySet().contains(sizeAttributeName)  );

			// If this node has a value for that attribute name
			if (getNode().getAttributes().keySet().contains(sizeAttributeName)) {

				try {
					// get the value for that attribute key
					float value = getNode().getFloatAttribute(sizeAttributeName);

					// Map input with latest converter
					float tmp = Mapper.getInstance().convert(sizeConverterName, value, Mapper.NODE, sizeAttributeName);

					setSize(tmp);

				} catch (NullPointerException np) {
					np.printStackTrace();
				}
			}
		}

	}

	private int calculateColor() {

		boolean newConverter = false;
		boolean newAttribute = false;

		int rtn = getColor().getRGB();

		// user selected converter name
		String userSelectedConverter = UserSettings.getInstance().getCommunityConverter();

		// logical gate to prevent unnecessary looping
		if (userSelectedConverter != null && !colorConverterName.equals(userSelectedConverter)) {
			newConverter = true;
			colorConverterName = userSelectedConverter;
		}

		// user selected attribute name
		String userSelectedAttColor = UserSettings.getInstance().getCommunityColorAtt();

		// logical gate to prevent unnecessary looping
		if (userSelectedAttColor != null && !colorAttributeName.equals(userSelectedAttColor)) {
			newAttribute = true;
			colorAttributeName = userSelectedAttColor;
		}

		if (newConverter || newAttribute) {

			//System.out.println(this.getClass().getName() + " ColorConvereted in community " + getNode().getId());

			// If this node has a value for that attribute name
			if (getNode().getAttributes().keySet().contains(colorAttributeName)) {

				try {
					// get the value for that attribute key
					float value = getNode().getFloatAttribute(colorAttributeName);

					// Map input with latest converter
					float tmp = Mapper.getInstance().convert(colorConverterName, value, Mapper.NODE,
							colorAttributeName);

					rtn = ColorMap.getInstance().getColorRGB(tmp);

				} catch (NullPointerException np) {

					System.out.println(np.getClass().getName() + " in VCommunity: " + getNode().getName()
							+ " for color att: " + colorAttributeName + " and converter: " + colorConverterName);
				}
			}
		}

		return rtn;
	}

	public void showCommunity() {

		// Hide or show the vCommunity according to the user defined degree
		// threshold
		if (filterVisibility()) {

			convertSize();
			setColor(calculateColor(), getAlpha());

			// Look for nodes based on id entered by user in control panel
			searchNode();

			// Display the community cover
			comCover.show(container, containsSearchedNode);

			// Check if community cover is completely deployed
			if (comCover.isDeployed()) {

				// ******

				// If adaptive performance threshold was updated
				if (Canvas.isAdapting()) {

					changeContainerDegreeThreshold();
				}

				// *******

				//comCover.showTray();
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

				// Show community overall description on mouse over
				if (isMouseOver) {
					description.show(this);
				}
			}

			/*
			 * Update position of each visualElement in the container relative
			 * to current vCommunity center. This is needed to reposition
			 * deployed and collapsed VCommunities when the user interacts with
			 * this VAtom
			 */
			if (Canvas.mouseEventOnCanvas && MouseHook.getInstance().isHooked(this)) {
				updateContainer();
			}
		}
	}

	/**
	 * Update the container degree threshold as a function of
	 * AdaptiveDegreeThresholdPercentage
	 */
	private void changeContainerDegreeThreshold() {

		// The position of this container's array of degrees
		// corresponding to a given percentage of relevant nodes
		int degreeThresholdPosition = (int) ((Canvas.getAdaptiveDegreeThresholdPercentage() / 100)
				* container.getNodes().size()) - 1;

		if (degreeThresholdPosition < 0) {

			container.degreeThreshold = 0;

		} else {

			container.degreeThreshold = container.degrees[degreeThresholdPosition];
		}
	}

	/**
	 * A community contains VisualAtoms that might be VNodes or VCommunities.
	 * Therefore a higher tier VCommunity may contain nested VCommunities. In
	 * order to display each VisualAtom correctly the class type is determined.
	 * 
	 * @param showVAtoms
	 *            true if show
	 * @param showEdges
	 *            true if show
	 */
	public void showCommunityContents(boolean showVAtoms, boolean showEdges) {

		// ** Display VEdges

		// if this VCommunity is above community visibility threshold
		if (showEdges && comCover.isUnlocked() && visible) {
			// Internal Edges
			// If the user chooses to turn on/off the internal edges
			if (UserSettings.getInstance().internalEdgeVisibilityForTier(tierSequence)) {

				showInternalEdges();
			}

			// External edges
			// If the user chooses to turn on/off the external edges
			if (UserSettings.getInstance().externalEdgeVisibilityForTier(tierSequence)) {

				showExternalEdges();
			}
		}

		// ** Display VNodes

		// If the user chooses to turn on/off the nodes
		if (UserSettings.getInstance().showNodes()) {

			// VCommunity open show:
			// internal VCommunities if any
			showVCommunities(showVAtoms);

			// VNodes
			showVNodes(showVAtoms);
		}
	}

	private void showInternalEdges() {
		// VCommunity open and it is not being modified by the user
		if (!Canvas.canvasBeingTransformed && !MouseHook.getInstance().isHooked(this) && !Canvas.canvasBeingZoomed) {

			// If the container Layout iterates to distribute nodes
			if (container.isLayoutIterative()) {

				if (container.isDone()) {

					// Show internal edges
					for (VEdge vE : container.getVEdges()) {

						// If the edge has any attribute
						if (vE.getEdge().getAttributeSize() > 0) {

							if (UserSettings.getInstance().getEventOnVSettings())

								vE.setVisibility(UserSettings.getInstance().getWeight());
						}

						if (container.currentLayout == Container.CONCENTRIC) {

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

						vE.setVisibility(UserSettings.getInstance().getWeight());
					}

					if (container.currentLayout == Container.CONCENTRIC) {

						vE.setLayoutAndCenter(container.currentLayout, this.pos);
					}

					// ********
					// Edge is visible if both of its nodes are above the
					// degree threshold
					// if (vE.getEdge().getSource().getDegree(0) >
					// container.degreeThreshold
					// && vE.getEdge().getTarget().getDegree(0) >
					// container.degreeThreshold) {
					//
					// vE.setSourceTargetVisibility(true);
					//
					// } else {
					//
					// vE.setSourceTargetVisibility(false);
					// }

					// *******

					vE.show();
				}

			}
		}
	}

	private void showExternalEdges() {
		// VCommunity open and it is not being modified by the user
		if (!Canvas.canvasBeingTransformed && !MouseHook.getInstance().isHooked(this) && !Canvas.canvasBeingZoomed) {

			// Show external edges
			for (VEdge vEE : container.getVExtEdges()) {

				// If the edge has any attribute
				if (vEE.getEdge().getAttributeSize() > 0) {
					if (UserSettings.getInstance().getEventOnVSettings())
						vEE.setVisibility(UserSettings.getInstance().getWeight());
				}

				if (container.currentLayout == Container.CONCENTRIC) {
					vEE.setLayoutAndCenter(container.currentLayout, this.pos);
				}

				// Set pink color
				// Color color = new Color(255, 100, 180);
				// vEE.setColor(color.getRGB());
				vEE.show();
			}
		}
	}

	private void showVCommunities(boolean communityOpen) {

		if (communityOpen) {

			// VCommunities
			for (VCommunity vC : container.getVCommunities()) {

				if (vC.getNode().getDegree(0) >= container.degreeThreshold) {

					vC.setAboveDegreeThreshold(true);

				} else {

					vC.setAboveDegreeThreshold(false);
				}

				// vC.setVisibility(true);

				// set subCommunities coordinates relative to vCommunity
				// position
				if (!vCommunitiesCentered) {
					container.translateVElementCoordinates(vC, this.getPos());
				}

				vC.showCommunity();

				/*
				 * 
				 * if (vC.comCover.isUnlocked() && !vC.lock) {
				 * container.setIncidentEdgesVisibility(vC.getNode(), false);
				 * vC.lock = true; }
				 * 
				 * if (!vC.comCover.isUnlocked() && vC.lock) {
				 * container.setIncidentEdgesVisibility(vC.getNode(), true);
				 * vC.lock = false; }
				 * 
				 */
			}

		}

		// This gate prevents vCommunity relocation in every draw() loop
		vCommunitiesCentered = true;
	}

	private void showVNodes(boolean communityOpen) {

		if (communityOpen) {

			if (container.isLayoutIterative()) {

				container.stepIterativeLayout(pos).done();
			}

			// If the container does not contain vCommunities
			if (container.getVCommunities().size() == 0) {

				// Get all the nodes
				for (VNode vN : container.getVNodes()) {

					if (vN.getNode().getDegree(0) >= container.degreeThreshold) {

						vN.setAboveDegreeThreshold(true);

					} else {

						vN.setAboveDegreeThreshold(false);
					}

					//
					// vN.setVisibility(true);

					// Center vNodes relative to a given position
					// if (Canvas.mouseEventOnCanvas &&
					// MouseHook.getInstance().isHooked(this)) {
					if (!vNodesCentered) {

						// set vNodes coordinates relative to vCommunity
						// position
						container.translateVElementCoordinates(vN, this.getPos());

					}

					// If vN is visible and not centered
					if (vNodesCentered) {
						// vN.show(vN.isDisplayed());
						vN.show();
						vN.setDisplayed(true);
					}
				}
				vNodesCentered = true;
			}
		} else {

			// This resets visibility of all VNodes in the container
			for (VNode vN : container.getVNodes()) {
				vN.setDisplayed(false);
			}
		}

	}

	/**
	 * Update position of each visualElement in the container relative to
	 * current vCommunity center.
	 */
	public void updateContainer() {

		if (!lastPosition.equals(pos)) {

			// Get the difference of centers
			PVector diffPos = lastPosition.sub(pos);

			// set new vNodes coordinates
			container.updateVNodesCoordinates(diffPos);
			lastPosition = pos;
		}
	}

	/**
	 * Filters visibility for all vCommunities. Special case for tier 0
	 * communities. Albeit it sets its visibility to false they are still
	 * visible otherwise their contents would not be visible and the graphPad
	 * would be blank
	 * 
	 * @return
	 */
	private boolean filterVisibility() {

		float visibilityThreshold = UserSettings.getInstance().getCommunitySizeThreshold();

		if (visibilityThreshold > container.size()) {

			visible = false;

			if (containsSearchedNode) {
				visible = true;
			}

		} else {

			visible = true;

		}

		/*
		 * Special case for tier 0 communities. Albeit it sets its visibility to
		 * false they are still visible otherwise their contents would not be
		 * visible and the graphPad would be blank
		 */

		if (tierSequence > 0) {

			return visible;

		} else {

			return true;
		}

	}

	// ***** Getters & Setters

	public void setContainer(Container nodesAndEdges) {
		container = nodesAndEdges;
	}

	public void setTierSequence(int sequence) {
		tierSequence = sequence;
	}

	public int getTierSequence() {
		return tierSequence;
	}

	// ***** Events
	public VCommunityCover getComCover() {
		return comCover;
	}

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

	// ***** Search Methods *******
	/**
	 * This method must be invoked ONLY by Higher Order Communities that contain
	 * Lower Order VCommunities
	 */
	public void searchNode() {
		// If the search tool is not null
		if (UserSettings.getInstance().getIdSearch() != null) {
			// If the search tool is looking for something new
			if (idSearch != UserSettings.getInstance().getIdSearch()) {
				idSearch = UserSettings.getInstance().getIdSearch();
				boolean rtn = searchNodeSuperCommunity(idSearch);
				if (rtn)
					System.out.println("VCommunity>: " + idSearch + " found in " + getNode().getId());
			}
		} else {
			// if the search tool is cleared
			if (idSearch != null) {
				resetNodeFoundSuperCommunity();
				idSearch = null;
			}
		}
	}

	/**
	 * 
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
