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
import netInt.utilities.SortedNodeList;
import processing.core.*;

public class MapperViewer extends PApplet {

	private static MapperViewer MVInstance = null;
	private PFont font;
	private ArrayList<Bar> bars;

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
		bars = new ArrayList<Bar>();
		MVInstance = this;
	}

	/**
	 * Launch MapperViewer window
	 */
	public void kickOffPApplet() {
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}

	public void settings() {
		size(300, displayHeight/2);
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

		if (bars != null) {
			int j = 40;
			text("PERCENTILES BELOW WHICH VALUES FALL", 20, j, 250, 150);

			// j = 35;

			for (int i = 0; i < bars.size(); i++) {
				bars.get(i).show(this, 20, 45 + (j + i * 35));
			}

		}

	}

	public void initMinMaxValues() {
		// Launch MapperViewer window
		kickOffPApplet();

		// NODES
		try {
			if (Mapper.getInstance().getNodeNumericalAttributeKeys() != null) {
				ArrayList<String> attributeKeys = Mapper.getInstance().getNodeNumericalAttributeKeys();
				for (int i = 0; i < attributeKeys.size(); i++) {
					// ************* THIS 'IF' CONDITIONAL IS NOT THE RIGHT WAY TO DO FILTER
					// WHICH ATTRIBUTES HAVE VALUE.
					// **************** MORE WORK NEED TO BE DONE HERE TO SOLVE
					// THIS ISSUE
					if (!attributeKeys.get(i).equals("Community size")) {
						Bar temp = new Bar(attributeKeys.get(i));
						temp.min = Mapper.getInstance().getNodeAttributesMin()
								.getValueofAttribute(attributeKeys.get(i));
						temp.max = Mapper.getInstance().getNodeAttributesMax()
								.getValueofAttribute(attributeKeys.get(i));
						bars.add(temp);
					}
				}
			}
		} catch (NullPointerException np) {
			System.out.println(this.getClass().getName() + " Mapper does not have Node Numerical Attributes");
		}

		// EDGES
		// try {
		// if (Mapper.getInstance().getEdgeNumericalAttributeKeys() != null) {
		//
		// ArrayList<String> attributeKeys =
		// Mapper.getInstance().getEdgeNumericalAttributeKeys();
		// for (int i = 0; i < attributeKeys.size(); i++) {
		// Bar temp = new Bar(attributeKeys.get(i));
		// temp.min =
		// Mapper.getInstance().getEdgeAttributesMin().getValueofAttribute(attributeKeys.get(i));
		// temp.max =
		// Mapper.getInstance().getEdgeAttributesMax().getValueofAttribute(attributeKeys.get(i));
		// bars.add(temp);
		// }
		// }
		// } catch (NullPointerException np) {
		// System.out.println(this.getClass().getName() + " Mapper does not have
		// Edge Numerical Attributes");
		// }

		System.out.println("MapperViewer initialized");
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
		private float sizes[];
		private int bins;
		private int fills[];

		// Contructor
		public Bar(String attributeName) {
			attribute = attributeName;
			lenght = 150;
			min = 0;
			max = 0;
			bins = 10;
			fills = new int[bins];

			// making of bin heights
			float[] percentiles = SortedNodeList.getPercentileValues(bins, attributeName);
			sizes = new float[percentiles.length];
			float[] minMax = Mapper.getInstance().getMinMaxForNodes(attributeName);
			for (int i = 0; i < percentiles.length; i++) {
				/*
				 * CHANGE CLASS TYPE TO EDGE FOR EDGE ATTRIBUTES!!!!!
				 */
				sizes[i] = Mapper.getInstance().convert(Mapper.LINEAR, percentiles[i], "Node", attributeName);
				sizes[i] *= 10f;
				// System.out.println(attributeName + " " + percentiles[i] +
				// " " + sizes[i]);
				// System.out.println("MIN: " + minMax[0] + " MAX:" +
				// minMax[1] + " VAL:" + percentiles[i]);

				fills[i] = ColorMap.getInstance().getMappedColorRGB(minMax[0], minMax[1], percentiles[i]);
			}
		}

		public void show(PApplet app, int orgX, int orgY) {

			// Bar name
			app.text(attribute, orgX, orgY);

			// Bar line
			app.stroke(155, 50);
			app.line(orgX + tab + 5, orgY + 15, orgX + tab + lenght - 5, orgY + 15);
			app.textSize(8);

			// Bins
			float wdth = (lenght - 5) / bins;
			for (int i = 0; i < bins; i++) {
				// app.noStroke();
				app.fill(fills[i]);
				app.rect(orgX + tab + 5 + wdth * i, orgY + 15, wdth, -5); // sizes[i]
				app.fill(155);
			}

			// Endpoint labels
			app.textAlign(PApplet.RIGHT);
			app.text(PApplet.nfs(min, 0, 1), orgX + tab, orgY + 15);
			app.textAlign(PApplet.LEFT);
			app.text(PApplet.nfs(max, 0, 1), orgX + tab + lenght, orgY + 15);
			app.textSize(11);
		}
	}
}
