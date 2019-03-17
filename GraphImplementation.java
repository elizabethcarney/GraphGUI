import java.util.*;

/**
 *  Implements the Graph interface.
 *  A graph is a pair <N,E> where N is a set of nodes and E is 
 *  a set of edges, and where each edge is an ordered pair <n1,n2> 
 *  where both n1 and n2 are members of N.
 *
 *  @author Elizabeth Carney
 *  @version CSC 212, May 3, 2018
 */
public class GraphImplementation<N,E> implements Graph<N,E> {
    /** A set of all nodes in the graph. */
    private Set<NodeImplementation> nodes = new HashSet<NodeImplementation>();
    /** A set of all edges in the graph. */
    private Set<EdgeImplementation> edges = new HashSet<EdgeImplementation>();
    
    /**
     *  Get a new set of all of the nodes in the graph. Changes made 
     *  to the returned set will not be reflected in the graph, though 
     *  changes made to particular nodes in the set may be.
     *  Performance: O(n)
     *
     *  @return a new set of the nodes in the graph
     */
    public Set<Node<N,E>> getNodes() {
	HashSet<Node<N,E>> nclone = new HashSet<Node<N,E>>();
	for(NodeImplementation nodey : nodes) {
	    nclone.add(nodey);
	}
	return nclone;
    }

    /**
     *  Get a new set of all of the edges in the graph.  Changes made to the
     *  returned set will not be reflected in the graph, though changes made to
     *  particular edges in the set may be.
     *  Performance: O(e)
     *
     *  @return a new set of the edges in the graph
     */
    public Set<Edge<N,E>> getEdges() {
	HashSet<Edge<N,E>> eclone = new HashSet<Edge<N,E>>();
	for(EdgeImplementation edgey : edges) {
	    eclone.add(edgey);
	}
	return eclone;
    }

    /**
     *  Find a particular edge given its tail and head.
     *  Performance: O(e)
     *
     *  @param tail : the tail ("from" node) of the edge to be found
     *  @param head : the head ("to" node) of the edge to be found
     *  @return the edge, or null if there is no such edge
     */
    public Edge<N,E> findEdge(Node<N,E> tail, Node<N,E> head) {
	for(EdgeImplementation edge : edges) {
	    if(tail == edge.tail && head == edge.head) {
		return edge;
	    }
	}
	return null;
    }

    /**
     *  Short-cut to get the number of nodes in the graph. This is
     *  equivalent to, though not necessarily implemented as, 
     *  getNodes().size().
     *  Performance: O(1)
     *
     *  @return the number of nodes in the graph
     */
    public int numNodes() {
	return nodes.size();
    }

    /**
     *  Short-cut to get the number of edges in the graph. This is
     *  equivalent to, though not necessarily implemented as, 
     *  getEdges().size().
     *  Performance: O(1)
     *
     *  @return the number of edges in the graph
     */
    public int numEdges() {
	return edges.size();
    }

    /**
     *  Adds a node to the graph. The new node will have degree 0.
     *  Performance: O(1);
     *
     *  @param data : the data to be associated with the node
     *  @return the new node
     */
    public Node<N,E> addNode(N data) {
	NodeImplementation newnode = new NodeImplementation(data);
	nodes.add(newnode);
	return newnode;
    }

    /**
     *  Adds an edge to the graph.
     *  Performance: O(1)
     *
     *  @param tail : the tail ("from" node) of the edge to be added
     *  @param head : the head ("to" node) of the edge to be added
     *  @return the new edge
     */
    public Edge<N,E> addEdge(E data, Node<N,E> tail, Node<N,E> head) {
	EdgeImplementation newedge = new EdgeImplementation(data, tail, head);
	edges.add(newedge);
	((NodeImplementation)tail).addToOuties(newedge);
	((NodeImplementation)head).addToInnies(newedge);
	return newedge;
    }

    /**
     *  Removes a node and all its incident edges from the graph.
     *  Performance: O(e)
     *
     *  @param node : the node to be removed
     *  @throws Error if the node does not belong to this graph
     */
    public void removeNode(Node<N,E> node) {
	if(!nodes.contains((NodeImplementation)node)) {
	    throw new Error("Node does not belong to graph.");
	}
	Set<EdgeImplementation> toremove = new HashSet<EdgeImplementation>();
	for(EdgeImplementation edge : edges) {
	    if(((NodeImplementation)node).outiesContains(edge) || ((NodeImplementation)node).inniesContains(edge)) {
		//if node is tail
		((NodeImplementation)node).removeFromOuties(edge);
		((NodeImplementation)edge.getHead()).removeFromInnies(edge);
		//if node is head
		((NodeImplementation)node).removeFromInnies(edge);
		((NodeImplementation)edge.getTail()).removeFromOuties(edge);
		//no matter what
		toremove.add(edge);
	    }
	}
	edges.removeAll(toremove);
	nodes.remove(node);
    }

