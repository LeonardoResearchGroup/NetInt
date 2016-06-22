package utilities.containers;

import java.util.ArrayList;
import java.util.Iterator;

import processing.core.PApplet;
import processing.core.PVector;
import visualElements.VEdge;
import visualElements.VNode;
import visualElements.interactive.VisualAtom;

public class LinearContainer extends Container {

	public LinearContainer(PApplet app) {
		super(app);
	}

	/**
	 * Assigns coordinates to each VisualAtom on an horizontal axis
	 */
	public void linearLayout(PVector orig, PVector end) {
		int count = 0;
		float dist = orig.dist(end);
		float xStep = (float) dist / (visualElements.size());

		// Organize nodes on a line
		Iterator<VisualAtom> itr = visualElements.iterator();
		while (itr.hasNext()) {
			VNode tmp = (VNode) itr.next();
			tmp.setX(orig.x + xStep + (xStep * count));
			tmp.setY(orig.y);
			count++;
		}
	}

	@Override
	public void updateContainer() {
		// public void updateEdges( ArrayList <VEdge> VEdges){
		// // Draw bezier curves
		// Iterator<VEdge> itrVEdge = VEdges.iterator();
		// while (itrVEdge.hasNext()) {
		// VEdge edge = itrVEdge.next();
		// edge.layout(visualElements, graph.getVertices());
		// edge.makeBezier();
		// }
		// }

	}

	public void show() {
		for (VisualAtom vA : visualElements) {
			VNode n = (VNode) vA;
			n.setDiam(n.getVertex().getOutDegree() + 5);
			n.show(true, true);
		}
	}

	public void show(ArrayList<VEdge> VEdges, boolean showNodes,
			boolean showEdges, boolean networkVisible) {
		if (showEdges || networkVisible) {
			for (VEdge e : VEdges) {
				e.show(app);
			}
		}
		if (showNodes || networkVisible) {
			for (VisualAtom vA : visualElements) {
				VNode n = (VNode) vA;
				n.setDiam(n.getVertex().getOutDegree() + 5);
				n.show(showNodes, networkVisible);
			}
		}
	}
}
