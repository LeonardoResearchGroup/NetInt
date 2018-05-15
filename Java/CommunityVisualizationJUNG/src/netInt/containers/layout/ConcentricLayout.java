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
package netInt.containers.layout;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.graph.Graph;
import netInt.canvas.Canvas;
import netInt.comparators.DegreeComparator;
import netInt.graphElements.Edge;
import netInt.graphElements.Node;
import netInt.utilities.GraphLoader;
import netInt.utilities.mapping.Mapper;

public class ConcentricLayout<V, E> extends AbstractLayout<V, E> {

	private List<V> vertex_ordered_list;

	public ConcentricLayout(Graph<V, E> graph) {
		super(graph);
		vertex_ordered_list = new ArrayList<V>(graph.getVertices());
		initialize();
	}

	public ConcentricLayout(Graph<V, E> graph, Dimension size) {
		super(graph, size);
		vertex_ordered_list = (List<V>) graph.getVertices();
		initialize();
	}

	public void setSize(Dimension size) {
		this.size = size;
		initialize();
	}

	public void setVertexOrder(Comparator<V> comparator) {
		Collections.sort(vertex_ordered_list, comparator);
	}

	/*
	 * (non-Javadoc) Assign locations to each node
	 * 
	 * @see edu.uci.ics.jung.algorithms.layout.Layout#initialize()
	 */
	@Override
	public void initialize() {
		if (size != null) {

			double width = size.getWidth();
			double height = size.getHeight();
			System.out.println(this.getClass().getName()+ " "+ width);
			
			float radius;

			if (width - height > 0) {
				radius = (float) height;
			} else {
				radius = (float) width;
			}

			if (radius > 200) {
				radius = 200;
			}
			getLocations2(radius);
		}
	}

	@Override
	public void reset() {
		initialize();

	}

	/**
	 * Allocates elements in a circumference of a given radius. When the
	 * circumference is full, a new concentric circumference is created to
	 * allocate the remaining elements and so on. The radius is increased by the
	 * size of the largest element allocated in the previous circumference
	 * 
	 * @deprecated
	 * @param radius
	 *            the initial length
	 * @return a hashmap
	 */
	public HashMap<Double, ArrayList<Double>> getLocations1(double radius) {

		// The collection with the radius and a collection of angles for each
		// node at that radius
		HashMap<Double, ArrayList<Double>> angles = new HashMap<Double, ArrayList<Double>>();

		// The collection of Nodes belonging to each radius
		// HashMap<Integer, ArrayList<V>> subsets = new HashMap<Integer,
		// ArrayList<V>>();

		// Temporary collections of angles and V
		ArrayList<Double> tmpA = new ArrayList<Double>();
		ArrayList<V> tmpV = new ArrayList<V>();

		float accLenght = 0;
		double circumference = getCircumference(radius);

		float largest = 0;

		for (V v : vertex_ordered_list) {
			Node n = (Node) v;
			float nodeLenght = n.getDegree(0);

			accLenght += nodeLenght;

			if (nodeLenght > largest) {
				largest = nodeLenght;
				;
			}

			if (accLenght <= circumference) {

				tmpV.add(v);
				double angle = Math.PI * 2 * (accLenght / circumference);
				tmpA.add(angle);
				angles.put(radius, tmpA);
				locations.put(v, getPoint2D(radius, angle));

			} else {

				angles.put(radius, tmpA);

				accLenght = nodeLenght;
				radius = radius + (largest);
				circumference = getCircumference(radius);

				tmpV = new ArrayList<V>();
				tmpA = new ArrayList<Double>();

				if (nodeLenght > largest) {
					largest = nodeLenght;
				}

				tmpV.add(v);
				double angle = Math.PI * 2 * (accLenght / circumference);
				tmpA.add(angle);
				locations.put(v, getPoint2D(radius, angle));
			}
		}
		return angles;
	}

	/**
	 * Allocates elements in a circumference of a given radius. When the
	 * circumference is full, a new concentric circumference is created to
	 * allocate the remaining elements in a new tier and so on. The radius is
	 * increased by the size of the largest element allocated in the previous
	 * circumference
	 * 
	 * @param radius
	 *            the initial length
	 * @param maxRadius
	 *            the max size of the inner concentric circumference
	 * @return the collection of tiered nodes
	 */
	private ArrayList<ArrayList<V>> getLocations2(double maxRadius) {

		// The collection of Nodes belonging to each tier
		ArrayList<ArrayList<V>> subsets = new ArrayList<ArrayList<V>>();

		// Temporary collection of nodes
		ArrayList<V> tmpV = new ArrayList<V>();

		double accLength = 0;
		double maxCircumference = getCircumference(maxRadius);
		double largest = 0;
		double lastRadius = 0;

		for (V v : vertex_ordered_list) {
			//Node n = (Node) v;

			float nodeLength = 10; // Mapper.getInstance().convert(Mapper.LINEAR,
									// n.getFloatAttribute("degree"),
									// Mapper.NODE, "degree") * 5;

			accLength += nodeLength;

			// This is to get the largest node diameter
			if (nodeLength > largest) {
				largest = nodeLength;
			}

			if (accLength <= maxCircumference) {

				tmpV.add(v);

			} else {

				// Set the locations for nodes in the collection and get the
				// tier radius
				lastRadius = setLocations(tmpV, accLength, lastRadius);

				// Add the collected nodes satisfying the former condition
				subsets.add(tmpV);

				// reset acclength to the diameter of the new firstnode
				accLength = nodeLength;

				// Set the next tier radius
				lastRadius += largest;

				// Gets the next tier circumference
				maxCircumference = getCircumference(lastRadius);

				// Reset the collection of nodes
				tmpV = new ArrayList<V>();

				// This is to get the largest node diameter
				largest = nodeLength;

				// add the current node to the new tier's collection
				tmpV.add(v);
			}
		}

		// Set the locations for nodes in the collection
		setLocations(tmpV, accLength, lastRadius);

		// Adds the very last tier's collection to subsets
		subsets.add(tmpV);

		// Return collection
		return subsets;
	}

