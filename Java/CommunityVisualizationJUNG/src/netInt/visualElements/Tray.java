package netInt.visualElements;

import java.awt.Color;
import java.util.ArrayList;

import geomerative.RG;
import geomerative.RShape;
import netInt.canvas.Canvas;
import netInt.containers.Container;
import netInt.utilities.geometry.CircleCircleTangent;

public class Tray {

	// The resulting shape
	private RShape union;

	// The RShape of this container
	private RShape master;

	private Container container;

	public Tray(RShape master, Container container) {

		this.master = master;

		this.container = container;
	}

	/**
	 * intersects all the shapes to build the final RShape.
	 * 
	 * First, it retrieves all the RShape circles. Then it creates all the
	 * parallelepipeds described by the tangential points between all the circles.
	 * Then merge them all in a single RShape. Then it unites all the circles to the
	 * merged RShape
	 */
	public void intersectShapes() {
		// fresh start
		// populate circles
		ArrayList<RShape> circles = getVCommunitiesCircles(container);

		ArrayList<RShape> paras = new ArrayList<RShape>();

		union = null;

		try {
			// create all the parallelepipeds
			for (int i = 0; i < circles.size() - 1; i++) {
				for (int j = i + 1; j < circles.size(); j++) {
					if (circles.get(i) != null && circles.get(j) != null) {
						RShape tmp = CircleCircleTangent.getTangentRBox(circles.get(i), circles.get(j));
						paras.add(tmp);
						// this is to set union with the first element in para
						// if (tmp != null && union == null) {
						// union = tmp;
						// }
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

	/**
	 * Goes over all the VCommunities inside the container and creates a RShape for
	 * each one
	 * 
	 * @param container
	 * @return
	 */
	private ArrayList<RShape> getVCommunitiesCircles(Container container) {

		ArrayList<RShape> tmp = new ArrayList<RShape>();

		tmp.add(master);

		for (VCommunity vC : container.getVCommunities()) {

			tmp.add(RShape.createCircle(vC.getX(), vC.getY(), container.getDimension().width));

		}

		return tmp;
	}

	public void show() {
		Canvas.app.stroke(Color.YELLOW.getRGB());
		RG.shape(union);
	}

	public void update() {
		intersectShapes();
	}

}