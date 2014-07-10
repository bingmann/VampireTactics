import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

// Detect cycles by finding all strongly connected components (SCCs).
// SCC IDs are then renumbered to correspond to increasing IDs for 
// the nodes that will remain in the graph after cycles are removed.

public class CycleDetector {
	
	private class TempEdge implements Comparable<TempEdge> {
		public final int source;
		public final int target;
		public TempEdge(int s, int d) {
			source = s; target = d;
		}
		
		@Override
		public int compareTo(TempEdge o) {
			if(o.source == source) 
				return Integer.signum(target - o.target);
			else
				return Integer.signum(source - o.source);
		}
		
		@Override
		public String toString() {
			return "(" + source + ", " + target + ")";
		}
		
	}

	/*
	 * Remove cycles.
	 * Find cycles using the CMG Algorithm.
	 * For all nodes with edges into a cycle: add edges to nodes which have incoming edges from the cycle 
	 * All nodes in cycles are erased. 
	 * Then rename all nodes, such that the IDs are consecutive starting with 0
	 * Store a mapping which maps the new IDs to old IDs 
	 */
	public IGraph removeCycles(IGraph graph) {
		
		int[] connected_components = new CMGAlgorithm().run(graph);

		// find sizes of all components:
		// this array will look like this: 1 1 1 4 0 0 0 1 1 1 ...
		int[] component_sizes = new int[graph.numNodes()];
		Arrays.fill(component_sizes, 0);
		for (int i = 0; i < graph.numNodes(); i++) {
			component_sizes[connected_components[i]] += 1;
		}
		
		// find number of nodes in the new graph
		int num_new_nodes = graph.numNodes();
		for (int i = 0; i < graph.numNodes(); i++) {
			if(component_sizes[connected_components[i]] > 1)
				--num_new_nodes;
			else if(hasSelfLoop(i, graph))
				--num_new_nodes;
		}
		// now we can compute two mappings: new_ids maps from old IDs to new IDs
		// _mapping from new IDs to old IDs
		// new IDs will be from 0 to num_new_nodes - 1, circles from num_new_nodes to graph.numNodes() - 1
		int[] new_ids = new int[graph.numNodes()];
		_mapping = new int[num_new_nodes];
		// marker that a nodes has not been already seen
		Arrays.fill(new_ids, -1);
		Arrays.fill(_mapping, -1);
		int node_id = 0;
		int comp_id = num_new_nodes;

		// outgoing edges for each circle
		// use the fact that these get consecutive IDs >= num_new_nodes
		int to_delete = graph.numNodes() - num_new_nodes;
		List<Set<Integer>> outgoing_edges = new ArrayList<Set<Integer> >(to_delete);
		for(int i = 0; i < to_delete; ++i)
			outgoing_edges.add(new HashSet<Integer>());
		
		boolean[] isCircle = new boolean[graph.numNodes()];
		for(int i = 0; i < isCircle.length; ++i)
			isCircle[i] = (component_sizes[connected_components[i]] > 1 || hasSelfLoop(i, graph));
		
		// iterate all nodes and set one of the new IDs, depending if node is part of a circle, or not 
		for (int i = 0; i < graph.numNodes(); i++) {
			if (!isCircle[i]) {
				assert(new_ids[i] == -1);
				new_ids[i] = node_id;
				_mapping[node_id] = i;
				++node_id;
			} else {
				assert(isCircle[i]);
				
				if(new_ids[connected_components[i]] == -1) { // new comp id
					new_ids[i] = comp_id;
					++comp_id;
				} else {
					new_ids[i] = new_ids[connected_components[i]];
				}
				assert(new_ids[i]) >= num_new_nodes;
				for(int e = graph.firstEdge(i); e != graph.firstInvalidEdge(i); ++e) {
					int target = graph.edgeTarget(e);
					// find all outgoing edges, even those to other circles
					if(connected_components[target] != connected_components[i])
						outgoing_edges.get(new_ids[i] - num_new_nodes).add(target); 
						// old ids, because new are not fully known yet
				}
			}
		}

		// transitive network of circles
		computeTransClosureOfCircleNetwork(outgoing_edges, num_new_nodes, graph.numNodes(), new_ids, isCircle);
		// only keep non circle nodes
		
		for(int i = 0; i < outgoing_edges.size(); ++i) {
			for (Iterator<Integer> it = outgoing_edges.get(i).iterator(); it.hasNext();) {
			    if (isCircle[it.next()]) {
			        it.remove();
			    }
			}
		}
		
		Set<TempEdge> temp_edges = new TreeSet<TempEdge>();
		
		// get edges for DAG
		for (int n = 0; n < graph.numNodes(); n++) {
			if(new_ids[n] < num_new_nodes) { // other nodes are not considered
				for(int e = graph.firstEdge(n); e != graph.firstInvalidEdge(n); ++e) {
					int t = new_ids[graph.edgeTarget(e)];
					if(t < num_new_nodes) // valid edge, add to result set
						temp_edges.add(new TempEdge(new_ids[n], t));
					else // edge to a circle -> add outgoing edges of that circle to result set
						for(Integer out : outgoing_edges.get(t - num_new_nodes)) {
							temp_edges.add(new TempEdge(new_ids[n], new_ids[out]));
						}
				}
			}
		}
		
		// create Graph from these edges
		Vertex[] nodes = new Vertex[num_new_nodes + 1];
		Edge[] edges = new Edge[temp_edges.size()];
		int cur_node = 0;
		int cur_edge = 0;
		nodes[0] = new Vertex(cur_edge);
		for(TempEdge e : temp_edges) {
			while(cur_node < e.source) 
				nodes[++cur_node] = new Vertex(cur_edge);
			edges[cur_edge++] = new Edge(e.target);
		}
		cur_node++;
		while(cur_node <= num_new_nodes) // remaining nodes + sentinel 
			nodes[cur_node++] = new Vertex(cur_edge);
		assert(cur_edge == temp_edges.size());
		
		IGraph dag = new MyGraph(nodes, edges);
		
		return dag;
	}

	private void computeTransClosureOfCircleNetwork(
			List<Set<Integer>> outgoing_edges, int num_new_nodes, int num_old_nodes, int[] new_ids, boolean[] isCircle) {
		for(int i = 0; i < outgoing_edges.size(); ++i) {
			Queue<Integer> fifo = new LinkedList<Integer>();
			for(Integer o : outgoing_edges.get(i)) {
				// if o is a circle
				if(isCircle[o]) {
					// do bfs in "circle graph"
					fifo.add(o);
				}
			}
			while(!fifo.isEmpty()) {
				for(Integer o2 : outgoing_edges.get(new_ids[fifo.poll()] - num_new_nodes)) {
					if(isCircle[o2] && !outgoing_edges.get(i).contains(o2)) {
						fifo.add(o2);
					}
					outgoing_edges.get(i).add(o2);
				}
			}
			
		}
	}

	private void printArray(int[] array) {
		for (int i = 0; i < array.length; i++) {
			System.out.print(" " + array[i]);
		}
		System.out.println();
	}
	
	private void printArray(String identifier, int[] array) {
		System.out.print(identifier + " ");
		printArray(array);
	}

	public int[] getMapping() {
		return _mapping;
	}

	private boolean hasSelfLoop(int v, IGraph graph) {
		for (int i = graph.firstEdge(v); i < graph.firstInvalidEdge(v); i++) {
			if (graph.edgeTarget(i) == v) {
				return true;
			}
		}
		return false;
	}

	private int[] _mapping;
}
