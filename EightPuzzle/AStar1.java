import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class AStar1 implements SearchAlgorithm{
	/**
	 * 
	 * @param n A Node with the initial state. 
	 * @return A node containing the Goal state, the parent node state, the action taken to get to the current state,
	 * and number of misplaced tiles.  
	 * This Function takes a Node with an String representing the initial state of the eight puzzle.
	 * It uses the number of incorrect tiles + the path cost to find the Goal State.
	 */
	// A*1, h = number of tiles that are not it correct position
	// page 93 says it's identical to uniform cost search
	// except A* uses g + h instead of g
	// g = path-cost
	// h = number of tiles in wrong position
	@Override
	public Node search(Node n) {
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max
		LinkedList<Node> frontier = new LinkedList<Node>(); 
		ArrayList<String> explored = new ArrayList<String>();
		frontier.sort(new Comparator<Node>() {
			@Override
			public int compare(Node a, Node b) {
				return a.heuristic - b.heuristic;
			}
		});
		frontier.add(n);
		Node child;
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop(); /* chooses the node with the lowest heuristic value in frontier */
			time++;
			//If this node has the goal state
			//return it
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
				child = new MoveWithAStar1()
						.newMovement(node, moves.charAt(i));
			
				//If Child is new
				// add it to the frontier.
				if( !explored.contains(child.state) ) { 
					frontier.add(child);
					space++;
				}
				//Calls this function to check if frontier already has this node.
				//and if it does, and the child has a lower heuristic value, then the child will replace
				//the node in the frontier
				EightPuzzle.findElementAndHeuristic(frontier, child);
					
			}
			
		}
		//Solution not found?
		//return failure node
		return new Node("failure", null, null, 0, 0);

	}
}
