# GraphGUI
A GUI illustrating graph behavior with the ability to select, unselect, add, delete, and edit nodes and edges. This was my final project for Data Structures (CSC 212) in May 2018.

**Features:**
  - Nodes are highlighted (change border color) when mouse hovers over them
  - Recognizes when an edge is clicked on
  - Nodes can be selected and unselected (change border color)
  - Multiple nodes and edges can be selected concurrently
  - Multiple nodes can be deleted at once
  - Multiple edges can be deleted at once
  - Nodes can be added
  - Edges can be added
  - Double-click a node or edge to modify or view its data (this does not affect set of selected nodes or edges)
  
**Demo: (click to watch video)**

[![Watch the demo here!](https://i.imgur.com/tdigfmR.png)](https://youtu.be/a7a6CrOWuDc)
  
  Throughout this project, I was very conscious of the user's experience. First of all, I made the appearance more aesthetically-pleasing by centering the nodes' data and making them a lighter blue. I wanted the user to be able to tell when they had selected a node, so I made sure that they changed color. (I was going to do this with the edges as well by making their data type Placed Data instead of Integer, but I couldn't get it in time.) For easy viewing, I made the default text in the text fields equal to the data of the currently-double-clicked node or edge. I also disabled the text fields when a node or edge was not double-clicked on in order to make sure that the user did not accidentally edit data when they did not mean to. I protected against blank entry, as well: if the user double-clicks a node, erases its data, and then clicks away, the data will not change from its previous value. Additionally, I put instructions at the top of the screen for adding an edge. The "Remove Node(s)" button greys out when no nodes are selected, and the "Remove Edge(s)" button does the same. I think the multiple-deletion-at-once feature is very convenient. 

To test out my GUI, please try:
  - Mousing over nodes
  - Selecting and unselecting nodes and edges
  - Double-clicking on nodes and edges and editing their data
  - Removing nodes and edges (one at a time or multiple at once)
  - Adding nodes
  - Adding edges
