/**
*
* @author Paul Bosonetto
* @version September 16, 2017
* 
* The Minesweeper class consists of an 8x8 array of Cell objects.
* <p>
* Each Cell has a String representation of either a "M", "-", or number.
* M indicating that it contains a mine, - indicating that no guess has been 
* made, or a number indicating the number of adjacent mines.
* <p>
* The class has no toString() method. Calls to print the board are made instead <br>
* to displayBoard() or displayPeek()
*/
import java.util.Random;
import java.util.Scanner;

public class Minesweeper{

	private Cell[][] printArray = new Cell[8][8];
	private Cell[][] peekArray = new Cell[8][8];
	//private Cell[][] mineArray = new Cell[10][10];
	private int mineCount;


	
	/**
	* Class constructor that takes no parameters. <p>
	* The first three methods called set up the board and the peek board. <p>
	* The fourth method handles the gameplay, user prompts, and input. 
	*/
	public Minesweeper(){
	
		initBoard();
		initPeek();
		setMines();
		getGuess();
		
	
	
	}
	
	/**
	* Creates an 8x8 board of Cell objects
	*/
	public void initBoard(){
	
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++){
				Cell c = new Cell();
				printArray[i][j] = c;
				//mineArray[i][j] = c;
				//peekArray[i][j] = c;	
			}
					
	}
	
	/**
	* Creates an 8x8 board of Cell objects for our 'peek' board
	*/
	public void initPeek(){
	
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++){
				Cell d = new Cell();
				peekArray[i][j] = d;	
			}				
	}


	/**
	* Uses java.util.Random to randomly place 10 mines on the board
	*/
	public void setMines(){
	
		Random random = new Random();
	
		int count = 0;
		while (count < 10){
			int x = random.nextInt(8);
			int y = random.nextInt(8);
						
			if (peekArray[x][y].getRep() == "M")
				continue;
				
			else {
			
				peekArray[x][y].setRep("M");
				count++;
				
			}
		}
		mineCount = 10;
	
	}
	
private class Cell{

	String rep;
	
	
	/**
	* Class constructor that takes no parameters <p> 
	* Initializes representation to "-" or unguessed
	*/
	public Cell(){
	
		this.rep = "-";
	
	}
	
	/**
	*
	* @Override 
	* @return String with current Cell representation
	*/
	public String toString(){
	
		return this.rep;
	
	
	}
	
	/**
	* Identical to the toString method but still useful because the name is a bit more intuitive for some purposes.
	* @return A String with current Cell representation.
	*/
	public String getRep(){
	
		return this.rep;
	
	}
	
