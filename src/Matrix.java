package core;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.*;
import java.util.regex.*;

// This class holds the core Matrix necesseities. Operations will be held in a different file.
public class Matrix
{

	private int rows, cols;
	public double [][] entries;
	public static Vector [] vectors;
	public static String format;

	// Constructors:
	//===============================================================================================

	//Console input constructor. (Have to look at exceptionhandling for it)
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
		this.vectors = new Vector[cols];

		double [] vals = new double[rows * cols];
		for (int i = 0; i < rows * cols; i++)
			vals[i] = in.nextDouble();

		populateMatrix(vals, rows, cols);

		in.close();
	}

	public Matrix(int rows, int cols, Vector [] v)
	{
		this.vectors = v;
		this.rows = rows;
		this.cols = cols;
		this.entries = matrixTo2DArray(this.vectors);
		System.out.println("EEEEE");
		System.out.println(this.entries);
	}

	// MatrixGUI constructor. Buffer holds data of entries.
	public Matrix(int rows, int cols, String buffer)
	{
		if (rows < 0 || cols < 0)
		{
			System.err.println("Invalid size arguments in Matrix(int, int, String).");
			return;
		}

		if (buffer == null)
		{
			System.err.println("Invalid String argument in Matrix(int, int, String)");
			return;
		}

		this.rows = rows;
		this.cols = cols;
		this.vectors = new Vector[cols];

		populateMatrix(parseBuffer(rows, cols, buffer), rows, cols);
		this.entries = matrixTo2DArray(this.vectors());
	}

	// Matrix Functions:
	//================================================================================================

	// Printing a Matrix in terms of its column Vectors.
	public void printMatrix(boolean column)
	{
		if (nullCheck(this, "printMatrix()"))
			return;

		// if column == true, print as individual Columns
		if (column)
		{

			for (int i = 0; i < cols; i++)
			{
				Vector v = vectors[i];

				System.out.println("Column: " + i);
				v.printVector();
			}
		}
		else
		{
			print2DArray(this.entries, this.rows, this.cols);
		}
	}

	//Method which prints all entries in a 2D array.
	public static void print2DArray(double [][] inputArray, int rows, int cols)
	{
		System.out.println("Matrix:");

		if (format == null)
			format = getMaxPrecision();

		for (int i = 0; i < cols; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				System.out.printf(String.format(format, inputArray[i][j]) + " ");
			}
			System.out.println();
		}

		System.out.println();
		return;
	}

	// Converting an array of Vectors into a 2D Array.
	public double [][] matrixTo2DArray(Vector [] v)
	{
		double [][] retVals = new double[rows][cols];


		for (int i = 0; i < cols; i++)
		{
			double [] vectorVals = v[i].getValues();

			int k = 0;
			for (int j = 0; j < rows; j++)
				retVals[j][i] = vectorVals[k++];
		}

		print2DArray(retVals, cols, rows);

		return retVals;
	}


	// Helper Functions:
	//================================================================================================

	// Convert buffer of inputs into the array of entries..
	public double [] parseBuffer(int rows, int cols, String buffer)
	{
		double [] retVals = new double[rows * cols];
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

		// Repeating the same process, however placing these values into an array of doubles.
		m = regex.matcher(buffer);

		for (int i = 0; i < count; i++)
		{
			m.find();
			retVals[i] = Double.parseDouble(m.group());
		}

		return retVals;
	}

	// Used to check number of valid intries in a buffer.
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
	public void populateMatrix(double [] parsedData, int rows, int cols)
	{
		int z = 0;

		for (int index = 0; index < cols; index++)
		{
			// Create a new column vector of size rows.
			double [] subValues =  new double [rows];
			z = index;
			for (int i = 0; i < rows; i++)
			{
				subValues[i] = parsedData[z];
				z += cols;
			}
			this.vectors[index] = new Vector(subValues);
		}
	}


 // Implement some function to find the max precision of a set of vectors. Come back to
	private static String getMaxPrecision()
	{
		int max = 0;
		for (int i = 0; i < vectors.length; i++)
		{
			//System.out.println(vectors[i].getPrecision());
			if (vectors[i].getPrecision() > max)
				max = vectors[i].getPrecision();
		}

		format = Vector.setFormat();
		return format;
	}
	// Utility Functions:
	//================================================================================================
	private boolean nullCheck(Matrix m, String function)
	{
		if (m == null)
		{
			System.err.println("Matrix object null in " + function + ".");
			return true;
		}

		if (m.vectors == null || m.vectors.length != m.cols)
		{
			System.err.println("Matrix vectors [] null in " + function + ".");
			return true;
		}

		return false;
	}

	// Getters and Setters:
	// ==============================================================================================
	public int getRows()
	{
		return this.rows;
	}

	public int getCols()
	{
		return this.cols;
	}

	public Vector [] getVectors()
	{
		return this.vectors;
	}

	public double [][] getEntries()
	{
		return this.entries;
	}

	public static void main (String [] args)
	{
		String buffer = "1.022 2.0311112 3.04 4.0";
		 Matrix m = new Matrix(2, 2, buffer);

		m.printMatrix(false);
		// m.matrixTo2DArray();
	}
}
