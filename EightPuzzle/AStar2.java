import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;

public class AStar2 implements SearchAlgorithm{
	/**
	 * 
	 * @param n Node with initial state of the puzzle
	 * @return Node with solution path
	 * This Search is the same as A*1 except the in case of f(n) = h(n) + g(n)
	 * h = sum of Manhattan Distances between all tiles and their correct positions
	 * g = path cost
	 */
	@Override
	public Node search(Node n) {
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max
		LinkedList<Node> frontier = new LinkedList<Node>(); 
		ArrayList<String> explored = new ArrayList<String>();
		frontier.sort(new Comparator<Node>() {
			@Override
			public int compare(Node a, Node b) {
				return a.heuristic - b.heuristic; // ordered from lowest sum to highest sum of Manhattan Distances
			}
		});
		frontier.add(n);
		Node child;
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop(); /* chooses the lowest-cost node in frontier */
			time++;
			if ( EightPuzzle.isGoal(node) ) {
				System.out.println("Time = " + time);
				System.out.println("Space = " + space);
				return node;
			}
			explored.add(node.state);
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { 
				child = new MoveWithAStar2()
						.newMovement(node, moves.charAt(i));
							
				//if child node is new
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
		//Solution was not found
		//return failure node
		return new Node("failure", null, null, 0, 0);

	}
}
