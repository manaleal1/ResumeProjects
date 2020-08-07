import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

class EightPuzzle{
		
		/*
			Goal:   1 2 3 8 0 4 7 6 5
			Easy:   1 3 4 8 6 2 7 0 5
			Medium: 2 8 1 0 4 3 7 6 5
			Hard:   5 6 7 4 0 8 3 2 1

		*/

	static class Node{
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
		private String actions() {
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
	
	static final String goalState = "123804765"; // a String of numbers representing the completed state of the eight puzzle
	
	/**
	 * 
	 * @param currentState A Node that isGoal will check to see if it has the same state as the goal state.
	 * @return True or False
	 * isGoal checks if all the tiles in a node are in the same position as the goal state tiles.
	 * Since both the state of the node and the goalState are Strings, isGoal can check if all tiles are
	 * equal by simply using the .equals method.  
	 */
	public static boolean isGoal(Node currentState) {
		if( currentState.state.equals(goalState) )
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @param s State of a node
	 * @param i Index of zero/The position of the blank tile
	 * @param j Index of a tile/ The position of a non-blank tile
	 * @return A String representing the state after a tile has been moved.
	 * This function switches the tile position of a blank tile with a non blank tile,
	 * which means a tile has been moved. The new state of the puzzle, represented as a String,
	 * is then returned
	 */
	public static String swap(String s, int i, int j) {
		char[] a = s.toCharArray();
		char temp = a[i];
		a[i] = a[j];
		a[j] = temp;
		s="";
		for (int k = 0; k < a.length; k++) {
			s += a[k]; 
		}
		return s;
	}
	
	/**
	 * 
	 * @param n A node the function checks to see if it is in the frontier 
	 * @param frontier a LinkedList representing all nodes to be expanded.
	 * @return true or false
	 * Checks every node in the frontier to see if Node n is in the frontier. 
	 */
	static boolean checkFrontier(Node n, LinkedList<Node> frontier){
		for(Node item : frontier) {
			if(item.state.equals(n.state) )
				return true; 
		}
		return false;
	}
	
	/**
	 * 
	 * @param n A Node with the initial state of the puzzle.
	 * @return a Node containing the goal state, all nodes along the solution path, 
	 * 			the direction the blank tile was moved to get the solution, the path-cost, and the depth
	 * This function expands all nodes at every depth and checks if a node is in the goal state.
	 */
	// Breadth-first → add nodes to the end of the queue
	public static Node breadthFirstSearch(Node n) {
		if (isGoal(n))
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
				if( moves.charAt(i) == 'L' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1), node, "LEFT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost, node.depth+1 );
				else if( moves.charAt(i) == 'R' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1), node, "RIGHT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost, node.depth+1 );
				else if( moves.charAt(i) == 'U' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3), node, "UP", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) ) + node.path_cost, node.depth+1 );
				else//if( moves.charAt(i) == 'D' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3), node, "DOWN", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost, node.depth+1 );
				
