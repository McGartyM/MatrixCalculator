package core;

import core.Matrix;
import core.Vector;

// This java file will hold the source code to any operations which act upon a matrix.
public class Operations
{
	// Operations To DO:
	// Reduced Row Echelon Form -- Next
	// Find Inverse of a matrix.
 	// Diagonalize a matrix.
	// Spectral Decomposition.

	public static Matrix scaleMatrix(Matrix m, int scalar)
	{
		if (!validReference(m, m, "scaleMatrix()."))
			return null;

		Vector [] v = m.getVectors();
		for(int i = 0; i < m.getCols(); i++)
			v[i].scaleVector(scalar);

		return new Matrix(v);
	}

	public static Matrix transpose(Matrix m)
	{
		int numVectors = m.getRows();
		Vector [] v = new Vector[numVectors];
		for (int i = 0; i < numVectors; i++)
			v[i] = new Vector(m.getEntries()[i]);

		return new Matrix(v);
	}

	//Add matricies vector style.
	public static Matrix addMatrix(Matrix m, Matrix n)
	{
		if (!validReference(m,n, "addMatrix().") || !validSize(m, n, "addMatrix()."))
			return null;

		Vector [] x = m.getVectors();
		Vector [] y = n.getVectors();

		Vector [] z = new Vector[m.getCols()];

		for (int i = 0; i < m.getCols(); i++)
		{
			if (x[i] == y[i])
				y[i].scaleVector(-1);

			z[i] = x[i].addVector(y[i]);
			z[i].printVector();
		}

		return new Matrix(z);
	}

	// Need to add an errorchecking function here.
	public static Matrix multiplyMatrix(Matrix m, Matrix n)
	{
		if (!validReference(m, n, "multiplyMatrix()"))
			return null;

		int mRow = m.getRows(), mCol = m.getCols();
		int nRow = n.getRows(), nCol = n.getCols();

		// Checking valid dimensions, else multiplication is not defined.
		if (mRow != nCol || mCol != nRow)
		{
			System.err.println("Invalid Matrix dimensions in MultiplyMatrix().");
			return null;
		}

		// TO DO: Row implementation of vectors.
		double [][] mValues = m.getEntries();
		double [] retValues = new double[mRow * nCol];

		// Traverse down each row.
		int k = 0;
		for (int i = 0; i < nCol; i++)
			for (int  j = 0; j < mRow; j++)
				retValues[k++] = Vector.multiplyVectors(mValues[j], n.getVectors()[i]);

		return new Matrix(Vector.arrayToVector(retValues, mRow));
	}

	public static boolean validReference(Matrix m, Matrix n, String function)
	{
		if (m == null)
		{
			System.err.println("Uninitialized Matrix m in "+ function);
			return false;
		}

		if (n == null)
		{
			System.err.println("Uninitialized Matrix n in "+ function);
			return false;
		}

		if (m.getVectors() == n.getVectors())
		{
			System.err.println("Matrix(s) in " + function + " refence to identical Vector [] objects.");
			System.err.println("Undefined behavior will occur, create distinct Cector [] to resolve");
		}

		return true;
	}

	public static boolean validSize(Matrix m, Matrix n, String function)
	{
		if (m.getRows() != n.getRows())
		{
			System.err.println("Invalid row arguments in "+ function);
			return false;
		}

		if (m.getCols() != n.getCols())
		{
			System.err.println("Invalid columns arguments in "+ function);
			return false;
		}

		return true;
	}
}
