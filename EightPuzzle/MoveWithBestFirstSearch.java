
/*
 * Uses the number of misplaced tiles as a heuristic when creating a new node
 */
public class MoveWithBestFirstSearch implements MoveBehavior  {
	@Override
	public Node newMovement(Node node,char move) {
		int moveValue = 0;
		String direction = "";
		switch(move) {
		case 'L':
			moveValue = MovementValues.LEFT.value();
			direction = MovementValues.LEFT.toString();
			break;
		case 'R':
			moveValue = MovementValues.RIGHT.value();
			direction = MovementValues.RIGHT.toString();
			break;
		case 'U':
			moveValue = MovementValues.UP.value();
			direction = MovementValues.UP.toString();
			break;
		case 'D':
			moveValue = MovementValues.DOWN.value();
			direction = MovementValues.DOWN.toString();
			break;
		default:
			System.out.println("Unexpected char in MoveWithBestFirstSearch: " + move);
			System.exit(0);
		}

		//A Node has: 
		//String state, 
		//Node parent, 
		//String action, 
		//int path-cost, 
		//int depth, 
		//int heuristic number of wrong tiles
		return new Node( 
				EightPuzzle.swap(
						node.state, 
						node.state.indexOf("0"),
						node.state.indexOf("0") + moveValue), 
				node, 
				direction, 
				Integer.parseInt( 
						Character.toString(
								node.state.charAt(node.state.indexOf("0") + moveValue)) ) + node.path_cost, 
				node.depth+1, 
				EightPuzzle.numberOfWrongTiles(
						EightPuzzle.swap(
								node.state, 
								node.state.indexOf("0"),
								node.state.indexOf("0") + moveValue)) );

	}
}
