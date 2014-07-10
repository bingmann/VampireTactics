import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class GraphHeuristics {

	public int[] bestHeuristic(IGraph input_graph) throws IOException {
		int[] result1 = findLeaves(input_graph);
		int[] result2 = broadestMultiStartBFSLevel(input_graph);
		int[] result3 = greedyPushFromRoots(input_graph);
		if (result1.length > result2.length)
			if(result3.length > result1.length)
				return result3;
			else
				return result1;
		else
			if(result3.length > result2.length)
				return result3;
			else
				return result2;
	}

	/*
	 * After eliminating the cycles, find and return all leaves in the graph
	 */
	public int[] findLeaves(IGraph input_graph) throws IOException {

		CycleDetector cycle_detector = new CycleDetector();
		IGraph dag = cycle_detector.removeCycles(input_graph);
		int[] mapping = cycle_detector.getMapping();

		ArrayList<Integer> resultList = new ArrayList<Integer>();

		// find all leaves
		for (int i = 0; i < dag.numNodes(); ++i) {
			if (dag.nodeDegree(i) == 0)
				resultList.add(i);
		}
		
		// convert and map to int[]
		int[] resultArray = new int[resultList.size()];
		for (int i = 0; i < resultList.size(); ++i) {
			resultArray[i] = mapping[resultList.get(i)];
		}
		


		return resultArray;
	}
	
	/*
	 * Start with roots. For each node v in the current result:
	 * Check if a better solution is possible by selecting children of v instead of v
	 */
	public int[] greedyPushFromRoots(IGraph input_graph) {
		
		CycleDetector cycle_detector = new CycleDetector();
		IGraph dag = cycle_detector.removeCycles(input_graph);
		int[] mapping = cycle_detector.getMapping();
		
		LinkedList<Integer> resultSet = new LinkedList<Integer>();

		// find all roots
		ArrayList<Integer> inDegrees = new ArrayList<Integer>(dag.numNodes());
		ArrayList<Boolean> isMarked = new ArrayList<Boolean>(dag.numNodes());
		
		// for each node, store from which it is initially warned (might be more than 1)
		List<Set<Integer>> isCovered = new ArrayList<Set<Integer>>();

		for (int n = 0; n < dag.numNodes(); ++n) {
			isCovered.add(new HashSet<Integer>());
			isMarked.add(false);
			inDegrees.add(0);
		}
		for (int n = 0; n < dag.numNodes(); ++n) {
			if (dag.nodeDegree(n) > 0) {
				for (int e = dag.firstEdge(n); e < dag.firstInvalidEdge(n); ++e) {
					int t = dag.edgeTarget(e);
					inDegrees.set(t, inDegrees.get(t) + 1);
				}
			}
		}
		
		// compute transitive closure, so that we do not need bfs to get all covered nodes
		IGraph dag_with_closure = new TransitiveClosureAlgorithm()
		.run(dag);
		
		//roots
		for(int n = 0; n < dag.numNodes(); ++n) {
			if(inDegrees.get(n) == 0) {
				resultSet.add(n);
				// covered nodes
				for(int e = dag_with_closure.firstEdge(n); e != dag_with_closure.firstInvalidEdge(n); ++e) {
					isCovered.get(dag_with_closure.edgeTarget(e)).add(n);
				}
			}
		}
		
		// iterate through result set until nothing has changed
		boolean resultHasChanged = true;
		while(resultHasChanged) {
			resultHasChanged = false;
			
		    // apparently it is not possible to iterate over list and add at the end.
		    Set<Integer> toAdd = new TreeSet<Integer>();  
		    // for each node in result set
		    Iterator<Integer> it = resultSet.iterator();
		    while(it.hasNext()){
		    	int n = it.next();
		    	// for each outgoing edge
		    	boolean pushed = false;
		    	for(int e = dag.firstEdge(n); e != dag.firstInvalidEdge(n); ++e) {
		    		// check if target node is only covered by n
		    		int t = dag.edgeTarget(e);
		    		if(isCovered.get(t).size() == 1) {
		    			// check coverage of t
		    			boolean addable = true;
		    			for(int e2 = dag_with_closure.firstEdge(t); e2 != dag_with_closure.firstInvalidEdge(t); ++e2) {
							addable = addable && !toAdd.contains(dag_with_closure.edgeTarget(e2));
						}
		    			if(addable) {
			    			// then push.
			    			toAdd.add(t);
			    			// add coverage of t
			    			for(int e2 = dag_with_closure.firstEdge(t); e2 != dag_with_closure.firstInvalidEdge(t); ++e2) {
								isCovered.get(dag_with_closure.edgeTarget(e2)).add(t);
							}
			    			pushed = true;
		    			}
		    		}
		    	}
		    	if(pushed) {
		    		// remove coverage of n
		    		for(int e = dag_with_closure.firstEdge(n); e != dag_with_closure.firstInvalidEdge(n); ++e) {
		    			isCovered.get(dag_with_closure.edgeTarget(e)).remove(n);
		    		}
		    		// remove current node from result set
		    		it.remove();
		    		resultHasChanged = true;
		    	}
		    }
		    resultSet.addAll(toAdd);
		}
		
		
		
		// convert and map to int[]
		int[] resultArray = new int[resultSet.size()];
		int i = 0;
		for (Integer n : resultSet) {
			resultArray[i++] = mapping[n];
		}

		return resultArray;
	}

	/*
	 * Start a BFS from all roots. Return the level with the most nodes, if it is a valid solution
	 */
	public int[] broadestMultiStartBFSLevel(IGraph input_graph)
			throws IOException {

		CycleDetector cycle_detector = new CycleDetector();
		IGraph dag = cycle_detector.removeCycles(input_graph);
		int[] mapping = cycle_detector.getMapping();

		ArrayList<Integer> resultList = new ArrayList<Integer>();

		// find all roots
		ArrayList<Integer> inDegrees = new ArrayList<Integer>(dag.numNodes());
		ArrayList<Boolean> isMarked = new ArrayList<Boolean>(dag.numNodes());

		for (int n = 0; n < dag.numNodes(); ++n) {
			isMarked.add(false);
			inDegrees.add(0);
		}
		for (int n = 0; n < dag.numNodes(); ++n) {
			if (dag.nodeDegree(n) > 0) {
				for (int e = dag.firstEdge(n); e < dag.firstInvalidEdge(n); ++e) {
					int t = dag.edgeTarget(e);
					inDegrees.set(t, inDegrees.get(t) + 1);
				}
			}
		}

		// use two FIFO, which are interchanged after each level
		LinkedList<Integer> curFIFO, nextFIFO;
		nextFIFO = new LinkedList<Integer>();
		int curMax = 0;

		// add all roots to first level
		for (int r = 0; r < dag.numNodes(); ++r) {
			if (inDegrees.get(r) == 0) {
				// r is a root
				nextFIFO.add(r);
				isMarked.set(r, true);
			}
		}

		while (!nextFIFO.isEmpty()) {

			curFIFO = nextFIFO;

			// check if found level is greater than max level
			if (nextFIFO.size() > curMax) {
				// delete current results
				resultList.clear();
				// copy all elements from next to result
				for (Integer n : nextFIFO) {
					resultList.add(n);
				}
				curMax = nextFIFO.size();
			}
			nextFIFO = new LinkedList<Integer>();

			// bfs
			for (Integer n : curFIFO) {
				for (int e = dag.firstEdge(n); e < dag.firstInvalidEdge(n); ++e) {
					Integer t = dag.edgeTarget(e);
					if (!isMarked.get(t)) {
						nextFIFO.add(t);
						isMarked.set(t, true);
					}
				}
			}
		}

		// convert and map to int[]
		int[] resultArray = new int[resultList.size()];
		for (int i = 0; i < resultList.size(); ++i) {
			resultArray[i] = mapping[resultList.get(i)];
		}

		// solution might not be consistent
		if (isConsistent(resultArray, input_graph))
			return resultArray;
		else
			return new int[0];
	}

	private boolean isConsistent(int[] solution, IGraph graph) {
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
	
	private boolean isConsistent(Integer[] solution, IGraph graph) {
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
