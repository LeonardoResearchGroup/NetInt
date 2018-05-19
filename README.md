# NetInt. Networked Interaction Visualization in Java #

NetInt is a Java-based node-link visualization prototype designed to visualize all kinds of directed graphs. It was originally developed to analyze financial communities of hundreds of thousands of bank clients clustered by their money transactions. 

It supports the visual discovery of patterns across the entire dataset by displaying disjoint clusters of vertices that could be filtered, zoomed in or drilled down interactively. The graphical user interface allows the user to get a nested interactive structure by choosing the clustering attribute from a list of categorical vertex attributes.

A soon to be released version of NetInt will integrate a hierarchical visualization, allowing users to choose several aggregation attributes to display the graph as an interactive nested-tier structure. 

<img width="1678" alt="screen shot 2018-05-18 at 9 10 25 pm" src="https://user-images.githubusercontent.com/10836823/40263944-06738658-5ae0-11e8-9d5d-3297afc2ea28.png">

The graph processing core is [JUNG](http://jung.sourceforge.net/), a Java graph library that handles all the operations on nodes or edges. The interactive environment for visualization and user direct manipulation is based on the core library of [Processing](http://processing.org) and OpenGL. NetInt allows for the connection of R scripts and packages such as [IGraph](http://igraph.org/) to do statistical processing not offered by JUNG.

## Main functionalities ##

### Graphical user interface ###

The prototype works on three windows simultaneously. The window on the left is the *Control Panel*. It contains all the GUI elements to load and export files, control visual elements settings, tweek environment settings, and load custom-made modules. The central window, where all graph visual elements are displayed, is named *Graph Pad*. On the right, the *Mapper Viewer* window shows the distribution of color gradients of all the numeric variables of vertices and edges. NetInt implements the [Viridis](https://bids.github.io/colormap/) color palete in all its four modes (viridis, magma, plasma and inferno).  An optional fourth window is the *Console catcher* where Java console messages are displayed for debugging.  

### Layouts ### 

Vertices are presented as dots and edges as arcs splited in thirds. The third touching the source vertex appears lighter than the third touching the target vertex. 

So far it workd with two force-directed layouts (Fuchterman-Reingold, Spring) and one concentric layout that arrange edges in a similar fashio to [circos](http://circos.ca/).

### Vertices ###  

### Edges ###  

### Communities ###   

### Propagation ###

![netint_propagation](https://user-images.githubusercontent.com/10836823/40263878-a8c9782e-5ade-11e8-87fb-d1702c6c1076.png)


### How to run it ###
package examples;

import java.io.File;

import netInt.GraphPad;
import netInt.gui.ControlPanel;
import netInt.gui.UserSettings;
import processing.core.PApplet;

/**
 * 
 * @author jsalam. June 2017
 *
 */
public class ControlPanel_Example extends PApplet {
	GraphPad pad;

	/**
	 * Required method from parent class. Define here the size of the visualization
	 * pad
	 * 
	 * @see processing.core.PApplet#settings()
	 */
	public void settings() {
		size(displayWidth - 201, displayHeight - 100, P2D);
	}

	/**
	 * Required method from parent class. It runs only once at the PApplet
	 * initialization. Instantiate the classes and initialize attributes declared in
	 * this class within this code block.
	 * 
	 * @see processing.core.PApplet#setup()
	 */
	public void setup() {
		pad = new GraphPad(this);

		// Initiate the Control Panel
		new ControlPanel(this, 200, this.height - 25);
	}

	/**
	 * Required method from parent class. It draws visualElements and other PApplet
	 * elements on the visualization pad. It constantly iterates over its contents
	 * 
	 * @see processing.core.PApplet#draw()
	 */
	public void draw() {
		background(UserSettings.getInstance().getColorBackground());
		pad.show();
	}

	/**
	 * Required method to launch graph import menu
	 * 
	 * @param selection
	 *            the file chosen by the user at the Control Panel
	 */
	public void selectImport(File selection) {

		pad.selectImport(selection);
	}

	public static void main(String[] args) {
		PApplet.main("examples.ControlPanel_Example");
	}
}

### What is this repository for? ###

* Quick summary
* Version

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* File configuration: NetInt works with directed graphml files only. 
* How to run tests
* Deployment instructions

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact
