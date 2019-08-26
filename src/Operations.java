package core;

import core.Matrix;
// This java file will hold the source code to any operations which act upon a matrix.
public class Operations
{
	public static Matrix gramSchmidt(Matrix matrix)
	{
			int i, j, k, z, count = 0;
			Matrix gram = matrix;
			Double [] vector = matrix.getVector(matrix, 1);
			Double [] length = new Double [matrix.cols];
			Double [] dotProducts;
			Double [] erase = new Double[matrix.rows];
			Double [] matrixVector = new Double[matrix.rows];

			// Set the first column of gram to the first column of Matrix.
			for (i = 0; i < matrix.rows; i++)
				gram.Array[i][0] = vector[i];

			// Storing the magnitude of each vector in length array.
			for (i = 0; i < matrix.cols; i++)
			{
				vector = matrix.getVector(matrix, i + 1);
				length[i] = gram.getDotProduct(vector, vector);

				// Debug;
				//System.out.printf("{%d} ", length[i]);
			}

			// Get the number of computations required for the dotProducts array.
			for (i = 1; i < matrix.rows; i++)
				count += i;

			int numColumn = 1;
			dotProducts = new Double[count];

			/// Number of vectors that need to be "analyzed".
			for(i = 1; (i <= count) && (i < matrix.cols); i++, numColumn++)
			{
				Double [] sumVector = erase;

				z = i;

				// In this loop, j and k represent the column of the matrix, and the dotproduct with V.
				for (j = i + 1 , k = numColumn; k >= 1; k--)
				{

					// Local variables for the calculations.
					Double [] tempVector;
					Double subProduct, sizeProduct, scalar;


					// Get the subProduct (Ex. X_n * V_(n - 1))
					subProduct = Matrix.getDotProduct(matrix.getVector(matrix, j), gram.getVector(gram, k));
						//System.out.printf("SubProduct of V - %d is: %.4f:", k, subProduct);

					// Get the sizeProduct. (Ex X_n * X_n)
					sizeProduct =  Matrix.getDotProduct(matrix.getVector(gram, k), gram.getVector(gram, k));
						//System.out.printf("DotProduct of V - %d is: %.4f\n", k, sizeProduct);

					// Calculate the scalar for X_n.
					// Correct SubProducts are the moment, need to make sure individual dot products are right.
					scalar = subProduct / sizeProduct;
						//System.out.printf("Vector Scalar: %.4f ", scalar);

					tempVector = gram.getVector(gram, z);

					// Scale the vector.
					tempVector = Matrix.scaleVector(tempVector, (-1) * scalar);

					//System.out.print("\n\nTemp Vector Final (post scale):");
						//printVector(tempVector, tempVector.length);

					sumVector = Matrix.addVectors(sumVector, tempVector, false);

					z--;
				}

				matrixVector = Matrix.addVectors(matrix.getVector(matrix, i + 1), sumVector, false);

				// Setting Values into gram.
				for(int l = 0; l < matrix.rows; l++)
					gram.Array[l][i] = matrixVector[l];

				//System.out.println("Debug: Current Gram at end of iteration");
				//Matrix.print2DArray(gram.Array, gram.rows, gram.cols);
			}
			return gram;
	}

}
