# NetInt #

NetInt is a Java-based node-link visualization tool designed to visualize all kinds of directed graphs. It was originally developed to analyze financial communities of hundreds of thousands of bank clients clustered by their money transactions. 

This visualization platform supports the visual discovery of patterns across the entire dataset, affording a view of disjoint clusters of vertices that could be filtered, zoomed in or drilled down. The graphical user interface allows the user to choose an aggregation attributes from a list of vertex attributes, displaying an interactive structure.

A soon to be released version of NetInt will integrate a hierarchical visualization, allowing users to choose several aggregation attributes to display the graph as an interactive nested-tier structure. 

<img width="1678" alt="screen shot 2018-05-18 at 9 10 25 pm" src="https://user-images.githubusercontent.com/10836823/40263944-06738658-5ae0-11e8-9d5d-3297afc2ea28.png">

The graph processing core is [JUNG](http://jung.sourceforge.net/), a Java graph library that handles all the operations on nodes or edges. The interactive environment for visualization and user direct manipulation is based on the core library of [Processing](http://processing.org) and OpenGL. NetInt allows for the connection of R scripts and packages such as [IGraph](http://igraph.org/) to do statistical processing not offered by JUNG.

## Main functionalities ##

### Graph format and layouts ###

NetInt works with directed graphml files only. Vertices are presented as dots and edges as arcs splited in thirds. The third touching the source vertex appears lighter than the third touching the target vertex. 

So far it workd with two force-directed layouts (Fuchterman-Reingold, Spring) and one concentric layout that arrange edges in a similar fashio to [circos](http://circos.ca/)      

### Propagation ###

![netint_propagation](https://user-images.githubusercontent.com/10836823/40263878-a8c9782e-5ade-11e8-87fb-d1702c6c1076.png)


### What is this repository for? ###

* Quick summary
* Version
* [Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

### How do I get set up? ###

* Summary of set up
* Configuration
* Dependencies
* Database configuration
* How to run tests
* Deployment instructions

### Contribution guidelines ###

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

* Repo owner or admin
* Other community or team contact
