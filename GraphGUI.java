import java.awt.*;
import java.awt.event.*;
import javax.swing.*;        
import java.io.*;
import java.lang.*;
import java.util.*;

/**
 *  GUI representation of a graph.
 *
 *  @author Elizabeth Carney, Nicholas R. Howe, John Ridgway
 *  @version CSC 212, May 3, 2018
 */
public class GraphGUI {
    /** Holds the graph GUI component */
    private GraphComponent graphComponent; 

    /** Holds the graph to solve */
    private GraphImplementation<PlacedData<Integer>, Integer> graph;

    /** The window */
    private JFrame frame;

    /** The node last selected by the user. */
    private Graph.Node<PlacedData<Integer>, Integer> chosenNode;

    /** A node clicked by the user. */
    private Graph.Node<PlacedData<Integer>, Integer> clickedNode;

    /** A node double-clicked by the user (to be edited). */
    private Graph.Node<PlacedData<Integer>, Integer> editingNode;

    /** The edge last selected by the user. */
    private Graph.Edge<PlacedData<Integer>, Integer> chosenEdge;

    /** A edge double-clicked by the user (to be edited). */
    private Graph.Edge<PlacedData<Integer>, Integer> editingEdge;

    /** Set of all nodes currently selected. */
    private Set<Graph.Node<PlacedData<Integer>, Integer>>
	nodesSelected = new HashSet<Graph.Node<PlacedData<Integer>, Integer>>();

    /** Set of all edges currently selected. */
    private Set<Graph.Edge<PlacedData<Integer>, Integer>>
	edgesSelected = new HashSet<Graph.Edge<PlacedData<Integer>, Integer>>();

    /** Text box for editing node's data. */
    public JTextField enterNodeData;

    /** Text box for editing edge's data. */
    public JTextField enterEdgeData;

    /** Button to remove selected node(s). */
    public JButton removeNodeButton;

    /** Button to remove selected edge(s). */
    public JButton removeEdgeButton;

    /** Label that appears when user adds new edge. */
    JLabel instructions;

    /** Flag telling whether the add edge button has been pressed. */
    public boolean addEBClicked = false;

    /** One of a new edge's nodes. */
    public Graph.Node<PlacedData<Integer>, Integer> firstN;

    /** The second of a new edge's nodes.*/
    public Graph.Node<PlacedData<Integer>, Integer> secondN;

    /**
     *  Constructor that builds a completely empty graph.
     */
    public GraphGUI() {
	this.graph = new GraphImplementation<PlacedData<Integer>, Integer>();
	initializeGraph();
	this.graphComponent = new GraphComponent(this.graph);
    }

    /**
     *  Create and show the GUI.
     */
    public void createAndShowGUI() {
	// Create and set up the window.
	frame = new JFrame("Graph Application");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	// Add components
	createComponents(frame.getContentPane());

	// Display the window.
	frame.pack();
	frame.setVisible(true);
    }