    /**
     *  Removes an edge from the graph.
     *  Performance: O(1)
     *
     *  @param edge : the edge to be removed
     *  @throws Error if the edge does not belong to this graph
     */
    public void removeEdge(Edge<N,E> edge) {
	if(!edges.contains(edge)) {
	    throw new Error("Edge does not belong to graph.");
	}
	((NodeImplementation)edge.getTail()).removeFromOuties((EdgeImplementation)edge);
	((NodeImplementation)edge.getHead()).removeFromInnies((EdgeImplementation)edge);
	edges.remove(edge);
    }

    /**
     *  Removes an edge from the graph given its tail and head nodes.
     *  Performance: O(e)
     *
     *  @param tail : the tail ("from" node) of the edge to be removed
     *  @param head   : the head ("to" node) of the edge to be removed
     *  @throws Error if the head or tail nodes do not belong to this graph
     */
    public void removeEdge(Node<N,E> tail, Node<N,E> head) {
	if(!nodes.contains(tail) || !nodes.contains(head)) {
	    throw new Error("Edge does not belong to graph.");
	}
	for(Edge<N,E> outy : tail.getOutgoingEdges()) {
	    if(((EdgeImplementation)outy).head == head) {
		removeEdge(outy);
	    }
	}
    }

    /**
     *  Returns the set of nodes in the graph that are not in group.
     *  This is equivalent to getNodeSet().removeAll(group), but may be 
     *  implemented differently.
     *  Performance: O(n)
     *
     *  @param group : a set of nodes
     *  @return all of the nodes of the graph not present in the group
     */
    public Set<Node<N,E>> otherNodes(Set<Node<N,E>> group) {
	HashSet<Node<N,E>> others = new HashSet<Node<N,E>>();
	for(NodeImplementation node : nodes) {
	    if(!group.contains(node)) {
		others.add(node);
	    }
	}
	return others;
    }

    /**
     *  Returns the set of nodes including all and only those nodes that
     *  are one end or the other of the given set of edges.
     *  Performance: O(e)
     *
     *  @param edges : a set of edges
     *  @return a set of nodes that those edges are incident to
     */
    public Set<Node<N,E>> endpoints(Set<Edge<N,E>> edges) {
	HashSet<Node<N,E>> ends = new HashSet<Node<N,E>>();
	for(EdgeImplementation edge : this.edges) {
	    ends.add(edge.tail);
	    ends.add(edge.head);
	}
	return ends;
    }

    /**
     *  Performs a breadth-first traversal of a graph starting from the
     *  given node. As each node or edge is processed, the appropriate
     *  method in the processor is invoked. The traversal will continue 
     *  until all of the nodes have been traversed or the processor
     *  returns true, whichever happens first. 
     *
     *  @param start     : the starting node for the traversal
     *  @param processor : the processing object to be applied to each node/edge
     *  @return true if the processor ever returns true, false otherwise
     *  @throws Error if the starting node is not a node of this graph
     */
    public boolean breadthFirstTraversal(Node<N,E> start, Graph.Processor<N,E> processor) {
	if(!nodes.contains(start)) {
	    throw new Error("Edge does not belong to graph.");
	}
	System.out.println("breadthFirst");
	Queue<Graph.Node<N,E>> queue = new LinkedList<Graph.Node<N,E>>();
	Set<Graph.Node<N,E>> visited = new HashSet<Graph.Node<N,E>>();
	queue.add(start);
	while (! queue.isEmpty()) {
	    Graph.Node<N,E> node = queue.remove();
	    System.out.println("Processing " + node);
	    if (visited.contains(node)) continue;
	    visited.add(node);
	    if (processor.preProcessNode(node)) {
		return true;
	    }
	    Set<Graph.Edge<N,E>> edges = node.getOutgoingEdges();
	    for (Graph.Edge<N,E> edge : edges) {
		if (processor.processEdge(edge)) {
		    return true;
		}
		Graph.Node<N,E> head = edge.getHead();
		queue.add(head);
	    }
	}
	return false;
    }

