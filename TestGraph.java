/**
 *  Tests implementation of graph interface.
 *
 *  @author Elizabeth Carney
 *  @version CSC 212, April 26, 2018
 */
public class TestGraph {
    /** Create and test graph. */
    public void execute() {
	// setup
	Graph<Integer, String> g = new GraphImplementation<Integer, String>();
	Graph.Node<Integer, String> node0 = g.addNode(56);
	Graph.Node<Integer, String> node1 = g.addNode(8);
	Graph.Node<Integer, String> node2 = g.addNode(29);
	Graph.Node<Integer, String> node3 = g.addNode(133);
	Graph.Node<Integer, String> node4 = g.addNode(42);
	g.addEdge("0,8", node0, node1);
	g.addEdge("0,29", node0, node2);
	g.addEdge("0,133", node0, node3);
	g.addEdge("1,56", node1, node0);
	g.addEdge("1,29", node1, node2);
	g.addEdge("1,133", node1, node3);
	g.addEdge("1,133", node1, node3);
	g.addEdge("2,133", node2, node3);
	g.addEdge("3,42", node3, node4);
	g.removeEdge(node1, node3);
	g.removeNode(node4);
	// test
	if(g.validateGraph()) {
	    System.out.println(g.toString());
	} else {
	    System.out.println("Graph rejected.");
	}
    }

    public static void main(String[] args) {
	new TestGraph().execute();
    }

    /**
     *  A processor for graph traversals.  There are three methods that may be
     *  called by the traversals.  preProcessNode will always be called prior to
     *  processing the children of a node.  postProcessNode will only be called by
     *  depth-first searches and will be called after processing all of the
     *  children of the node.  processEdge will always be called to process an
     *  edge.  If any of the methods returns true the traversal will stop at that
     *  point and return true, regardless of whether all of the nodes have been
     *  traversed.
     *
     *  @param <N>  the type of data stored at each node
     *  @param <E>  the type of data stored with each edge
     */
    private class ProcessorImplementation<N,E> implements Graph.Processor<N,E> {
	/**
	 *  Called before processing the children of each node.
	 *
	 *  @param node  the node to be processed
	 *  @return true if the traversal should be stopped immediately and true
	 *          returned, false otherwise
	 */
	public boolean preProcessNode(Graph.Node<N,E> node) {
	    return false;
	}

	/**
	 *  Called after processing the children of each node.
	 *
	 *  @param node  the node to be processed
	 *  @return true if the traversal should be stopped immediately and true
	 *          returned, false otherwise
	 */
	public boolean postProcessNode(Graph.Node<N,E> node) {
	    return false;
	}

	/**
	 *  Called to process each edge.
	 *
	 *  @param edge  the edge to be processed
	 *  @return true if the traversal should be stopped immediately and true
	 *          returned, false otherwise
	 */
	public boolean processEdge(Graph.Edge<N,E> edge) {
	    return false;
	}
    }
}
