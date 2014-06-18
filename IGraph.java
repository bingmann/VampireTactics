/**
 * Dies ist das Interface, das ihre Adjazenzarray Datenstruktur unterstützen
 * muss, damit die Tests durchlaufen. Die Tests arbeiten ausschließlich auf den
 * durch IGraph.java bereitgestellten Methoden.
 */

public interface IGraph {

	// Liefert die ID der ersten zum Knoten node inzidenten Kante zurück.
	public int firstEdge(int node);

	// Liefert die ID der ersten zum Knoten node nicht mehr inzidenten Kante
	// zurück.
	public int firstInvalidEdge(int node);

	// Liefert die Anzahl der zu Knoten node inzidenten Kanten
	public int nodeDegree(int node);

	// Liefert die Anzahl Knoten im Graphen zurück
	public int numNodes();

	// Liefert die Anzahl Kanten im Graphen zurück
	public int numEdges();

	// Liefert die ID des Endknotens der Kante edge zurück
	public int edgeTarget(int edge);

	// Markiere Knoten mit ID v als gewarnt
	public void markNodeAsWarned(int v);

	// Markiere Knoten mit ID v als gebissen
	public void markNodeAsBitten(int v);

	// Liefert true zurück, wenn Knoten mit ID v gewarnt wurde
	public boolean isMarkedAsWarned(int v);

	// Liefert true zurück, wenn Knoten mit ID v gebissen wurde
	public boolean isMarkedAsBitten(int v);

	// Setzt Markierungen für gebissen & gewarnt zurück
	public void resetMarks(int i);

	// Zwei Graphen sind gleich, wenn ihre Adjazenzlisten-DS gleich ist
	public boolean equals(Object obj);

}