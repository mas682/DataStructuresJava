package cs445.a3;

import java.util.Scanner;
import java.util.Arrays;

import java.io.File;
import java.io.FileNotFoundException;

public class SemiMagic {
	
	private static int[][] preSet;
	private static boolean empty;

    public static boolean isFullSolution(int[][] square) {
        // TODO: Complete this method
		//check that every slot has a answer in it
		for(int j = 0; j < square.length; j++)
		{
		for(int i = 0; i < square[j].length; i++)
		{
			if(square[j][i] == 0){
				return false;
			}
		}
		}
		
		//if it gets to this point, the solution is known to be complete,
		//check if it is valid
		if(reject(square))
		{
			return false;
		}
		
		//solution is complete and valid
		//make sure you are SUPPOSED TO RETURN TRUE
        return true;
    }

    public static boolean reject(int[][] square) {
        // TODO: Complete this method
		//check to see if rows/columns add up to correct amount
		//check to make sure all numbers were used and no repeats
		int max = square.length*(square.length*square.length+1)/2;
		int total = 0;
		for( int j = 0; j < square.length; j++)
		{
		for( int i = 0; i < square[j].length; i++)
		{	

			for(int m = 0; m < square.length; m++){
			for(int k = 0; k < square[j].length; k++)
			{
			if(square[m][k] == 0)
			{
				return false;
			}
			}	
			}
			if(square[j][i] != 0){
				total = 0;
				for(int n = 0; n < square[j].length; n++)
				{
				if( i != n && square[j][i] == square[j][n])
				{
				//compare values in row to value in first spot of row
				//will this compare work even if not considering previous
				//values in column? think about rows too
				return true;
				}
				//Determines if the line is over the max limit
				total += square[j][n];
				}
				if(total != max)
				{

				return true;
			
				}
				total = 0;
				for(int c = 0; c < square.length; c++)
				{
				if( j != c && square[j][i] == square[c][i])
				{
					return true;
				}

				total += square[c][i];
				}
				if(total != max)
				{////added total <= max
				return true;
				}
				
				//need to add reject for it two of the same numbers in whole square...
				for(int a = 0; a < square.length; a++)
				{
				for(int b = 0; b < square[a].length; b++)
				{
					if(a!= j && i !=j && square[a][b] == square[j][i])
					{
						return true;
					}
				}
				}				
				
				
				
				///need to make a check to see if all numbers leading up to and
				//including n squared are used
				
			}
		}
		
		

    }

	return false;
	}

    public static int[][] extend(int[][] square) {
        // TODO: Complete this method
		//initialize the new partial solution
		int row = square.length;
		int col = square[0].length;
		int [][] temp = new int[row][col];
		
		boolean extended = true;
		
		//while(extended && num < row * col)
		////////////
		for(int a = 0; a < square.length; a++)
		{
			for(int b = 0; b < square[a].length; b++)
			{
				temp[a][b] = square[a][b];
				if(!empty)
				{
					if(preSet[a][b] != 0)
					{
						temp[a][b] = preSet[a][b];
					}
				}
			}
		}

		
		while(extended)
		//loop so num does not get larger than possible values
		{
		
		for(int i = 0; i < row; i++)
		{
		for(int j = 0; j < col; j++)
		{	
		
		if(temp[i][j] == 0)
		{
				int maxValue = row * col;
				int [] contains = new int[maxValue];
				int val = 1;
				int indY = 0;
				int sum = 0;
				int y = 0;
				int rowMax = square.length*(square.length*square.length+1)/2;
				boolean valFound = true;
				while(val <= maxValue)
				{
					if(contains(temp, val) || val == square[i][j])
					{
						val++;
					}
					else
					{
						contains[indY] = val;
						indY++;
						val++;
					}
				}	
				
				while(valFound)
			    {
					if(contains[y] > square[i][j])
					{
						sum = 0;
						if(j == col-2)
						{
							while(valFound && contains[y] !=0)
							{
							sum = 0;
							for(int e = 0; e < col; e++)
							{
								sum += temp[i][e];
							}
							sum += contains[y];

							if(rowMax - sum > maxValue)
							{
								valFound = true;
								y++;
							}
							else{
							temp[i][j] = contains[y];
							valFound = false;
							}
							}
							if(contains[y] == 0)
								return null;
						}
						else if(j == col-1)
						{
							int max = 0;
							boolean search = true;;
							for(int g = 0; g < col; g++)
							{
								sum += temp[i][g];
							}
					
							max = rowMax - sum;
					
							if(sum > rowMax)
							{
								search = false;
								return null;
							}
							else if(max <= 0)
							{
								search = false;
								return null;
							}
							while(search && contains[y] !=0)
							{
							if(contains[y] == max)
							{
							temp[i][j] = contains[y];
							search = false;
							valFound = false;
							}
							else if(contains[y] < max)
							{
								y++;
							}
							else
							{
								return null;
							}
							}
						}
						else
						{
						temp[i][j] = contains[y];

						valFound = false;
						}
						y++;
					}
					else if(contains[y] == 0)
					{
						valFound = false;
						return null;
					}
					else
					{
						y++;
						valFound = true;
					}
					}
					return temp;
		}
		}
		}
		
		//}
		extended = false;
		}
        return null;
    }		
	
