import java.util.Random;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/**
 * This is an implementation of the Dots and Boxes game.
 * You play against a computer that chooses its move
 * based on the minimax algorithm.
 *
 */
class DotsAndBoxes {
 
	//allEdges is a list that keeps track of all edges that have been made in the game.
	//boxValues is a list of Integers that holds all values of the boxes in the game.
	static ArrayList<int[]> allEdges = new ArrayList<int[]>();
	static ArrayList<Integer> boxValues = new ArrayList<Integer>(); 
	
	//boxInfoList does bookkeeping:
	//	Keeps track of who owns a box
	//	The value of a box
	//	The vertices of a box.
	static ArrayList<BoxInfo> boxInfoList = new ArrayList<BoxInfo>(); 
	static int numberOfPlies; // The user specifies how many move ahead the computer will consider.
	
	//A box is identified by its 4 edges.
	static class Box{
		int[] edges;
		public Box(int[] edges) {
			this.edges = edges;
		}
	}
	//All boxes have: 
	//  either an owner or no owner.
	//  a random value associated with the box
	//  4 specific edges.
	static class BoxInfo{
		String boxOwner;
		int valueOfBox;
		Box box;
		public BoxInfo(String boxOwner, int valueOfBox, Box box){
			this.boxOwner = boxOwner;
			this.valueOfBox = valueOfBox;
			this.box = box;
		}
	}
	
	/**
	 * terminalTest checks to see if the computer has already considered a certain
	 * number of moves ahead of the game.
	 * @param currentPly Integer representing the number of moves the computer has considered so far.
	 * @return true if the computer has already considered a certain number of moves ahead of the game.
	 * false if otherwise.
	 */
	static boolean terminalTest(int currentPly) {
		if(currentPly == numberOfPlies)
			return true;
		return false;
	}
	
	//
	/**
	 * Looks ahead by a certain amount of moves and returns an action by on which action seem most advantageous. 
	 * @param boardsize number of boxes in a row/column
	 * @return an action represented as an array that contains two integer (two vertices on the board)
	 */
	static int[] minimax(int boardsize) {
		ArrayList<int[]> actions = new ArrayList<int[]>(); //Move that the computer can consider making.
		int boxCounter = 0; //counter for boxes in board
		
		//The possible actions the computer can take
		//The computer can make any move as long as it has not already been made.
		for(int j = 0; j < boardsize*boardsize; j++) {
			for(int i = 0; i < boardsize; i++) {
				if( !isConnected(boxCounter, boxCounter+1,allEdges) )
					actions.add(new int[]{boxCounter, boxCounter+1}) ;
				boxCounter++; 
			}
			if(boxCounter == (boardsize+1)*(boardsize+1)-1)
				break;
			boxCounter = boxCounter - boardsize;
			for (int k = 0; k < boardsize+1; k++) {
				if( !isConnected(boxCounter,boxCounter+boardsize+1,allEdges) )
					actions.add(new int[]{boxCounter,boxCounter+boardsize+1 }) ;
				boxCounter++;
			}	
		}
		
		//if there's only one move to make, then the computer should make that move.
		//the else if statement is for a 1x1 board...if you wanted to play that for some reason.
		//the else if statement forces the computer to make the last move on a 1x1 board.
		if(actions.size() == 1)
			return actions.get(0);
		else if( actions.size() == 0 && boxInfoList.size() == 1 )
			return new int[] {boxCounter,boxCounter+1};
		
		
		//make a copy of bookkeeping list
		ArrayList<BoxInfo> copyOfBoxInfoList = new ArrayList<BoxInfo>();
		for(BoxInfo a: boxInfoList) {
			copyOfBoxInfoList.add(new BoxInfo(a.boxOwner,a.valueOfBox,a.box));
		}
		
		//Call max_value for each action and put the edge and the result of max_value in a list
		ArrayList<Integer> maxResults = new ArrayList<Integer>();
		for(int[] act: actions) {
			maxResults.add( max_value(1,boardsize,act,actions,copyOfBoxInfoList) );
		}
		
		//Iterate through maxResults list to get the index of the greatest max_value found so far.
		//Then when finished iterating, use the index to return an action.  
		int greatestSoFar = 0;
		for(int i = 1; i < maxResults.size(); i++) {
			if( maxResults.get(greatestSoFar) < maxResults.get(i)  )
				greatestSoFar = i;
		}
		return actions.get( greatestSoFar );
	}
	
