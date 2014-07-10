import java.util.Arrays;
import java.util.Stack;

// Cheriyan-Mehlhorn-Gabow algorithm for strongly connected components (SCC)
// see: Mehlhorn, Kurt, Stefan Näher, and Peter Sanders. "Engineering DFS-based graph algorithms."

public class CMGAlgorithm extends DFSVisitor {

	public int[] run(IGraph graph) {
		super.visitGraph(graph);
		return _components;
	}

	@Override
	protected void init(IGraph graph) {
		_components = new int[graph.numNodes()];
		_open_representatives = new Stack<Integer>();
		_open_nodes = new Stack<Integer>();
		_dfs_position = 1;

		// all nodes are initially unmarked
		Arrays.fill(_components, Integer.MAX_VALUE);
	}

	@Override
	protected void root(int node_id) {
		_open_representatives.push(node_id);
		_open_nodes.push(node_id);
	}

	@Override
	protected void traverseNonTreeEdge(int v, int w) {
		if (_components[w] < 0) {
			while (_components[w] > _components[_open_representatives.peek()]) {
				_open_representatives.pop();
			}
		}
	}

	@Override
	protected void traverseTreeEdge(int v, int w) {
		_open_representatives.push(w);
		_open_nodes.push(w);
	}

	@Override
	protected void backtrack(int u, int v, IGraph graph) {
		if (v == _open_representatives.peek()) {
			_open_representatives.pop();
			int w;
			do {
				w = _open_nodes.pop();
				_components[w] = v;
			} while (w != v);
		}
	}

	@Override
	protected boolean isMarked(int u) {
		return _components[u] != Integer.MAX_VALUE;
	}

	@Override
	protected void mark(int u) {
		_components[u] = -_dfs_position;
		++_dfs_position;
	}

	@Override
	protected void reset() {

	}

	private int _dfs_position;
	private int[] _components;
	private Stack<Integer> _open_representatives;
	private Stack<Integer> _open_nodes;

}