    public static int[][] next(int[][] square) {
        // TODO: Complete this method
		//changes the most recently placed num to next possible value
		int rows = square.length;
		int cols = square[0].length;
		int maxValue = rows*cols;
		int[][] temp = new int[rows][cols];
		int an = 0;
		int z = 1;
		int rowMax = square.length*(square.length*square.length+1)/2;//formula
		for(int i = 0; i < rows; i++)
		{
		for(int j = 0; j < cols; j++)
		{

		if(((j == cols - 1 && i == rows -1) || (j == cols - 1 && i <= rows -2 && 
					square[i+1][0] == 0) 
					|| (j == cols - 2 && square[i][j+1] == 0 )
					|| (j <= cols -2 && square[i][j+1] == 0 )) && empty)
		{	
			if(square[i][j] == maxValue)
			{	
				//the value is the highest value it can be,
				//so we cannot do anything else
				return null;
			}
			else{
				
				int [] contains = new int[maxValue];
				int val = 1;
				int indY = 0;
				while(val <= maxValue)
				{
					if(contains(temp, val) || val == square[i][j])
						val++;
					else
					{
						contains[indY] = val;
						indY++;
						val++;
					}
				}	
						
				if(j == cols - 1){
					boolean found = true;
					
					int max = 0, possibleSum = 0, y = 0;///maximum possible value to place in spot
					boolean valFound = true;
					
					for(int g = 0; g < cols; g++)
					{
						possibleSum += temp[i][g];
					}
					
					max = rowMax - possibleSum;
					
					if(possibleSum > rowMax)
					{
						return null;
					}
					else if(max <= 0)
					{
						return null;
					}

					do
					{
						if(contains[y] == max)
						{
							temp[i][j] = max;
							valFound = false;

						}

						else if(contains[y] == 0)
						{
							valFound = false;
							return null;////////no value can be added here
										///if you have reached this point
						}	
						else
						{
							y++;
							valFound = true;
						}
						
					}while(y < maxValue && valFound);//end of for loop
					
					return temp;
					
				}	else{

					int index3 = 0;
					boolean valFound = true;
					int y = 0;
					int q = 1;
					int sum = 0;

					
					while(valFound)
					{
					if(contains[y] > square[i][j])
					{
						sum = 0;
						if(j == cols-2)
						{
							for(int e = 0; e < cols; e++)
							{
								sum += temp[i][e];
							}
							sum += contains[y];

							if(rowMax - sum > maxValue)
							{
								valFound = true;
								y++;
							}
							else{
							temp[i][j] = contains[y];
							valFound = false;
							}
						}
						else
						{
						temp[i][j] = contains[y];
						valFound = false;
						}
					}
					else if(contains[y] == 0)
					{

						valFound = false;
						return null;
					}
					else
					{

						y++;
						valFound = true;
					}
					}			

		return temp;
			}	
		//end of else loop
		}//end inner else
		}
		///////////////add on

		else if(!empty && preSet[i][j] == 0 && (square[i][j] == 0 || ((j == cols - 1 && i == rows -1) 
		|| (j == cols - 1 && i <= rows -2 && square[i+1][0] == 0 )
					|| (j == cols - 2 && square[i][j+1] == 0 )
					|| (j <= cols -2 && square[i][j+1] == 0 ))))
		{
			if(square[i][j] == maxValue)
			{	
				//the value is the highest value it can be,
				//so we cannot do anything else
				return null;
			}
			else{
				
				int [] contains = new int[maxValue];
				int val = 1;
				int indY = 0;
				while(val <= maxValue)
				{
					if(contains(temp, val) || val == square[i][j])
						val++;
					else
					{
						contains[indY] = val;
						indY++;
						val++;
					}
				}	

						
				if(j == cols - 1){

					boolean found = true;
					
					int max = 0, possibleSum = 0, y = 0;///maximum possible value to place in spot
					boolean valFound = true;

					
					for(int g = 0; g < cols; g++)
					{
						possibleSum += temp[i][g];
					}
					
					max = rowMax - possibleSum;
					
					if(possibleSum > rowMax)
					{
						return null;
					}
					else if(max <= 0)
					{
						return null;
					}

			
					do
					{
						if(contains[y] == max)
						{
							temp[i][j] = max;
							valFound = false;

						}
						//else if(y == contains.length-1)
						else if(contains[y] == 0)
						{
							valFound = false;
							return null;////////no value can be added here
										///if you have reached this point
						}	
						else
						{
							y++;
							valFound = true;
						}
						
					}while(y < maxValue && valFound);//end of for loop
					
					return temp;
					
				}	else{
					int index3 = 0;
					boolean valFound = true;
					int y = 0;
					int q = 1;
					int sum = 0;
					
					while(valFound)
					{
					if(contains[y] > square[i][j])
					{
						sum = 0;
						if(j == cols-2)
						{
							for(int e = 0; e < cols; e++)
							{
								sum += temp[i][e];
							}
							sum += contains[y];;
							if(rowMax - sum > maxValue)
							{
								valFound = true;
								y++;
							}
							else{
							temp[i][j] = contains[y];
							valFound = false;
							}
						}
						else
						{
						temp[i][j] = contains[y];
						valFound = false;
						}
					}
					else if(contains[y] == 0)
					{
						valFound = false;
						return null;
					}
					else
					{
						y++;
						valFound = true;
					}
					}			

		return temp;
			}	
		//end of else loop
		}//end inner else
		}
		
		///////////////////
	//end outermost if
		else{
				//this is not the last value extended, so just copy it
				temp[i][j] = square[i][j];

		}
		}
	
		}
		//should you return temp or null??	
        return null;/////////////////////////or temp?
    }
	public static boolean contains(int [][] temp, int a)
	{
				for(int c = 0; c < temp.length; c++)
				{
				for(int b = 0; b < temp[c].length; b++)
				{
					if(a == temp[c][b])
						return true;
				}
				}
		return false;
	}
	
