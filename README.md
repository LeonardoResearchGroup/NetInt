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

So far it works with two force-directed layouts (Fuchterman-Reingold, Spring) and one concentric layout that arrange edges in a similar fashion to [circos](http://circos.ca/).

### Vertices ###  

### Edges ###  

### Communities ###

### Graph computations ###

NetInt is not intended to do graph computations, but you can do operations on the graph such as calculate centralities or do graph partitions if you use methods from JUNG library directly on the graph. This would require you to access the static attribute *theGraph* of the class *GraphLoader*. If you want to do operations on the user defined graph partitions, you have access to all of them retrieving the static collection *subGraphs* of the same class.   

```
GraphLoader.theGraph;
GraphLoader.subGraphs;
```
	
### Propagation ###

The detection and visualization of communities is extremely valuable in the study of propagation because it revealed community structures, key vertices, critical intermediaries, suspicious sources, and transaction loops. NetInt allows users to interactively explore potential propagations or contagions in the network by choosing an specific source and observe the percolation step by step. The exmaple below shows how the transaccions of a single source reach two communities in three steps. 

![netint_propagation](https://user-images.githubusercontent.com/10836823/40263878-a8c9782e-5ade-11e8-87fb-d1702c6c1076.png)


### How is it built? ###

	import java.io.File;
	import netInt.GraphPad;
	import netInt.gui.ControlPanel;
	import netInt.gui.UserSettings;
	import processing.core.PApplet;

	public class Example extends PApplet {
		
		GraphPad pad;

		/**
		 * Defines the visualization pad size
		 */
		public void settings() {
			size(displayWidth - 201, displayHeight - 100, P2D);
		}

		/**
		 * Instantiates the classes and initializes attributes declared in this class within this method.
		 */
		public void setup() {
			pad = new GraphPad(this);
			new ControlPanel(this, 200, this.height - 25);
		}

		/**
		 * Recursively draws visual elements on the visualization pad.
		 */
		public void draw() {
			background(UserSettings.getInstance().getColorBackground());
			pad.show();
		}

		/**
		 * Required method to launch graph import menu
		 * @param selection
		 *            the file chosen by the user in the Control Panel
		 */
		public void selectImport(File selection) {
			pad.selectImport(selection);
		}

		public static void main(String[] args) {
			PApplet.main("yourPackage.Example");
		}
	}

### What is this repository for? ###

* Quick summary
* Version

### How do I get set up? ###

* Summary of set up: If you have a dataset in [graphml](http://graphml.graphdrawing.org/) format ready to be visualized, just download the released execultable file (NetIntTest.jar), run it and import your file. 

* Configuration: Your computer should have Java 1.8 or higher.

* File configuration: NetInt works with graphml files only. The file should follow the graphml [standard](http://graphml.graphdrawing.org/).

* How to run tests
* Deployment instructions

### Who do I talk to? ###

* This project is developed and maintained by Juan Salamanca (jsal@illinois.edu) and Cesar Loaiza (cdloaiza@icesi.edu.co). Luis Felipe Rivera and Javier Diaz have contributed extensively.
