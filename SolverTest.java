/**
 * Diese Datei enthält kleine Testinstanzen für Solver.
 */

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class SolverTest {

	@Test
	public void checkForValids() throws IOException {
		IGraph graph = new MyGraph("testfiles/testcase1.txt");
		int[] solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase2.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase3.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase4.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase5.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase6.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase7.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase8.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));

		graph = new MyGraph("testfiles/testcase9.txt");
		solution = new Solver().solve(graph);
		assertTrue(check(solution, graph));
	}

	private boolean check(int[] solution, IGraph graph) {
		for (int i = 0; i < solution.length; i++) {
			graph.markNodeAsBitten(solution[i]);
			propagateWarnings(solution[i], graph);
		}
		int num_bitten_not_warned = 0;
		for (int i = 0; i < graph.numNodes(); i++) {
			if (graph.isMarkedAsBitten(i) && !graph.isMarkedAsWarned(i)) {
				++num_bitten_not_warned;
			}
		}
		return solution.length == num_bitten_not_warned;
	}

	private void propagateWarnings(int root, IGraph graph) {
		for (int e = graph.firstEdge(root); e < graph.firstInvalidEdge(root); e++) {
			int w = graph.edgeTarget(e);
			if (!graph.isMarkedAsWarned(w)) {
				graph.markNodeAsWarned(w);
				propagateWarnings(w, graph);
			}
		}
	}
}
