package netInt.visualElements;

import java.util.ArrayList;

import geomerative.RG;
import geomerative.RShape;
import netInt.canvas.Canvas;
import netInt.containers.Container;
import netInt.graphElements.Node;
import netInt.utilities.geometry.CircleCircleIntersection;
import netInt.utilities.geometry.CircleCircleTangent;
import netInt.visualElements.primitives.VisualAtom;
import processing.core.PApplet;
import processing.core.PVector;
import processing.event.MouseEvent;

public class Tray extends VisualAtom {

	private static final long serialVersionUID = 1L;

	// The resulting shape
	private RShape union;

	// All the circles including this at the first position
	private ArrayList<RShape> circles;

	// Constructor
	public Tray(VCommunity communityNode, Container container) {

		super(communityNode.getX(), communityNode.getY(), container.getDimension().width,
				container.getDimension().width);

		circles = new ArrayList<RShape>();

		// this is the master shape of this VCommunity
		circles.add(RShape.createCircle(communityNode.getX(), communityNode.getY(), container.getDimension().width));

		// initialize resulting union
		union = circles.get(0);

		// populate circles
		getOtherVCommunitiesCircles(container);

		// events
		eventRegister(Canvas.app);

	}

	/**
	 * intersects all the shapes to build the final RShape.
	 * 
	 * First, it creates all the parallelepipeds described by the tangential points
	 * between all the circles. Then merge them all in a single RShape. Then it
	 * unites all the circles to the merged RShape
	 */
	public void intersectShapes() {
		// fresh start
		ArrayList<RShape> paras = new ArrayList<RShape>();
		union = null;

		try {
			// create all the parallelepipeds
			for (int i = 0; i < circles.size() - 1; i++) {
				for (int j = i + 1; j < circles.size(); j++) {
					if (circles.get(i) != null && circles.get(j) != null) {
						paras.add(CircleCircleTangent.getTangentRBox(circles.get(i), circles.get(j)));
					}
				}
			}

			// get the first parallelepiped in the list
			for (int i = 0; i < paras.size(); i++) {
				if (paras.get(i) != null) {
					union = paras.get(i);
					break;
				}
			}

			// merge all parallelepipeds
			for (int i = 0; i < paras.size() - 1; i++) {
				for (int j = i + 1; j < paras.size(); j++) {
					if (paras.get(i) != null && paras.get(j) != null) {
						union = union.union(paras.get(j));
					}
				}
			}
			// add all the circles
			if (union == null) {
				union = circles.get(0);
				for (int i = 1; i < circles.size(); i++) {
					union = union.union(circles.get(i));
				}
			} else {
				for (int i = 0; i < circles.size(); i++) {
					union = union.union(circles.get(i));
				}
			}

		} catch (Exception e) {
			System.out.println(this.getClass().getName() + "" + e.getMessage());
		}

	}

	private void getOtherVCommunitiesCircles(Container container) {

		for (VCommunity vC : container.getVCommunities()) {

			circles.add(RShape.createCircle(vC.getPos().x, vC.getPos().y, container.getDimension().width));

		}
	}

	public void show() {
		RG.shape(union);
	}

	public void eventRegister(PApplet theApp) {
		theApp.registerMethod("mouseEvent", this);
	}

	public void mouseEvent(MouseEvent e) {

		if (e.getAction() == MouseEvent.DRAG) {
			// Relocate this circle
//			circles.get(0).translate(Canvas.getCanvasMouse().x - circles.get(0).getX() - circles.get(0).getWidth() / 2,
//					Canvas.getCanvasMouse().y - circles.get(0).getY() - circles.get(0).getHeight() / 2);
			
//			circles.get(0).translate(Canvas.app.mouseX - circles.get(0).getX() - circles.get(0).getWidth() / 2,
//					Canvas.app.mouseY - circles.get(0).getY() - circles.get(0).getHeight() / 2);

			
			//if (circles.get(0).contains(Canvas.getCanvasMouse().x, Canvas.getCanvasMouse().y)) {
				// Intersect shapes
				//intersectShapes();
			//}
		}
	}
}