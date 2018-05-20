# NetInt. Networked Interaction Visualization in Java #

NetInt is a Java-based node-link visualization prototype designed to visualize communities in directed graphs.

It supports the visual discovery of patterns across the entire dataset by displaying disjoint clusters of vertices that could be filtered, zoomed in or drilled down interactively. The graphical user interface allows the user to get a nested interactive structure by choosing the clustering attribute from a list of categorical vertex attributes.

A soon to be released version of NetInt will integrate a hierarchical visualization, allowing users to choose several aggregation attributes to display the graph as an interactive nested-tier structure. 

<img width="1678" alt="screen shot 2018-05-18 at 9 10 25 pm" src="https://user-images.githubusercontent.com/10836823/40263944-06738658-5ae0-11e8-9d5d-3297afc2ea28.png">

The graph processing core is [JUNG](http://jung.sourceforge.net/), a Java graph library that handles all the operations on nodes or edges. The interactive environment for visualization and user direct manipulation is based on the core library of [Processing](http://processing.org) and OpenGL. NetInt allows for the connection of R scripts and packages such as [IGraph](http://igraph.org/) to do statistical processing not offered by JUNG.

## Motivation ##

NetInt was originally developed to analyze financial communities of bank clients clustered by their money transactions. Our initial attempt was to visualize the dataset in [Gephi](https://gephi.org/), but even with professional GPUs it didn't display efficiently the hundreds of thousands of graph elements, therefore we decided to prototype our own graph visualization solution. Moreover, we were interested in community detection and financial analysis of the resulting communities, therefore we needed to subset the graph and display the results relative to each community. All these things are possible in Gephi, but when you work with large graphs getting the result is tedious.

Caveat: NetInt doesn't do community detection itself. It relies on third party community detection libraries such as iGraph in R. What it does is to subset the graph in subgraphs using JUNG and display them all at once in an interactive manner.  

## Main functionalities ##

### Fast community visualization of large graphs ###

Netint does not show the whole graph at once. It starts displaying a top tier and progresively unpacks graph portions on demand. To do so, during loading process the user is asked to select a clustering attribute from the list of vertices' categorical attributes. Netint creates containers for each category, inserts in each container the corresponding nodes, and displays only the containers (See image below). But, be aware that if the user selects a clustering attribute with many values (in the order than 200), the loading process could be lengthy.

<img width="420" alt="asset 1 2x" src="https://user-images.githubusercontent.com/10836823/40271062-a8dc2ab2-5b5d-11e8-83f2-5c8bbb1e7f06.png">

*Example of top tier visualizaton of 20K nodes and 200K edges clustered in 22 communities* 

### Graphical user interface ###

The prototype runs on three windows simultaneously: Control panel, Graph pad, and Mapper viewer. The **Control panel** contains two GUI accordeons. One for all the GUI elements to load and export files, and another for the visual elements' settings (node, edge, and community), environment settings, and custom-made modules. The **Graph pad** is the canvas for all the graph visual elements. Finally, the **Mapper viewer** shows the distribution of color gradients of all the numeric variables of vertices and edges. An optional fourth window is the Console catcher, where Java console messages are displayed for debugging.

![controlpanel](https://user-images.githubusercontent.com/10836823/40272885-1a957042-5b7b-11e8-9a5c-bd41f54096b1.png)
*Accordeon sections of Control panel* 

### Layouts ### 

So far NetInt works with two force-directed layouts (Fuchterman-Reingold, Spring) and one concentric layout that arrange edges in a similar fashion to [circos](http://circos.ca/). Graphs are visualized by default with the concentric layout because it is extremely fast.

### Mapping Edges and Vertices attributes ### 

Vertices are displayed as circles and edges as arcs. Arcs are splited in three sections, each drawn by separate bezier curves. To indicate edge directionality, the section touching the source vertex has a lighter color than the section touching the target vertex. 

NetInt implements the [Viridis](https://bids.github.io/colormap/)color palette in all its four modes (viridis, magma, plasma and inferno).

<img width="312" alt="screen shot 2018-05-19 at 3 22 39 pm" src="https://user-images.githubusercontent.com/10836823/40272945-c3bba442-5b7c-11e8-808f-8c4e28f5aa18.png">

### Community characterization ###


### Intercommunity interaction ###


### Filtering ###


### Graph computations ###

NetInt is not intended to do graph computations, but you can do operations on the graph such as calculate centralities or do graph partitions if you use methods from JUNG library directly on the graph. This would require you to access the static attribute *theGraph* of the class *GraphLoader*. If you want to do operations on the user defined graph partitions, you have access to all of them retrieving the static collection *subGraphs* of the same class.   

```
GraphLoader.theGraph;
GraphLoader.subGraphs;
```
	
### Propagation ###

The detection and visualization of communities is extremely valuable in the study of propagation because it revealed community structures, key vertices, critical intermediaries, suspicious sources, and transaction loops. NetInt allows users to interactively explore potential propagations or contagions in the network by choosing an specific source and observe the percolation step by step. The example below shows how the transaccions of a single source reach two communities in three steps. 

<img width="1011" alt="artboard 1 2x" src="https://user-images.githubusercontent.com/10836823/40269876-8dc558a2-5b49-11e8-86d7-6346e2bb4ec2.png">

## How do I get set up? ##

* Summary of set up: If you have a dataset in [graphml](http://graphml.graphdrawing.org/) format ready to be visualized, just download the latest released execultable [file](https://github.com/LeonardoResearchGroup/NetInt/releases), run it and import your file. 

* Configuration: Your computer should have Java 1.8 or higher.

* File configuration: NetInt works with graphml files only. The file should follow the graphml [standard](http://graphml.graphdrawing.org/).

## How is it built? ##

In general terms, it is all about creating instances of GraphPad and ControlPanel classes, and visualize the output on an instance of the PApplet class. The instance of GrapPad contains all the interactive visual elements inside community containers. Each container has methods that map the attributes of vertices and edges to the attributes of visual elements. The code below shows how this process happens in the main executable class.    

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


### Who do I talk to? ###

* This project is developed and maintained by Juan Salamanca (jsal@illinois.edu) and Cesar Loaiza (cdloaiza@icesi.edu.co). Luis Felipe Rivera and Javier Diaz have contributed extensively.
