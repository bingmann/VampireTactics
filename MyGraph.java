import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Dies ist eine Klasse zur Repräsentation von Knoten im Knotenarray der
 * Adjazenzarray Datenstruktur. Sie können dieses Klasse falls notwendig
 * erweitern. Die Tests arbeiten ausschließlich auf den durch IGraph.java
 * bereitgestellten Methoden.
 */
class Vertex {
	private int _first_edge;

	public Vertex(int firstEdge) {
		setFirstEdge(firstEdge);
	}

	// Liefert den Index der ersten, zum Knoten gehörenden Kante im Kantenarray
	// zurück
	public int firstEdge() {
		return _first_edge;
	}

	// Setzt den Index der ersten, zum Knoten gehörenden Kante im Kantenarray,
	// auf firstEdge
	public void setFirstEdge(int _firstEdge) {
		this._first_edge = _firstEdge;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Vertex))
			return false;
		Vertex other = (Vertex) obj;
		if (_first_edge != other._first_edge)
			return false;
		return true;
	}
}

/**
 * Dies ist eine Klasse zur Repräsentation von Kanten im Kantenarray der
 * Adjazenzarray Datenstruktur. Sie können dieses Interface falls notwendig
 * erweitern. Die Tests arbeiten ausschließlich auf den durch IGraph.java
 * bereitgestellten Methoden.
 */
class Edge {
	private int _target;

	public Edge(int target) {
		_target = target;
	}

	// Liefert den Endknoten der Kante zurück
	public int target() {
		return _target;
	}

	// Setzt den Endknoten der Kante auf target
	public void setTarget(int target) {
		_target = target;
	}

	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof Edge))
			return false;
		Edge other = (Edge) obj;
		if (_target != other._target)
			return false;
		return true;
	}
}

public class MyGraph implements IGraph {

	private Vertex[] _nodes;
	private Edge[] _edges;
	private boolean[] _warned;
	private boolean[] _bitten;
	private int _num_nodes;
	private int _num_edges;

	public MyGraph(String filename) throws IOException {
		BufferedReader buf = new BufferedReader(new FileReader(filename));

		// skip comments
		String line = buf.readLine();
		while (line.startsWith("#")) {
			line = buf.readLine();
		}
		Scanner scanner = new Scanner(line);

		_num_nodes = scanner.nextInt();
		_num_edges = scanner.nextInt();

		scanner.close();

		_nodes = new Vertex[_num_nodes + 1];
		_edges = new Edge[_num_edges];
		int current_edge_id = 0;
		int current_node_id = -1;
		for (int i = 0; i < _num_nodes; i++) {
			++current_node_id;
			_nodes[current_node_id] = new Vertex(current_edge_id);
			if ((line = buf.readLine()) != null) {
				Scanner edge_scanner = new Scanner(line);
				while (edge_scanner.hasNextInt()) {
					_edges[current_edge_id] = new Edge(
							edge_scanner.nextInt() - 1);
					++current_edge_id;
				}
				edge_scanner.close();
			}
		}
		buf.close();
		// add sentinel vertex
		_nodes[_num_nodes] = new Vertex(current_edge_id);

		initializeArrays();
	}

	public MyGraph(Vertex[] nodes, Edge[] edges) {
		setNodeArray(nodes);
		setEdgeArray(edges);
	}

	public String toString() {
		String graph_string = new String("#nodes=" + _num_nodes + "\n#edges="
				+ _num_edges + "\n");
		for (int i = 0; i < _num_nodes; ++i) {
			Vertex node = _nodes[i];
			graph_string = graph_string.concat("Node: " + i + "\n");
			for (int j = node.firstEdge(); j < _nodes[i + 1].firstEdge(); ++j) {
				graph_string = graph_string.concat(_edges[j].target() + " ");
			}
			graph_string = graph_string.concat("\n");
		}
		return graph_string;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (!(obj instanceof MyGraph))
			return false;
		MyGraph other = (MyGraph) obj;
		if (_num_nodes != other._num_nodes || _num_edges != other._num_edges
				|| !Arrays.equals(_edges, other._edges)
				|| !Arrays.equals(_nodes, other._nodes)) {
			return false;
		}
		return true;
	}

	@Override
	public int firstEdge(int node) {
		return _nodes[node].firstEdge();
	}

	@Override
	public int firstInvalidEdge(int node) {
		return _nodes[node + 1].firstEdge();
	}

	@Override
	public int nodeDegree(int node) {
		return _nodes[node + 1].firstEdge() - _nodes[node].firstEdge();
	}

	@Override
	public int numNodes() {
		return _num_nodes;
	}

	@Override
	public int numEdges() {
		return _num_edges;
	}

	@Override
	public int edgeTarget(int edge) {
		return _edges[edge].target();
	}

	public void setNodeArray(Vertex[] nodes) {
		_nodes = nodes;
		// subtract sentinel
		_num_nodes = nodes.length - 1;
		initializeArrays();
	}

	public void setEdgeArray(Edge[] edges) {
		_edges = edges;
		_num_edges = edges.length;
	}

	@Override
	public void markNodeAsBitten(int v) {
		_bitten[v] = true;
	}

	@Override
	public boolean isMarkedAsBitten(int v) {
		return _bitten[v];
	}

	@Override
	public void markNodeAsWarned(int v) {
		_warned[v] = true;
	}

	@Override
	public boolean isMarkedAsWarned(int v) {
		return _warned[v];
	}

	public void resetMarks(int v) {
		_warned[v] = false;
		_bitten[v] = false;
	}

	private void initializeArrays() {
		_warned = new boolean[_num_nodes];
		_bitten = new boolean[_num_nodes];
		Arrays.fill(_warned, false);
		Arrays.fill(_bitten, false);
	}

}
