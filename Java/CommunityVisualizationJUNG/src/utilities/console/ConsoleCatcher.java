package utilities.console;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import controlP5.ControlP5;
import controlP5.Textarea;

import processing.core.PApplet;
import processing.core.PFont;

/**
 * Adapted from:
 * http://stackoverflow.com/questions/8708342/redirect-console-output-to-string-
 * in-java
 * 
 * @date Dec 25, 2016
 * @author jsalam
 *
 */
public class ConsoleCatcher extends PApplet {
	private PFont font;
	private ByteArrayOutputStream baos;
	private Textarea textArea;
	private ControlP5 cp5;

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
		this.surface.setSize(width, height);
		this.surface.setLocation(displayWidth - width, displayHeight - 300);
		this.surface.setAlwaysOnTop(true);
		// Font
		font = createFont("Arial", 11, false);
		textFont(font);
	}

	public void exit() {
		  println("Console catcher closed");
		}
	
	public void draw() {
		background(100);
		textArea.setText(baos.toString());
		textArea.scroll(1);
	}
}