				//If the function hasn't seen the current state of the puzzle before
				//Then it will check if the current node is the in the goal state.
				//If it is, the solution is returned
				//otherwise it gets added to the frontier
				if( !explored.contains(child.state) ) {
					if( isGoal(child) ) {
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
	

	/**
	 * 
	 * @param n A Node with the initial state of the puzzle
	 * @return A Node with the solution path or a Node with failure String
	 * This function expands a node until finds the goal state. 
	 * If it encounters a node it has already seen before,
	 * then it will no longer expand that node and will continue expanding all other nodes
	 */
	// Depth-first → add nodes to the front (stack)
	public static Node depthFirstSearch(Node n) {
		if( isGoal(n) )
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
				if( moves.charAt(i) == 'L' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1), node, "LEFT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost, node.depth+1 );
				else if( moves.charAt(i) == 'R' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1), node, "RIGHT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost, node.depth+1);
				else if( moves.charAt(i) == 'U' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3), node, "UP", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) ) + node.path_cost, node.depth+1 );
				else//if( moves.charAt(i) == 'D' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3), node, "DOWN", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost, node.depth+1);
			
				
				//If the new node has a state that hasn't been seen by the function
				//It will check if the node is in the goal state.
				//If it is, the node will be returned
				//otherwise the function adds it to the frontier.
				if( !explored.contains(child.state) || !checkFrontier(child,frontier) ) {
					if( isGoal(child) ) {
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
	
	
	/**
	 * THIS IS FOR UNIFORM COST SEARCH
	 * findElementAndPathCost
	 * checks if the child state is already in the frontier
	 * IF it is THEN 
	 * 		IF child.path-cost < currentNode.pathcost THEN return replace currentNode with child 
	 *  
	 */
	static void findElementAndPathCost (LinkedList<Node> l, Node n) {
		int counter = 0;
		l.sort((Node a,Node b) -> {return a.path_cost - b.path_cost;});
		for(Node currentNode : l) {
			if(currentNode.state.equals(n.state))
				if(currentNode.path_cost > n.path_cost) {
					l.remove(counter);
					l.add(n);
					return;
				}
			counter+=1;
		}
	}
	/**
	 * 
	 * @param l A LinkedList of nodes
	 * @param n A node
	 * This function check if a node in l has the same state as node n. 
	 * If it does, and the  heuristic value of n is less than the value
	 * the node in the list, then it will be replace by n
	 */
	static void findElementAndHeuristic (LinkedList<Node> l, Node n) {
		int counter = 0;
		l.sort((Node a,Node b) -> {return a.heuristic - b.heuristic;});
		for(Node currentNode : l) {
			if(currentNode.state.equals(n.state))
				if(currentNode.heuristic > n.heuristic) {
					l.remove(counter);
					l.add(n);
					return;
				}
			counter+=1;
		}
	}
	
	
	/**
	 * 
	 * @param n A node with the initial state
	 * @return	A Solution node or a failure node
	 * This function looks for goal state using a queue of nodes that are ordered from 
	 * lowest path-cost to greates path cost. 
	 */
	//Uniform cost → sort the nodes on the queue based on the cost of reaching the node from start node
	public static Node UniformCostSearch(Node n) {
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
			if ( isGoal(node) ) {
				System.out.println("Time = " + time);
				System.out.println("Space = " + space);
				return node;
			}
			explored.add(node.state);
			// actions() is called to see which tiles can move based on where
			// the blank tile is and what the current puzzle state is.
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { 
				//A Node as String state, Node parent, String action, int path_cost, int depth
				if( moves.charAt(i) == 'L' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1), node, "LEFT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost, node.depth+1 );
				else if( moves.charAt(i) == 'R' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1), node, "RIGHT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost, node.depth+1 );
				else if( moves.charAt(i) == 'U' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3), node, "UP", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) ) + node.path_cost, node.depth+1 );
				else//if( moves.charAt(i) == 'D' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3), node, "DOWN", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost, node.depth+1 );
			
				//If this node hasn't been seen before 
				//add it to the frontier
				if( !explored.contains(child.state) ) {
					frontier.add(child);
					space++;
				}
				//Calls this function to check if frontier already has this node.
				//and if it does, and the child has a lower path cost, then the child will replace
				//the node in the frontier
				findElementAndPathCost(frontier, child);
					
			}
			
		}
		
