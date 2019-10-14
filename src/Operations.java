package core;

import core.Matrix;
import core.Vector;

// This java file will hold the source code to any operations which act upon a matrix.
public class Operations
{
	// Operations To DO:
	// Reduced Row Echelon Form -- Next
	// Find cofactors of a matrix / Leads to adjugate matrix.
	// Find Inverse of a matrix.
 	// Diagonalize a matrix.
	// Spectral Decomposition.
	// Determinant.
	// Done. Maybe implement small properties like rank, etc.

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

	// Using Guass-Jordan Elimination, reducate a metrix to its reduced row echelon form.
	public static Matrix reducedREF(Matrix m)
	{
		int numCols = m.getCols();
		int numRows = m.getRows();
		double [][] values = m.getEntries();

		// Reorder matrix so leading zero rows are lower in the matrix.
		reorderMatrix(m, numRows, numCols);

		// Remove the bottom triangle.
		// col < numCols deals with matricies which are taller than wide. Prevents out of bounds.
		for (int row = 0, col = 0; row < numRows && col < numCols; row++, col++)
		{
			if (values[row][col] == 0)
				continue; // No div by 0.

			// Scale a vector down, so the diagonal value will be one.
			double scaleDown = 1 / values[row][col];
			values[row] = Vector.scaleArray(values[row], scaleDown);

			// Take this vector, and subtract it by every values[i][col] version of that vector.
			for (int subRow = row + 1; subRow < numRows; subRow++)
			{
				double rowScalar = values[subRow][col];
				double [] scaledRow = Vector.scaleArray(values[row].clone(), rowScalar);
				values[subRow] = Vector.arithmeticArray(scaledRow, values[subRow], col, true);
			}
		}

		// Remove the upper triangle. Abstracted to a separate loop because of different bounds.
		// Col <= numRows was added to allow guassian eliminiation to find inverse matricies.
		for (int col = 1; col < numCols - 1 && col <= numRows; col++)
		{
			// Compute only for rows above diagonal. Same process as for bottom triangle.
			for (int row = 0; row < col; row++)
			{
				double scalar = values[row][col];
				if (scalar == 0)
					continue;

				double [] subRow = Vector.scaleArray(values[col].clone(), scalar);
				values[row] = Vector.arithmeticArray(subRow, values[row], 0, true);
			}
		}

		m.setEntries(values);
		return m;
	}

	// Swap rows within a matrix, so that nonzero entries lie along the diagonal.
	public static void reorderMatrix(Matrix m, int numRows, int numCols)
	{
		double [][] values = m.getEntries();
		for (int col = 0; col < numCols; col++)
		{
			// Find the first nonzero, entry in reach column. Applied to each column.
			for (int row = col; row < numRows; row++)
			{
				// Check the rows above and below for a potential swap.
				if (values[row][col] != 0)
					break;

				for (int subRow = row + 1; subRow < numRows; subRow++)
					if (values[subRow][col] != 0)
						{
							swapRows(values, subRow, col); // Prevent swapping back.
							break;
						}
			}
		}
	}

	public static boolean rrefIsInconsistent(double [][] values, int cols)
	{
		for (int i = 0; i < cols - 1; i++)
			if (values[i][i] == 0 && values[i][i + 1] != 0)
				return true;

		return false;
	}
	public static void swapRows(double [][] array, int row1, int row2)
	{
		double [] firstRow = array[row1];
		array[row1] = array[row2];
		array[row2] = firstRow;

		return;
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
