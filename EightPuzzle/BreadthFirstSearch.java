import java.util.ArrayList;
import java.util.LinkedList;

public class BreadthFirstSearch implements SearchAlgorithm {
	/**
	 * 
	 * @param n A Node with the initial state of the puzzle.
	 * @return a Node containing the goal state, all nodes along the solution path, 
	 * 			the direction the blank tile was moved to get the solution, the path-cost, and the depth
	 * This function expands all nodes at every depth and checks if a node is in the goal state.
	 */
	// Breadth-first → add nodes to the end of the queue

	@Override
	public Node search(Node n) {
		if (EightPuzzle.isGoal(n))
			return n;
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max		
		LinkedList<Node> frontier = new LinkedList<Node>(); //FIFO queue
		ArrayList<String> explored = new ArrayList<String>();
		frontier.add(n);	//The first node expanded
		Node child;			// Node representing the puzzle state where a tile has moved to new position
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop(); 
			time++;
			// if function has seen the node before, 
			// go through the loop again to expand the next node
			if(explored.contains(node.state)) 
				continue;
			explored.add( node.state ); 
			
			// actions() is called to see which tiles can move based on where
			// the blank tile is and what the current puzzle state is. 
			String moves = node.actions(); 
			for(int i = 0; i < moves.length(); i++) { 
				//A Node has String state, Node parent, String action, int path_cost, int depth
				child = new MoveWithoutHeuristic()
						.newMovement(node,moves.charAt(i));
				
				
				//If the function hasn't seen the current state of the puzzle before
				//Then it will check if the current node is the in the goal state.
				//If it is, the solution is returned
				//otherwise it gets added to the frontier
				if( !explored.contains(child.state) ) {
					if( EightPuzzle.isGoal(child) ) {
						System.out.println("Time = " + time);
						System.out.println("Space = "+ space);
						return child;
						
					}
					else { 
						frontier.add(child);
						space++;
					}
				}
				
			}
		}
		
		//When all nodes have been expanded, the function will return a node
		//containing a String that says failure to indicate that a solution 
		//was not found
		return new Node("failure",null,null,0,0);

		
	}
}
