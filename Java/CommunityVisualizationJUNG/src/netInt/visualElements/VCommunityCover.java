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

import java.awt.Color;
import java.io.Serializable;

import netInt.canvas.Canvas;
import netInt.containers.Container;
import netInt.gui.UserSettings;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
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
	private Container container;

	// Tray (internal class)
	public Tray tray;

	public VCommunityCover(VCommunity communityNode) {
		increment = 10;
		unlocked = false;
		i = 0;
		this.communityNode = communityNode;
		strokeThickness = 0;
		angle = PConstants.TWO_PI / 360;
		enableClosing = false;
		eventRegister(Canvas.app);
		this.container = communityNode.container;
		tray = new Tray(communityNode.getX(), communityNode.getY(), container.getDimension().width);
	}

	protected void show(boolean containsSearchedNode) {

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
			tray.updateCircle1(communityNode.getPos().x, communityNode.getPos().y, container.getDimension().width);

			tray.show(communityNode.getColor());

		}

		Canvas.app.ellipse(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter(),
				communityNode.getDiameter());

		// tray.updateCircle2(communityNode.getX(), communityNode.getY(),
		// container.getDimension().width);

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
		Canvas.app.fill(100);
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

	public class Tray {
		private PVector circle1;
		private PVector circle2;
		private int segments;
		private float[] anglesA;
		private float[] anglesB;

		public Tray(float circle1X, float circle1Y, float circle1R) {
			circle1 = new PVector(circle1X, circle1Y, circle1R);
			segments = 7;
			anglesA = new float[segments + 3];
			anglesB = new float[segments + 3];
			// makeTangents();
		}

		public Tray(float circle1X, float circle1Y, float circle1R, float circle2X, float circle2Y, float circle2R) {
			circle1 = new PVector(circle1X, circle1Y, circle1R);
			circle2 = new PVector(circle2X, circle2Y, circle2R);
			segments = 7;
			anglesA = new float[segments + 3];
			anglesB = new float[segments + 3];
			makeTangents();
		}

		private void makeTangents() {
			// estimate angles and complements
			float aG = -PApplet.atan((circle2.y - circle1.y) / (circle2.x - circle1.x));
			if (circle2.x < circle1.x) {
				aG = PApplet.PI + aG;
			}

			float aB = PApplet.asin((circle2.z - circle1.z)
					/ PApplet.sqrt(PApplet.pow((circle2.x - circle1.x), 2) + PApplet.pow((circle2.y - circle1.y), 2)));
			float aAA = aG - aB;
			float aAB = aG + aB;
			float complementC1 = PApplet.PI - 2 * aB;
			float complementC2 = PApplet.PI + 2 * aB;

			float segmentC1 = complementC1 / segments;

			float segmentC2 = complementC2 / segments;

			// angles for vertices of circle 1

			anglesA[0] = (PApplet.PI / 2 - aAA) - segmentC1;

			for (int i = 1; i < anglesA.length; i++) {

				anglesA[i] = (PApplet.PI / 2 - aAA) + (segmentC1 * (i - 1));
			}

			// angles for vertices of circle 2

			anglesB[0] = (PApplet.PI + PApplet.PI / 2 - aAB) - segmentC2;

			for (int i = 1; i < anglesB.length; i++) {

				anglesB[i] = (PApplet.PI + PApplet.PI / 2 - aAB) + (segmentC2 * (i - 1));
			}
		}

		public void update(float circle1X, float circle1Y, float circle1R, float circle2X, float circle2Y,
				float circle2R) {
			circle1 = new PVector(circle1X, circle1Y, circle1R);
			circle2 = new PVector(circle2X, circle2Y, circle2R);
			makeTangents();
		}

		public void updateCircle1(float circle1X, float circle1Y, float circle1R) {
			circle1 = new PVector(circle1X, circle1Y, circle1R);
			if (circle2 != null)
				makeTangents();
		}

		public void updateCircle2(float circle2X, float circle2Y, float circle2R) {
			circle2 = new PVector(circle2X, circle2Y, circle2R);
			makeTangents();
		}

		private void show(Color color) {

			Canvas.app.fill(color.getRGB(), 20);

			if (circle2 != null) {
				// display
				Canvas.app.stroke(30,70);
				// Canvas.app.fill(100, 30);
				Canvas.app.beginShape();
				// first circle
				float posX = 0;
				float posY = 0;
				for (int i = 0; i < anglesA.length - 1; i++) {
					posX = circle1.x + PApplet.cos(anglesA[i]) * circle1.z / 2;
					posY = circle1.y + PApplet.sin(anglesA[i]) * circle1.z / 2;
					Canvas.app.curveVertex(posX, posY);
					// ellipse(posX, posY, 5, 5);
				}

				// second circle
				for (int i = 0; i < anglesB.length; i++) {
					posX = circle2.x + PApplet.cos(anglesB[i]) * circle2.z / 2;
					posY = circle2.y + PApplet.sin(anglesB[i]) * circle2.z / 2;
					Canvas.app.curveVertex(posX, posY);
					// ellipse(posX, posY, 5, 5);
				}
				Canvas.app.endShape(PApplet.CLOSE);
				// Canvas.app.ellipse(circle2.x, circle2.y, circle2.z, circle2.z);
			} else {

				Canvas.app.ellipse(circle1.x, circle1.y, circle1.z, circle1.z);
			}

		}

	}

}