	/**
	 * max_value returns a utility value that allows the minimax algorithm to see
	 * which action is most advantageous for the computer.
	 * @param ply Number of moves the computer ahead the computer has considered so far.
	 * @param boardsize Integer representing the number of boxes in a row/column
	 * @param state A array of two integers. It is the action that the computer is considering to make. 
	 * @param actions The actions left in the game that the computer can consider taking
	 * @param bookkeeping Info about each box.
	 * @return a utility value that is the difference of the computer score and human score.
	 */
	static int max_value(int ply, int boardsize, int[] state, ArrayList<int[]> actions, ArrayList<BoxInfo> bookkeeping) {
		//check if this move completes a box
		isBoxComplete(state[0], state[1], boardsize, actions, bookkeeping, "c");
		
		//Check if the computer has already considered enough moves ahead in the game.
		if( terminalTest(ply) ) {
			return score("c",bookkeeping) - score("h",bookkeeping);
		}
		//Determine what moves are left (in this case, for the human)
		ArrayList<int[]> actionsCopy = new ArrayList<int[]>();
		for (int[] act : actions) {
			if(act != state)
				actionsCopy.add(new int[] {act[0], act[1]});
		}
		
		//If there are no more moves to make, just return the score.
		if( actionsCopy.size() == 0 ) {
			return score("c",bookkeeping) - score("h",bookkeeping);
		}
		
		//Find the min_value of each action and store the result in a list.
		ArrayList<Integer> minValueResults = new ArrayList<Integer>();
		for (int[] act : actionsCopy) {
			minValueResults.add( min_value(ply+1,boardsize,act,actionsCopy,bookkeeping) );
		}

		//Iterate through maxResults list to get the index of the greatest min_value found so far.
		//Then when finished iterating, use the index to return the greatest min_value.  
		int greatestSoFar = 0; 
		for(int i = 1; i < minValueResults.size(); i++) {
			if( minValueResults.get(greatestSoFar) < minValueResults.get(i)  )
				greatestSoFar = i;
		}
		return minValueResults.get(greatestSoFar);

	}
	
	/**
	 * mix_value returns a utility value that allows the minimax algorithm to determine
	 * which action is most advantageous for the computer.
	 * @param ply Number of moves the computer ahead the computer has considered so far.
	 * @param boardsize Integer representing the number of boxes in a row/column
	 * @param state A array of two integers. It is the action that the computer is considering to make. 
	 * @param actions The actions left in the game that the computer can consider taking
	 * @param bookkeeping Info about each box.
	 * @return a utility value that is the difference of the computer score and human score.
	 */
	static int min_value(int ply, int boardsize, int[] state, ArrayList<int[]> actions, ArrayList<BoxInfo> bookkeeping) {
		isBoxComplete(state[0], state[1], boardsize, actions, bookkeeping, "h");
		
		//Check if the computer has already considered enough moves ahead in the game.
		if( terminalTest(ply) ) {
			return score("c",bookkeeping) - score("h",bookkeeping);
		}
		//Determine what moves are left (in this case, for the computer)
		ArrayList<int[]> actionsCopy = new ArrayList<int[]>();
		for (int[] act : actions) {
			if(act != state)
				actionsCopy.add(new int[] {act[0],act[1]});
		}
		
		//If there are no more moves to make, just return the score.
		if( actionsCopy.size() == 0 ) {
			return score("c",bookkeeping) - score("h",bookkeeping);
		}
		
		//Find the man_value of each action and store the result in a list.
		ArrayList<Integer> maxValueResults = new ArrayList<Integer>();
		for (int[] act : actionsCopy) {
			maxValueResults.add( max_value(ply+1,boardsize,act,actionsCopy,bookkeeping) );
		}

		//iterate through maxResults list to get the index of the smallest max_value found so far.
		//Then when finished iterating, use the index to return the smallest max_value.  
		int smallestSoFar = 0; 
		for(int i = 1; i < maxValueResults.size(); i++) {
			if( maxValueResults.get(smallestSoFar) > maxValueResults.get(i)  )
				smallestSoFar = i;
		}
		return maxValueResults.get(smallestSoFar);

	}
	
