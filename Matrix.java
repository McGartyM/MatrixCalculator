import java.io.*;
import java.util.*;
import java.lang.Object.*;
import java.util.Arrays;
import java.text.*;

class Matrix
{
	public double [][] Array;
	public int rows, cols;

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
		this.Array = new double[rows][cols];

		setMatrix(this, in);
		in.close();
	}

	// Matrix constructor for a blank or "empty" matrix.
	public Matrix(int rows, int cols)
	{
		this.rows = rows;
		this.cols = cols;

		this.Array = new double[rows][cols];
	}

	public static int getSize(double [] array)
	{
		int count = 1;

		//System.out.printf("[From getSize(): %d]", array.length);
		return array.length;
	}
//===========================================================================
// Must extend implementation to non-square matricies
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

	public static double[][] formatArray(double [][] inputArray, int cols, int rows)
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

	public static void print2DArray(double [][] inputArray, int cols, int rows)
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

// Should add an option to print vectors as integers or floats.
	public static void printVector(double [] vector, int dimensions)
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

	public static double [] getVector(Matrix matrix, int column)
	{
		double [] vector;

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

		vector = new double[matrix.rows];

		//System.out.println("Column: " + column + ":");
		for (int i = 0; i < matrix.rows; i++)
		{
			// Column - 1 is because matrices could from 1 - n while arrays go 0 - n
			vector[i] = matrix.Array[i][column - 1];
			//System.out.print(vector[i] + " ");
		}

		//System.out.println();
		return vector;
	}

	public static double getDotProduct(double [] vector1, double [] vector2)
	{
		int dim1, dim2;
		double dotProduct = 0;
		// Should include some way of checking for mismatched dimensions.
		dim1 = getSize(vector1);
		dim2 = getSize(vector2);

		if (dim1 != dim2)
		{
			System.out.println("Unmatched vector dimensions in getDotProduct().");
			return -1;
		}

		for (int i = 0; i < dim1; i++)
			dotProduct += (vector1[i] * vector2[i]);

		//System.out.println("The dotProduct is " + dotProduct);
		//printVector(vector1, vector1.length);
		//printVector(vector2, vector2.length);

		return dotProduct;
	}


	public static double [] addVectors(double [] vector1, double [] vector2, boolean negative)
	{
		int i;
		int dim1 = getSize(vector1);
		int dim2 = getSize(vector2);
		double [] vectorSum;

		if (dim1 != dim2)
		{
			System.out.println("Unmatched vector dimensions in addVectors().");
			return null;
		}
		vectorSum = new double[dim1];

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

	public static double [] scaleVector(double [] vector, double scalar)
	{
		int dim = getSize(vector);

		for (int i = 0; i < dim; i++)
			vector[i] *= scalar;

		return vector;
	}

	public static Matrix gramSchmidt(Matrix matrix)
	{
			int i, count = 0;
			Matrix gram = new Matrix(matrix.rows, matrix.cols);
			double [] vector = getVector(matrix, 1);
			double [] length = new double [matrix.cols];
			double [] dotProducts;
			double [] erase = new double[matrix.rows];
			double [] matrixVector = new double[matrix.rows];

			// Set the first column of gram to the first column of Matrix.

			//printVector(getVector(matrix, 1), matrix.cols);
			for (i = 0; i < matrix.rows; i++)
				gram.Array[i][0] = vector[i];

			// Getting the lengths of each individual vector.
			for (i = 0; i < matrix.cols; i++)
			{
				vector = getVector(matrix, i + 1);
				length[i] = getDotProduct(vector, vector);
				//System.out.print(length[i] + " ");
			}

			// Get the number of computations required for dotProducts loop.
			for (i = 1; i < matrix.rows; i++)
				count += i;

			//System.out.println("Count: " + count);
			int numColumn = 1, j, k;

			dotProducts = new double[count];

			/// Number of vectors that need to be "analyzed".
			for(i = 1; (i <= count) && (i < matrix.cols); i++, numColumn++)
			{
				double [] sumVector = erase;

				// System.out.println("=================================================");
				// System.out.println("Next Iteration: " + (i + 1) + "-" + numColumn);
				// System.out.println("=================================================");

				int z = i;
				for (j = i + 1 , k = numColumn; k >= 1; k--)
				{

					double [] tempVector;
					double subProduct, sizeProduct, scalar;

					// In this loop, j and k represent the column of the matrix, and the dotproduct with V.

					//System.out.print(j + " - " + k + " - " + i + "    ");
						//printVector(sumVector, sumVector.length);

					// Get the subProduct (Ex. X_n * V_(n - 1))
					subProduct = getDotProduct(getVector(matrix, j), getVector(gram, k));
						//System.out.printf("SubProduct of V - %d is: %.4f:", k, subProduct);

					// Get the sizeProduct. (Ex X_n * X_n)
					sizeProduct =  getDotProduct(getVector(gram, k), getVector(gram, k));
						//System.out.printf("DotProduct of V - %d is: %.4f\n", k, sizeProduct);

					// Calculate the scalar for X_n.
					// Correct SubProducts are the moment, need to make sure individual dot products are right.
					scalar = subProduct / sizeProduct;
						//System.out.printf("Vector Scalar: %.4f ", scalar);

					tempVector = getVector(gram, z);

					//System.out.print("[Unmodified tempVector]");
						//printVector(tempVector, tempVector.length);

					// Scale the vector.
					tempVector = scaleVector(tempVector, (-1) * scalar);
					//System.out.print("\n\nTemp Vector Final (post scale):");
						//printVector(tempVector, tempVector.length);

					sumVector = addVectors(sumVector, tempVector, false);

					//printVector(sumVector, getSize(sumVector));
					z--;
				}
				//sumVector = addVectors(sumVector, getVector(matrix, j), false);
				matrixVector = addVectors(getVector(matrix, i + 1), sumVector, false);

				//System.out.printf("Final post-loop ");
				//printVector(matrixVector, matrixVector.length);

				// Setting Values into gram.
				for(int l = 0; l < matrix.rows; l++)
				{
					gram.Array[l][i] = matrixVector[l];
				}

				//System.out.println("Debug: Current Gram at end of iteration");
				//print2DArray(gram.Array, gram.rows, gram.cols);
			}

			System.out.println()
			return gram;
	}

	public static void main(String [] args)
	{
		Matrix matrix = new Matrix();
		Matrix orthogonal;
		System.out.println();

		print2DArray(matrix.Array, matrix.cols, matrix.rows);
		System.out.println("=========================================");
		orthogonal = gramSchmidt(matrix);
	}
}

//#####################################################################################3
			// TO DO:
			//ssssssssssssssss
			// Implement some way of singling out zero columns
			// Testing out the using Github with Atom.
