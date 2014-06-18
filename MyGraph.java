/*
 * Implementieren Sie in dieser Datei die Adjazenzarray-Datenstruktur.
 * Verwenden Sie die Klassen Vertex bzw. Edge, um Knoten bzw. Kanten 
 * zu repräsentieren.
 */

import java.io.IOException;
import java.util.Arrays;

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

/**
 * Diese Klasse soll den Graphen mit Hilfe eines Adjazenz-Arrays darstellen.
 */
public class MyGraph implements IGraph {

	private Vertex[] _nodes;
	private Edge[] _edges;
	private int _num_nodes;
	private int _num_edges;

	public MyGraph(String filename) throws IOException {
		// TODO: Implementierung
	}

	public MyGraph(Vertex[] nodes, Edge[] edges) {
		// TODO: Implementierung
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
		// TODO: Implementierung
		return 0;
	}

	@Override
	public int firstInvalidEdge(int node) {
		// TODO: Implementierung
		return 0;
	}

	@Override
	public int nodeDegree(int node) {
		// TODO: Implementierung
		return 0;
	}

	@Override
	public int numNodes() {
		// TODO: Implementierung
		return 0;
	}

	@Override
	public int numEdges() {
		// TODO: Implementierung
		return 0;
	}

	@Override
	public int edgeTarget(int edge) {
		// TODO: Implementierung
		return 0;
	}

	@Override
	public void markNodeAsBitten(int v) {
		// TODO: Implementierung
	}

	@Override
	public boolean isMarkedAsBitten(int v) {
		// TODO: Implementierung
		return false;
	}

	@Override
	public void markNodeAsWarned(int v) {
		// TODO: Implementierung
	}

	@Override
	public boolean isMarkedAsWarned(int v) {
		// TODO: Implementierung
		return false;
	}

	public void resetMarks(int v) {
		// TODO: Implementierung
	}

}
