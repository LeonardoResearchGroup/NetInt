package visualElements.gui;

import java.awt.Dimension;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import controlP5.ControlP5;
import executable.Executable;
import processing.core.PApplet;
import utilities.GraphLoader;

/**
 * 
 * Singleton pattern.
 * 
 * @author Felipe Rivera
 *
 */
public class ChooseHelper {

	private static final ChooseHelper INSTANCE = new ChooseHelper();

	// Private constructor.
	private ChooseHelper() {
	}

	public static ChooseHelper getInstance() {
		return INSTANCE;
	}

	/**
	 * Shows the Open File window of the OS
	 * 
	 * @param save
	 * @param extension
	 * @param applet
	 */
	public void showFileChooser(boolean save, String extension, PApplet applet) {
		if (!save) {
			applet.selectInput("Select a graphml file to process:", "selectImport");
		}
	}

	public String showJFileChooser(boolean save, String extension) {
		String path = null;
		javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
		chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
				extension, extension);
		chooser.setFileFilter(filter);

		int action;

		if (save) {
			action = chooser.showSaveDialog(null);
		} else {
			action = chooser.showOpenDialog(null);
		}

		if (action == javax.swing.JFileChooser.APPROVE_OPTION) {
			path = chooser.getSelectedFile().getAbsolutePath();
		}
		return path;
	}
}