    /**
     *  Create the components and add them to the frame.
     *
     *  @param pane the frame to which to add them
     */
    public void createComponents(Container pane) {
	pane.add(this.graphComponent);
	MyMouseListener ml = new MyMouseListener();
	this.graphComponent.addMouseListener(ml);
	this.graphComponent.addMouseMotionListener(ml);
	JPanel panel = new JPanel();
	panel.setLayout(new BorderLayout());
	JPanel editpanel = new JPanel();
	editpanel.setLayout(new BorderLayout());
	JPanel editnodepanel = new JPanel();
	editnodepanel.setLayout(new FlowLayout());
	JPanel editedgepanel = new JPanel();
	editedgepanel.setLayout(new FlowLayout());
	JPanel travpanel = new JPanel();
	travpanel.setLayout(new FlowLayout());
	JPanel newpanel = new JPanel();
	newpanel.setLayout(new FlowLayout());

	// Components of the edit node panel:
	JButton addNodeButton = new JButton("Add Node");
	addNodeButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    graph.addNode(new PlacedData<Integer>(0, 80, 275));
		    graphComponent.repaint();
		}
	    });
	editnodepanel.add(addNodeButton);
	removeNodeButton = new JButton("Remove Node(s)");
	removeNodeButton.setEnabled(false);
	removeNodeButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    Set<Graph.Node<PlacedData<Integer>, Integer>>
			toRemoveNodes = new HashSet<Graph.Node<PlacedData<Integer>, Integer>>();
		    for (Graph.Node<PlacedData<Integer>, Integer> node : nodesSelected) {
			graph.removeNode(node);
			toRemoveNodes.add(node);
		    }
		    nodesSelected.removeAll(toRemoveNodes);
		    enterEdgeData.setText("");
		    removeNodeButton.setEnabled(false);
		    graphComponent.repaint();
		}
	    });
	editnodepanel.add(removeNodeButton);
	JLabel editNodeLabel = new JLabel("Edit node data: ");
	editnodepanel.add(editNodeLabel);
	enterNodeData = new JTextField(2);
	enterNodeData.setEnabled(false);
	enterNodeData.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    String newNodeData = enterNodeData.getText();
		    if (newNodeData.length() != 0) {
			editingNode.getData().setData(Integer.valueOf(newNodeData));
		    }
		    enterNodeData.setText("");
		    enterNodeData.setEnabled(false);
		    graphComponent.repaint();
		}
	    });
	editnodepanel.add(enterNodeData);

	// Components of the edit edge panel:
	instructions = new JLabel("Select the tail node.");
	newpanel.add(instructions);
	instructions.setVisible(false);
	JButton addEdgeButton = new JButton("Add Edge");
	addEdgeButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    addEBClicked = true;
		    instructions.setVisible(true);
		}
	    });
	editedgepanel.add(addEdgeButton);
	removeEdgeButton = new JButton("Remove Edge(s)");
	removeEdgeButton.setEnabled(false);
	removeEdgeButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    Set<Graph.Edge<PlacedData<Integer>, Integer>>
			toRemoveEdges = new HashSet<Graph.Edge<PlacedData<Integer>, Integer>>();
		    for (Graph.Edge<PlacedData<Integer>, Integer> edge : edgesSelected) {
			graph.removeEdge(edge);
			toRemoveEdges.add(edge);
		    }
		    edgesSelected.removeAll(toRemoveEdges);
		    enterEdgeData.setText("");
		    removeEdgeButton.setEnabled(false);
		    graphComponent.repaint();
		}
	    });
	editedgepanel.add(removeEdgeButton);
	JLabel editEdgeLabel = new JLabel("Edit edge data: ");
	editedgepanel.add(editEdgeLabel);
	enterEdgeData = new JTextField(2);
	enterEdgeData.setEnabled(false);
	enterEdgeData.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    String newEdgeData = enterEdgeData.getText();
		    if (newEdgeData.length() != 0) {
			editingEdge.setData(Integer.valueOf(newEdgeData));
		    }
		    enterEdgeData.setText("");
		    enterEdgeData.setEnabled(false);
		    graphComponent.repaint();
		}
	    });
	editedgepanel.add(enterEdgeData);

	// Components of the traversal panel: 
	JButton dftButton = new JButton("DFT");
	dftButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    new Thread(new Runnable(){
			    public void run() {
				if (chosenNode != null) {
				    new GraphTraverser().depthFirstTraversal
					(chosenNode, new NodeProcessor());
				}
			    }
			}).start();
		}
	    });
	travpanel.add(dftButton);
	JButton bftButton = new JButton("BFT");
	bftButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    new Thread(new Runnable(){
			    public void run() {
				if (chosenNode != null) {
				    new GraphTraverser().breadthFirstTraversal
					(chosenNode, new NodeProcessor());
				}
			    }
			}).start();
		}
	    });
	travpanel.add(bftButton);
	JButton resetButton = new JButton("Reset");
	resetButton.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {
		    for (Graph.Node<PlacedData<Integer>, Integer> node : graph.getNodes()) {
			node.getData().setColor(new Color(168, 219, 237));
		    }
		    graphComponent.repaint();
		}
	    });
	travpanel.add(resetButton);
	
	editpanel.add(editnodepanel, BorderLayout.NORTH);
	editpanel.add(editedgepanel, BorderLayout.SOUTH);
	panel.add(editpanel, BorderLayout.NORTH);
	panel.add(travpanel, BorderLayout.SOUTH);
	pane.add(newpanel, BorderLayout.NORTH);
	pane.add(panel, BorderLayout.SOUTH);
    }

    /**
     *  Disables all text fields and sets default text within them to empty.
     */
    public void disableAll() {
	enterNodeData.setText("");
	enterNodeData.setEnabled(false);
	enterEdgeData.setText("");
	enterEdgeData.setEnabled(false);
    }

    /**
     *  Execute the application.
     */
    public void execute() {
	// Schedule a job for the event-dispatching thread:
	// creating and showing this application's GUI.
	javax.swing.SwingUtilities.invokeLater(new Runnable() {
		public void run() {
		    createAndShowGUI();
		}
	    });
    }

    /**
     *  The obligatory main method for the application.  With no
     *  arguments the application will read the graph from the standard
     *  input; with one argument (a file name) it will read the graph
     *  from the named file.
     *
     *  @param args  the command-line arguments
     */
    public static void main(String[] args) throws IOException {
	GraphGUI graphicGraph;
	graphicGraph = new GraphGUI();
	graphicGraph.execute();
    }

    /**
     *  A mouse listener to handle click and drag actions on nodes.
     */
    private class MyMouseListener extends MouseAdapter {
	/** How far off the center of the node was the click? */
	private int deltaX;
	private int deltaY;

	// Colors
	private Color myColor = new Color(168, 219, 237);
	private Color selectColor = new Color(60, 79, 224);

	/**
	 *  Finds the shortest distance between mouse click point and line.
	 *  Used to tell whether user clicked within 5px of an edge.
	 *
	 *  @return the distance between the point and the line
	 */
	public double findDist(Graph.Edge<PlacedData<Integer>, Integer> edge,
			       double edgeX1, double edgeY1, double edgeX2, double edgeY2,
			       double mouseX, double mouseY) {
	    double edgeSlope = (edgeY2 - edgeY1) / (edgeX2 - edgeX1);
	    double dist;

	    if (edgeSlope == 0.0) {
		dist = -1.0;
	    } else {
		double perpSlope = (-1) * (1 / edgeSlope);
		/* solving intersection:
		   edge equation: y = edgeSlope(x) - edgeSlope(edgeX1) + edgeY1
		   perp equation: y = perpSlope(x) - perpSlope(mouseX) + mouseY
		   x = (edgeSlope*edgeX1 - edgeY1 - perpSlope*mouseX + mouseY) / (edgeSlope - perpSlope)
		*/
		double commonX = (edgeSlope*edgeX1 - edgeY1 - perpSlope*mouseX + mouseY) / (edgeSlope - perpSlope);
		double commonY = edgeSlope*(commonX - edgeX1) + edgeY1;
		double dx = Math.abs(mouseX - commonX);
		double dy = Math.abs(mouseY - commonY);
		dist = Math.sqrt(dx*dx + dy*dy);
	    }
	    return dist;
	}

	/**
	 *  Tells whether x is less than y and y is less than z.
	 *
	 *  @return true if x <= y <= z
	 */
	public boolean ordered(double x, double y, double z) {
	    return (x <= y) && (y <= z);
	}

	public void mouseDragged(MouseEvent e) {
	    if (chosenNode != null) {
		chosenNode.getData().setX(e.getX() + deltaX);
		chosenNode.getData().setY(e.getY() + deltaY);
	    }
	    graphComponent.repaint();
	}

	public void mouseReleased(MouseEvent e) {
	    chosenNode = null;
	    chosenEdge = null;
	    clickedNode = null;
	    editingNode = null;
	    editingEdge = null;
	}

	public void mousePressed(MouseEvent e) {
	    double mouseX = e.getX();
	    double mouseY = e.getY();

	    disableAll();

	    // Recognize when a node is pressed
	    for (Graph.Node<PlacedData<Integer>, Integer> node : graph.getNodes()) {
		double nodeX = node.getData().getX();
		double nodeY = node.getData().getY();
		if (Math.sqrt((nodeX-mouseX)*(nodeX-mouseX)
			      +(nodeY-mouseY)*(nodeY-mouseY))
		    <= graphComponent.NODE_RADIUS) {
		    chosenNode = node;
		}
	    }
	    if (chosenNode != null) {
		deltaX = chosenNode.getData().getX() - e.getX();
		deltaY = chosenNode.getData().getY() - e.getY();
	    }

	    // Handle user clicking two nodes for the tail and head of a new edge to be added
	    if ((addEBClicked) && (chosenNode != null) && (firstN == null)) {
		firstN = chosenNode;
		instructions.setText("Select the head node.");
	    } else if ((addEBClicked) && (chosenNode != null) && (firstN != null) && (secondN == null)) {
		secondN = chosenNode;
		int edata = graph.getEdges().size() + 1;
		if (firstN != secondN) {
		    graph.addEdge(edata, firstN, secondN);
		}
		addEBClicked = false;
		firstN.getData().setBorderColor(myColor);
		nodesSelected.remove(firstN);
		nodesSelected.remove(secondN);
		instructions.setVisible(false);
		firstN = null;
		secondN = null;
		graphComponent.repaint();
	    }
	}

	public void mouseClicked(MouseEvent e) {
	    double mouseX = e.getX();
	    double mouseY = e.getY();

	    // Recognize when a node is clicked
	    for (Graph.Node<PlacedData<Integer>, Integer> node : graph.getNodes()) {
		double nodeX = node.getData().getX();
		double nodeY = node.getData().getY();
		if (Math.sqrt((nodeX-mouseX)*(nodeX-mouseX)
			      +(nodeY-mouseY)*(nodeY-mouseY))
		    <= graphComponent.NODE_RADIUS) {
		    clickedNode = node;
		}
	    }
	    // If double-click, editing becomes possible
	    if ((clickedNode != null) && (e.getClickCount() == 2)) {
		editingNode = clickedNode;
		int curr_enode_data = editingNode.getData().getData();
		enterNodeData.setEnabled(true);
		enterNodeData.setText(Integer.toString(curr_enode_data));

		if (nodesSelected.contains(editingNode)) {
		    editingNode.getData().setBorderColor(myColor);
		    nodesSelected.remove(editingNode);
		} else {
		    editingNode.getData().setBorderColor(selectColor);
		    nodesSelected.add(editingNode);
		}
		graphComponent.repaint();
	    } else if (clickedNode != null) {
		removeNodeButton.setEnabled(true);
		if (nodesSelected.contains(clickedNode)) {
		    clickedNode.getData().setBorderColor(myColor);
		    nodesSelected.remove(clickedNode);
		} else {
		    clickedNode.getData().setBorderColor(selectColor);
		    nodesSelected.add(clickedNode);
		}
		graphComponent.repaint();
	    }

	    // Recognize when an edge is clicked (less than 5px away from line)
	    for (Graph.Edge<PlacedData<Integer>, Integer> edge : graph.getEdges()) {
		int rad = GraphComponent.NODE_RADIUS;
		double theta = Math.atan2(edge.getHead().getData().getY() - edge.getTail().getData().getY(),
					  edge.getHead().getData().getX() - edge.getTail().getData().getX());
		double edgeX1 = edge.getTail().getData().getX() + Math.cos(theta)*rad;
		double edgeY1 = edge.getTail().getData().getY() + Math.sin(theta)*rad;
		double edgeX2 = edge.getHead().getData().getX() - Math.cos(theta)*rad;
		double edgeY2 = edge.getHead().getData().getY() - Math.sin(theta)*rad;
		double dist = findDist(edge, edgeX1, edgeY1, edgeX2, edgeY2, mouseX, mouseY);
		if (dist == -1.0) {
		    dist = Math.abs(mouseY - edgeY1);
		    if ((dist < 5) && (ordered(edgeX1, mouseX, edgeX2) || ordered(edgeX2, mouseX, edgeX1))) {
			chosenEdge = edge;
		    }
		} else if (dist < 5) {
		    if (ordered(edgeX1, mouseX, edgeX2) || ordered(edgeX2, mouseX, edgeX1)) { 
			if (ordered(edgeY1, mouseY, edgeY2) || ordered(edgeY2, mouseY, edgeY1)) { 
			    chosenEdge = edge;
			}
		    }
		}
	    }
	    // If double-click, editing becomes possible
	    if ((chosenEdge != null) && (e.getClickCount() == 2)) {
		editingEdge = chosenEdge;
		int curr_eedge_data = editingEdge.getData();
		enterEdgeData.setEnabled(true);
		enterEdgeData.setText(Integer.toString(curr_eedge_data));

		if (edgesSelected.contains(chosenEdge)) {
		    edgesSelected.remove(chosenEdge);
		} else {
		    edgesSelected.add(chosenEdge);
		}
		graphComponent.repaint();
	    } else if (chosenEdge != null) {
		removeEdgeButton.setEnabled(true);
		if (edgesSelected.contains(chosenEdge)) {
		    edgesSelected.remove(chosenEdge);
		} else {
		    edgesSelected.add(chosenEdge);
		}
		graphComponent.repaint();
	    }
	}

	public void mouseMoved(MouseEvent e) {
	    double mouseX = e.getX();
	    double mouseY = e.getY();
	    	    	
	    if (nodesSelected.size() == 0) {
		removeNodeButton.setEnabled(false);
	    }
	    if (edgesSelected.size() == 0) {
		removeEdgeButton.setEnabled(false);
	    }
	    
	    // Recognize when a node is moused-over
	    for (Graph.Node<PlacedData<Integer>, Integer> node : graph.getNodes()) {
		double nodeX = node.getData().getX();
		double nodeY = node.getData().getY();
		if (Math.sqrt((nodeX-mouseX)*(nodeX-mouseX)
			      +(nodeY-mouseY)*(nodeY-mouseY))
		    <= graphComponent.NODE_RADIUS) {
		    node.getData().setBorderColor(selectColor);
		} else if (!nodesSelected.contains(node)) {
		    node.getData().setBorderColor(myColor);
		}
		graphComponent.repaint();
	    }
	}
	
    }
  
    private void initializeGraph() {
	Graph.Node<PlacedData<Integer>, Integer> node1 = graph.addNode(new PlacedData<Integer>(1, 50, 50));
	Graph.Node<PlacedData<Integer>, Integer> node2 = graph.addNode(new PlacedData<Integer>(2, 150, 50));
	Graph.Node<PlacedData<Integer>, Integer> node3 = graph.addNode(new PlacedData<Integer>(3, 150, 250));
	Graph.Node<PlacedData<Integer>, Integer> node4 = graph.addNode(new PlacedData<Integer>(4, 50, 150));
	Graph.Node<PlacedData<Integer>, Integer> node5 = graph.addNode(new PlacedData<Integer>(5, 250, 50));
	Graph.Node<PlacedData<Integer>, Integer> node6 = graph.addNode(new PlacedData<Integer>(6, 175, 100));
	Graph.Node<PlacedData<Integer>, Integer> node7 = graph.addNode(new PlacedData<Integer>(7, 250, 150));
	Graph.Edge<PlacedData<Integer>, Integer> edge1 = graph.addEdge(1, node1, node2);
	Graph.Edge<PlacedData<Integer>, Integer> edge2 = graph.addEdge(2, node1, node3);
	Graph.Edge<PlacedData<Integer>, Integer> edge3 = graph.addEdge(3, node2, node4);
	Graph.Edge<PlacedData<Integer>, Integer> edge4 = graph.addEdge(4, node2, node5);
	Graph.Edge<PlacedData<Integer>, Integer> edge5 = graph.addEdge(5, node3, node6);
	Graph.Edge<PlacedData<Integer>, Integer> edge6 = graph.addEdge(6, node3, node7);
	Graph.Edge<PlacedData<Integer>, Integer> edge7 = graph.addEdge(7, node3, node1);
	System.out.println(graph.validateGraph());
	System.out.println(graph.toString());
    }


    private class NodeProcessor implements Graph.Processor<PlacedData<Integer>, Integer> {
	public boolean processEdge(Graph.Edge<PlacedData<Integer>, Integer> edge) {
	    // do nothing
	    return false;
	}

	public boolean preProcessNode(Graph.Node<PlacedData<Integer>, Integer> node) {
	    System.out.println("p");
	    node.getData().setColor(java.awt.Color.red);
	    graphComponent.repaint();
	    try {
		Thread.sleep(1000);
	    } catch (Exception e) {
	    }
	    return false;
	}

	public boolean postProcessNode(Graph.Node<PlacedData<Integer>, Integer> node) {
	    // do nothing
	    return false;
	}
    }
}