	private double setLocations(ArrayList<V> nodes, double totalLength, double lastRadius) {

		// Distribute all possible angles in all length units
		double angleFraction = (Math.PI * 2) / totalLength;

		// Calculate the tier's radius
		double radius = totalLength / (Math.PI * 2);

		// If the radius is too small
//		if (radius < 60) {
//			radius = 60;
//		}
		System.out.println(this.getClass().getName() + " radius= " + radius );

		// If the radius is smaller than the previous tier
		if (radius < lastRadius) {
			radius = lastRadius;
		}

		// Accumulated length
		double accLength = 0;

		for (V v : nodes) {

			//Node n = (Node) v;
			float nodeLength = 10; // Mapper.getInstance().convert(Mapper.LINEAR,
									// n.getFloatAttribute("degree"), "Node",
									// "degree") * 5;

			accLength += nodeLength;

			// get the angle
			double angle = (accLength) * angleFraction;

			// set the location for that vertex
			locations.put(v, getPoint2D(radius, angle));
		}
		return radius;
	}

	private Point2D getPoint2D(double radius, double angle) {
		double posX = Math.cos(angle) * radius;
		double posY = Math.sin(angle) * radius;
		return new Point2D.Double(posX, posY);
	}

	public double getCircumference(double radius) {
		double result = 2 * Math.PI * radius;
		return result;
	}

	public void printList(HashMap<Integer, ArrayList<V>> subSet) {
		System.out.println("\n + Subset");
		for (Integer i : subSet.keySet()) {
			System.out.println("\n Tier: " + i);
			for (V v : subSet.get(i)) {
				Node n = (Node) v;
				System.out.println(n.getDegree(0) + ": " + n.getName());
			}
		}
	}

	public void printListAngles(HashMap<Float, ArrayList<Double>> subSet) {
		System.out.println("\n + Angles for Subset");
		for (Float i : subSet.keySet()) {
			System.out.println("\n Tier: " + i);
			for (Double v : subSet.get(i)) {
				System.out.println(v);
			}
		}
	}

	public void printList(ArrayList<ArrayList<V>> list) {
		int c = 0;
		for (ArrayList<V> a : list) {
			System.out.println("Tier: " + c);
			for (V v : a) {
				Node n = (Node) v;
				System.out.println("  " + n.getId());
			}
			c++;
		}
	}

	public static class GraphLoaderInternal {

		GraphLoader loader;
		String sourcePath;

		public GraphLoaderInternal(String sourceFile) {
			switch (sourceFile) {
			case "risk":
				risk();
				break;
			case "infomap":
				infomap();
				break;
			case "louvain":
				louvain();
				break;
			}
		}

		/// ++ Loading a graph

		public void risk() {
			sourcePath = "/Users/jsalam/A Borrar/Risk.graphml";
			String[] nestedAttributes = { "Continent" };
			String[] nodeAttributes = { "label" };
			String[] edgeAttributes = new String[0];
			loader = new GraphLoader(sourcePath, nestedAttributes, nodeAttributes, edgeAttributes, 0);
		}

		public void infomap() {
			sourcePath = "/Users/jsalam/A Borrar/infomapeafit.graphml";
			String[] nestedAttributes = { "community" };
			String[] nodeAttributes = { "name" };
			String[] edgeAttributes = new String[0];
			loader = new GraphLoader(sourcePath, nestedAttributes, nodeAttributes, edgeAttributes, 0);
		}

		public void louvain() {
			sourcePath = "/Users/jsalam/A Borrar/comunidadesFinal.graphml";
			String[] nestedAttributes = { "community" };
			String[] nodeAttributes = { "name" };
			String[] edgeAttributes = new String[0];
			loader = new GraphLoader(sourcePath, nestedAttributes, nodeAttributes, edgeAttributes, 0);
		}
	}

	public static void main(String[] args) {
		new GraphLoaderInternal("risk");

		ConcentricLayout<Node, Edge> cLayout = new ConcentricLayout<Node, Edge>(GraphLoader.theGraph);
		// cLayout.setVertexOrder(new DegreeComparator(0));
		cLayout.printList(cLayout.getLocations2(10));

	}
}
