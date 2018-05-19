# NetInt #

NetInt is a Java-based node-link visualization tool initially developed to analyze financial communities of hundreds of thousands of bank clients clustered by their transactions. The latest open-source release is designed to visualize all kinds of directed graphs.

<img width="1678" alt="screen shot 2018-05-18 at 9 10 25 pm" src="https://user-images.githubusercontent.com/10836823/40263944-06738658-5ae0-11e8-9d5d-3297afc2ea28.png">

It supports the visual discovery of patterns across the entire dataset, affording a hierarchical view of disjoint clusters of vertices that could be filtered, zoomed in or drilled down. The graph processing core is [JUNG](http://jung.sourceforge.net/), a Java graph library that handles all the operations on nodes or edges. The interactive environment for visualization and user direct manipulation is based on the core library of [Processing](http://processing.org) and OpenGL. NetInt allows for the connection of R scripts and packages such as [IGraph](http://igraph.org/) to do statistical processing not offered by JUNG.

![netint_hierarchy](https://user-images.githubusercontent.com/10836823/40263709-1dad979a-5adc-11e8-979f-db0c0b3a8954.png)

The dataset of the example above contains circa 20K vertices and 200K edges. The user choosed two aggregation attributes (financial community and industry segment), and NetInt created an interactive nested-tier structure. Some of the edges are concealed to simplify the visualization.

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
