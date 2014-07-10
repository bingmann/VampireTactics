
// DFS framework as described in
// Mehlhorn, Kurt, and Peter Sanders. "Algorithms and data structures." The Basic Toolbox (2008).

public abstract class DFSVisitor {

	public void visitGraph(IGraph graph) {
		init(graph);
		for (int i = 0; i < graph.numNodes(); i++) {
			reset();
			if (!isMarked(i)) {
				mark(i);
				root(i);
				startDFS(i, i, graph);
			}
		}
	}

	private void startDFS(int u, int v, IGraph graph) {
		for (int e = graph.firstEdge(v); e < graph.firstInvalidEdge(v); e++) {
			int w = graph.edgeTarget(e);
			if (isMarked(w)) {
				traverseNonTreeEdge(v, w);
			} else {
				traverseTreeEdge(v, w);
				mark(w);
				startDFS(v, w, graph);
			}
		}
		backtrack(u, v, graph);
	}

	protected abstract void init(IGraph graph);

	protected abstract void reset();

	protected abstract void root(int node_id);

	protected abstract void traverseNonTreeEdge(int v, int w);

	protected abstract void traverseTreeEdge(int v, int w);

	protected abstract void backtrack(int u, int v, IGraph graph);

	protected abstract boolean isMarked(int u);

	protected abstract void mark(int u);
}
