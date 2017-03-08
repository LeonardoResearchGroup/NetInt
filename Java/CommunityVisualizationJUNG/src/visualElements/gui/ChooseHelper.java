package visualElements.gui;

import processing.core.PApplet;

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
	 * 
	 * @param applet
	 *            The applet calling the input window
	 * 
	 */
	public void showFileChooser(PApplet applet) {
		applet.selectInput("Select a graphml file to process:", "selectImport");
	}

	/**
	 * Calls the System File Browser
	 * 
	 * @param save
	 *            either or not show the save dialog
	 * @param extension
	 *            the file extension of the file to be loaded
	 * @return The path to the chosen file
	 */
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
