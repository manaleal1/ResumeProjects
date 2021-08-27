
public class Node {
	String state;	//State of the puzzle
	Node parent;	//Parent of the node
	String action;	//Direction blank tile moved to get to current state
	int path_cost;	//Tile that moved (Tiles 1 through 8)
	int depth;		//Number of movements along solution path
	int heuristic;	//How the node will be evaluated
	public Node (String state, Node parent, String action, int path_cost, int depth ){
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.path_cost = path_cost;
		this.depth = depth;
	}
	//This contructor is for GBF, A*1, A*2
	public Node (String state, Node parent, String action, int path_cost, int depth, int heuristic ){
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.path_cost = path_cost;
		this.depth = depth;
		this.heuristic = heuristic;
	}
	
	/**
	 * 
	 * @return A String where every letter represents the possible moves the blank tile 
	 * 		   can make based on a given state.
	 * First it checks where the blank tile, which i've labeled as "0", is in the puzzle 
	 * Depending on where the blank tile is, the function will return a String of letters that
	 * let the program know which tiles in the puzzle can move.
	 * L = left
	 * R = Right
	 * U = Up
	 * D = Down
	 */
	String actions() {
		int position; 
		position = state.indexOf("0"); 
		if( position % 3 == 0 ) { 		//first column
			if( position == 0 )		 //first row
				return "DR";
			else if( position == 3 ) //second row
				return "UDR";
			else					//third row
				return "UR";		
		}
		else if( position % 3 == 1 ) { 	//second column
			if( position == 1 )		//First row
				return "DRL";
			else if( position == 4 )//Second row
				return "UDRL";
			else					//Third row
				return "URL";
		}
		else							//Third column
			if( position == 2 )		//First row
				return "DL";
			else if( position == 5 )//Second row
				return "UDL";
			else					//Third row
				return "UL";
		
	}

}
