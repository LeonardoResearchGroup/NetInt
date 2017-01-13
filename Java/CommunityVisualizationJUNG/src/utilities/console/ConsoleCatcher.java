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
	private PrintStream previous;
	private boolean capturing;
	private String capturedValue = "..\n";
	private Textarea textArea;
	private ControlP5 cp5;

	public ConsoleCatcher() {
		super();
		PApplet.runSketch(new String[] { this.getClass().getName() }, this);
	}
	
	public void settings(){
		size(500, 80);
	}

	public void setup() {
		cp5 = new ControlP5(this);
		cp5.enableShortcuts();
		textArea = cp5.addTextarea("console").setPosition(10, 10).setSize(width - 20, height - 20).setScrollActive(1);
		this.surface.setSize(width, height);
		this.surface.setLocation(750, 500);
		this.surface.setAlwaysOnTop(true);
		// Font
		font = createFont("Arial", 11, false);
		textFont(font);
	}

	public void draw() {
		background(100);
	}

	public void startCapture() {
		if (capturing) {
			return;
		}

		capturing = true;
		previous = System.out;
		baos = new ByteArrayOutputStream();

		OutputStream outputStreamCombiner = new OutputStreamCombiner(Arrays.asList(previous, baos));
		PrintStream custom = new PrintStream(outputStreamCombiner);

		System.setOut(custom);
	}

	public void stopCapture() {
		if (!capturing) {
			return;
		}

		System.setOut(previous);

		if (!baos.toString().isEmpty()) {
			capturedValue += baos.toString();
			textArea.setText(capturedValue);
		}

		baos = null;
		previous = null;
		capturing = false;
	}

	public String getCapturedValue() {
		return capturedValue;
	}

	private static class OutputStreamCombiner extends OutputStream {
		private List<OutputStream> outputStreams;

		public OutputStreamCombiner(List<OutputStream> outputStreams) {
			this.outputStreams = outputStreams;
		}

		public void write(int b) throws IOException {
			for (OutputStream os : outputStreams) {
				os.write(b);
			}
		}

		public void flush() throws IOException {
			for (OutputStream os : outputStreams) {
				os.flush();
			}
		}

		public void close() throws IOException {
			for (OutputStream os : outputStreams) {
				os.close();
			}
		}
	}
}
