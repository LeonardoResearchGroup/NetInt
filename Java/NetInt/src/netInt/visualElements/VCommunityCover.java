package netInt.visualElements;

import java.io.Serializable;

import netInt.containers.Container;
import processing.core.PConstants;

public class VCommunityCover implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// The community was clicked to be opened
	private boolean unlocked;
	// The community was completely open
	private boolean coverDeployed;
	// Amount of degrees to open the community
	private int increment;
	// the user presses "c" to close the community
	private boolean enableClosing;
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
		strokeThickness = 2;
		angle = PConstants.TWO_PI / 360;
		enableClosing = false;
	}

	protected void show(Container container, boolean containsSearchedNode) {
		// If community not opened
		if (!unlocked) {
			// listen to the mouse and open the community
			unlocked = communityNode.leftClicked;
			// else, ask if the "C" key is pressed
		} else if (enableClosing) {
			// listen to the mouse and close the community
			unlocked = communityNode.leftClicked;
		}
		// Colors and visual attributes
		Canvas.app.stroke(100);
		Canvas.app.strokeWeight(0);
		Canvas.app.fill(communityNode.getColorRGB());

		// If mouse over darken its color
		if (communityNode.isMouseOver) {
			Canvas.app.fill(communityNode.darker());
		}

		// highlights itself if it contains a searched node and it has not been
		// deployed
		if (containsSearchedNode && !coverDeployed) {
			Canvas.app.fill(255, 0, 0, 100);
			Canvas.app.arc(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter() - 10,
					communityNode.getDiameter() - 10, -PConstants.PI, PConstants.PI);
		}
		// These lines open two arcs around the community center generating the
		// opening effect
		if (unlocked) {
			if (i < 180) {
				i += increment;
			}else{
				coverDeployed = true;
			}
			Canvas.app.stroke(255, 20);
			Canvas.app.strokeWeight(strokeThickness);
			Canvas.app.fill(255, 10);
			Canvas.app.arc(communityNode.getPos().x, communityNode.getPos().y, communityNode.getDiameter(),
					communityNode.getDiameter(), -PConstants.PI, PConstants.PI);

		} else {
			if (i > 0) {
				i -= increment;
			}else{
				coverDeployed = false;
			}
		}
		// *** DRAWS RIGHT HALF INVOLUTE
		Canvas.app.stroke(communityNode.getColorRGB());
		Canvas.app.strokeWeight(strokeThickness);
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
		// Labels
		showCoverLable(communityNode, container);
	}

	/**
	 * Displays community information from graph stored in a Container
	 * @param canvas
	 * @param communityNode
	 * @param container
	 */
	public void showCoverLable(VNode communityNode, Container container) {
		Canvas.app.textAlign(PConstants.CENTER, PConstants.CENTER);
		Canvas.app.fill(250, 200);
		Canvas.app.text(container.getName(), communityNode.getPos().x, communityNode.getPos().y);
		Canvas.app.noFill();
		Canvas.app.stroke(180);
		// Canvas.app.rect(0, 0, container.dimension.width,
		// container.dimension.height);
		Canvas.app.text("Nodes: " + container.getGraph().getVertexCount(), communityNode.getPos().x, communityNode.getPos().y + 20);
		Canvas.app.text("Edges: " + container.getGraph().getEdgeCount(), communityNode.getPos().x, communityNode.getPos().y + 35);
	}

	/// **** Getters and Setters

	// Getters
	public boolean isEnableClosing() {
		return enableClosing;
	}

	public boolean isDeployed() {
		return coverDeployed;
	}
	
	public boolean isUnlocked() {
		return unlocked;
	}

	public boolean isUnlockedAndDeployed() {
		return unlocked && coverDeployed;
	}
	
	public int getStrokeThickness() {
		return strokeThickness;
	}
	
	// Setters 
	
//	public void setUnlockedA(boolean val) {
//		unlocked = val;
//	}



	public void setStrokeThickness(int strokeThickness) {
		this.strokeThickness = strokeThickness;
	}

	public void setEnableClosing(boolean enableClosing) {
		this.enableClosing = enableClosing;
	}


}
