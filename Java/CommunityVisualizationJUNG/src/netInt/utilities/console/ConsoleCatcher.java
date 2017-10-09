/*******************************************************************************
 * This library is free software. You can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. This library is distributed  WITHOUT ANY WARRANTY;
 * without even the implied warranty of  MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE. See the file COPYING included with this distribution 
 * for more information.
 * 	
 * Copyright (c) 2017 Universidad Icesi. All rights reserved. www.icesi.edu.co
 *******************************************************************************/
package netInt.utilities.console;

import java.io.ByteArrayOutputStream;

import controlP5.ControlP5;
import controlP5.Textarea;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Adapted from:
 * http://stackoverflow.com/questions/8708342/redirect-console-output-to-string-
 * in-java
 * 
 * @author juan salamanca
 * Dec 25, 2016
 */
public class ConsoleCatcher extends PApplet {
	private PFont font;
	private ByteArrayOutputStream baos;
	private Textarea textArea;
	private ControlP5 cp5;
	private String output;

	public ConsoleCatcher(ByteArrayOutputStream baos) {
		super();
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
		this.baos = baos;
	}

	public void settings() {
		size(500, 180);
	}

	public void setup() {
		cp5 = new ControlP5(this);
		cp5.enableShortcuts();
		textArea = cp5.addTextarea("console").setPosition(10, 10).setSize(width - 20, height - 20).setScrollActive(1);
		textArea.hideScrollbar();
		int color = color(186, 216, 231);
		textArea.setColor(color);
		this.surface.setSize(width, height);
		this.surface.setLocation(displayWidth - width, displayHeight - 300);
		this.surface.setAlwaysOnTop(true);
		// Font
		font = createFont("Arial", 11, false);
		textFont(font);
		output = "Console Catcher started";
	}

	public void exit() {
		System.out.println("Console catcher closed");
	}

	public void draw() {
		background(70);
		if (!output.equals(baos.toString())) {
			output = baos.toString();
			textArea.setText(output);
			textArea.scroll(1);
		}
	}
}
