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
package examples;

import java.util.ArrayList;

import geomerative.RG;
import geomerative.RShape;
import netInt.utilities.geometry.CircleCircleTangent;
import processing.core.PApplet;

public class SandBox extends PApplet {

	RShape union;
	ArrayList<RShape> paras;
	ArrayList<RShape> circles;

	public void settings() {
		size(displayWidth - 227, displayHeight - 300, P2D);
	}

	public void setup() {
		RG.init(this);
		circles = new ArrayList<RShape>();
		circles.add(RShape.createCircle(500, 300, 200));
		circles.add(RShape.createCircle(700, 300, 500));
		circles.add(RShape.createCircle(400, 300, 300));
		paras = new ArrayList<RShape>();
		union = circles.get(0).union(circles.get(1));
	}

	public void draw() {
		background(250);
		stroke(0);
		noFill();
		RG.shape(union);
		fill(0);
		text(frameRate, 10, 10);
	}

	public void mouseMoved() {
		circles.get(0).translate(mouseX - circles.get(0).getX() - circles.get(0).getWidth() / 2,
				mouseY - circles.get(0).getY() - circles.get(0).getHeight() / 2);
		intersectShapes();
	}

	private void intersectShapes() {
		// fresh start
		paras.clear();
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

			// merge all the parallelepipeds
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
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		PApplet.main("examples.SandBox");
	}
}