    public static void testIsFullSolution() {
        // TODO: Complete this method
		//test is full solution giving several partial solutions
		int[][] fullSolutions = {
			{8, 1, 6},
			{3, 5, 7},
			{4, 9, 2},
		};
		int[][] notFullSolutions = {
			{1, 2, 5},
			{3, 4, 0},
			{6, 0, 0},
		};
		
		
		setOriginal(fullSolutions);
        System.err.println("These should be full:");
        //for (int[] test : fullSolutions) {
            if (isFullSolution(fullSolutions))
			{
                System.err.println("Full sol'n:\t");
				printSquare(fullSolutions);
            }
			else {
                System.err.println("Not full sol'n:\t");
				printSquare(fullSolutions);
		}
		setOriginal(notFullSolutions);
		System.err.println("These should NOT be full:");
        //for (int[] test : notFullSolutions) {
            if (isFullSolution(notFullSolutions))
			{
                System.err.println("Full sol'n:\t" );
				printSquare(notFullSolutions);
            }
			else {
                System.err.println("Not full sol'n:\t");
				printSquare(notFullSolutions);
            }
       // }
	}
    public static void testReject() {
        // TODO: Complete this method
		//test the reject method using several partial solutions
		
		    int[][] notRejected = new int[][] {
			{8, 1, 6},
			{3, 5, 7},
			{4, 9, 2},
        };

        int[][] rejected = new int[][] {
            {1, 2, 3},
            {4, 5, 3},
            {7, 8, 9},

        };

        System.err.println("These should NOT be rejected:");
        //for (int[] test : notRejected) {
            if (reject(notRejected)) {
                System.err.println("\tRejected:\t");
				printSquare(notRejected);
            } else {
                System.err.println("\tNot rejected:\t");
				printSquare(notRejected);
            }

        System.err.println("These should be rejected:");
        //for (int[] test : rejected) {
            if (reject(rejected)) {
                System.err.println("\tRejected:\t");
				printSquare(rejected);
            } else {
                System.err.println("\tNot rejected:\t");
				printSquare(rejected);
            }
    }

    public static void testExtend() {
        // TODO: Complete this method
		//test the extend method using several partial solutions
		
		int[][] notExtended = new int[][] {
			{8, 1, 6},
			{3, 5, 7},
			{4, 9, 2},
        };
		int [][] temp = new int[3][3];
        int[][] extended = new int[][] {
            {1, 0, 3},
            {4, 5, 3},
            {7, 8, 9},

        };
		setOriginal(notExtended);
        System.err.println("These should NOT be extended:");
        //for (int[] test : notRejected) {
         temp = extend(notExtended);
			if(temp == null)
			{
				System.err.println("\tNot extended:\t");
				printSquare(notExtended);
            } else {				
				System.err.println("\tExtended:\t");
				printSquare(temp);
            }
		setOriginal(extended);
        System.err.println("These should be extended:");
        //for (int[] test : rejected) {
		temp = extend(extended);
            if (temp == null) {
                System.err.println("\tNOt Extended:\t");
				printSquare(extended);
            } else {
                System.err.println("\tExtended:\t");
				printSquare(temp);
            }
    }

