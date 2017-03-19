package examples;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import netInt.GraphPad;
import netInt.utilities.GraphmlKeyReader;
import netInt.utilities.console.ConsoleCatcher;
import netInt.visualElements.gui.ControlPanel;
import netInt.visualElements.gui.ImportMenu;
import netInt.visualElements.gui.UserSettings;
import processing.core.PApplet;

/**
 * 
 * @author jsalam
 * @version alpha
 *
 */
public class NetIntApp extends PApplet {

	private GraphPad pad;

	// The floating window displaying system messages
	protected static ConsoleCatcher consoleCatcher;

	// The menu displayed once a graph file is selected
	protected static ImportMenu importMenu;

	public ControlPanel controlPanel;

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes
	 * declared in this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		textSize(10);
		surface.setLocation(200, -300);
		smooth();
		pad = new GraphPad(this);

		// Output Console Catcher. The line below enables a console Catcher.
		consoleCatcher = new ConsoleCatcher(GraphPad.initSystemOutToConsole());

		// Control Panel Frame
		controlPanel = new ControlPanel(this, 200, this.height - 25);
	}

	/**
	 * Required method from parent class. It draws visualElements and other
	 * PApplet elements on the visualization pad. It constantly iterates over
	 * its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(UserSettings.getInstance().getColorBackground());
		pad.show();
	}

	/**
	 * Required method from parent class. Define here the size of the
	 * visualization pad
	 * 
	 * @see processing.core.PApplet#settings()
	 */
	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	/**
	 * Receives the file with the path pointing to the graph file and triggers
	 * an import process. The import process follows the parameters chosen by
	 * the user from the import menu.
	 * 
	 * The method is invoked by showFileChooser() at the ChooseHelper class
	 * 
	 * @param selection
	 *            The file to be imported
	 */
	public void selectImport(File selection) {
		if (selection != null) {
			GraphPad.setFile(selection);
			GraphmlKeyReader reader = new GraphmlKeyReader(selection);
			
			// this creates and displays the menu
			String[] layoutKeys = { "Fruchterman_Reingold", "Spring", "Circular" };
			ArrayList<String> layoutAttributes = new ArrayList<String>(Arrays.asList(layoutKeys));
			
			// Import Menu
			System.out.println("Instantiating Import Menu");
			importMenu = new ImportMenu(this);
			importMenu.makeLists(reader.getKeyNamesForNodes(), reader.getKeyNamesForEdges(), layoutAttributes);
		}
	}

	public static void main(String[] args) {
		PApplet.main("examples.NetIntApp");
	}
}