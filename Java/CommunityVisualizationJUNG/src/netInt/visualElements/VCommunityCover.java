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

import java.io.Serializable;

import netInt.canvas.Canvas;
import netInt.containers.Container;
import netInt.gui.UserSettings;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.event.KeyEvent;
import processing.event.MouseEvent;

public class VCommunityCover implements Serializable {

	private static final long serialVersionUID = 1L;

	// The community was clicked to be opened
	private boolean unlocked;

	// The community was completely open
	private boolean coverDeployed;

	// Amount of degrees to open the community
	private int increment;

	// the user presses "c" to close the community
	private boolean enableClosing;

	// false if there are too many communities on canvas
	private boolean showLabel = true;

	// Counter
	private int i;

	// visibility settings
	private int strokeThickness;
	private float angle;
	private float angle2;
	private VCommunity communityNode;

	public VCommunityCover(VCommunity communityNode) {
		increment = 10;
		unlocked = false;
		i = 0;
		this.communityNode = communityNode;
		strokeThickness = 0;
		angle = PConstants.TWO_PI / 360;
		enableClosing = false;
		eventRegister(Canvas.app);
	}

	protected void show(Container container, boolean containsSearchedNode) {

		// If mouse over, darken its color
		if (communityNode.isMouseOver) {
			strokeThickness = 1;

			if (unlocked && enableClosing) {
				strokeThickness = 2;
			}

			if (!showLabel) {
				showCoverLable(communityNode, container);
			}
		} else {

			strokeThickness = 0;
		}

		Canvas.app.strokeWeight(strokeThickness);

		// If community not opened
		if (!unlocked) {

			// Colors and visual attributes
			Canvas.app.stroke(100);
			Canvas.app.fill(communityNode.getColorRGB());

			if (coverDeployed) {
				fold();
			}

		} else {
			Canvas.app.fill(100, 50);

			if (!coverDeployed) {
				unfold();
				drawArcs();
			}
		}

		Canvas.app.ellipse(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter(),
				communityNode.getDiameter());

		// Labels
		if (showLabel) {
			Canvas.app.textSize(10);
			showCoverLable(communityNode, container);
		}

		// highlights itself if it contains a searched node and it has
		// not been deployed
		if (containsSearchedNode) {
			Canvas.app.fill(255, 0, 0);
			Canvas.app.ellipse(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter(),
					communityNode.getDiameter());
		}

	}

	private void fold() {
		if (i > 0) {
			i -= increment;
		} else {
			coverDeployed = false;
		}
	}

	private void unfold() {
		if (i < 180) {
			i += increment;
		} else {
			coverDeployed = true;
		}
	}

	private void drawArcs() {

		// *** DRAWS RIGHT HALF INVOLUTE
		Canvas.app.stroke(communityNode.getColorRGB());
		Canvas.app.noFill();

		// Increments the angle of the involute
		angle2 = (angle * i) + PConstants.PI + PConstants.HALF_PI;

		// *** Arc right half
		Canvas.app.arc(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter(),
				communityNode.getDiameter(), angle2, PConstants.TWO_PI + PConstants.HALF_PI);

		// *** DRAWS LEFT HALF INVOLUTE

		// Decrements the angle of the involute
		angle2 = (-angle * i) + PConstants.PI + PConstants.HALF_PI;

		// *** Arc left half
		Canvas.app.arc(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter(),
				communityNode.getDiameter(), PConstants.HALF_PI, angle2);
	}

	/**
	 * Displays community information from graph stored in a Container
	 * 
	 * @param communityNode
	 *            communityNode
	 * @param container
	 *            container
	 */
	public void showCoverLable(VNode communityNode, Container container) {
		Canvas.app.textAlign(PConstants.CENTER, PConstants.CENTER);
		Canvas.app.fill(250, 200);
		Canvas.app.text(container.getName(), communityNode.getPos().x, communityNode.getPos().y + 10);
		if (communityNode.isMouseOver) {
			Canvas.app.text("Nodes: " + container.getGraph().getVertexCount(), communityNode.getPos().x,
					communityNode.getPos().y + 30);
			Canvas.app.text("Edges: " + container.getGraph().getEdgeCount(), communityNode.getPos().x,
					communityNode.getPos().y + 45);
		}
		Canvas.app.noFill();
		Canvas.app.stroke(180);
	}

	// **** Getters and Setters ****

	// Getters

	public boolean isDeployed() {
		return coverDeployed;
	}

	public boolean isUnlocked() {
		return unlocked;
	}

	public boolean isUnlockedAndDeployed() {
		return unlocked && coverDeployed;
	}

	// Setters

	public void setStrokeThickness(int strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

	public void setShowLabel(boolean showLabel) {
		this.showLabel = showLabel;
	}

	// ******** Events *********

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
		theApp.registerMethod("keyEvent", this);
	}

	public void mouseEvent(MouseEvent e) {

		if (e.getAction() == MouseEvent.CLICK) {

			// If clicked, not opened and mouse over
			if (!unlocked && communityNode.isMouseOver) {
				unlocked = true;

				// Used in adaptive performance. Switch off all edges
				Canvas.setAdaptiveDegreeThresholdPercentage(100);

			}
			// If clicked, opened and enabled to be closed
			if (unlocked && enableClosing) {
				unlocked = false;
			}
		} else if (e.getAction() == MouseEvent.RELEASE) {
			if (communityNode.isMouseOver) {

				// Used in adaptive performance. Switch off all edges
				Canvas.setAdaptiveDegreeThresholdPercentage(100);
			}
		}
	}

	public void keyEvent(KeyEvent k) {
		kPressed(k);
	}

	private void kPressed(KeyEvent k) {
		// Control closing communities

		if (k.getAction() == KeyEvent.PRESS) {
			if (k.getKey() == 'c' || k.getKey() == 'C') {
				if (communityNode.isMouseOver) {
					enableClosing = true;
				}
			}
		} else {
			if (k.getAction() == KeyEvent.RELEASE) {
				if (k.getKey() == 'c' || k.getKey() == 'C') {
					enableClosing = false;
				}
			}
		}
	}
}