    public static void testNext() {
        // TODO: Complete this method
		//test the next method using several partial solutions
		int [][] temp = new int[3][3];
		int[][] noNext = {
			{8, 1, 6},
			{3, 5, 7},
			{4, 9, 2},
		};
		int[][] nextTest = {
			{1, 2, 5},
			{3, 4, 0},
			{6, 0, 0},
		};
		
        System.err.println("These can NOT be next'd:");
        //for (int[] test : fullSolutions) {
			temp = next(noNext);
            if (temp == null)
			{
                System.err.println("Not next'd:\t");
				printSquare(noNext);
            }
			else {
                System.err.println("Next'd to:\t");
				printSquare(temp);
			}
		
		System.err.println("These can be next'd:");
			temp = next(nextTest);
            if (temp == null)
			{
                System.err.println("Not next'd:\t" );
				printSquare(nextTest);
            }
			else {
                System.err.println("Next'd to:\t");
				printSquare(temp);
            }
       // }
    }

    /**
     * Returns a string representation of a number, padded with enough zeros to
     * align properly for the current size square.
     * @param num the number to pad
     * @param size the size of the square that we are padding to
     * @return the padded string of num
     */
    public static String pad(int num, int size) {
        // Determine the max value for a square of this size
        int max = size * size;
        // Determine the length of the max value
        int width = Integer.toString(max).length();
        // Convert num to string
        String result = Integer.toString(num);
        // Pad string with 0s to the desired length
        while (result.length() < width) {
            result = " " + result;
        }
        return result;
    }

    /**
     * Prints a square
     * @param square the square to print
     */
    public static void printSquare(int[][] square) {
        if (square == null) {
            System.out.println("Null (no solution)");
            return;
        }
        int size = square.length;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(pad(square[i][j], size) + " ");
            }
            System.out.print("\n");
        }
    }

    /**
     * Reads a square of a specified size from a plaintext file
     * @param filename the name of the file to read from
     * @param size the size of the square in the file
     * @return the square
     * @throws FileNotFoundException if the named file is not found
     */
    public static int[][] readSquare(String filename, int size)
                throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        int[][] square = new int[size][size];
        int val = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                square[i][j] = scanner.nextInt();
            }
        }
        return square;
    }

    /**
     * Solves the magic square
     * @param square the partial solution
     * @return the solution, or null if none
     */
    public static int[][] solve(int[][] square) {
        if (isFullSolution(square))
		{
		return square;
		}
		
        int[][] attempt = extend(square);
        while (attempt != null) {
			
            int[][] solution;
            solution = solve(attempt);
            if (solution != null) return solution;
            attempt = next(attempt);

        }
        return null;
    }

    public static void main(String[] args) {
        if (args.length >= 1 && args[0].equals("-t")) {
            System.out.println("Running tests...");
            testIsFullSolution();
            testReject();
            testExtend();
            testNext();
        } else if (args.length >= 1) {
            try {
                // First get the specified size
                int size = Integer.parseInt(args[0]);

                int[][] square;
                if (args.length >= 2) {
                    // Read the square from the file
                    square = readSquare(args[1], size);
                } else {
                    // Initialize to a blank square
                    square = new int[size][size];
                }

                System.out.println("Initial square:");
                printSquare(square);

                System.out.println("\nSolution:");
				setOriginal(square);////this is used to determine which values can be changed
				isEmpty(square);
				if(!empty);
					setOriginal(square);
                int[][] result = solve(square);
                printSquare(result);
            } catch (NumberFormatException e) {
                // This happens if the first argument isn't an int
                System.err.println("First argument must be the size");
            } catch (FileNotFoundException e) {
                // This happens if the second argument isn't an existing file
                System.err.println("File " + args[1] + " not found");
            }
        } else {
            System.err.println("See usage in assignment description");
        }
    }
	
	
	public static void setOriginal(int[][] square)
	{
		if(square == null)
		{
			//do nothing
		}
		else
		{
			setOriginalValues(square);
		}
	}
	
	///////////////////create an int array as a private field/////////
	
	
	private static void setOriginalValues(int [][] square)
	{
		preSet = new int [square.length][square[0].length];
		
		for(int i = 0; i < square.length; i++)
		{
			for(int j = 0; j < square[i].length; j++)
			{
				if(square[i][j] != 0)
				{
					preSet[i][j] = square[i][j];
				}
				else
				{
					preSet[i][j] = 0;
				}
			}
		}
	}
	
	public static void isEmpty(int [][] square)
	{	empty = true;
		for(int i = 0; i < square.length; i++)
		{
			for( int j = 0; j < square[i].length; j++)
			{
				if(square[i][j] != 0)
				{
					empty = false;
				}
			}
		}
	}
		
}