    /**
     *  Performs a depth-first traversal of a graph starting from the
     *  given node. As each node or edge is processed, the appropriate
     *  method in the processor is invoked. The traversal will continue 
     *  until all of the nodes have been traversed or the processor 
     *  returns true, whichever happens first.
     *
     *  @param start     : the starting node for the traversal
     *  @param processor : the processing class to be applied to each node/edge
     *  @return true if the processor ever returns true, false otherwise
     *  @throws Error if the starting node is not a node of this graph
     */
    public boolean depthFirstTraversal(Node<N,E> start, Graph.Processor<N,E> processor) {
	if(!nodes.contains(start)) {
	    throw new Error("Edge does not belong to graph.");
	}
	LinkedList<Graph.Node<N,E>> queue = new LinkedList<Graph.Node<N,E>>();
	Set<Graph.Node<N,E>> visited = new HashSet<Graph.Node<N,E>>();
	queue.push(start);
	while (! queue.isEmpty()) {
	    Graph.Node<N,E> node = queue.pop();
	    if (visited.contains(node)) continue;
	    visited.add(node);
	    if (processor.preProcessNode(node)) {
		return true;
	    }
	    Set<Graph.Edge<N,E>> edges = node.getOutgoingEdges();
	    for (Graph.Edge<N,E> edge : edges) {
		if (processor.processEdge(edge)) {
		    return true;
		}
		Graph.Node<N,E> head = edge.getHead();
		queue.push(head);
	    }
	    if (processor.postProcessNode(node)) {
		return true;
	    }
	}
	return false;
    }

