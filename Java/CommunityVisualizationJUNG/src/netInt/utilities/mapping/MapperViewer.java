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
package netInt.utilities.mapping;

import java.util.ArrayList;

import jViridis.ColorMap;
import netInt.utilities.SortedEdgeList;
import netInt.utilities.SortedNodeList;
import processing.core.*;

public class MapperViewer extends PApplet {

	private static MapperViewer MVInstance = null;
	private PFont font;
	private ArrayList<Bar> nodeBars, edgeBars;
	private String currentColorMap;

	// Singleton pattern
	public static MapperViewer getInstance() throws NullPointerException {
		if (MVInstance == null) {
			return null;
		}
		return MVInstance;
	}

	/**
	 * This constructor is used to create a window for user visualization of
	 * mapping attributes
	 * 
	 * @param _parent
	 *            parent PApplet
	 */
	public MapperViewer() {
		super();
		nodeBars = new ArrayList<Bar>();
		edgeBars = new ArrayList<Bar>();
		currentColorMap = ColorMap.getInstance().getColorMapName();
		MVInstance = this;
	}

	/**
	 * Launch MapperViewer window
	 */
	public void kickOffPApplet() {
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	public void settings() {
		size(300, displayHeight / 2, P2D);
	}

	public void setup() {
		this.surface.setLocation(displayWidth - 400, 45);
		this.surface.setAlwaysOnTop(true);
		this.surface.setResizable(true);

		// Font
		font = createFont("Arial", 9, false);
		textFont(font);
	}

	public void draw() {

		background(70);

		ColorMap.getInstance().showPalette(this, 20, 15, 256);

		if (!ColorMap.getInstance().getColorMapName().equals(currentColorMap)) {
			currentColorMap = ColorMap.getInstance().getColorMapName();
			resetBars();
		}

		text("PERCENTILES BELOW WHICH VALUES FALL", 20, 35, 250, 150);

		int jump = 23;

		// Node Bars
		if (nodeBars != null) {

			int firstNodeBar = 65;

			fill(165, 199, 236, 120);

			textSize(10);

			text("Node Attributes", 20, firstNodeBar);

			stroke(165, 199, 236, 120);

			line(20, firstNodeBar + 3, width - 20, firstNodeBar + 3);

			fill(200, 200, 200);

			firstNodeBar += jump;

			for (int i = 0; i < nodeBars.size(); i++) {

				nodeBars.get(i).show(this, 20, firstNodeBar + (i * jump));
			}
		}

		// Edge Bars
		if (edgeBars != null) {

			int firstEdgeBar = ((nodeBars.size() + 1) * jump) + 80;

			fill(165, 199, 236, 120);

			textSize(10);

			text("Edge attributes", 20, firstEdgeBar);

			stroke(165, 199, 236, 120);

			line(20, firstEdgeBar + 3, width - 20, firstEdgeBar + 3);

			fill(200, 200, 200);

			firstEdgeBar += jump;

			for (int i = 0; i < edgeBars.size(); i++) {

				edgeBars.get(i).show(this, 20, firstEdgeBar + (i * jump));
			}
		}
	}

	/**
	 * Launches PApplet and initializes bars
	 */
	public void init() {
		kickOffPApplet();
		initMinMaxValues();
	}

	public void initMinMaxValues() {

		// NODES
		try {
			// If the Mapper has node numerical attributes
			if (Mapper.getInstance().getNodeNumericalAttributeKeys() != null) {

				// Get all numerical attribute names
				ArrayList<String> attributeKeys = Mapper.getInstance().getNodeNumericalAttributeKeys();

				// For each numerical attribute name
				for (int i = 0; i < attributeKeys.size(); i++) {

					// Make a Bar
					Bar temp = new Bar(attributeKeys.get(i), Mapper.NODE);

					try {

						// Set min and max values
						temp.min = Mapper.getInstance().getNodeAttributesMin()
								.getValueofAttribute(attributeKeys.get(i));

						temp.max = Mapper.getInstance().getNodeAttributesMax()
								.getValueofAttribute(attributeKeys.get(i));

						// Add bar to collection
						nodeBars.add(temp);

					} catch (NullPointerException np) {
						temp.min = 0;
						temp.max = 0;
						nodeBars.add(temp);
						System.out.println(
								this.getClass().getName() + ".  Min-max set to 0 for bar " + attributeKeys.get(i));
					}
				}
			}
		} catch (NullPointerException np) {
			System.out.println(this.getClass().getName() + " WARNING. Mapper does not have Node Numerical Attributes");
		}
		// EDGES
		try {
			// If the Mapper has edge numerical attributes
			if (Mapper.getInstance().getEdgeNumericalAttributeKeys() != null) {

				// Get all numerical attribute names
				ArrayList<String> attributeKeys = Mapper.getInstance().getEdgeNumericalAttributeKeys();

				for (int i = 0; i < attributeKeys.size(); i++) {

					// Make a bar
					Bar temp = new Bar(attributeKeys.get(i), Mapper.EDGE);

					// Set min an max
					temp.min = Mapper.getInstance().getEdgeAttributesMin().getValueofAttribute(attributeKeys.get(i));

					temp.max = Mapper.getInstance().getEdgeAttributesMax().getValueofAttribute(attributeKeys.get(i));

					// Add Bars to collection
					edgeBars.add(temp);
				}
			}
		} catch (NullPointerException np) {
			System.out.println(this.getClass().getName() + " Mapper does not have Edge Numerical Attributes");
		}

		System.out
				.println("MapperViewer initialized. Node atts: " + nodeBars.size() + ", edge atts: " + edgeBars.size());
	}

	public void resetBars() {
		nodeBars = new ArrayList<Bar>();
		edgeBars = new ArrayList<Bar>();
		initMinMaxValues();
	}

	public void exit() {
		System.out.println("MapperViewer closed");
	}

	// INTERNAL CLASS
	public class Bar {
		public String attribute = "No Name";
		public int orgX;
		public int orgY;
		public int lenght;
		private float min;
		private float max;
		private int tab = 65;
		private int bins;
		private int fills[];

		// Constructor
		public Bar(String attributeName, String graphElementType) {
			attribute = attributeName;
			lenght = 150;
			min = 0;
			max = 0;
			bins = 10;
			fills = new int[bins];
			float[] percentiles = new float[0];

			// making of bin heights
			try {

				float[] minMax = null;

				switch (graphElementType) {

				case Mapper.NODE:

					minMax = Mapper.getInstance().getMinMaxForNodes(attributeName);

					percentiles = SortedNodeList.getPercentileValues(bins, attributeName);

					break;
				case Mapper.EDGE:
					minMax = Mapper.getInstance().getMinMaxForEdges(attributeName);

					percentiles = SortedEdgeList.getPercentileValues(bins, attributeName);

					break;
				default:

					System.out.println("Mapper Viewer *** Warning: wrong graph element type in bar constructor");
				}

				for (int i = 0; i < percentiles.length; i++) {

					fills[i] = ColorMap.getInstance().getMappedColorRGB(minMax[0], minMax[1], percentiles[i]);
				}

			} catch (Exception e) {

				for (int j = 0; j < bins; j++) {
					// White
					fills[j] = 16777215;
				}
			}
		}

		public void show(PApplet app, int orgX, int orgY) {

			// Bar name
			app.textSize(10);
			app.fill(155);
			app.text(attribute, orgX, orgY);

			// Bar line
			app.stroke(155, 50);
			app.line(orgX + tab + 5, orgY + 10, orgX + tab + lenght - 5, orgY + 10);
			app.textSize(8);

			// Bins
			float wdth = (lenght - 5) / bins;
			for (int i = 0; i < bins; i++) {
				// app.noStroke();
				app.fill(fills[i]);
				app.rect(orgX + tab + 5 + wdth * i, orgY + 10, wdth, -5); // sizes[i]
				app.fill(155);
			}

			// Endpoint labels
			app.textAlign(PApplet.RIGHT);
			app.text(PApplet.nfs(min, 0, 1), orgX + tab, orgY + 10);
			app.textAlign(PApplet.LEFT);
			app.text(PApplet.nfs(max, 0, 1), orgX + tab + lenght, orgY + 10);
			app.textSize(11);
		}
	}
}
