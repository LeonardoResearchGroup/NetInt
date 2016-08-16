package visualElements.gui;

import java.awt.Dimension;
import java.io.File;

import executable.Executable;
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
	
	public static ChooseHelper getInstance()
	{
		return INSTANCE;
	}

	public void showFileChooser(boolean save, String extension, PApplet applet) {
		
		
		if (!save) {
			applet.selectInput("Select a file to process:", "selectImport");
		}
		
	}
	
	public void processImport(File file, Executable applet)
	{
		ModalWindow modalW = new ModalWindow("Loading graph", new Dimension(188, 190), applet);
		
		modalW.open();
		
		Object[] data = modalW.getData();
		
		if(data != null)
		{
			applet.getApp().loadGraph(file,(String)data[0],(String)data[1],(Integer)data[2]);
			applet.setActiveGraph(true);
		}
		
	}

}
