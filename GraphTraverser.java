import java.util.*;

public class GraphTraverser<N,E> {
  public boolean depthFirstTraversal(Graph.Node<N,E> start, Graph.Processor<N,E> processor) {
    LinkedList<Graph.Node<N,E>> stack = new LinkedList<Graph.Node<N,E>>();
    stack.push(start);
    Set<Graph.Node<N,E>> visited = new HashSet<Graph.Node<N,E>>();
    while (! stack.isEmpty()) {
      Graph.Node<N,E> node = stack.pop();
      if (visited.contains(node)) continue;
      visited.add(node);
      if (processor.preProcessNode(node)) {
        return true;
      }
      for (Graph.Edge<N,E> edge : node.getOutgoingEdges()) {
        if (processor.processEdge(edge)) {
          return true;
        }
        stack.push(edge.getHead());
      }
      if (processor.postProcessNode(node)) {
        return true;
      }
    }
    return false;
  }

  /**
   *  Performs a breadth-first traversal of a graph starting from the
   *  given node.  As each node or edge is processed the appropriate
   *  method in the processor is invoked.
   *
   *  @param start  the starting node for the traversal.
   *  @param processor the processing class to be applied to each node/edge.
   *  @return true if the processor ever returns true, false otherwise
   */
  public boolean breadthFirstTraversal(Graph.Node<N,E> start, Graph.Processor<N,E> processor) {
    Queue<Graph.Node<N,E>> queue = new LinkedList<Graph.Node<N,E>>();
    Set<Graph.Node<N,E>> visited = new HashSet<Graph.Node<N,E>>();
    queue.add(start);
    while (! queue.isEmpty()) {
      Graph.Node<N,E> node = queue.remove();
      if (visited.contains(node)) continue;
      visited.add(node);
      if (processor.preProcessNode(node)) {
        return true;
      }
      for (Graph.Edge<N,E> edge : node.getOutgoingEdges()) {
        if (processor.processEdge(edge)) {
          return true;
        }
        queue.add(edge.getHead());
      }
      if (processor.postProcessNode(node)) {
        return true;
      }
    }
    return false;
  }
}
