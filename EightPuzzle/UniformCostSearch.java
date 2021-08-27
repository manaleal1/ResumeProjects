
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class UniformCostSearch implements SearchAlgorithm{
	/**
	 * 
	 * @param n A node with the initial state
	 * @return	A Solution node or a failure node
	 * This function looks for goal state using a queue of nodes that are ordered from 
	 * lowest path-cost to greates path cost. 
	 */
	//Uniform cost → sort the nodes on the queue based on the cost of reaching the node from start node
	@Override
	public Node search(Node n) {
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max
		LinkedList<Node> frontier = new LinkedList<Node>(); //Nodes to be expanded
		ArrayList<String> explored = new ArrayList<String>(); //Nodes that have been seen before
		frontier.sort(new Comparator<Node>() {
			@Override
			public int compare(Node a, Node b) {
				return a.path_cost - b.path_cost;	//frontier is sorted from lowest path cost to highest path cost
			}
		});
		frontier.add(n); //initial node to be expanded
		Node child;
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop(); /* chooses the lowest-cost node in frontier */
			time++;
			//If the current node is in the goal state
			//Then it will be returned
			if ( EightPuzzle.isGoal(node) ) {
				System.out.println("Time = " + time);
				System.out.println("Space = " + space);
				return node;
			}
			explored.add(node.state);
			// actions() is called to see which tiles can move based on where
			// the blank tile is and what the current puzzle state is.
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { 
				child = new MoveWithoutHeuristic()
						.newMovement(node, moves.charAt(i));
				
				//If this node hasn't been seen before 
				//add it to the frontier
				if( !explored.contains(child.state) ) {
					frontier.add(child);
					space++;
				}
				//Calls this function to check if frontier already has this node.
				//and if it does, and the child has a lower path cost, then the child will replace
				//the node in the frontier
				EightPuzzle.findElementAndPathCost(frontier, child);
					
			}
			
		}
		
		//If no solution was found 
		//return the failure node
		return new Node("failure", null, null, 0, 0);

		
	}
}
