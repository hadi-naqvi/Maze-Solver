/**
 * Recursion Maze Assignment | Recursive Breadth-First Maze Solver Algorithm
 * Date: October 18th, 2021
 * @author Hadi Naqvi
 * Teacher: Mr. Ho
 */

// Imports the I/O package to read text files and the java.util package in order to use ArrayLists/Queues
import java.io.*;
import java.util.*;

 public class NaqviHadiMazeAssignment {
     public static void main(String[] args) throws IOException {
         // Imports the maze from its text file and stores it in a 2D char array
		 char[][] maze = importMaze();
		 
		 // Initializes a Queue with its underlying implementation being a Linked-list
		 Queue<String> paths = new LinkedList<>();
		 paths.add("");
		 
		 // Finds the starting position in the maze and stores its (x, y) position in an array and also initializes every element of the visitedLocations array as false (Except Start)
		 boolean[][] visitedLocations = new boolean[maze.length][maze[0].length];
		 int[] startPos = new int[2];
		 // Iterates over every column in every row of the maze to find the starting position (S)
		 for (int row = 0; row < maze.length; row++) {
			 for (int col = 0; col < maze[0].length; col++) {
				 visitedLocations[row][col] = false;
				 if (maze[row][col] == 'S') {
					startPos[0] = col;
					startPos[1] = row;
					// Sets the start position as a visited location
					visitedLocations[row][col] = true;
				 }
			 }
		 }
		 
		 // Attempts to solve the maze, however if the number of recursive calls exceeds the stack size, it tells the user to increase the stack thread size in the JVM arguments
		 try {
			// Calls a recursive method which solves the maze and outputs the result (Including no-solution result)
			String path = solveMaze(maze, paths, visitedLocations, startPos);
			printMaze(maze, startPos, path);
		 }
		 // Prevents a StackOverFlow error caused by a large dataset, and tells the user to re-run the program with a higher thread stack size in order to solve the maze
		 catch (StackOverflowError e) {
			 System.err.println("The dataset (maze) is too large.");
			 System.err.println("To solve the maze, re-run the program with a higher thread stack size (Change JVM argument -Xss and allocate more memory)");
			 System.err.println("Try running again by doing: \"java -Xss8M NaqviHadiMazeAssignment.java\"");
		 }
     }
	 
	 
	 /**
	 * This method reads the text file which stores the maze and returns a 2D Char array with each char in the maze
	 * @return The 2D char array which stores the maze
	 */
     public static char[][] importMaze() throws IOException {
		// Initializes a string variable which will store the file name of the maze
		String fileName = "";
		
		// Repeatedly prompts the user to enter a valid *.txt file name until they enter a valid file name
		boolean validFile = false;
		System.out.println("Welcome to Hadi's Maze Solver!");
		System.out.println("Enter the file name of the maze you would like to solve (e.g. maze0):");
		while (!validFile) {
			// Prompts the user for a file name, then checks if it exists by creating a File object and using the .exists() method
			fileName = new Scanner(System.in).nextLine();
			if (new File(fileName + ".txt").exists()) {
				validFile = true;
			}
			else {
				System.out.println("The file name you entered does not exist in the working directory. Please re-enter the file name:");
			}
		}
		
		// BufferedReader object is created using the FileReader object of the maze's text file
		BufferedReader mazeReader = new BufferedReader(new FileReader(fileName + ".txt"));
		ArrayList<String> mazeRows = new ArrayList<String>();
		
		// Each line in the maze is added to an ArrayList storing each line/row of the maze
		String currentLine = mazeReader.readLine();
		while (currentLine != null) {
			mazeRows.add(currentLine);
			currentLine = mazeReader.readLine();
		}
		mazeReader.close();
		
		// Converts the ArrayList storing each row to a 2D char array containing each individual cell in every row of the maze matrix
		char[][] maze = new char[mazeRows.size()][mazeRows.get(0).toCharArray().length];
		for (int row = 0; row < mazeRows.size(); row++) {
			maze[row] = mazeRows.get(row).toCharArray();
		}
		
		// Returns the 2D char array which stores the entire maze/matrix
        return maze;
     }

	 
	 /**
	 /* This method prints out the maze and the solution's path if one was found
	 /* @param maze The 2D char array which stores the maze
	 /* @param path The array storing the path to the solution of the maze if one was found
	 */
     public static void printMaze(char[][] maze, int[] startPos, String path) {
         // If there was a solution/path found, then it is marked in the maze with + chars
		 if (path.length() > 0) {
			 int[] currentPos = startPos;
			 String currentPath = "";
			 // Follows the entire path from start-to-end and marks each cell/location it crosses with a + to mark its path
			 for (int i = 0; i < path.length() - 1; i++) {
				 currentPath += Character.toString(path.charAt(i));
				 currentPos = findPos(maze, startPos, currentPath);
				 maze[currentPos[0]][currentPos[1]] = '+';
			 }
			 System.out.println("\nA solution to the maze was found!");
		 }
		 // If there was no solution found, the user is told so and the maze is printed without a path
		 else {
			 System.out.println("\nThere is no solution to the maze.");
		 }
		 
		 // Iterates over every column in every row and prints each cell in the maze matrix, and prints each new row on a new line, which outputs the entire maze
		 for (int row = 0; row < maze.length; row++) {
			 for (int col = 0; col < maze[0].length; col++) {
				 System.out.print(maze[row][col] + " ");
			 }
			 System.out.println();
		 }
     }
	 
	 
	 /**
	 * This method finds and returns the end position a given path leads to
	 * @param maze The 2D char array which stores the maze/matrix
	 * @param startPos The starting position of the maze
	 * @param path A path represented by a string which contains each move from the starting position (e.g. RRRDDLLU)
	 * @return The end position the path leads to
	 */
	 public static int[] findPos(char[][] maze, int[] startPos, String path) {
		 // Initializes two variables which refer to the displacement in the x-axis and y-axis from the starting point of the maze
		 int xOffset = 0;
		 int yOffset = 0;
		 
		 // Iterates over every move in the path from start-to-end and calculates the total offset/displacement in the x-axis and y-axis
		 for (int i = 0; i < path.length(); i++) {
			 if (path.charAt(i) == 'L') {
				 xOffset--;
			 }
			 else if (path.charAt(i) == 'R') {
				 xOffset++;
			 }
			 else if (path.charAt(i) == 'U') {
				 yOffset--;
			 }
			 else {
				 yOffset++;
			 }
		 }
		 
		 // Returns the indexes of the end position the path leads to after calculating and applying the displacement from the starting point
		 int[] pos = {startPos[1] + yOffset, startPos[0] + xOffset};
		 return pos;
	 }
	 
	 
	 /**
	 * This method determines whether a position is a valid move for the robot to make if the location hasn't been visited, is within the bounds of the maze and is not a wall
	 * @param maze The 2D char array which stores the maze/matrix
	 * @param pos The given position being validated
	 * @param visitedLocations The 2D boolean array which keeps track of every visited location in the maze
	 * @return True/false depending on whether the move is valid
	 */
	 public static boolean validPos(char[][] maze, int[] pos, boolean[][] visitedLocations) {
		 // Checks if the given position is within the bounds of the maze
		 if (pos[0] >= 0 && pos[0] < maze.length && pos[1] >= 0 && pos[1] < maze[0].length) {
			 // Checks if the given position is an empty space (not a wall) and if it hasn't already been visited, if so the method returns true
			 if ((maze[pos[0]][pos[1]] == '.' || maze[pos[0]][pos[1]] == 'G') && !(visitedLocations[pos[0]][pos[1]])) {
				 return true;
			 }
		 }
		 
		 // If the move is not valid, the method returns false
		 return false;
	 }
	 
	 
	 /**
	 * This method attempts to find a solution and its path in a maze recursively using the Breadth-First Search tree-traversal algorithm, and outputs the result
	 * @param maze The 2D char array which stores the maze/matrix
	 * @param paths The Queue which stores the different paths being traversed in the Breadth-First Search
	 * @param visitedLocations The 2D boolean array which keeps track of every visited location in the maze
	 * @param startPos The coordinates of the starting point in the maze
	 * @return The solution's path represented by a String (e.g. UDLLRRLUD), or an empty string which represents no solution
	 */
	 public static String solveMaze(char[][] maze, Queue<String> paths, boolean[][] visitedLocations, int[] startPos) {
		 // Stores the 4 directional moves the robot can make as chars
		 char[] directions = {'L', 'R', 'U', 'D'};
		 
		 // Base-case: If there are no more paths to search, there is no solution and the method returns an empty string (No solution)
		 if (paths.size() == 0) {
			 return "";
		 }
		 
		 // Dequeues a path from the queue, and stores the (x, y) position of the location where the path ends/leads to
		 String path = paths.poll();
		 int[] pathEndPos = findPos(maze, startPos, path);
		 
		 // Base-case: If the path currently being searched leads to the end/goal, then the method returns its path (A solution is found)
		 if (maze[pathEndPos[0]][pathEndPos[1]] == 'G') {
			 return path;
		 }
		 
		 // Finds all valid adjacent positions the robot can move to from its current position in the path and adds them to the queue so that they can be searched
		 for (char direction: directions) {
			 int[] adjacentPos = findPos(maze, startPos, path + Character.toString(direction));
			 if (validPos(maze, adjacentPos, visitedLocations)) {
				 paths.add(path + Character.toString(direction));
				 visitedLocations[adjacentPos[0]][adjacentPos[1]] = true;
			 }
		 }
		 
		 // Recursively keeps searching paths in the maze until there is either a solution or no solution (The 2 base cases above)
		 return solveMaze(maze, paths, visitedLocations, startPos);
	 }
	 
	 
 }