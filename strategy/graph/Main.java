import java.util.ArrayList;
import java.util.List;


class Graph {

	public static class Node {
		private int value;
		private List<Node> adjNodes; 
		
		public Node(int value) {
			this.value = value;
			this.adjNodes = new ArrayList<Node>();
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
		
		public void addAdjNode(Node node) {
			adjNodes.add(node);
		}
		
		public void removeAdjNode(Node node) {
			adjNodes.remove(node);
		}
	}
	
	private List<Node> nodes;
	private NodePrinter nodePrinter;
	
	public Graph(NodePrinter nodePrinter) {
		this.nodePrinter = nodePrinter;
		this.nodes = new ArrayList<Node>();
	}
	
	public void addVertex(Node node) {
		nodes.add(node);
	}
	
	public void addEdge(Node from, Node to) {
		if (!nodes.contains(from)) {
			nodes.add(from);
		}
		
		if (!nodes.contains(to)) {
			nodes.add(to);
		}
		
		from.addAdjNode(to);
	}
	
	public void print() {
		for (Node node: nodes) {
			System.out.print("\n" + node.value + ": ");
			for (Node adj: node.adjNodes) {
				nodePrinter.print(adj);
			}
		}
	}
}

interface NodePrinter {
	void print(Graph.Node node);
}


class SimpleNodePrinter implements NodePrinter {
	@Override
	public void print(Graph.Node node) {
		System.out.print(" " + node.getValue() + " ");
	}
}

class NiceNodePrinter implements NodePrinter {
	@Override
	public void print(Graph.Node node) {
		System.out.print("-(" + node.getValue() + ")-");
	}
}


public class Main {

	public static void main(String[] args) {
		
		Graph g1 = new Graph(new SimpleNodePrinter());
		Graph g2 = new Graph(new NiceNodePrinter());
		
		Graph.Node nodes[] = new Graph.Node[] { new Graph.Node(10), new Graph.Node(20), new Graph.Node(30) };
		for (Graph.Node node: nodes) {
			g1.addVertex(node);
			g2.addVertex(node);
		}
		
		g1.addEdge(nodes[0], nodes[1]);
		g1.addEdge(nodes[0], nodes[2]);
		
		g2.addEdge(nodes[1], nodes[2]);
		g2.addEdge(nodes[1], nodes[0]);
		
		g1.print();
		System.out.println();
		g2.print();
	}
}
