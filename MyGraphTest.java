/**
 * Diese Datei enthält kleine Testinstanzen für MyGraph.
 */

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

public class MyGraphTest {
	@Test
	public void connectedGraphHasCorrectNumberOfNodes() throws IOException {
		assertEquals(4, new MyGraph("testfiles/graph_connected.txt").numNodes());
	}

	@Test
	public void connectedGraphHasCorrectNumberOfEdges() throws IOException {
		assertEquals(3, new MyGraph("testfiles/graph_connected.txt").numEdges());
	}

	@Test
	public void connectedGraphHasCorrectFirstEdgePointers() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		assertEquals(0, graph.firstEdge(0));
		assertEquals(1, graph.firstEdge(1));
		assertEquals(3, graph.firstEdge(2));
		assertEquals(3, graph.firstEdge(3));
	}

	@Test
	public void connectedGraphHasCorrectInvalidEdgePointers()
			throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		assertEquals(1, graph.firstInvalidEdge(0));
		assertEquals(3, graph.firstInvalidEdge(1));
		assertEquals(3, graph.firstInvalidEdge(2));
		assertEquals(3, graph.firstInvalidEdge(3));
	}

	@Test
	public void connectedGraphHasCorrectEdgeArray() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		assertEquals(1, graph.edgeTarget(graph.firstEdge(0)));
		assertEquals(2, graph.edgeTarget(graph.firstEdge(1)));
		assertEquals(3, graph.edgeTarget(graph.firstEdge(1) + 1));

		// Knoten 2 und 3 haben keine ausgehenden Kanten
		for (int edge_id = graph.firstEdge(2); edge_id < graph
				.firstInvalidEdge(2); edge_id++) {
			assertEquals(true, false);
		}
		for (int edge_id = graph.firstEdge(3); edge_id < graph
				.firstInvalidEdge(3); edge_id++) {
			assertEquals(true, false);
		}
	}

	@Test
	public void connectedGraphHasCorrectNodeDegrees() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		assertEquals(1, graph.nodeDegree(0));
		assertEquals(2, graph.nodeDegree(1));
		assertEquals(0, graph.nodeDegree(2));
		assertEquals(0, graph.nodeDegree(3));
	}

	@Test
	public void unconnectedGraphHasCorrectNumberOfNodes() throws IOException {
		assertEquals(6,
				new MyGraph("testfiles/graph_unconnected.txt").numNodes());
	}

	@Test
	public void unconnectedGraphHasCorrectNumberOfEdges() throws IOException {
		assertEquals(3,
				new MyGraph("testfiles/graph_unconnected.txt").numEdges());
	}

	@Test
	public void unconnectedGraphHasCorrectFirstEdgePointers()
			throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_unconnected.txt");
		assertEquals(0, graph.firstEdge(0));
		assertEquals(1, graph.firstEdge(1));
		assertEquals(1, graph.firstEdge(2));
		assertEquals(2, graph.firstEdge(3));
		assertEquals(2, graph.firstEdge(4));
	}

	@Test
	public void unconnectedGraphHasCorrectInvalidEdgePointers()
			throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_unconnected.txt");
		assertEquals(1, graph.firstInvalidEdge(0));
		assertEquals(1, graph.firstInvalidEdge(1));
		assertEquals(2, graph.firstInvalidEdge(2));
		assertEquals(2, graph.firstInvalidEdge(3));
		assertEquals(3, graph.firstInvalidEdge(4));
		assertEquals(3, graph.firstInvalidEdge(5));
	}

	@Test
	public void unconnectedGraphHasCorrectEdgeArray() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_unconnected.txt");
		assertEquals(1, graph.edgeTarget(graph.firstEdge(0)));
		assertEquals(3, graph.edgeTarget(graph.firstEdge(2)));
		assertEquals(5, graph.edgeTarget(graph.firstEdge(4)));

		// Knoten 1,3 und 5 haben keine ausgehenden Kanten
		for (int edge_id = graph.firstEdge(1); edge_id < graph
				.firstInvalidEdge(1); edge_id++) {
			assertEquals(true, false);
		}
		for (int edge_id = graph.firstEdge(3); edge_id < graph
				.firstInvalidEdge(3); edge_id++) {
			assertEquals(true, false);
		}
		for (int edge_id = graph.firstEdge(5); edge_id < graph
				.firstInvalidEdge(5); edge_id++) {
			assertEquals(true, false);
		}
	}

	@Test
	public void unconnectedGraphHasCorrectNodeDegrees() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_unconnected.txt");
		assertEquals(1, graph.nodeDegree(0));
		assertEquals(0, graph.nodeDegree(1));
		assertEquals(1, graph.nodeDegree(2));
		assertEquals(0, graph.nodeDegree(3));
		assertEquals(1, graph.nodeDegree(4));
		assertEquals(0, graph.nodeDegree(5));

	}

	@Test
	public void nodesCanBeMarkedAsBitten() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		graph.markNodeAsBitten(2);
		assertEquals(true, graph.isMarkedAsBitten(2));
	}

	@Test
	public void nodesCanBeMarkedAsWarned() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		graph.markNodeAsWarned(2);
		assertEquals(true, graph.isMarkedAsWarned(2));
	}

	@Test
	public void marksCanBeReset() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		graph.markNodeAsWarned(2);
		graph.markNodeAsBitten(2);
		graph.resetMarks(2);
		assertEquals(false, graph.isMarkedAsBitten(2));
		assertEquals(false, graph.isMarkedAsWarned(2));
	}

	@Test
	public void equalGraphsAreIdentifiedAsSuch() throws IOException {
		IGraph graph1 = new MyGraph("testfiles/graph_connected.txt");
		IGraph graph2 = new MyGraph("testfiles/graph_connected.txt");
		assertEquals(true, graph1.equals(graph2));
	}

	@Test
	public void attributesAreInitiallySetToFalse() throws IOException {
		IGraph graph = new MyGraph("testfiles/graph_connected.txt");
		for (int i = 0; i < graph.numNodes(); i++) {
			assertFalse(graph.isMarkedAsBitten(i));
			assertFalse(graph.isMarkedAsWarned(i));
		}
	}
}
