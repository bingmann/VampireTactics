import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;


public class Solver {
	static class Pair implements Comparable<Pair> {
		public int _u;
		public int _v;

		public Pair(int u, int v) {
			_u = u;
			_v = v;
		}

		@Override
		public int compareTo(Pair arg0) {
			if (_u == arg0._u)
				return _v - arg0._v;
			return _u - arg0._u;
		}
	}

	// Return the DAG with all edges reversed
	static public IGraph reverseDag(IGraph g) {

		ArrayList<Pair> edges = new ArrayList<Pair>(g.numEdges());

		for (int u = 0; u < g.numNodes(); u++) {

			for (int e = g.firstEdge(u); e < g.firstInvalidEdge(u); e++) {
				int v = g.edgeTarget(e);
				edges.add(new Pair(v, u));
			}
		}

		Collections.sort(edges);

		Vertex[] nodeArr = new Vertex[g.numNodes() + 1];
		Edge[] edgeArr = new Edge[g.numEdges()];

		{
			int currV = -1;
			int np = 0, nq = 0;

			for (Pair e : edges) {
				if (e._u != currV) {
					nodeArr[np++] = new Vertex(nq);
					currV = e._u;
				}
				edgeArr[nq++] = new Edge(e._v);
			}
			nodeArr[np] = new Vertex(nq);
		}

		return new MyGraph(nodeArr, edgeArr);
	}

	public int[] solve(IGraph input_graph) throws IOException {

		System.out.println("Running solver");
		return new GraphHeuristics().bestHeuristic(input_graph);
	}

	private void applyPermutation(IGraph dag_with_closure,
			int optimal_num_bites, int[] permutation) {
		for (int i = 0; i < optimal_num_bites; i++) {
			dag_with_closure.markNodeAsBitten(permutation[i]);
		}
	}

	private void applyMapping(int[] mapping, int optimal_num_bites,
			int[] permutation) {
		for (int i = 0; i < optimal_num_bites; i++) {
			permutation[i] = mapping[permutation[i]];
		}
	}

	private boolean checkGraph(IGraph dag, int optimum) {
		// graph with transitive closure
		for (int i = 0; i < dag.numNodes(); i++) {
			if (dag.isMarkedAsBitten(i)) {
				for (int edge = dag.firstEdge(i); edge < dag
						.firstInvalidEdge(i); edge++) {
					dag.markNodeAsWarned(dag.edgeTarget(edge));
				}
			}
		}
		int num_bitten_not_warned = 0;
		for (int i = 0; i < dag.numNodes(); i++) {
			if (dag.isMarkedAsBitten(i) && !dag.isMarkedAsWarned(i)) {
				++num_bitten_not_warned;
			}
			dag.resetMarks(i);
		}
		return num_bitten_not_warned == optimum;
	}

}
