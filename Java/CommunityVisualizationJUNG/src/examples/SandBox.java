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

import java.io.File;

import geomerative.RG;
import geomerative.RShape;
import netInt.utilities.geometry.CircleCircleTangent;
import processing.core.PApplet;
import processing.core.PVector;

public class SandBox extends PApplet {

	PVector c1, c2;
	PVector[] points;
	RShape parallelepiped, union;
	RShape rC1, rC2;

	public void settings() {

		size(displayWidth - 227, displayHeight - 300, P2D);
	}

	public void setup() {
		RG.init(this);
		c1 = new PVector(500, 300, 200);
		c2 = new PVector(700, 300, 500);
		rC1 = RShape.createCircle(500, 300, 200);
		rC2 = RShape.createCircle(700, 300, 500);
		union = rC1.union(rC2);
	}

	public void draw() {
		background(200);
		noStroke();
		fill(105, 30);
		stroke(0);
		RG.shape(union);
	}

	public void mouseMoved() {
		rC1.translate(mouseX - rC1.getX(), mouseY - rC1.getY());
		parallelepiped = CircleCircleTangent.getTangentRBox(rC1, rC2);

		if (parallelepiped != null) {
			union = parallelepiped.union(rC1);
			union = union.union(rC2);
		} else {
			union = rC1.union(rC2);
		}
	}

	public static void main(String[] args) {
		PApplet.main("examples.SandBox");
	}
}
