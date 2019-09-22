package core;

import core.Matrix;
import core.Vector;
// This java file will hold the source code to any operations which act upon a matrix.
public class Operations
{
	// Operations To DO:
	// Add Matricies
	// Scale Matricies
	// Multiply Matricies
	// Reduced Row Echelon Form
	// Find Inverse of a matrix.
 	// Diagonalize a matrix.

	public static Matrix addMatrix(Matrix m, Matrix n)
	{
		if (!checkDimensions(m.getRows(), n.getRows(), m.getCols(), n.getCols()))
		{
			System.out.println("Invalid Dimensions in addMatrix()");
			return null;
		}

		Vector [] x = m.getVectors(), y = n.getVectors();
		Vector [] z = new Vector[m.getCols()];

		for (int i = 0; i < m.getCols(); i++)
		{
			z[i] = x[i].addVector(y[i]);
		}

		Matrix s = new Matrix(m.getRows(), m.getCols(), z);
		System.out.println(s.getRows() + " " + s.getCols() + " " + s.entries);

		return s;
	}

	public static Boolean checkDimensions(int rowsA, int rowsB, int colsA, int colsB)
	{
		if (rowsA != rowsB || colsA != colsB)
		{
			System.err.println("Invalid dimensions.");
			return false;
		}

		return true;
	}

	public static void main (String [] args)
	{
		String buffer = "1.0 2.0 3.0 4.0";

		Matrix m = new Matrix(2, 2, buffer);
		Matrix n = new Matrix(2, 2, buffer);
		Matrix z = addMatrix(m, n);

		z.printMatrix(false);
	}
}
