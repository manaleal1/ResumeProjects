import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
/*
 * Implementation of Breadth First Search, Depth First Search, Uniform Cost Search, 
 * (Greedy) Best First Search, and both A Star Search algorithms
 * based on descriptions given in chapter 3 of Textbook "Artificial Intelligence A Modern Approach, Third Edition"
 * by Stuart Russell and Peter Norvig
 */
public class EightPuzzle{
		
		/*
			Goal:   1 2 3 8 0 4 7 6 5
			Easy:   1 3 4 8 6 2 7 0 5
			Medium: 2 8 1 0 4 3 7 6 5
			Hard:   5 6 7 4 0 8 3 2 1

		*/
	
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
	public static boolean checkFrontier(Node n, LinkedList<Node> frontier){
		for(Node item : frontier) {
			if(item.state.equals(n.state) )
				return true; 
		}
		return false;
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
		l.sort((Node a,Node b) -> {
			return a.heuristic - b.heuristic;
		});
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
					
				SearchAlgorithm sa;
				
				selection = input.readLine();
				switch( selection.trim() ) {
				case "1":
					System.out.println("The program will now use Breadth-First Search to find the solution to " + puzzle);
					sa = new BreadthFirstSearch();
					printSolution( sa.search( new Node(puzzle,null,null,0,0) ), "Total Path-cost = " );
					break;
				case "2":
					System.out.println("The program will now use Depth-First Search to find the solution to " + puzzle);
					sa = new DepthFirstSearch();
					printSolution( sa.search(new Node(puzzle,null,null,0,0) ), "Total Path-cost = " );
					break;
				case "3":
					System.out.println("The program will now use Uniform Cost Search to find the solution to " + puzzle);
					sa = new UniformCostSearch();
					printSolution( sa.search( new Node(puzzle,null,null,0,0) ), "Total Path-cost = " );
					break;
				case "4":
					System.out.println("The program will now use Best-First Search to find the solution to " + puzzle);
					System.out.println("For this program, Best-First Search will use h = number of misplaced tiles"); 
					sa = new BestFirstSearch();
					printSolution( sa.search( new Node(puzzle,null,null,0,0, numberOfWrongTiles(puzzle)) ), "Misplaced tiles = " );
					break;
				case "5":
					System.out.println("The program will now use A*1 Search to find the solution to " + puzzle);
					System.out.println("For this program, A*1 Search will use f(n) = g(n) + h(n)");
					System.out.println("h = number of misplaced tiles");
					System.out.println("g = path-cost");
					sa = new AStar1();
					printSolution( sa.search( new Node(puzzle,null,null,0,0, numberOfWrongTiles(puzzle)) ), "f(n) = " );
					break;
				case "6":
					System.out.println("The program will now use A*2 Search to find the solution to " + puzzle);
					System.out.println("For this program, A*2 Search will use f(n) = g(n) + h(n)");
					System.out.println("h = sum of Manhattan Distances between all tiles and their correct positions ");
					System.out.println("g = path-cost");
					sa = new AStar2();
					printSolution( sa.search( new Node(puzzle,null,null,0,0, manhattanDistances(puzzle)) ), "f(n) = " );
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
				
	}//end of main
}//end of class
