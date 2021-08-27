
/*
 * Doesn't need a heuristic to create a new node
 */
public class MoveWithoutHeuristic implements MoveBehavior{
	@Override
	public Node newMovement(Node node, char move) {
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
			System.out.println("Unexpected char in MoveWithoutHeuristic: " + move);
			System.exit(0);
		}

		//A Node has:
		//String state, 
		//Node parent, 
		//String action, 
		//int path_cost, 
		//int depth
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
				node.depth+1 );

	}
}