    /**
     *  Returns a string representation of the graph.
     *
     *  @return a string representation of the graph
     */
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("\n");
	builder.append("Graph");
	builder.append("\n");
	for(NodeImplementation nodey : nodes) {
	    builder.append("\n  Node: ");
	    builder.append(nodey.getData().toString());
	    for(Edge<N,E> edgey : nodey.getOutgoingEdges()) {
		builder.append("\n    To: ").append(edgey.getHead().getData()).append(", ").append(edgey.getData());
	    }
	}
	builder.append("\n\n Edges: ");
	for(EdgeImplementation edgey : edges) {
	    builder.append("\n    ").append(edgey.getHead().getData()).append(", ").append(edgey.getData());
	}
	builder.append("\n");
	return builder.toString();
    }

    /**
     *  Verifies the internal consistency of the graph structure.
     *
     *  @return true if consistent, false otherwise
     */
    public boolean validateGraph() {
	boolean heads = true;
	boolean tails = true;
	for(EdgeImplementation edgey : edges) {
	    if(!(edgey.getHead().getIncomingEdges().contains(edgey))) {
		heads = false;
	    }
	    if(!(edgey.getTail().getOutgoingEdges().contains(edgey))) {
		tails = false;
	    }
	}
	return (heads) && (tails);
    }

    /**
     *  Represents a node in a graph.
     */
    private class NodeImplementation implements Graph.Node<N,E> {
	/** The data associated with this node. */
	private N data;
	/** The set of edges entering this node. */
	private Set<EdgeImplementation> innies = new HashSet<EdgeImplementation>();
	/** The set of edges leaving this node. */
	private Set<EdgeImplementation> outies = new HashSet<EdgeImplementation>();
	
	/**
	 *  Create a node with the given data.
	 *
	 *  @param data : the data to be associated with the node
	 */
	public NodeImplementation(N data) {
	    this.data = data;
	}

	/**
	 *  Returns the data associated with this node.
	 *  Performance: O(1);
	 *
	 *  @return the data associated with this node
	 */
	public N getData() {
	    return data;
	}

	/**
	 */
	public void setData(N data) {
	    this.data = data;
	}

	public boolean outiesContains(EdgeImplementation edge) {
	    return outies.contains(edge);
	}

	public void addToOuties(EdgeImplementation newedge) {
	    outies.add(newedge);
	}

	public void removeFromOuties(EdgeImplementation edge) {
	    outies.remove(edge);
	}

	/**
	 *  Return a new set of edges leaving this node, the set of all
	 *  edges whose tail is this node. Changes to the returned set will
	 *  not be reflected in the graph, but changes to individual edges
	 *  in the set may be.
	 *  Performance: O(e)
	 *
	 *  @return the set of edges leaving this node
	 */
	public Set<Edge<N,E>> getOutgoingEdges() {
	    HashSet<Edge<N,E>> oclone = new HashSet<Edge<N,E>>();
	    for(EdgeImplementation edgey : outies) {
		oclone.add(edgey);
	    }
	    return oclone;	    
	}

	public boolean inniesContains(EdgeImplementation edge) {
	    return innies.contains(edge);
	}

	public void addToInnies(EdgeImplementation newedge) {
	    innies.add(newedge);
	}

	public void removeFromInnies(EdgeImplementation newedge) {
	    innies.remove(newedge);
	}
	
	/**
	 *  Return a new set of edges entering this node, the set of all
	 *  edges whose head is this node. Changes to the returned set will
	 *  not be reflected in the graph, but changes to individual edges
	 *  in the set may be.
	 *  Performance: O(e)
	 *
	 *  @return the set of edges entering this node
	 */
	public Set<Edge<N,E>> getIncomingEdges() {
	    HashSet<Edge<N,E>> iclone = new HashSet<Edge<N,E>>();
	    for(EdgeImplementation edgey : innies) {
		iclone.add(edgey);
	    }
	    return iclone;
	}

	/**
	 *  Is this node equal to that node (are the contents equal)?
	 *  Performance: O(1)
	 *
	 *  @param that : the node to compare to this one
	 *  @return true if the data associated with this is equal to the 
	 *          data associated with that
	 */
	public boolean equals(Node<N,E> that) {
	    return (that.getData() == data);
	}

	/**
	 *  Is this node equal to that object (are the contents equal)?
	 *  Performance: O(1)
	 *  
	 *  @param that : the object to compare to this one
	 *  @return true if that is a Node and the data associated with 
	 *          this is equal to the data associated with that
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object that) {
	    return (that instanceof Node) && this.equals((Node<N,E>)that);
	}

	public String toString() {
	    return data.toString();
	}

        /**
	 *  Returns a hash code for this node. This must be defined such
	 *  that if two nodes are equal (as determined by equals) then 
	 *  their hash codes are the same.
	 *  Performance: O(1)
	 *
	 *  @return the hash code computed for this object
	 */
	public int hashCode() {
	    return data.hashCode();
	}
    }

    /**
     *  Represents an edge in the graph.
     */
    private class EdgeImplementation implements Graph.Edge<N,E> {
	/** The data associated with this edge. */
	private E data;
	/** The edge's tail node. */
	private NodeImplementation tail;
	/** The edge's head node. */
	private NodeImplementation head;

	/**
	 *  Create an edge with the given data pointing from the tail node to the head node.
	 *
	 *  @param data : the data to be associated with the edge
	 *  @param head : the edge's head node
	 *  @param tail : the edge's tail node
	 */
	public EdgeImplementation(E data, Node<N,E> tail, Node<N,E> head) {
	    this.data = data;
	    this.tail = (NodeImplementation)tail;
	    this.head = (NodeImplementation)head; 
	}

	/**
	 *  Returns the data associated with this edge.
	 *  Performance: O(1)
	 *
	 *  @return the data associated with this edge
	 */
	public E getData() {
	    return data;
	}

	/**
	 */
	public void setData(E data) {
	    this.data = data;
	}

	/**
	 *  Returns the tail ("from" node) of this edge.
	 *  Performance: O(1)
	 *
	 *  @return the tail
	 */
	public Node<N,E> getTail() {
	    return tail;
	}

	/**
	 */
	public void setTail(Node<N,E> tail) {
	    this.tail = (NodeImplementation)tail;
	}

	/**
	 *  Returns the head ("to" node) of this edge.
	 *  Performance: O(1)
	 *
	 *  @return the head
	 */
	public Node<N,E> getHead() {
	    return head;
	}

	/**
	 */
	public void setHead(Node<N,E> head) {
	    this.head = (NodeImplementation)head;
	}

	/**
	 *  Is this edge equal to that edge (are the head, tail, and
	 *  contents equal to each other)?
	 *  Performance: O(1)
	 *
	 *  @param that : the object to compare to this one
	 *  @return true if the head, tail, and data associated with
	 *          this are equal to the head, tail, and data 
	 *          associated with that
	 */
	public boolean equals(Edge<N,E> that) {
	    return(that.getData() == data);
	}

	/**
	 *  Is this edge equal to that edge (are the head, tail, and
	 *  contents equal to each other)?
	 *  Performance: O(1)
	 *
	 *  @param that : the object to compare to this one
	 *  @return true if that is an Edge and the two edges are equal
	 *          as determined by .equals(Edge<N,E> that)
	 */
	@SuppressWarnings("unchecked")
	public boolean equals(Object that) {
	    return (that instanceof Edge) && this.equals((Edge<N,E>)that);
	}

	public String toString() {
	    return data.toString();
	}

	/**
	 *  Returns a hash code for this edge. This must be defined 
	 *  such that if two edges are equal (as determined by equals)
	 *  then their hash codes are the same.
	 *  Performance: O(1)
	 *
	 *  @return the hash code computed for this object
	 */
	public int hashCode() {
	    return data.hashCode();
	}
    }
}
