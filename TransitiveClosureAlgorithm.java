import java.util.ArrayList;
import java.util.Arrays;

// Calculate transitive closure during DFS

public class TransitiveClosureAlgorithm extends DFSVisitor {

	public IGraph run(IGraph graph) {
		super.visitGraph(graph);

		// sentinels
		_nodes[graph.numNodes()] = new Vertex(_current_edge_id);

		Edge[] hint = new Edge[1];
		IGraph new_graph = new MyGraph(_nodes, _edges.toArray(hint));
		// System.out.println("graph with transitive closure:");
		// System.out.println(new_graph.toString());
		return new_graph;
	}

	@Override
	protected void init(IGraph graph) {
		_nodes = new Vertex[graph.numNodes() + 1];
		_edges = new ArrayList<Edge>();
		_marks = new boolean[graph.numNodes()];
		Arrays.fill(_marks, false);
		_current_edge_id = 0;
	}

	@Override
	protected void root(int node_id) {
		_nodes[node_id] = new Vertex(_current_edge_id);
	}

	@Override
	protected void traverseNonTreeEdge(int v, int w) {
		// account for self-loops
		if (v == w) {
			_edges.add(new Edge(w));
			++_current_edge_id;
		}
	}

	@Override
	protected void traverseTreeEdge(int v, int w) {
		_edges.add(new Edge(w));
		++_current_edge_id;
	}

	@Override
	protected void backtrack(int u, int v, IGraph graph) {
		// do nothing
	}

	@Override
	protected boolean isMarked(int u) {
		return _marks[u] == true;
	}

	@Override
	protected void mark(int u) {
		_marks[u] = true;
	}

	@Override
	protected void reset() {
		// ugly temporary workaround
		Arrays.fill(_marks, false);
	}

	private boolean[] _marks;
	private Vertex[] _nodes;
	private ArrayList<Edge> _edges;
	private int _current_edge_id;
}