	/**
	* accessor method to set representation
	* @param rep String representation of Cell contents: "-", "M", or "X", X = numAdjMines
	*/
	public void setRep(String rep){
	
		this.rep = rep;
		
		
	}
}
	
	
	/**
	* This method handles the gameplay, user input, and calls methods to check guesses and find #adjacent mines
	*<p>
	* The while loop keeps the game going until variable ok = false (meaning user guessed wrong)
	* or until mineCount = 0, which means the user has guessed all the mine locations correctly
	*<p>
	* A series of if, else if statements check the user input to 1) make sure it is formatted correctly
	* 2) process it into either a row, column, answer guess OR a 'peek' guess
	* <p>
	* Outside the while loop, if / else, if statements handle winning and losing and setting up new games
	*/
	public void getGuess(){
	
		Scanner scan = new Scanner(System.in);
		boolean ok = true;
		
		while (ok == true && mineCount > 0){
			
			System.out.println();
			displayBoard();
			System.out.println();
		
			System.out.println("------------------------------");
			System.out.print("If you want to peek at board, enter 'peek' (without quotes).\n");
			System.out.print("Otherwise, enter a guess ");
			System.out.print("x y m, where x = row number,\ny = column number, ");
			System.out.print("m = 'yes' if you think there is a mine,\n'no' if not. ");
			System.out.print("x and y should be 0 <= x,y <= 7\n\n");
			System.out.println("------------------------------");
		
			String s = scan.nextLine();
			String[] args = s.split(" ");
			
			System.out.println("------------------------------");
			System.out.println();
		
			if (args.length == 1 && args[0].equals("peek"))
				displayPeek();
			
			else if (args.length == 3){
		
				int x = Integer.parseInt(args[0]);
				int y = Integer.parseInt(args[1]);
				String ans = args[2];
				
				if (x < 0 || x > 7 || y < 0 || y >7){
				
					System.out.print("!!! Your line and column numbers must be ");
					System.out.print("between 0 and 7 (inclusive) !!!\n");
					System.out.println("Do not dissapoint me again.");
					continue;
					
				}
				
				ok = checkGuess(x, y, ans);
			}
		
			else {
		
				System.out.print("\n***Please read instructions carefully and try again.\n");
				System.out.println("You should either be entering 1 argument or 3.***");
			
			}
			
		}
		
		if (mineCount == 0){
		
			
			System.out.print("Yay! You won! Congrats! Should've put some money ");
			System.out.println("on this game! Do you want to play again? y or n");	
			String s = scan.next();
			
			if (s.equals("y") || s.equals("yes")){
			
				initBoard();
				initPeek();
				setMines();
				getGuess();
				
		
			}
		}
		
		else {
			System.out.print("You lose! Loser! Play again? y or n: ");
			String s = scan.next();
		
			if (s.equals("y") || s.equals("yes")){
				
				initBoard();
				initPeek();
				setMines();
				getGuess();
		
			}
		}
					
	}
	
	/**
	* This method finds the number of adjacent mines to a particular Cell location.
	* It breaks up the Cell locations into: corner; left,right, upper, or bottom edge; or middle
	* <p>
	* @param x		row number
	* @param y		column number
	* @return		the number of adjacent mines
	*/
	public int findNumAdjacentMines(int x, int y){
		
		//handle the case where Cell is in middle, i.e. not an edge or corner
		if (!isLeftEdge(x,y) && !isRightEdge(x,y) && !isUpperEdge(x,y) && !isBottomEdge(x, y)
			&& isCorner(x,y).equals("notCorner")){
			
			int count = 0;
			
			if (peekArray[x+1][y].getRep().equals("M"))
				count++;
				
			if (peekArray[x-1][y].getRep().equals("M"))
				count++;
				
			if (peekArray[x][y-1].getRep().equals("M"))
				count++;
				
			if (peekArray[x][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x-1][y-1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y-1].getRep().equals("M"))
				count++;
			
			if (peekArray[x-1][y+1].getRep().equals("M"))
				count++;
				
					
			return count;
		}
		
		else if (isLeftEdge(x,y)){
			
			int count = 0;
			
			if (peekArray[x-1][y].getRep().equals("M"))
				count++;
				
			if (peekArray[x-1][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y].getRep().equals("M"))
				count++;
				
			return count;
		
		}
		
		else if (isRightEdge(x,y)){
		
			int count = 0;
			
			if (peekArray[x][y-1].getRep().equals("M"))
				count++;
			
			if (peekArray[x-1][y-1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y-1].getRep().equals("M"))
				count++;
				
			if (peekArray[x-1][y].getRep().equals("M"))
				count++;
				
			return count;
		
		}
		
		else if (isUpperEdge(x,y)){
			
			int count = 0;
			
			if (peekArray[x][y-1].getRep().equals("M"))
				count++;
				
			if (peekArray[x][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y].getRep().equals("M"))
				count++;
				
			if (peekArray[x+1][y-1].getRep().equals("M"))
				count++;
			
			return count;
		
		}
		
		else if (isBottomEdge(x,y)){
			
			int count = 0;
			
			if (peekArray[x-1][y-1].getRep().equals("M"))
				count++;
				
			if (peekArray[x-1][y].getRep().equals("M"))
				count++;
				
			if (peekArray[x-1][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x][y+1].getRep().equals("M"))
				count++;
				
			if (peekArray[x][y-1].getRep().equals("M"))
				count++;
			
			return count;
		
		}
		
		//check numAdjMines for corner Cells <br>
		//UL is upper left, UR upper right, BR bottom right, BL bottom left
		else if (!isCorner(x,y).equals("not corner")){
			
			int count = 0;
			String s = isCorner(x,y);
			
			if (s.equals("isULCorner")){
			
				if (peekArray[x][y+1].getRep().equals("M"))
					count++;
					
				if (peekArray[x+1][y+1].getRep().equals("M"))
					count++;
					
				if (peekArray[x+1][y].getRep().equals("M"))
					count++;
					
				return count;
			}
			
			if (s.equals("isURCorner")){
			
				if (peekArray[x][y-1].getRep().equals("M"))
					count++;
					
				if (peekArray[x+1][y].getRep().equals("M"))
					count++;
					
				if (peekArray[x+1][y-1].getRep().equals("M"))
					count++;
					
				return count;
			
			}
			
			if (s.equals("isBRCorner")){
			
				if (peekArray[x-1][y-1].getRep().equals("M"))
					count++;
				
				if (peekArray[x-1][y].getRep().equals("M"))
					count++;
					
				if (peekArray[x][y-1].getRep().equals("M"))
					count++;
					
				return count;
			
			}
			
			if (s.equals("isBLCorner")){
			
				if (peekArray[x-1][y].getRep().equals("M"))
					count++;
					
				if (peekArray[x-1][y+1].getRep().equals("M"))
					count++;
					
				if (peekArray[x][y].getRep().equals("M"))
					count++;
					
				return count;
			
			}
			
			else 
				return -1; //this should never happen...
			
		
		}
		
		return -1; //this should also never happen...
	
	}
	
	/**
	*
	*@param i row number
	*@param j column number
	*@return true/false if Cell is on left edge
	*/
	public boolean isLeftEdge(int i, int j){
		
		if (i != 0 && i!=7 && j == 0)
			return true;
		
		return false;
	
	}

	/**
	*
	*@param i row number
	*@param j column number
	*@return true/false if Cell is on right edge
	*/
	public boolean isRightEdge(int i, int j){
		
		if (i != 0 && i != 7 && j == 7)
			return true;
			
		return false;
		
	}
	
	/**
	*
	*@param i row number
	*@param j column number
	*@return true/false if Cell is on upper edge
	*/
	public boolean isUpperEdge(int i, int j){
	
		if (i == 0 && j!= 0 && j!= 7)
			return true;
		
		return false;
	
	}
	
	/**
	*
	*@param i row number
	*@param j column number
	*@return true/false if Cell is on bottom edge
	*/
	public boolean isBottomEdge(int i, int j){
		
		if (i == 7 && j != 0 && j != 7)
			return true;
			
		return false;
	}

	/**
	*
	*@param i row number
	*@param j column number
	*@return String indicating if Cell at location (i,j) is a corner, if so which corner
	*/
	public String isCorner(int i, int j){
	
		if (i == 0 && j == 0) return "isULCorner";
		if (i == 7 && j == 7) return "isBRCorner";
		if (i == 7 && j == 0) return "isBLCorner";
		if (i == 0 && j == 7) return "isURCorner";
		
		else
			return "notCorner";
		
		
	
	}
	
	/**
	*This method checks a user guess to see whether their predicition holds true.  
	*<p>
	*@param x row number
	*@param y column number
	*@param ans yes/no answer to whether user thinks there is a mine in that spot
	*@return true/false if their prediction was true
	*/
	public boolean checkGuess(int x, int y, String ans){
	
		String a = ans.toLowerCase();
	
	
		if (peekArray[x][y].getRep().equals("M") && a.equals("yes")){
		
			mineCount--;
			System.out.printf("Yay! You found a mine! There are %d mines remaining.\n", mineCount);
			
			printArray[x][y].setRep("M");
			return true;
			
		}
		
		if (!peekArray[x][y].getRep().equals("M") && a.equals("no")){

			printArray[x][y].setRep(Integer.toString(findNumAdjacentMines(x, y)));
			return true;
		}
		
		
		
		else
		
			return false;
		
		
	
	
	
	}
	
	/**
	*This method displays the 'peek' board - shows the locations of all mines
	*<p>
	*/	
	public void displayPeek(){
	
		StringBuilder sb = new StringBuilder("");
		
		for (int i = 0; i < 8; i++){
			sb.append("\n");
			for (int j = 0; j < 8; j++){
				sb.append(peekArray[i][j]);
				
			}
		
		}
		
		sb.append("\n");
		System.out.println(sb.toString());
	
	
	}
	
	/**
	*This method displays the current game board - only shows results of user guesses
	*
	*/
	public void displayBoard(){
	
		StringBuilder sb = new StringBuilder("");
		
		for (int i = 0; i < 8; i++){
			sb.append("\n");
			for (int j = 0; j < 8; j++){
				sb.append(printArray[i][j]);
				
			}
		
		}
		
		sb.append("\n");
		System.out.println(sb.toString());
	
	
	}
	
	/*
	*Main method creates a new Minesweeper object. Not much is done here. 
	*/
	public static void main(String[] args){
	
		Minesweeper m = new Minesweeper();
		
	}
	
}


