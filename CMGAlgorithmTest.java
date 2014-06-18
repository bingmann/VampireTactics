/**
 * Diese Datei enthält kleine Testinstanzen für CMGAlgorithm.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

public class CMGAlgorithmTest {

	@Test
	public void findConnectedComponents() throws IOException {
		IGraph graph = new MyGraph("testfiles/sccs_book.txt");
		int[] sccs = new CMGAlgorithm().run(graph);
		assertEquals(graph.numNodes(), sccs.length);
		assertTrue(sccs[0] == sccs[1] && sccs[0] == sccs[2]);
		assertTrue(sccs[3] == sccs[4] && sccs[4] == sccs[7]
				&& sccs[4] == sccs[8] && sccs[4] == sccs[9]
				&& sccs[4] == sccs[10]);
		assertFalse(sccs[0] == sccs[3] || sccs[0] == sccs[5]
				|| sccs[0] == sccs[6]);
		assertFalse(sccs[5] == sccs[6]);

		int[] sccs_sizes = calculateComponentSizes(graph, sccs);
		assertTrue(sccs_sizes[sccs[0]] == 3);
		assertTrue(sccs_sizes[sccs[3]] == 6);
		assertTrue(sccs_sizes[sccs[5]] == 1);
		assertTrue(sccs_sizes[sccs[6]] == 1);
	}

	@Test
	public void detectSingletonComponents() throws IOException {
		IGraph graph = new MyGraph("testfiles/sccs_singleton.txt");
		int[] sccs = new CMGAlgorithm().run(graph);
		assertEquals(graph.numNodes(), sccs.length);

		assertFalse(sccs[0] == sccs[1] || sccs[0] == sccs[2]
				|| sccs[0] == sccs[3] || sccs[0] == sccs[4]
				|| sccs[1] == sccs[2] || sccs[1] == sccs[3]
				|| sccs[1] == sccs[4] || sccs[2] == sccs[3]
				|| sccs[2] == sccs[4] || sccs[3] == sccs[4]);

		int[] sccs_sizes = calculateComponentSizes(graph, sccs);
		assertTrue(sccs_sizes[sccs[0]] == 1);
		assertTrue(sccs_sizes[sccs[1]] == 1);
		assertTrue(sccs_sizes[sccs[2]] == 1);
		assertTrue(sccs_sizes[sccs[3]] == 1);
		assertTrue(sccs_sizes[sccs[4]] == 1);
	}

	@Test
	public void detectGraphAsOneComponent() throws IOException {
		IGraph graph = new MyGraph("testfiles/sccs_one.txt");
		int[] sccs = new CMGAlgorithm().run(graph);
		assertEquals(graph.numNodes(), sccs.length);

		assertTrue(sccs[0] == sccs[1] && sccs[0] == sccs[2]
				&& sccs[1] == sccs[2]);

		int[] sccs_sizes = calculateComponentSizes(graph, sccs);
		assertTrue(sccs_sizes[sccs[0]] == 3);
	}

	@Test
	public void detectEveryNodeAsOneComponent() throws IOException {
		IGraph graph = new MyGraph("testfiles/sccs_none.txt");
		int[] sccs = new CMGAlgorithm().run(graph);
		assertEquals(graph.numNodes(), sccs.length);

		assertFalse(sccs[0] == sccs[1] || sccs[0] == sccs[2]
				|| sccs[1] == sccs[2]);

		int[] sccs_sizes = calculateComponentSizes(graph, sccs);
		assertTrue(sccs_sizes[sccs[0]] == 1);
		assertTrue(sccs_sizes[sccs[1]] == 1);
		assertTrue(sccs_sizes[sccs[2]] == 1);
	}

	@Test
	public void detectSimpleCycle() throws IOException {
		IGraph graph = new MyGraph("testfiles/sccs_simple_cycle.txt");
		int[] sccs = new CMGAlgorithm().run(graph);
		assertEquals(graph.numNodes(), sccs.length);

		assertTrue(sccs[1] == sccs[3] && sccs[1] == sccs[4]);
		assertFalse(sccs[0] == sccs[1] || sccs[1] == sccs[2]
				|| sccs[1] == sccs[2]);
	}

	private int[] calculateComponentSizes(IGraph graph,
			int[] connected_components) {
		int[] component_sizes = new int[graph.numNodes()];
		Arrays.fill(component_sizes, 0);
		for (int i = 0; i < graph.numNodes(); i++) {
			component_sizes[connected_components[i]] += 1;
		}
		return component_sizes;
	}
}
