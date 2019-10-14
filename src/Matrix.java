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

	// Implement a row vector Vector []
	private int rows, cols;
	private double [][] entries;
	private Vector [] vectors;
	private String format;

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

	// Add a condition which checks that the lengths are equal.
	public Matrix(Vector [] v)
	{
		for (int i = 0; i < v.length; i++)
			if (v[i].length() != v[0].length())
			{
				System.err.println("All vectors must be of identical size in Matrix(Vector []) constructor.");
				return; // matrix.getVectors() == null.
			}

		this.vectors = v;
		this.rows = v[0].length();
		this.cols = v.length;
		this.entries = matrixTo2DArray();
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
		this.entries = matrixTo2DArray();
	}
	// Matrix Functions:
	//================================================================================================

	// Printing a Matrix in terms of its column Vectors.
	public void printMatrix(boolean column)
	{
		// Will not work, since this == null, deferencing will cause an exception.
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
			print2DArray();
		}
	}

	//Method which prints all entries in a 2D array.
	public void print2DArray()
	{
		System.out.println("Matrix:");

		if (this.format == null)
			this.format = getMaxPrecision();

		for (int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
				System.out.printf(String.format(this.format, this.entries[i][j]) + " ");

			System.out.println();
		}
		System.out.println();
		return;
	}

	// Converting an array of Vectors into a 2D Array.
	public double [][] matrixTo2DArray()
	{
		double [][] retVals = new double[rows][cols];


		for (int i = 0; i < cols; i++)
		{
			double [] vectorVals = vectors[i].getValues();

			int k = 0;
			for (int j = 0; j < rows; j++)
				retVals[j][i] = vectorVals[k++];
		}

		//print2DArray(retVals, cols, rows);

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
	private String getMaxPrecision()
	{
		int max = 0;

		for (int i = 0; i < this.vectors.length; i++)
		{
			//System.out.println(vectors[i].getPrecision());
			if (this.vectors[i].getPrecision() > max)
				max = this.vectors[i].getPrecision();
		}

		this.format = Vector.setFormat();
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

		if (m.getVectors() == null || m.vectors.length != m.cols)
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

	// Assumes the matrix is of identical parameters.
	// May want to improve on later
	public void setEntries(double [][] newEntries)
	{
		this.entries = newEntries;
	}
}
