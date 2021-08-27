import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class BestFirstSearch implements SearchAlgorithm{
	/**
	 * 
	 * @param n Node with the initial state
	 * @return Node containing either solution or "failure"
	 * This function evaluates a node to see if it should be expanded.
	 * It does this until it finds the solution
	 * 
	 */
	//Best-first, h = number of tiles that are not it correct position
	//see page 99
	@Override
	public Node search(Node n) {
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max
		LinkedList<Node> frontier = new LinkedList<Node>();  //Nodes to be expanded
		ArrayList<String> explored = new ArrayList<String>(); //Nodes that have been seen
		//Sort the frontier so that nodes that have little misplaced tiles
		//appear at the beginning of the list
		frontier.sort((a,b)-> { 
			//lambda function for Comparator
			return a.heuristic - b.heuristic;	
		});
		frontier.add(n); //First node to be expanded 
		Node child;
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop(); /* chooses the lowest-cost node in frontier */
			time++;
			//If this node is in the goal state
			// return it
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
				child = new MoveWithBestFirstSearch()
						.newMovement(node, moves.charAt(i));
			
				//If the child node hasn't been seen before
				//add it to the frontier
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
		//Search can't find the solution
		//return failure node
		return new Node("failure", null, null, 0, 0);

	}
}
