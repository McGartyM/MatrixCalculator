package core;

import java.io.*;
import java.util.*;
import java.lang.Object.*;
import java.util.Arrays;
import java.text.*;
import java.util.regex.*;

// This file will serve to be home of the core Matrix "struct".
// As well as the Matrix and Vector untility functions required.
public class Matrix
{

	public Double [][] Array;
	public int rows, cols;

	// Console input constructor.
	public Matrix()
	{
		Scanner in = new Scanner(System.in);

		System.out.println("Enter the number of rows and columns for your matrix.");

		System.out.println("Enter Rows:");
			int rows = in.nextInt();
		System.out.println("Columns:");
			int cols = in.nextInt();

		this.rows = rows;
		this.cols = cols;
		this.Array = new Double[rows][cols];

		setMatrix(this, in);
		in.close();
	}

	// Constructor to be used with by MatrixGUI.java or with any predetermined data.
	public Matrix(int rows, int cols, String buffer)
	{

		this.rows = rows;
		this.cols = cols;
		this.Array = new Double[rows][cols];

		populateArray(this.Array, parseBuffer(rows, cols, buffer), rows, cols);
	}

	// Method to take text from an field and parse the non-numerical values.
	public Double [] parseBuffer(int rows, int cols, String buffer)
	{
		Double [] retVals = new Double[rows * cols];
		Pattern regex = Pattern.compile("[+-]?[0-9]*\\.?[0-9]+");
		Matcher m = regex.matcher(buffer);
		int count = 0;

		// Find the number of numerical occurances and compare against given total.
		while (m.find())
			count++;

		if (count != (rows * cols))
		{
			System.out.println("Incorrect number of entries in textbox.");
			return null;
		}

		// Repeating the same process, however placing these values into an array of Doubles.
		m = regex.matcher(buffer);

		for (int i = 0; i < count; i++)
		{
			m.find();
			retVals[i] = Double.parseDouble(m.group());
		}

		return retVals;
	}

	// Similar to parseBuffer() but returns if there is a correct number of entries.
	// Should be used with an already set matrix, ensuring correct number of entries.
	public static boolean checkEntries(String buffer, int total)
	{
		Pattern regex = Pattern.compile("[+-]?[0-9]*\\.?[0-9]+");
		Pattern nonRegex = Pattern.compile("[^\\d\\s\\.?]");

		Matcher Numerical = regex.matcher(buffer);
		Matcher Invalid = nonRegex.matcher(buffer);

		int count = 0;

		// Find the number of valid occurances.
		while(Numerical.find())
			count++;

		// Any invalid occurance (non numeric or whitespace) should trip a false return.
		if (Invalid.find() || count != total) // Could optimize potentially.
			return false;

		return true;
	}

	// Method to fill a matrix's Array field with the parsed data from parseBuffer().
	public void populateArray(Double [][] Array, Double [] parsedData, int rows, int cols)
	{
		int k = 0;

		// Nested loop to traverse all entries of a "2d array".
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
			{
				try
				{
						Array[i][j] = parsedData[k];

						// Debug:
						//System.out.println("Inserting: " + parsedData[k] + " " + Array[i][j]);
						k++;
				}
				// Neither exception should occur, since data is being filtered beforehand; however precaution.
				catch(NumberFormatException exception)
				{
					System.out.println("Error adding parsed[k] to input Array");
				}
				catch (NullPointerException nullExcep)
				{
					System.out.println("Invalid Matrix input try again.");
					Array = null;
					return;
				}
			}
			System.out.println();
	}

	// Setting a matrix manually from the console. (Pretty much outdated at this point in dev.)
	public void setMatrix(Matrix emptyMatrix, Scanner in)
	{

		System.out.println("Enter your matrix entries.");
		for (int i = 0; i < emptyMatrix.cols; i++)
		{
			for (int j = 0; j < emptyMatrix.rows; j++)
			{
				emptyMatrix.Array[i][j] = in.nextDouble();
			}
		}
	}

// Beginning of utility functions to allow this faux matrix behavior.
//=================================================================================================
	public static Double[][] formatArray(Double [][] inputArray, int cols, int rows)
	{
		DecimalFormat df = new DecimalFormat("#.####");

		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
			{
				String formatted = df.format(inputArray[i][j]);
				inputArray[i][j] = Double.parseDouble(formatted);
			}

			return inputArray;
	}