		//If no solution was found 
		//return the failure node
		return new Node("failure", null, null, 0, 0);
	} 

	/**
	 * 
	 * @param s A string representing the state of the eight puzzle
	 * @return An Integer representing the number of misplaced tiles.
	 * This Function compares each character in the Goal State to the current state.
	 * The only character not compared is in position 4 because that position is suppose
	 * to hold the blank state. Therefore, at most, there can be eight tiles that are misplaced.
	 */
	static int numberOfWrongTiles(String s) {
		int counter = 0;
		for (int i = 0; i < 4; i++) {
			if( goalState.charAt(i) != s.charAt(i) )
				counter++;
		}
		for (int i = 5; i < 9; i++) {
			if( goalState.charAt(i) != s.charAt(i) )
				counter++;
		}
			
		return counter;
	}
	/**
	 * 
	 * @param n Node with the initial state
	 * @return Node containing either solution or "failure"
	 * This function evaluates a node to see if it should be expanded.
	 * It does this until it finds the solution
	 * 
	 */
	//Best-first, h = number of tiles that are not it correct position
	public static Node BestFirstSearch(Node n) { //see page 99
		int time = 0;  //number of nodes popped off the queue
		int space = 1; //size of the queue at its max
		LinkedList<Node> frontier = new LinkedList<Node>();  //Nodes to be expanded
		ArrayList<String> explored = new ArrayList<String>(); //Nodes that have been seen
		//Sort the frontier so that nodes that have little misplaced tiles
		//appear at the beginning of the list
		frontier.sort(new Comparator<Node>() {
			@Override
			public int compare(Node a, Node b) {
				return a.heuristic - b.heuristic;	
			}
		});
		frontier.add(n); //First node to be expanded 
		Node child;
		while( !frontier.isEmpty() ) {
			Node node = frontier.pop(); /* chooses the lowest-cost node in frontier */
			time++;
			//If this node is in the goal state
			// return it
			if ( isGoal(node) ) {
				System.out.println("Time = " + time);
				System.out.println("Space = " + space);
				return node;
			}
			explored.add(node.state);	
			// actions() is called to see which tiles can move based on where
			// the blank tile is and what the current puzzle state is.
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { 
				//A Node has String state, Node parent, String action, int path-cost, int depth, int h 
				if( moves.charAt(i) == 'L' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1), node, "LEFT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1)) );
				else if( moves.charAt(i) == 'R' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1), node, "RIGHT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1)) );
				else if( moves.charAt(i) == 'U' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3), node, "UP", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3)) );
				else//if( moves.charAt(i) == 'D' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3), node, "DOWN", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3)) );
			
				//If the child node hasn't been seen before
				//add it to the frontier
				if( !explored.contains(child.state) ) { 
					frontier.add(child);
					space++;
				}
				//Calls this function to check if frontier already has this node.
				//and if it does, and the child has a lower heuristic value, then the child will replace
				//the node in the frontier
				findElementAndHeuristic(frontier, child);
					
			}
			
		}
		//Search can't find the solution
		//return failure node
		return new Node("failure", null, null, 0, 0);
	}
	
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
	public static Node AStar1(Node n){
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
			if ( isGoal(node) ) {
				System.out.println("Time = " + time);
				System.out.println("Space = " + space);
				return node;
			}
			explored.add(node.state);
			// actions() is called to see which tiles can move based on where
			// the blank tile is and what the current puzzle state is.
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { //String state, Node parent, String action, int path-cost, int depth, int number of misplaced tiles + previous path-costs + current path cost
				if( moves.charAt(i) == 'L' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1), node, "LEFT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) )+ node.path_cost );
				else if( moves.charAt(i) == 'R' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1), node, "RIGHT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1))+ Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost );
				else if( moves.charAt(i) == 'U' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3), node, "UP", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) )+ node.path_cost );
				else//if( moves.charAt(i) == 'D' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3), node, "DOWN", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost, node.depth+1, numberOfWrongTiles(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost );
			
				//If Child is new
				// add it to the frontier.
				if( !explored.contains(child.state) ) { 
					frontier.add(child);
					space++;
				}
				//Calls this function to check if frontier already has this node.
				//and if it does, and the child has a lower heuristic value, then the child will replace
				//the node in the frontier
				findElementAndHeuristic(frontier, child);
					
			}
			
		}
		//Solution not found?
		//return failure node
		return new Node("failure", null, null, 0, 0);
	}
	/**
	 * 
	 * @param s String representing the eight puzzle.
	 * @return The sum of the Manhattan distances of every tile to its goal position 
	 * Adds up the amount of spaces each tile is from its goal position and return the total
	 */
	static int manhattanDistances(String s) {
		int totalsum = 0; // holds the sum of Manhattan distances of every tile to its goal position
		int position;
		//If tile 1 is not in its goal position
		// its manhattan distance depends on where it is
		/*
		 * remember:
		 * 		  columns
		 * 		   1 2 3
		 * 		   -----
		 * 		1| 0 1 2
		 * rows 2| 3 4 5
		 * 		3| 6 7 8
		 * each number is a position
		 */
		position = s.indexOf("1");
		if(s.indexOf("1") != 0 ) { 	
			if(position == 1 ||position == 3)	//if tile 1 in (row 1, column 2) or (row 2 column 1) 
				totalsum += 1;					//Manhattan Distance for tile 1 is 1.
			else if(position == 2 || position == 4 || position == 6) //if it's in (row 1,column 3),(row 2,column 2), or (row 3, column 1)
				totalsum += 2;					//Manhattan Distance for tile 1 is 2.
			else if(position == 5 || position == 7) //if it's in (row 2,col 3), (row 3, col 2)
				totalsum += 3;					//Manhattan Distance for tile 1 is 3.
			else 						//if tile one in position 8 (row 3, column 3)
				totalsum += 4;					//Manhattan Distance for tile 1 is 4.

		}
		//the function then check tile 2
		//If tile 2 is not in goal position
		// then its manhattan Distance is greater than 0
		position = s.indexOf("2");
		if(position != 1 ) {		
			if(position == 0 || position == 2 || position == 4) //Is tile 2 in position 0, 2 or 4
				totalsum += 1;									//Then Manhattan Distance for tile 2 is 1.
			else if(position == 3 || position == 5 || position == 7) //Is tile 2 in position 3, 5 or 7
				totalsum += 2;										 //Then Manhattan Distance for tile 2 is 2.
			else 					//Is tile 2 in position 6 or 8
				totalsum += 3;		//Then Manhattan Distance for tile 2 is 3.
		}
		//Check tile 3's position
		position = s.indexOf("3"); 
		if(position != 2 ) {
			if(position == 1 || position == 5)
				totalsum += 1;
			else if(position == 0 || position == 4 || position == 8)
				totalsum += 2;
			else if(position == 3 || position == 7)
				totalsum += 3;
			else
				totalsum += 4;
		}
		//Check tile 4's position
		position = s.indexOf("4");
		if(position != 5 ) {
			if(position == 2 || position == 4 || position == 8)
				totalsum += 1;
			else if(position == 1 || position == 3 || position == 7)
				totalsum += 2;
			else
				totalsum += 3;
		}
		//Check tile 5's position
		position = s.indexOf("5"); 
		if(position != 8 ) {
			if(position == 5 || position == 7)
				totalsum += 1;
			else if(position == 2 || position == 4 || position == 6)
				totalsum += 2;
			else if(position == 1 || position == 3)
				totalsum += 3;
			else
				totalsum += 4;
		}
		//Check tile 6's position
		position = s.indexOf("6");
		if(position != 7 ) {
			if(position == 4 || position == 6 || position == 8)
				totalsum += 1;
			else if(position == 1 || position == 3 || position == 5)
				totalsum += 2;
			else
				totalsum += 3;
		}
		//Check tile 7's position
		position = s.indexOf("7");
		if(position != 6 ) {
			if(position == 3 || position == 7)
				totalsum += 1;
			else if(position == 0 || position == 4 || position == 8)
				totalsum += 2;
			else if(position == 1 || position == 5)
				totalsum += 3;
			else
				totalsum += 4;
		}
		//Lastly, Check tile 8's position
		position = s.indexOf("8");
		if(position != 3 ) {
			if(position == 0 || position == 4 || position == 6)
				totalsum += 1;
			else if(position == 1 || position == 5 || position == 7)
				totalsum += 2;
			else
				totalsum += 3;

		}

		return totalsum; //return sum of Manhattan distances
	}
	
	/**
	 * 
	 * @param n Node with initial state of the puzzle
	 * @return Node with solution path
	 * This Search is the same as A*1 except the in case of f(n) = h(n) + g(n)
	 * h = sum of Manhattan Distances between all tiles and their correct positions
	 * g = path cost
	 */
	public static Node AStar2(Node n){
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
			if ( isGoal(node) ) {
				System.out.println("Time = " + time);
				System.out.println("Space = " + space);
				return node;
			}
			explored.add(node.state);
			String moves = node.actions();
			for(int i = 0; i < moves.length(); i++) { 
				//A Node has String state, Node parent, String action, int Manhattan Distance of new state
				if( moves.charAt(i) == 'L' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1), node, "LEFT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost, node.depth+1, manhattanDistances(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-1)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-1)) ) + node.path_cost );
				else if( moves.charAt(i) == 'R' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1), node, "RIGHT", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) ) + node.path_cost, node.depth+1, manhattanDistances(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+1)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+1)) )+ node.path_cost );
				else if( moves.charAt(i) == 'U' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3), node, "UP", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) ) + node.path_cost, node.depth+1, manhattanDistances(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")-3)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")-3)) )+ node.path_cost );
				else//if( moves.charAt(i) == 'D' )
					child = new Node( swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3), node, "DOWN", Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) ) + node.path_cost, node.depth+1, manhattanDistances(swap(node.state, node.state.indexOf("0"),node.state.indexOf("0")+3)) + Integer.parseInt( Character.toString(node.state.charAt(node.state.indexOf("0")+3)) )+ node.path_cost );
			
				//if child node is new
				//add it to the frontier
				if( !explored.contains(child.state) ) {
					frontier.add(child);
					space++;
				}
				//Calls this function to check if frontier already has this node.
				//and if it does, and the child has a lower heuristic value, then the child will replace
				//the node in the frontier
				findElementAndHeuristic(frontier, child);
				
			}
			
		}
		//Solution was not found
		//return failure node
		return new Node("failure", null, null, 0, 0);
	}
	
	/**
	 * 
	 * @param puzzle The puzzle, most likely the node with the solution path
	 * @param message String that will correctly label a value from node
	 * A helper function that prints out the solution path along with some book keeping information 
	 */
	static void printSolution(Node puzzle, String message) {
		if(puzzle != null) {
			printSolution(puzzle.parent, message);
			for (int i=0; i < puzzle.state.length(); i++) {
				System.out.print(puzzle.state.charAt(i));
				if(i%3 == 2) System.out.println();
			}
			
			if(message.equals("Total Path-cost = "))
				System.out.println(puzzle.action+ ", " + message + puzzle.path_cost + ", depth = " + puzzle.depth);
			else if(message.equals("Misplaced tiles = "))
				System.out.println(puzzle.action+ ", " + "Total path-cost = " + puzzle.path_cost + ", " +message + puzzle.heuristic + ", depth = " + puzzle.depth);
			else if(message.equals("f(n) = "))
				System.out.println(puzzle.action+ ", " + "Total path-cost = " + puzzle.path_cost + ", " + message + puzzle.heuristic + ", depth = " + puzzle.depth);
				
			System.out.println();
			
		}
			
	}
	
	/**
	 * The Main Method has the User interface
	 * 
	 */
	public static void main (String[] args) throws IOException{
		
		BufferedReader input = new BufferedReader( new InputStreamReader(System.in) );

		String selection = "";
		System.out.println("Assignment 1: Eight Puzzle");
		System.out.println("This program allows the user to choose a search algorithm to find the solution for 3 different eight puzzles.");
		
		//As long as the user doesn't enter 0, the program will continue
		while( !selection.equals("0") ) {
			System.out.println("Enter a number for an initial state of the puzzle:");
			System.out.println("1 = 134862705 (Easy)");   //misplaced: 5; manhattan: 5
			System.out.println("2 = 281043765 (Medium)"); //misplaced: 6; manhattan: 7
			System.out.println("3 = 567408321 (Hard)");   //misplaced: 8; manhattan: 24
			System.out.println("0 = Exit the program");
			
			String puzzle;
			
			//if the user enters 1, the program will use the easy puzzle
			//if the user enters 2, the program will use the medium puzzle
			//if the user enters 3, the program will use the hard puzzle
			//if the user enters 0, the program will terminate itself
			while( true ) {
				selection = input.readLine();
				switch( selection.trim() ) {
				case "1":
					puzzle ="134862705";
					break;
				case "2":
					puzzle ="281043765";
					break;
				case "3":
					puzzle = "567408321";
					break;
				case "0":
					System.out.println("Good-bye.");
					input.close();
					System.exit(0);
				default:
					System.out.println("Invalid input. Try Again.");
					continue;
				}		
				
				break;
			}

			
			while( true ) {
				System.out.println("Enter a number for the search algorithm you would like the program to use.");
				System.out.println("Note: if the Algorithm does not return anything after 5 minutes, just restart the program.");
				System.out.println("Chances are, the Algorithm can't find the solution.");
				System.out.println("1 = Breadth-First Search"); //hard goes on for more than 5 minutes
				System.out.println("2 = Depth-First Search");  	//medium and hard goes on for more than 5 minutes
				System.out.println("3 = Uniform Cost Search");	//hard goes on for more than 5 minutes
				System.out.println("4 = Best-First Search");	//OK
				System.out.println("5 = A*1 Search");			//hard goes on forever
				System.out.println("6 = A*2 Search");			//hard goes on forever
				System.out.println("0 = Exit the Program");
								
				selection = input.readLine();
				switch( selection.trim() ) {
				case "1":
					System.out.println("The program will now use Breadth-First Search to find the solution to " + puzzle);
					printSolution( breadthFirstSearch( new Node(puzzle,null,null,0,0) ), "Total Path-cost = " );
					break;
				case "2":
					System.out.println("The program will now use Depth-First Search to find the solution to " + puzzle);
					printSolution( depthFirstSearch(new Node(puzzle,null,null,0,0) ), "Total Path-cost = " );
					break;
				case "3":
					System.out.println("The program will now use Uniform Cost Search to find the solution to " + puzzle);
					printSolution( UniformCostSearch( new Node(puzzle,null,null,0,0) ), "Total Path-cost = " );
					break;
				case "4":
					System.out.println("The program will now use Best-First Search to find the solution to " + puzzle);
					System.out.println("For this program, Best-First Search will use h = number of misplaced tiles"); 
					printSolution( BestFirstSearch( new Node(puzzle,null,null,0,0, numberOfWrongTiles(puzzle)) ), "Misplaced tiles = " );
					break;
				case "5":
					System.out.println("The program will now use A*1 Search to find the solution to " + puzzle);
					System.out.println("For this program, A*1 Search will use f(n) = g(n) + h(n)");
					System.out.println("h = number of misplaced tiles");
					System.out.println("g = path-cost");
					printSolution( AStar1( new Node(puzzle,null,null,0,0, numberOfWrongTiles(puzzle)) ), "f(n) = " );
					break;
				case "6":
					System.out.println("The program will now use A*2 Search to find the solution to " + puzzle);
					System.out.println("For this program, A*2 Search will use f(n) = g(n) + h(n)");
					System.out.println("h = sum of Manhattan Distances between all tiles and their correct positions ");
					System.out.println("g = path-cost");
					printSolution( AStar2( new Node(puzzle,null,null,0,0, manhattanDistances(puzzle)) ), "f(n) = " );
					break;
				case "0":
					System.out.println("Good-bye.");
					input.close();
					System.exit(0);
				default:
					System.out.println("Invalid input. Try Again.");
					continue;
				}
			
				break;
			}
			
			
		} //first while
		
		//Stack uses push and pop when using linkedlist		
		//Queue uses add and pop
		
	}//end of main
}//end of class
