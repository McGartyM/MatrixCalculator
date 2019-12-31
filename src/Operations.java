package core;

import core.Matrix;
import core.Vector;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.lang.Math;

// Primary class which holds all the Matrix Operations..
// None of these methods should modify the Matricies passed into them.
// As of now, the operations should be non-destructive but some more testing is necessary
// to confirm this behavior.
public class Operations
{

	// Arithmetic Operations
	// ===========================================================================

	// Given a Matrix, scale it by the given parameter.
	// Invalid matrix objects will be returned, if passed.
	public static Matrix scale(Matrix m, double scalar)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.scale()", "original matrix");
			return m;
		}

		Matrix n = m.copy();
		Vector [] v = n.vectors();
		for(int i = 0; i < n.cols(); i++)
			v[i] = v[i].scale(scalar);

		return new Matrix(v);
	}

	// Given two Matricies, it computes and returns their sum (m + n).
	// Identical Matrix dimensions are required, else null is returned.
	public static Matrix add(Matrix m, Matrix n)
	{
		if (m == null || n == null || !m.isValid() || !n.isValid())
		{
			valid(m, "Operations.add()", "null");
			valid(n, "Operations.add()", "null");
			return null;
		}

		if (m.rows() != n.rows() || m.cols() != n.cols())
		{
			System.err.println("Non-identical Matrix dimensions in Operations.add():");
			System.err.printf("Argument m is (%d x %d) and n is (%d x %d).", m.rows(), m.cols(), n.rows(), n.cols());
			return null;
		}

		Vector [] x = m.vectors();
		Vector [] y = n.vectors();
		Vector [] z = new Vector[m.cols()];

		for (int i = 0; i < m.cols(); i++)
			z[i] = x[i].add(y[i]);

		return new Matrix(z);
	}

	// Given two Matricies, it computes and returns their difference. (m - n)
	// Identical Matrix dimensions are required, else null is returned.
	public static Matrix subtract(Matrix m, Matrix n)
	{
		if (m == null || n == null || !m.isValid() || !n.isValid())
		{
			valid(m, "Operations.subtract()", "null");
			valid(n, "Operations.subtract()", "null");
			return null;
		}

		if (m.rows() != n.rows() || m.cols() != n.cols())
		{
			System.err.println("Non-identical Matrix dimensions in Operations.subtract():");
			System.err.printf("Argument m is (%d x %d) and n is (%d x %d).", m.rows(), m.cols(), n.rows(), n.cols());
			return null;
		}

		Vector [] x = m.vectors();
		Vector [] y = n.vectors();
		Vector [] z = new Vector[m.cols()];

		for (int i = 0; i < m.cols(); i++)
			z[i] = x[i].subtract(y[i]);

		return new Matrix(z);
	}


	// Given two Matricies, it multiplies them in the order they are passed.
	// Requires (m x n) and (n x p) dimensioned arguments, else null is returned.
	public static Matrix multiply(Matrix m, Matrix n)
	{
		if (m == null || n == null || !m.isValid() || !n.isValid())
		{
			valid(m, "Operations.multiply()", "null");
			valid(n, "Operations.multiply()", "null");
			return null;
		}

		if (m.cols() != n.rows())
		{
			System.err.println("Non-identical inner Matrix dimensions in Operations.multiply():");
			System.err.printf("Argument m is (%d x %d) and n is (%d x %d).", m.rows(), m.cols(), n.rows(), n.cols());
			return null;
		}

		int numRows = m.rows();
		int numCols = n.cols();

		double [][] mEntries = m.entries();
		Vector [] nVectors = n.vectors();
		Vector mRow = new Vector(m.cols()); // since m.cols() == n.rows()

		double [][] newMatrix = new double[numRows][numCols];
		for (int i = 0; i < numRows; i++)
		{
			mRow.setEntries(mEntries[i]);

			for (int j = 0; j < numCols; j++)
				newMatrix[i][j] = mRow.dotProduct(nVectors[j]);
		}

		return new Matrix(newMatrix);
	}

	// Given a Matrix, raised it to the power of the passed valued.
	// This function does allow for negative exponents.
	// Any invalid or non-square Matrix will return the argument.
	public static Matrix power(Matrix m, int power)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.power()", "original matrix");
			return m;
		}

		if (!squareCheck(m, "Operations.power()", "original matrix"))
			return m;

		if (power == 0)
			return identity(m.rows()); // Since m is square.

		// Note that M^(-n) = ( M^(-1) )^n
		// Hence, the inverse can be computed just once.
		if (power < 0)
		{
			m = invert(m);

			if (m == null)
			{
				System.err.println("Matrix " + m + " does not have an inverse.");
				System.err.println("Returning the original Matrix.");
				return m;
			}
		}

		// Create a local copy, so it does not square matrix each iteration.
		Matrix retVal = m.copy();
		int pow = Math.abs(power);
		for (int i = 1; i < pow; i++)
			retVal = multiply(retVal, m);

		return retVal;
	}

	// Properties:
	// ===========================================================================

	// Given a Matrix, calculate and return it's trace.
	// For this implementation, the trace is only defined for square Matricies.
	// If an invalid Matrix is passed, Double.NaN is returned.
	public static double trace(Matrix m)
	{
		Double retVal = findTrace(m);
		if (retVal != null)
			return retVal.doubleValue();

		System.err.println("Trace is undefined, returning NaN");
		return Double.NaN;
	}

	// Given a Matrix, calculate and return its rank.
	// An invalid matrix will cause Integer.MIN_VALUE to be returned as an *error*.
	public static int rank(Matrix m)
	{
		Integer retVal = computeRank(m);
		if (retVal != null)
			return retVal.intValue();

		return Integer.MIN_VALUE;
	}

	// Returns whether or not the given Matrix has Linearly Independent columns.
	// In other words, the rank is equal to the minimum dimension of the Matrix.
	// Invalid Matricies will default to a false return value.
	public static boolean isLinDep(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.isLinDep()", "false");
			return false;
		}

		// Full Rank guarentees linear independence.
		return (rank(m) == Math.min(m.rows(), m.cols()));
	}

	// Returns the eigenvalues of a given Matrix using Gram-Schmidt and QR decomposition.
	// The returned array WILL contained duplicate eigenvalues. (Multiplicity is preserved)
	// Invalid Matricies will return a null reference.
	public static double [] eigenvalues(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid (m, "Operations.eigenvalues()", "null");
			return null;
		}

		// Create a local matrix object.
		Matrix reference = m.copy();
		int dim = Math.min(m.rows(), m.cols());

		// Store the results of each iteration in cache and update A.
		double [][] a = reference.entries();
		Matrix A = new Matrix(a);
		Matrix [] cache;

		// Manually do the first two iterations. R = cache[1], R = cache[0]
		cache = qr(A);
		A = multiply(cache[1], cache[0]);
		double [] old = new double [dim];

		// Copying the updated A matrix's contents.
		for (int i = 0; i < dim; i++)
			old[i] = A.entries()[i][i];

		cache = qr(A);
		A = multiply(cache[1], cache[0]);
		double [] current = new double [dim];

		for (int i = 0; i < dim; i++)
			current[i] = A.entries()[i][i];

		int its = 5;
		double threshold = Math.pow(m.epsilon(), 2);
		while (eigenError(old, current, threshold))
		{
			for (int i = 0; i < its; i++)
			{
				cache = qr(A);
				A = multiply(cache[1], cache[0]);
			}

			// copy eigenvalues into the matrix.
			for (int i = 0; i < dim; i++)
			{
				old[i] = current[i];
				current[i] = A.entries()[i][i];
			}
		}

		// Parsing the eigenvalues from the main diagonal of the Matrix.
		double [] eigenvalues = new double[m.rows()];
		for (int i = 0; i < m.rows(); i++)
		{
			eigenvalues[i] = A.entries()[i][i];

			// Check if the eigenvalue is almost an integer and round it if it is.
			int floor = (int) Math.floor(eigenvalues[i]);
			int ceil = (int) Math.ceil(eigenvalues[i]);

			if (Vector.nearlyEqual(eigenvalues[i], Math.floor(eigenvalues[i]) , m.epsilon()))
				eigenvalues[i] = Math.floor(eigenvalues[i]);

			if (Vector.nearlyEqual(eigenvalues[i], Math.ceil(eigenvalues[i]), m.epsilon()))
				eigenvalues[i] = Math.ceil(eigenvalues[i]);
		}

		return eigenvalues;
	}

	// -- WIP --
	// Given a Matrix, compute it's eigenvectors.
	// If an Invalid Matrix is passed, null will be returned.

	// -- Note --
	// As of now, it may not find all of the eigenvectors for larger matricies.
	// The issue occurs when a Matrix has large multiplicities in its eigenvalues.
	// Needs improvements on parsing the results after rref from the (A - (lambda I)) | 0 matrix.
	public static Vector [] eigenvectors(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.eigenvectors()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.eigenvectors()", "null"))
			return null;

		// Zero, or identity matricies, have identity eigenvectors.
		if (m.equals(new Matrix(m.rows())) || m.equals(identity(m.rows())))
			return identity(m.rows()).vectors();

		double [] eigenvalues = eigenvalues(m);

		// Remove duplicate eigenvalues
		HashSet <Double> h = new HashSet<>();
		ArrayList <Double> evs = new ArrayList<>();
		for (int i = 0; i < eigenvalues.length; i++)
			if (h.add(eigenvalues[i]))
				evs.add(eigenvalues[i]);

		int dim = evs.size();
		Vector [] eigenvectors = new Vector [dim];
		double [][] reference = m.entries();

		ArrayList <Vector> returnVectors = new ArrayList<>();
		// Create dim number of reference matricies and store.
		for (int i = 0, vecNumber = 0; i < dim; i++)
		{
			Matrix subresult = new Matrix(reference);
			Matrix lambda = identity(m.rows());
			lambda = scale(lambda, evs.get(i));

			Matrix eigenMatrix = subtract(subresult, lambda);
			eigenMatrix = rref(eigenMatrix, true);

			boolean [] colPivots = findPivots(eigenMatrix, true);
			boolean [] rowPivots = findPivots(eigenMatrix, false);

			// The number of non-pivot columns is the number of eigenvectors.
			int numEigen = 0;
			for (int z = 0; z < colPivots.length; z++)
				if (!colPivots[z])
					numEigen++;

			double [] vector = new double [m.cols()];
			double [][] entries = eigenMatrix.entries();

			// Not Necessary:
			Vector [] multiples = new Vector[m.cols()];
			for (int z = 0; z < m.cols(); z++)
				multiples[z] = new Vector(m.rows());

			System.out.println("Eigenvalue[ " + i + " ] = " + evs.get(i));
			eigenMatrix.print();

			// Check the individual reduced eigenvalue matrix.
			for (int j = 0; j < m.rows(); j++)
			{
				// Implies the row is empty, no point checking.
				if (!rowPivots[j])
					continue;

				for (int k = j; k < m.cols(); k++)
				{
					System.out.println("Setting in vector: " + k + " at index" + j + " with " + entries[j][k]);
					multiples[k].setEntry(j, (-1) * entries[j][k]);

					// Copy Values into the row:
					if (!colPivots[k])
					{
						multiples[k].setEntry(k, 1);
					}
				}
			}

			for (int z = 0; z < multiples.length; z++)
				if (!colPivots[z])
					returnVectors.add(multiples[z]);
		}

		Vector [] retVal = new Vector [returnVectors.size()];
		for (int i = 0; i < retVal.length; i++)
		{
			retVal[i] = returnVectors.get(i);
			if (retVal[i] != null)
				retVal[i].print();
		}

		return retVal;
	}

	// Given a Matrix, calculate and return the Determinant using LU Decomposition.
	// When invalid Matricies are passed, Double.NaN will be returned.
	public static double determinant(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.determinant()", "Double.NaN");
			return Double.NaN;
		}

		if (!squareCheck(m, "Operations.determinant()", "Double.NaN"))
			return Double.NaN;

		// The zero Matrix has determinant zero.
		if (m.equals(new Matrix(m.rows())))
			return 0;

		// Occurs when the matrix is non-invertible.
		Matrix [] lu = lu(m);
		if (lu == null)
			return 0;

		double [][] lValues = lu[1].entries();
		double [][] uValues = lu[2].entries();

		// Compute the product across each trace.
		double detLU = 1;
		for (int i = 0; i < m.rows(); i++)
			detLU *= (lValues[i][i] * uValues[i][i]);

		if (sgnPerm(m) % 2 == 1)
			detLU *= -1;

		return detLU;
	}

	// Given a Matrix, return it's transpose.
	// An invalid Matrix will return a null reference.
	public static Matrix transpose(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.transpose()", "null");
			return null;
		}

		int numVectors = m.rows();
		Vector [] v = new Vector[numVectors];
		double [][] mEntries = m.entries();

		for (int i = 0; i < numVectors; i++)
			v[i] = new Vector(mEntries[i]);

		return new Matrix(v);
	}

	// Standard operations
	// ===========================================================================

	// Instantiates and returns an N x N identity matrix.
	// Values of N less than one will return a null reference.
	public static Matrix identity(int N)
	{
		if (N <= 0)
		{
			System.err.println("-- Matrix dimension cannot be <= 0 in Operations.identity() -- ");
			System.err.println("-- Returning null. --");
			return null;
		}

		double [][] values = new double [N][N];
		for (int i = 0; i < N; i++)
			values[i][i] = 1;

		return new Matrix(values);
	}

	// Given two matricies, M and N, they are appended into a single Matrix.
	// Per usual, invalid Matrix arguments will return null.
	public static Matrix append(Matrix m, Matrix n)
	{
		if (m == null || n == null || !m.isValid() || !n.isValid())
		{
			valid(m, "Operations.append()", "null");
			valid(n, "Operations.append()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.append()", "null"))
			return null;

		int mCols = m.cols();
		int nCols = n.cols();
		Vector [] newVecs = new Vector [mCols + nCols];

		// Append vectors from m
		Vector [] vecs = m.vectors();
		for (int i = 0; i < mCols; i++)
			newVecs[i] = vecs[i];

		// Append vectors from n
		vecs = n.vectors();
		for (int i = 0; i < nCols; i++)
			newVecs[i + mCols] = vecs[i];

		return new Matrix(newVecs);
	}

	// Splits a Matrix into two distinct Matricies based on the size parameter.
	// Size determines the number of columns in the first Matrix and the remaining columns
	// will comprise the second Matrix.
	// An invalid Matrix and out of bounds sizes will return null.
	public static Matrix [] split(Matrix m, int size)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.split()", "null");
			return null;
		}

		if (size <= 0 || size > m.cols())
		{
			System.err.println("-- Invalid split point in Operations.split() --");
			System.err.println("-- Returning null. --");
			return null;
		}

		int appendedSize = m.cols();

		Vector [] first = new Vector[size];
		Vector [] second = new Vector[appendedSize - size];
		Vector [] reference = m.vectors();

		for (int i = 0; i < size; i++)
			first[i] = reference[i];

		for (int i = size; i < appendedSize; i++)
			second[i - size] = reference[i];

		Matrix []  retVal = new Matrix[2];
		retVal[0] = new Matrix(first);
		retVal[1] = new Matrix(second);

		return retVal;
	}

	// Given a Matrix, it is reduced to its reduced row echelon form through Gauss-Jordan Elimination.
	// The sorted parameter, if set true, will run three passes of rref which improves accuracy.
	// Null references will be returned if the passed Matrix is invalid.
	public static Matrix rref(Matrix m, boolean sorted)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.rref()", "null");
			return null;
		}

		Matrix n = m.copy();
		reorderMatrix(n);

		if (!sorted)
		{
			reducedREF(n);
			return n;
		}

		for (int i = 0; i < 3; i++)
		{
			reducedREF(n);
			reorderMatrix(n);
		}

		return n;
	}

	// Given a Matrix, it returns whether or not the Matrix is inconsistent.
	// Invalid Matricies will default to false when passed.
	public static boolean isInconsistent(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.isInconsistent()", "false");
			return false;
		}

		int cols = m.cols() - 1;
		int rows = m.rows() - 1;
		double [][] values = m.entries();

		boolean isInconsistent = false;
		for (int i = 0; i <= rows; i++)
		{
			int count = 0;
			for (int j = 0; j <= cols; j++)
			{
				if (values[i][j] != 0)
					break;

				count++;
			}

			if(count == cols && values[i][cols] != 0)
				isInconsistent |= true;
		}

		return isInconsistent;
	}

	// Given a Matrix, compute its inverse through Gauss-Jordan reduction.
	// The algorithm appends an identity Matrix to m and computes it's rref.
	// Currently restricted to square Matricies.
	// Non-square and invalid Matricies will return null.

	// -- Note --
	// Currently the method assumes the Matrix is invertible.
	// Therefore, the Matrix may not have a *conventional* inverse if its determinant is zero.
	// The method will still return a valid Matrix, which is normally it's Pseudo-Inverse.
	public static Matrix invert(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.invert()", "null");
			return null;
		}

		// Only Square Matricies are invertible.
		if (!squareCheck(m, "Operations.invert()", "null"))
			return null;

		// No point doing the reduction if the passed Matrix is an identity.
		if (m.equals(new Matrix(m.rows())))
			return m;

		// Can be uncommented to allow for tranditional inverse computation.
		// if (!isInvertible(m))
		// {
		// 	System.out.printf("Matrix (%s) is non-invertible.\n", m);
		// 	return null;
		// }

		m = append(m, identity(m.rows()));
		Matrix [] retVal = split(rref(m, true), m.rows() + 1 / 2);

		return retVal[1];
	}

	// Given a Matrix, returns if it is invertible through its determinant and rank.
	// An invertible Matrix has full rank and a non-zero determinant.
	// Non-square and invalid matricies will default to a false return.
	public static boolean isInvertible(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.isInvertible()", "false");
			return false;
		}

		if (!squareCheck(m, "Operations.isInvertible()", "false"))
			return false;

		// The inverse of the zero matrix, is arguably any Matrix; including itself.
		if (m.equals(new Matrix(m.rows())))
			return true;

		boolean isInvertible = true;
		int rank = rank(m);
		if (rank != m.rows())
			isInvertible |= false;

		double determinant = determinant(m);
		if (determinant == 0)
			isInvertible |= false;

		return isInvertible;
	}

	// Given a Matrix, compute and return it's cofactor Matrix.
	// Null will be returned if the passed Matrix is invalid or non-square.
	public static Matrix cofactor(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operation.cofactor()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.cofactor()", "null"))
			return null;

		if (m.equals(new Matrix(m.rows())))
			return m;

		int dim = m.rows();
		double [][] cofactor = new double[dim][dim];
		Matrix temp = new Matrix(dim - 1);

		// Calculate the determinant for each minor of the M matrix.
		for (int i = 0; i < dim; i++)
			for (int j = 0; j < dim; j++)
			{
				Double currentDet = cofactor(m, temp, i + 1, j + 1);
				cofactor[i][j] = (Double.isNaN(currentDet)) ? 0 : currentDet;
			}

		return new Matrix(cofactor);
	}

	// Given a Matrix, compute and return its adjugate; the cofactor transposed.
	// Same restriction as the cofactor method. Null will be returned if the passed
	// Matrix is non-square or invalid.
	public static Matrix adjugate(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.adjugate()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.adjugate()", "null"))
			return null;

		return transpose(cofactor(m));
	}

	// Given a Matrix, scale all of its columns vectors such that they become unit vectors.
	// Requires the Matrix object be valid, else null is returned.
	public static Matrix normalize(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.normalize()", "original matrix");
			return m;
		}

		Matrix n = m.copy();
		Vector [] vecs = n.vectors();
		for (int i = 0; i < vecs.length; i++)
		{
			double magnitude = Math.sqrt(vecs[i].dotProduct(vecs[i]));

			// Ignore div by zeros.
			if (Vector.nearlyEqual(magnitude, 0, m.epsilon()))
				continue;

			n.setVector(i, vecs[i].scale((double) 1 / magnitude));
		}

		return n;
	}

	// Given a Matrix, it is orthogonalize by the Gram-Schmidt process.
	// This method does NOT return an orthonormal matrix.
	// Invalid matricies will return null if passed.
	public static Matrix orthogonalize(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.orthogonalize()", "null");
			return null;
		}

		// Arbitrary choice of three iterations to improve numeric stability and error.
		int iterations = 3;
		for (int i = 0; i < iterations; i++)
			m = gramSchmidt(m);

		return m;
	}

	// Decompositions:
	// ==========================================================================

	// Given a Matrix, it returns an array with its QR decomposition.
	// An invalid or null Matrix argument will return null.
	public static Matrix [] qr(Matrix m)
	{
		if (m == null | !m.isValid())
		{
			valid(m, "Operations.qr", "null");
			return null;
		}

		// Assumed that any matrix has an othogonal equivalent.
		Matrix Q = orthogonalize(m);
		Q = normalize(Q);

		Matrix QT = transpose(Q);
		Matrix R = multiply(QT, m);

		Matrix [] retVal = new Matrix[2];
		retVal[0] = Q;
		retVal[1] = R;

		// Verification that the decomposition is true.
		if (m.equals(multiply(Q, R)))
		{
			return retVal;
		}

		System.err.println("Something went wrong in Operations.qr()");
		return null;
	}

	// Returns the LU decomposition of a Matrix.
	// If the matrix is not invertible, there is no guarentee an LU decomp exists.
	// Therefore null will be returned if the argument is invalid, or no inverse exists.
	public static Matrix [] lu(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.lu()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.lu()", "null"))
			return null;

		int dim = m.rows();
		double [][] lValues = new double [dim][dim];
		double [][] uValues = new double [dim][dim];

		// Set the main diagonal of L to 1.
		for (int i = 0; i < dim; i++)
			lValues[i][i] = 1;

		double [][] entries = m.copy().entries();
		Matrix permutation = identity(dim);

		for (int col = 0; col < dim; col++) // 1 -> dim
		{
			boolean wasSwapped = false;

			for (int row = dim - 1; row > col; row--)
			{
				// If a pivot is zero, swap it with a non-zero row.
				if (entries[col][col] == 0)
				{
					for (int swap = col; swap < dim; swap++)
					{
						if (entries[swap][col] != 0)
						{
							// Swapping rows:
							double [] temp = entries[swap];
							entries[swap] = entries[col];
							entries[col] = temp;

							wasSwapped = true;
							permutation.swapRows(swap, col);
						}
					}
				}

				// Copy the scalar into the L Matrix.
			 	lValues[row][col] = (wasSwapped) ? 0 : entries[row][col] / entries[col][col];

				for (int i = 0; i < dim; i++)
						entries[row][i] -= (lValues[row][col] * entries[col][i]);
			}

			uValues[col] = entries[col];
		}

		Matrix [] retVal = new Matrix [3];
		retVal[0] = transpose(permutation);
		retVal[1] = new Matrix(lValues);
		retVal[2] = new Matrix(uValues);

		Matrix result = multiply(retVal[0], retVal[1]);
		result = multiply(result, retVal[2]);

		Matrix permuted = multiply(permutation, m);
		if (!permuted.equals(result))
		{
			System.out.println("No LU decomposition exists for this matrix.");
			return null;
		}

		return retVal;
	}

	// Given a Matrix, it computes returns an array with its Column and Row space equivalents.
	// It converts an (a x b) matrix into an (a x r) and (r x b) matricies.
	// Invalid or uninitialized matricies will return null.
	public static Matrix [] rankDecomp(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.rankDecomposition()", "null");
			return null;
		}

		// Reference to starting contents, for creating column matrix.
		double [][] reference = m.copy().entries();

		// rref the matrix.
		Matrix reduced = rref(m, true);

		int rows = reduced.rows();
		int cols = reduced.cols();
		double [][] entries = reduced.entries();

		ArrayList <Integer> keepRows = new ArrayList<>();
		ArrayList <Integer> keepCols = new ArrayList<>();

		// Determine which rows have pivots, and keep them
		for (int i = 0; i < rows; i++)
			for (int j = i; j < cols; j++)
				if (!Vector.nearlyEqual(entries[i][j], 0, m.epsilon()))
				{
					keepRows.add(i);
					keepCols.add(j);
					break;
				}

		double [][] rv = (keepRows.size() == 0) ? new double [rows][cols] :
		                                          new double [keepRows.size()][cols] ;
		double [][] cv = (keepCols.size() == 0) ? new double [rows][rows] :
		                                          new double [rows][keepCols.size()];

		for (int i = 0; i < keepRows.size(); i++)
		{
			int index = keepRows.get(i);
			for (int j = 0; j < cols; j++)
				rv[i][j] = entries[index][j];
		}
		// Fill the new column values array.
		for (int i = 0; i < keepCols.size(); i++)
		{
			int index = keepCols.get(i);
			for (int j = 0; j < rows; j++)
				cv[j][i] = reference[j][index];
		}

		Matrix [] retVal = new Matrix[2];
		retVal[0] = new Matrix(cv);
		retVal[1] = new Matrix(rv);

		// Can again confirm that the values here are equal to the starting matrix.
		if (!m.equals( multiply (retVal[0], retVal[1]) ))
		{
			System.err.println("An unknown error has occured");
			return null;
		}

		return retVal;
	}

	// Given a matrix, it computes and returns it's diagonal equivalent.
	// -- Note --
	// Not every Matrix is guarenteed to have a diagonal equivalence.
	// Therefore, the eigenvalues that do exist will be included, if they exist.
	// Per usual, non-square and invalid matricies will return null.
	public static Matrix diagonalize(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.diagonalize()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.diaglonalize()", "null"))
			return null;

		double [] eigenvalues = eigenvalues(m);
		Vector [] vecs = new Vector [m.rows()];
		for (int i = 0; i < m.rows(); i++)
		{
			vecs[i] = new Vector(m.rows());
			vecs[i].setEntry(i, eigenvalues[i]);
		}

		return new Matrix(vecs);
	}


	// Decomposes a Matrix into the form of VDV(^-1), where  V is a Matrix of eigenvectors
	// and D is a Diagonal Matrix.
	// Not all Matricies are guarenteed to have a valid eigendecomposition.
	// If the eigenvector Matrix is not full rank, the calculation will return null.
	// In addition, non-square and invalid matricies will return null.
	public static Matrix [] eigenDecomp(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.eigenDecomp()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.eigenDecomp()", "null"))
			return null;

		Matrix v = new Matrix (eigenvectors(m));
		Matrix inv = invert(v);
		Matrix d = diagonalize(m);

		if (v == null || inv == null || d == null)
			return null;

		// Chec for accurate decompositions.
		Matrix M = multiply(v, multiply(d, inv));

		if (!M.equals(m))
		 	System.err.println("An unknown error has occured in Operations.eigenDecomp()");

		return new Matrix []{v, d, inv};
	}

	// Private Helper Functions:
	// ===========================================================================

	// Private helper function to calculate the trace.
	// It allows the use of null as an *error code* for when an invalid Matrix is passed.
	private static Double findTrace(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.trace()", "null");
			return null;
		}

		if (!squareCheck(m, "Operations.trace()", "null"))
			return null;

		double [][] entries = m.entries();

		double trace = 0;
		for (int i = 0; i < m.rows(); i++)
			trace += entries[i][i];

		return trace;
	}

	// Private helper method in rank calculation.
	private static Integer computeRank(Matrix m)
	{
		if (m == null || !m.isValid())
		{
			valid(m, "Operations.rank()", "Integer.MIN_VALUE");
			return null;
		}

		Matrix reduced = rref(m, true);

		int cols = reduced.cols();
		int rows = reduced.rows();

		int rank = 0;
		double [][] entries = reduced.entries();

		// Find the number of pivots.
		for (int i = 0; i < rows; i++)
			for (int j = i; j < cols; j++)
				if (entries[i][j] != 0)
				{
					rank++;
					break;
				}

		return rank;
	}

	// Private error checking method to determine if two Vectors are *identical* up to
	// some user-defined error. It assumed that both of the arrays are valid, or else
	// this function would not have been called.
	private static boolean eigenError(double [] old, double [] current, double error)
	{
		for (int i = 0; i < old.length; i++)
			if (!Vector.nearlyEqual(old[i], current[i], error))
				return true;

		return false;
	}

	// Columns parameter allows user to check for pivots in either the rows and columns.
	private static boolean [] findPivots(Matrix m, boolean columns)
	{
		int numRows = m.rows();
		int numCols = m.cols();
		double [][] values = m.entries();

		int numPivots = Math.min(numRows, numCols);
		boolean [] pivots = new boolean [numPivots];

		for (int row = 0; row < numRows; row++)
		{
				for (int col = row; col < numCols; col++)
				{
					if (!Vector.nearlyEqual(values[row][col], 0, m.epsilon()))
					{
						if (columns)
							pivots[col] = true;
						else
							pivots[row] = true;
						break;
					}
				}
		}

		return pivots;
	}

	// Private helper method which applies the rref operation onto the Matrix.
	// Since checking happens before this method is ever called, it assumed the Matrix is valid.
	private static void reducedREF(Matrix m)
	{
		int numCols = m.cols();
		int numRows = m.rows();
		double [][] values = m.entries();

		removeLower(m);
		removeUpper(m);
	}

	// Private helper method to calculate the determinant of a Matrix's minors.
	// The row and col parameter determine which row and column to omit, creating the minor.
	// This function assumes the matrix is initialized, or else it would not be called.
	private static Double cofactor(Matrix m, Matrix minor, int row, int col)
	{
		double [][] minorEntries = minor.entries();
		double [][] entries = m.entries();

		// Assumed that row and col will be indexed from 1.
		int numRows = m.rows();
		int numCols = m.cols();

		// Copying the minor 2D array from the m matrix.
		for (int i = 0, mr = 0; i < numRows; i++, mr++)
		{
			if (i == row - 1)
			{
				mr--;
				continue;
			}

			for (int j = 0, mc = 0; j < numCols; j++, mc++)
			{
				if (j == (col - 1))
				{
					mc--;
					continue;
				}

				minorEntries[mr][mc] = entries[i][j];
			}
		}

		minor.setEntries(minorEntries);
		double retVal = determinant(minor);

		// In case the determinant is undefined, which is possible.
		if (Double.isNaN(retVal))
			return Double.NaN;

		retVal *= ((row + col) % 2 == 1) ? -1 : 1;
		return retVal;
	}

	// Private helper method which applies the Gram-Schmidt algorithm to a matrix.
	// It assumes the matrix is valid, or else it would not be called.
	private static Matrix gramSchmidt(Matrix m)
	{
		Vector [] vecs = m.vectors();
		Vector [] newVecs = new Vector [vecs.length];

		newVecs[0] = new Vector(vecs[0].entries());

		// Compute for all vectors
		for (int i = 1; i < vecs.length; i++)
		{
			Vector projectedSum = new Vector(new double [newVecs[0].length()]);

			for (int j = 0; j < i ; j++)
			{
				Vector projected = Vector.projection(vecs[i], newVecs[j]);
				projectedSum = projectedSum.add(projected);
			}

			newVecs[i] = vecs[i].subtract(projectedSum);
		}

		return new Matrix(newVecs);
	}

	// Swap the rows of a matrix, such that non-zero entries lie along the main diagonal.
	// Assumed that the Matrix is already valid, else it would not have been called.
	// The method also assumes a reference to the matrix m is passed in.
	private static void reorderMatrix(Matrix m)
	{
		int numRows = m.rows();
		int numCols = m.cols();
		double [][] values = m.entries();

		int [] pivotColumns = new int [numRows];
		Arrays.fill(pivotColumns, (int) 1e9); // 1e9 ~= *infinity*

		// Determine pivot columns.
		for (int row = 0; row < numRows; row++)
			for (int col = 0; col < numCols; col++)
				if (!Vector.nearlyEqual(values[row][col], 0, m.epsilon()))
				{
					pivotColumns[row] = col;
					break;
				}

		double [][] newOrder = new double [numRows][numCols];
		boolean [] choosen = new boolean [numRows];

		// Insert each row in ascending order.
		for (int i = 0, z = 0; i < pivotColumns.length; i++)
		{
			int min = Integer.MAX_VALUE;
			int minIndex = -1;

			for (int j = 0; j < pivotColumns.length; j++)
				if (pivotColumns[j] <= min && !choosen[j])
				{
					min = pivotColumns[j];
					minIndex = j;
				}

			if (minIndex >= 0)
			{
				choosen[minIndex] = true;
				System.arraycopy(values[minIndex], 0, newOrder[z++], 0, numCols);
			}
		}

		m.setEntries(newOrder);
	}

	// "Removes" the botton triangle of a Matrix through row reductions and row operations.
	// Assumed that the Matrix is already valid, else it would not be called.
	private static void removeLower(Matrix m)
	{
		int numRows = m.rows();
		int numCols = m.cols();
		double [][] values = m.entries();

		// Remove the bottom triangle
		int numPivots = Math.min(numRows, numCols);
		for (int col = 0, seenPivots = 0; col < numCols && seenPivots < numPivots; col++)
		{
			double pivotValue = 0;
			int pivotRow = -1;

			// Finding the value at the pivot index.
			for (int row = col; row < numRows; row++)
			{
				if (!Vector.nearlyEqual(values[row][col], 0, m.epsilon()))
				{
					pivotValue = values[row][col];
					pivotRow = row;
					seenPivots++;
					break;
				}
			}

			// No pivot is found
			if (Vector.nearlyEqual(pivotValue, 0, m.epsilon()))
				continue;

			// Scale down so the pivot is one.
			double scalar = 1 / pivotValue;
			for (int i = col; i < numCols; i++)
				values[pivotRow][i] *= scalar;

			// Subtract the pivot from all rows below it.
			for (int subRow = pivotRow + 1; subRow < numRows; subRow++)
			{
				double subScalar = values[subRow][col];
				for (int i = col; i < numCols; i++)
				{
					values[subRow][i] -= (subScalar * values[pivotRow][i]);
					if (Vector.nearlyEqual(values[subRow][i], 0, m.epsilon()))
						values[subRow][i] = 0;
				}
			}
		}

		m.setEntries(values);
	}

	// "Removes" the upper triangle of a Matrix through row reductions and operations.
	// Assumed that Matrix is valid, else it would not have been called.
	private static void removeUpper(Matrix m)
	{
		int numRows = m.rows();
		int numCols = m.cols();
		double [][] values = m.entries();

		for (int row = 0, pivotCol = 0; row < numRows; row++)
		{
			double pivotValue = 0;

			// Find the pivot column in a specific row.
			for (int subCol = pivotCol; subCol < numCols; subCol++)
				if (!Vector.nearlyEqual(values[row][subCol], 0, m.epsilon()))
				{
					pivotValue = values[row][subCol];
					pivotCol = subCol;
					break;
				}

			// No pivot in the row, should not be called often.
			if (Vector.nearlyEqual(pivotValue, 0, m.epsilon()))
				continue;

			// Scale down the array.
			double scalar = 1 / pivotValue;
			for (int i = pivotCol; i < numCols; i++)
				values[row][i] *= scalar;

			// Subtract across each row above it.
			for (int subRow = row - 1; subRow >= 0; subRow--)
			{
				double subScalar = values[subRow][pivotCol];

				for (int i = pivotCol; i < numCols; i++)
					values[subRow][i] -= subScalar * values[row][i];
			}
		}

		m.setEntries(values);
	}

	// Returns the sign of a permutation Matrix.
	// Essentially a copy and paste of the LUDecomp function. Since there is  an associated
	// permutation matrix with the decompositions. The redundancy could be eliminated with
	//a wrapper method method around the function, but I am leaving it for now.
	private static int sgnPerm(Matrix m)
	{
		int dim = m.rows();
		double [][] lValues = new double [dim][dim];
		double [][] uValues = new double [dim][dim];

		// Create Identity for the L matrix.
		for (int i = 0; i < dim; i++)
			lValues[i][i] = 1;

		double [][] entries = m.copy().entries();
		Matrix permutation = identity(dim);

		int operations = 0;
		for (int col = 0; col < dim; col++)
		{

			boolean wasSwapped = false;
			for (int row = dim - 1; row > col; row--)
			{

				// If a pivot is zero, swap it with a non-zero row.
				if (entries[col][col] == 0)
				{
					for (int swap = col; swap < dim; swap++)
					{
						if (entries[swap][col] != 0)
						{
							// Swap the rows
							double [] temp = entries[swap];
							entries[swap] = entries[col];
							entries[col] = temp;

							permutation.swapRows(swap, col);
							wasSwapped = true;
							operations++;
						}
					}
				}

				// Copy the scalar into the L Matrix.
				lValues[row][col] = (wasSwapped) ? 0 : entries[row][col] / entries[col][col];

				for (int i = 0; i < dim; i++)
						entries[row][i] -= (lValues[row][col] * entries[col][i]);
			}

			uValues[col] = entries[col];
		}

		return operations;
	}

	// Error-checking methods
	//========================================================================

	// Error-logging function. When a Matrix is invalid it prints to the console what
	// the specific error is. It also can be used to check is a Matrix object is properly initialized.
	private static boolean valid(Matrix m, String function, String retVal)
	{
		if (m == null)
		{
			System.err.printf("-- Null matrix was passed into %s. -- \n", function);
			System.err.printf("-- Returning %s. --\n", retVal);
			return false;
		}

		if (!(m instanceof Matrix))
		{
			System.err.printf("-- Non-Matrix object ( %s ) was passed into %s. --\n", m, function);
			System.err.printf("-- Returning %s. --\n", retVal);
			return false;
		}

		if (!m.isValid())
		{
			System.err.printf("--Invalid Matrix ( %s ) was passed into %s. -- \n", m, function);
			System.err.printf("--Returning %s. --\n", retVal);
			return false;
		}

		return true;
	}

	// Just a simple method to check if a Matrix is square.
	// Many computations rely on the Matrix being square, so I abstracted the check.
	private static boolean squareCheck(Matrix m, String function, String retVal)
	{
		if (m.rows() != m.cols())
		{
			System.err.printf("-- Matrix ( %s ) is non-square in %s. --\n", m, function);
			System.err.printf("-- Returning %s. --\n", m, function);
			return false;
		}

		return true;
	}
}