	// Method which prints all entries in a 2D array.
	public static void print2DArray(Double [][] inputArray, int cols, int rows)
	{
		inputArray = formatArray(inputArray, cols, rows);
		System.out.println("Matrix:");

		for (int i = 0; i < cols; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				if (inputArray[i][j] < 0)
					System.out.printf("%8.4f ",inputArray[i][j]);
				else
					System.out.printf("%8.4f ", inputArray[i][j]);
			}
			System.out.println();
		}

		System.out.println();
		return;
	}

	// Similar to print2DArray() but prints individual vectors in transpose notation.
	public static void printVector(Double [] vector, int dimensions)
	{
		System.out.print("(");
		for (int i = 0; i < dimensions; i++)
		{
			if (i == (dimensions - 1))
				System.out.print(vector[i]);
			else
				System.out.print(vector[i] + " ");
		}
		System.out.println(")");

		return;
	}

	// Given a matrix input, returns an array representing a column vector of that matrix.
	public static Double [] getVector(Matrix matrix, int column)
	{
		Double [] vector;

		if (matrix == null)
		{
			System.out.println("Uninitialized matrix in getVector().");
			return null;
		}

		if (column > matrix.cols || column < 1)
		{
			System.out.println("Invalid column access in getVector().");
			return null;
		}

		vector = new Double[matrix.rows];

		for (int i = 0; i < matrix.rows; i++)
			vector[i] = matrix.Array[i][column - 1]; // column -1 since arrays index 0-n.

		// Debug Statements.
		// System.out.println("Column: " + column);
		// printVector(vector, matrix.rows));

		return vector;
	}

	// Given two vectors of identical dimensions, calculates the dot product between them.
	public static Double getDotProduct(Double [] vector1, Double [] vector2)
	{
		int dim1, dim2;
		Double dotProduct = 0.0;

		dim1 = vector1.length;
		dim2 = vector2.length;

		if (dim1 != dim2)
		{
			System.out.println("Unmatched vector dimensions in getDotProduct().");
			return -1.0;
		}

		for (int i = 0; i < dim1; i++)
			dotProduct += (vector1[i] * vector2[i]);

		//System.out.println("In getDotProduct(), result is: " + dotProduct);
		//printVector(vector1, vector1.length);
		//printVector(vector2, vector2.length);

		return dotProduct;
	}

	// Given two vectors of identical dimension, calculates the addition of these two vectors.
	public static Double [] addVectors(Double [] vector1, Double [] vector2, boolean negative)
	{
		int i;
		int dim1 = vector1.length;
		int dim2 = vector2.length;
		Double [] vectorSum;

		if (dim1 != dim2)
		{
			System.out.println("Unmatched vector dimensions in addVectors().");
			return null;
		}
		vectorSum = new Double[dim1];

		// Having a boolean representing vector subtraction eases need to another function.
		if (negative == false)
		{
			for (i = 0; i < dim1; i++)
				vectorSum[i] = vector1[i] + vector2[i];
		}
		else if (negative == true)
		{
			for (i = 0; i < dim1; i++)
				vectorSum[i] = vector1[i] - vector2[i];
		}

		//System.out.println("Printing vectorSum from addVectors().");
		//printVector(vectorSum, dim1);

		return vectorSum;
	}

	// Given a vector, scale it by a given value.
	public static Double [] scaleVector(Double [] vector, Double scalar)
	{
		int dim;

		// Check if vector array is valid.
		if (vector == null)
		{
			System.err.println("Invalid vector in scaleVector()");
			return null;
		}

		dim = vector.length;
		for (int i = 0; i < dim; i++)
			vector[i] *= scalar;

		return vector;
	}

	public int getRows()
	{
		return this.rows;
	}

	public int getCols()
	{
		return this.cols;
	}

	public Double [][] getArray()
	{
		return this.Array;
	}
}
