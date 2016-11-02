package visualElements.gui;

import java.awt.Dimension;
import java.io.File;
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
	 * @param save
	 * @param extension
	 * @param applet
	 */
	public void showFileChooser(boolean save, String extension, PApplet applet) {
		if (!save) {
			applet.selectInput("Select a file to process:", "selectImport");
		}
	}
	
	public String showJFileChooser(boolean save, String extension)
	{
		
		String path = null;
		javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
		chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		javax.swing.filechooser.FileNameExtensionFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(extension, extension);
		chooser.setFileFilter(filter);
		
		int action;
		
		if(save)
		{
			action = chooser.showSaveDialog(null);
		}
		else
		{
			action = chooser.showOpenDialog(null);
		}
		
		if(action == javax.swing.JFileChooser.APPROVE_OPTION)
		{
			path = chooser.getSelectedFile().getAbsolutePath();
		
		}
		
		return path;
	}

	public void processImport(File file, Executable applet) {
		ModalWindow modalW = new ModalWindow("Loading graph", new Dimension(188, 190), applet);
		modalW.open();
		Object[] data = modalW.getData();
		if (data != null) {
			System.out.println("0: "+ data[0] +"1: "+ data[1]+"2: "+ data[2]);
			//applet.getApp().loadGraph(file, (String) data[0], (String) data[1], (String) data[2], (String) data[3], (Integer) data[2]);
			applet.getApp().loadGraph(file, (String) data[0], (String) data[1], " ", " ", (Integer) data[2], GraphLoader.GRAPHML);
			applet.setActiveGraph(true);
		}
	}
}
