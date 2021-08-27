import java.util.ArrayList;
import java.util.LinkedList;

public class DepthFirstSearch implements SearchAlgorithm {
	/**
	 * 
	 * @param n A Node with the initial state of the puzzle
	 * @return A Node with the solution path or a Node with failure String
	 * This function expands a node until finds the goal state. 
	 * If it encounters a node it has already seen before,
	 * then it will no longer expand that node and will continue expanding all other nodes
	 */
	// Depth-first → add nodes to the front (stack)
	@Override
	public Node search(Node n) {
		if( EightPuzzle.isGoal(n) )
			return n;
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max
		LinkedList<Node> frontier = new LinkedList<Node>(); //FILO Queue
		ArrayList<String> explored = new ArrayList<String>(); //All states the function has encountered
		frontier.push(n);	//the initial state Node will be expanded
		Node child;
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop();
			time++;
			//If the current node has been seen before
			//The function will repeat the loop with a new node from the frontier
			if(explored.contains(node.state))
				continue;
			explored.add( node.state ); 
			
			// actions() is called to see which tiles can move based on where
			// the blank tile is and what the current puzzle state is.
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { 
				//A Node has String state, Node parent, String action, int path_cost
				child = new MoveWithoutHeuristic()
						.newMovement(node, moves.charAt(i));
				
				//If the new node has a state that hasn't been seen by the function
				//It will check if the node is in the goal state.
				//If it is, the node will be returned
				//otherwise the function adds it to the frontier.
				if( !explored.contains(child.state) || !EightPuzzle.checkFrontier(child,frontier) ) {
					if( EightPuzzle.isGoal(child) ) {
						System.out.println("Time = " + time);
						System.out.println("Space = " + space);
						return child;	
					}
					else {
						frontier.push(child);
						space++;
					}
				}	
			}
			
		}
		
		//When the function no longer has nodes to expand
		//it will return a failure node
		//Meaning a solution was not found.
		return new Node("failure", null, null, 0, 0);
	
		
	}
}