	/**
	 * This function updates boxInfoList to identify which player completed the box.
	 * @param player String "h" for human or String "c" for computer 
	 * @param vertex The number/vertex that is used to identify a box
	 */
	static void addOwnerToBookkeeping(String player, int vertex,ArrayList<BoxInfo> bookkeeping){
		for(BoxInfo info: bookkeeping) {
			if(info.box.edges[0] == vertex) {
				info.boxOwner = player;
			}
		}
			
	}
		
	/**
	 * isConnected checks if one vertex is connected to another vertex.
	 * @param a Integer representation of a vertex of an edge 
	 * @param b Integer representation of the other vertex of the same edge.
	 * @return true if the the vertices are paired together in the edgeList. False if otherwise.
	 * 
	 */
	static boolean isConnected(int a, int b, ArrayList<int[]> edgeList){
		for(int[] tuple: edgeList)
			if( (tuple[0] == a && tuple[1] == b) || (tuple[0] == b && tuple[1] == a) )
				return true;
				
		return false;
	}
	
	/**
	 * Checks if the edge placed on the board completed a box
	 * @param a Integer representation of a vertex of an edge
	 * @param b Integer representation of the other vertex of an edge
	 * @param boardsize The number of boxes in a row/column
	 * @param player String that identifies either the human "h" or computer "c"
	 * @return True if edge completes a box or False if edge does not complete a box 
	 */
	static boolean isBoxComplete(int a, int b, int boardsize, ArrayList<int[]> edgeList, ArrayList<BoxInfo> bookkeeping, String player){
		//What kind of Edge was made?
		//if Vertical edge...
		if( Math.abs(a - b) == boardsize+1 ) {
			if( (a % (boardsize+1) == 0) && (b % (boardsize+1) == 0) ) { //Is this a left most column edge?
				//check if this edge completes a box
				if( isConnected(a,b,edgeList) && isConnected(a,a+1,edgeList) && isConnected(b,b+1,edgeList) && isConnected(a+1,b+1,edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a, b),bookkeeping);
					return true;
				}
				return false;
			}
			else if( (a % (boardsize+1) == 3) && (b % (boardsize+1) == 3) ) { //Is this a right most column edge?
				//check if 1 complete box
				if( isConnected(a,b,edgeList) && isConnected(a,a-1,edgeList) && isConnected(b,b-1,edgeList) && isConnected(a-1,b-1,edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a-1, b-1),bookkeeping);
					return true;
				}
				return false;				
			}
			else { //else check for 2 possible boxes: a box made on the left of the edge and a box made on the right
				boolean finishedBox = false;
				if( isConnected(a,b,edgeList) && isConnected(a,a+1,edgeList) && isConnected(b,b+1,edgeList) && isConnected(a+1,b+1,edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a, b),bookkeeping);
					finishedBox = true;
				}
				if( isConnected(a,b,edgeList) && isConnected(a,a-1,edgeList) && isConnected(b,b-1,edgeList) && isConnected(a-1,b-1,edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a-1, b-1),bookkeeping);
					finishedBox = true;
				}
				return finishedBox;	
			}
		}
		else { //else if horizontal edge was made...
			if( a <= boardsize && b <= boardsize ) { //Is it a Top row edge?
				//check for a box
				if( isConnected(a,b,edgeList) && isConnected(a,a+boardsize+1,edgeList) && isConnected(b,b+boardsize+1,edgeList) && isConnected(a+boardsize+1,b+boardsize+1,edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a, b),bookkeeping);
					return true;
				}
				return false;
			}
			else if( ((a < ((boardsize+1)*(boardsize+1))) && (a >= (((boardsize+1)*(boardsize+1))-(boardsize+1)))) 
				  && ((b < ((boardsize+1)*(boardsize+1))) && (b >= (((boardsize+1)*(boardsize+1))-(boardsize+1)))) ) { //Is it a Bottom row edge?
				//check for a box
				if( isConnected(a,b,edgeList) && isConnected(a,a-(boardsize+1),edgeList) && isConnected(b,b-(boardsize+1),edgeList) && isConnected(a-(boardsize+1),b-(boardsize+1),edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a-(boardsize+1), b-(boardsize+1)),bookkeeping);
					return true;
				}
				return false;
			}
			else {//else check 2 boxes: a box made below the edge and a box made above
				boolean finishedBox = false;
				if( isConnected(a,b,edgeList) && isConnected(a,a+boardsize+1,edgeList) && isConnected(b,b+boardsize+1,edgeList) && isConnected(a+boardsize+1,b+boardsize+1,edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a, b),bookkeeping);
					finishedBox = true;
				}
				if( isConnected(a,b,edgeList) && isConnected(a,a-(boardsize+1),edgeList) && isConnected(b,b-(boardsize+1),edgeList) && isConnected(a-(boardsize+1),b-(boardsize+1),edgeList) ) {
					//check who made the mark and add the owner of the box to bookkeeping
					addOwnerToBookkeeping(player, Math.min(a-(boardsize+1), b-(boardsize+1)),bookkeeping);
					finishedBox = true;
				}
				return finishedBox;

			}
		}
		
	}

	/**
	 * 
	 * @param player String "h" identifies human player or "c" for computer player
	 * @param bookkeeping A List that contains information on every box.
	 * @return An integer representing the number of boxes a player owns.
	 */
	static int score(String player, ArrayList<BoxInfo> bookkeeping) {
		int totalScore = 0;
		for(BoxInfo a: bookkeeping)
			if( a.boxOwner.equals(player) )
				totalScore += a.valueOfBox;
		return totalScore;
	}
	
	/**
	 * This function prints the current state of the board.
	 * This includes dots, edges, values of boxes, and owner (if there is) of a box
	 * @param boardsize Number of boxes in a row/column.
	 */
	static void printCurrentBoard(int boardsize) {
		int counter = 0;   // counter to iterate through all vertices in the board
		int boxNumber = 0; // A counter used to get all box Values from boxValues list
	
		while(counter < (boardsize+1) * (boardsize+1) ) {
			for (int i = 0; i < boardsize; i++) {
				if( isConnected(counter, counter+1,allEdges) ) {
					System.out.print("*---");
					counter++;
				}
				else {
					System.out.print("*   ");
					counter++;
				}
			}
			System.out.println("*");
			if(counter == ((boardsize+1)*(boardsize+1))-1 )
				break;
			counter = counter - boardsize;
			
			for (int j = 0; j < boardsize; j++) {
				if( isConnected(counter, counter+(boardsize+1),allEdges) && !boxInfoList.get(boxNumber).boxOwner.isEmpty() ) {
					System.out.print("|"+ boxInfoList.get(boxNumber).boxOwner + boxValues.get(boxNumber) + " ");
					counter++;
					boxNumber++;					
				}
				else if( isConnected(counter, counter+(boardsize+1),allEdges) ) {
					System.out.print("| " + boxValues.get(boxNumber) + " ");
					counter++;
					boxNumber++;
				}
				else {
					System.out.print("  " + boxValues.get(boxNumber) + " ");
					counter++;
					boxNumber++;
				}
			}
			if( isConnected(counter, counter+(boardsize+1),allEdges) ) {
				System.out.print("|");
				counter++;				
			}
			else {
				counter++;
			}

			System.out.println();
		}
		
	}
	
	/**
	 * This function identifies all the boxes in the game by their vertices 
	 * and adds all boxes to bookkeeping (boxInfoList)
	 * @param boardsize
	 * @param boxes
	 */
	static void addBoxesToBoxInfoList(int boardsize, ArrayList<BoxInfo> boxes) {
		int boxCounter = 0; //counter for boxes in board
		int infoCounter = 0; //counter for items in BoxInfoList
		while( infoCounter < (boardsize*boardsize) ) {
			for(int i = 0; i < boardsize; i++) {
				boxes.get(infoCounter).box = new Box(new int[]{boxCounter, boxCounter+1, boxCounter+boardsize+1, boxCounter+1+boardsize+1}) ;
				infoCounter++;
				boxCounter++;
			}
				boxCounter++;
		}
			
	}
	
	
	/**
	 * Tries to make sure the user didn't input incorrect information...it's not perfect
	 * For example, the user can enter still connect a dot at the end of a row
	 * with a dot at the beginning of the next row.
	 * @param v1 Integer representation of a vertex
	 * @param v2 Integer representation of a vertex
	 * @param boardsize Integer representing the number of boxes in a row/column
	 * @return true if nothing is wrong with player input. false if something is wrong with user input.
	 */
	static boolean checkInput(int v1, int v2, int boardsize) {
		// make sure the user can't make an edge that has already been made
		if( isConnected(v1, v2,allEdges) ) {
			System.out.println("That edge has already been made!");
			System.out.println("Try again.");
			return false;
		
		}
		// make sure the user can't make impossible edges.
		if(v1 < 0 || v2 < 0 ) {
			System.out.println("You picked a vertex that doesn't exist!");
			System.out.println("Try Again.");
			return false;
		}
		else if(v1 >= (boardsize+1)*(boardsize+1) || v2 >= (boardsize+1)*(boardsize+1) ) {
			System.out.println("You picked a vertex that doesn't exist!");
			System.out.println("Try Again.");
			return false;
		}
		else if( (v2 != v1+1 && v2 != v1-1) && (v2 != v1+(boardsize+1) && v2 != v1-(boardsize+1)) ) {
			System.out.println("You can't make an edge with those vertices!");
			System.out.println("Try again.");
			return false;
		}
		return true;

	}
	
	/**
	 * printReferenceMatrix prints a matrix of numbers where every number corresponds to a vertex on the board. 
	 * It is helpful when you want to make sure you will connect the vertices you have chosen.
	 * @param boardsize number of boxes in a row/column
	 */
	static void printReferenceMatrix(int boardsize) {
		for (int i = 0; i < boardsize+1; i++) {
			for (int j = 0; j < boardsize+1; j++) {
				System.out.printf("%2d ",j+(i*(boardsize+1)));
			}
			System.out.println();
		}

	}
	
	//User-interface
	public static void main(String[] args) throws IOException {
		BufferedReader input = new BufferedReader( new InputStreamReader(System.in) );
		
		Random number = new Random();
		
		//e = (v + b) - 1; v = (n+1)*(n+1); b = n*n; n = input
		//2x2 board = 12  edges, 9  vertices, 4 boxes  
		//3x3 board = 24 edges, 16 vertices,  9 boxes
		//4x4 board = 40 edges, 25 vertices, 16 boxes		
		
		//Get the ply number and the number of boxes per row/column from the user.
		System.out.println("The game will now begin.");
		System.out.println("How many plies should the AI search?");
		numberOfPlies = Integer.parseInt(input.readLine().trim());
		System.out.println("What do you want the size of the board to be?");
		System.out.println("Enter one number x and the board size will be x by x");
		int boardsize = Integer.parseInt(input.readLine().trim());
		
		//Create random values for each box.
		//Then add each value to bookkeeping list boxInfoList
		for(int i = 0; i < (boardsize)*(boardsize);i++ ) {
			boxValues.add( number.nextInt(5) + 1 );		
			boxInfoList.add( new BoxInfo("", boxValues.get(i), null ) );
		}
		addBoxesToBoxInfoList(boardsize, boxInfoList); //add all box edges to the boxInfoList

		System.out.println("This is the game board.");
		printCurrentBoard(boardsize); //Print the current state of the board
		
		//Numbers/Vertices to refer to when making an edge
		System.out.println("Refer to the following matrix of numbers when creating an edge.");
		System.out.println("The numbers represent the dots in the game.");
		System.out.println("You can only connect dots that are adjacent to each other.");
		System.out.println("Creating an edge: NUMBER [SPACE] NUMBER [ENTER]" );
		System.out.println("Examples: \"0 1\" \"1 "+(boardsize+2)+"\" \"" + (boardsize+2)+ " " +(boardsize+1)  + "\"");
		printReferenceMatrix(boardsize);
		
		System.out.println("Last piece of advice, If the computer is taking too long to make a move");
		System.out.println("restart the program and choose a smaller ply number.");

		String human = "h";		//identification of human player
		String computer = "c";  //Identification of computer player
		String[] vertices;		//vertices to the player wants to connect
		int v1;					//desired vertex
		int v2;					//other desired vertex
		int[] computerEdge;

		//Keep asking for edges to make until there are no more edges to make.
		while(allEdges.size() != (boardsize+1) * (boardsize+1) + (boardsize * boardsize) - 1 ) {
			System.out.println("Your turn:");
			vertices = input.readLine().split(" ");
			if(vertices.length == 1) {
					System.out.println("You only picked one vertex!");
					System.out.println("Try again.");
					continue;
			}
				
			//Convert String input to Integer
			v1 = Integer.parseInt(vertices[0]); 
			v2 = Integer.parseInt(vertices[1]);
			
			//If you enter the wrong input, you get another chance to make an edge.
			if( !checkInput(v1, v2, boardsize) ) {
				continue;
			}
		
			//record the edge made
			//and check if the edge completed a box
			allEdges.add(new int[] {v1, v2});						
			isBoxComplete(v1, v2, boardsize, allEdges, boxInfoList, human);
			
			//print the new State of the board along with the score of both players.
			printCurrentBoard(boardsize);
			System.out.println("human: " + score("h",boxInfoList));
			System.out.println("computer: " + score("c",boxInfoList));
			
			//Here the computer will take a turn.
			
			//add the action to allEdges
			//call isBoxComplete
			System.out.println("Now the Computer will make a move.");		
			
			//The computer will call minimax and an action will be returned
			//The computer's move will be recorded and checked to see if it complete a box
			computerEdge = minimax(boardsize);
			allEdges.add( new int[] {computerEdge[0], computerEdge[1]}  );
			isBoxComplete(computerEdge[0], computerEdge[1], boardsize, allEdges, boxInfoList, computer);
			
			//Print the state of the game after the computer has made a move.
			printCurrentBoard(boardsize);
			System.out.println("human: " + score("h",boxInfoList));
			System.out.println("computer: " + score("c",boxInfoList));
			
			//Print the reference matrix for the human player's turn.
			printReferenceMatrix(boardsize);

		}
		
		//Print the results of the game.
		System.out.println("Game Over!");
		if( score("h",boxInfoList) < score("c",boxInfoList) ) 
			System.out.println("You Lose! :(");
		else if( score("h",boxInfoList) > score("c",boxInfoList) )
			System.out.println("You Win! :)");
		else
			System.out.println("Draw! :|");
		
	}

}
