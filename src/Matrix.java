package core;

// This class serves as a wrapper to the traditional 2D array implementation of
// Matricies. No traditional operations are included in this class. All of the
// Matrix operations can be found in Operations.java.

// The Matrix holds data in two ways.
// 1. A traditional 2D array of doubles.
// 2. An array of column Vectors.
//
// This choice is not necessary, but some Matrix operations are made easier if the
// data can be abstracted to a collection of Vectors. It also reduces potential computation
// of copying data into distinct arrays just for a single computation.
public class Matrix {

	private int rows, cols;
	private double[][] entries;
	private Vector[] vectors;
	private boolean isValid; // Initialization flag

	// Default values for four decimal point precision.
	private int precision = 4;
	private String format = "%.4f";
	private double epsilon = 1e-5;

	// Constructors:
	// ===========================================================================

	// Uninitialized Matrix constructor.
	// Creates a default 0 x 0 matrix with null references.
	public Matrix()
	{
		this.rows = 0;
		this.cols = 0;
		this.isValid = false;
		this.vectors = null;
		this.entries = null;
	}

	// Returns a dim x dim Matrix where all entries are initialized to zero.
	// Dimensions less than one will return an uninitialized Matrix.
	public Matrix(int dim)
	{
		this();
		if (dim <= 0)
			return;

		this.rows = dim;
		this.cols = dim;
		this.isValid = true;
		this.vectors = new Vector [dim];
		this.entries = new double [dim][dim];

		for (int i = 0; i < dim; i++)
			this.vectors[i] = new Vector(dim);
	}

	// Creates a Matrix with values copied from a 2D array.
	// If the passed array is non-rectangular or uninitialized an uninitialized
	// Matrix will be returned.
	public Matrix(double [][] entries)
	{
		this();

		if (entries == null)
			return;

		// Verify the 2D array reference is valid.
		if (entries.length == 0 || entries[0] == null)
		{
			System.err.println("-- Null 2D array reference passed into Matrix constructor --");
			System.err.println("-- Returning uninitialized matrix object. -- ");
			return;
		}

		// Verify the Matrix is rectangular.
		int firstLength = entries[0].length;
		for (int i = 0; i < entries.length; i++)
			if (entries[i] == null || entries[i].length != firstLength)
				return;

		this.rows = entries.length;
		this.cols = entries[0].length;
		this.isValid = true;


		// Populate the Double array
		this.entries = new double [this.rows][this.cols];
		for (int i = 0; i < this.rows; i++)
			System.arraycopy(entries[i], 0, this.entries[i], 0, this.cols);

		// Iniitalize the Vectors array.
		this.vectors = new Vector [this.cols];
		for (int i = 0; i < this.cols; i++)
			vectors[i] = new Vector(this.rows);

		updateVectors();
	}

	// Creates a Matrix with values copied from an array of Vectors.
	// If the passed array is non-rectangular or uninitialized an
	// uninitialized Matrix will be returned.
	public Matrix(Vector [] v)
	{
		this();

		if (v == null)
			return;

		// Verify each vector has identical length.
		for (int i = 0; i < v.length; i++)
		{
			if (v[i] == null || v[i].length() == 0)
				return;
			if (v[i].length() != v[0].length())
				return;
		}

		this.rows = v[0].length();
		this.cols = v.length;
		this.isValid = true;

		this.vectors = new Vector[this.cols];
		for (int i = 0; i < this.cols; i++)
			this.vectors[i] = v[i].copy();

		this.entries = vectorsTo2DArray();
	}

	// Matrix Methods:
	// ===========================================================================

	// Prints the contents of a Matrix to the console as a 2D Array.
	// No output will be generated for invalid Matricies.
	public void print()
	{
		if (!isValid)
			return;

		for (int i = 0; i < rows; i++)
		{
			for (int j = 0; j < cols; j++)
				System.out.printf(String.format(this.format, this.entries[i][j]) + " ");

			System.out.println();
		}
		System.out.println();
	}

	// Prints the contents of the Matrix to the console in terms of its column vectors.
	// No output will be generated for invalid Matricies.
	public void printColumn()
	{
		if (!isValid)
			return;

		for (int i = 0; i < vectors.length; i++)
		{
			vectors[i].print();
			System.out.println();
		}
	}

	// Internally swaps two rows within a Matrix.
	// Invalid matricies or indicies that are out of bounds will not update the Matrix.
	public void swapRows(int row1, int row2)
	{
		if (row1 < 0 || row2 < 0)
			return;

		// An invalid matrix does not have a valid row or vectors reference.
		if (!isValid)
			return;

		if (row1 > entries.length || row2 > entries.length)
			return;

		double[] firstRow = entries[row1];
		entries[row1] = entries[row2];
		entries[row2] = firstRow;

		// Update the two values in each Vector.
		for (int i = 0; i < entries[row1].length; i++)
		{
			double first = vectors[i].valueAt(row1);
			double second = vectors[i].valueAt(row2);

			vectors[i].setEntry(row1, second);
			vectors[i].setEntry(row2, first);
		}

		return;
	}

	// Generates a copy of the Matrix with identical entries.
	public Matrix copy()
	{
		return new Matrix(vectors);
	}

	// Returns whether or not two Matricies are identical across every index.
	// Each equality operation is a fuzzy comparison with respect to the precision field.
	@Override
	public boolean equals(Object o)
	{
		if (!(o instanceof Matrix))
			return false;
		Matrix m = (Matrix) o;

		// Two uninitialized matricies.
		if (!this.isValid && !m.isValid())
			return true;

		// One uninitialized matrix is undefined.
		if (!this.isValid || !m.isValid())
			return false;

		// Different dimensional Matricies will never be equal.
		if (this.rows != m.rows() || this.cols != m.cols)
			return false;

		Vector [] mVectors = m.vectors();
		double epsilon = Math.min(this.epsilon, m.epsilon());

		boolean equals = true;
		for (int i = 0; i < cols; i++)
			if (!vectors[i].equals(mVectors[i]))
				equals = false;

		return equals;
	}

	// Getters and Setters:
	// ===========================================================================
	public int rows()
	{
		return this.rows;
	}

	public int cols()
	{
		return this.cols;
	}

	public Vector[] vectors()
	{
		return this.vectors;
	}

	public double[][] entries()
	{
		return this.entries;
	}

	public boolean isValid()
	{
		return this.isValid;
	}

	public int precision()
	{
		return this.precision;
	}

	public double epsilon()
	{
		return this.epsilon;
	}

	public String format()
	{
		return this.format;
	}

	// Updates the values of a Matrix with a new 2D array of values.
	// For simplicty, this method requires the passed array have identical dimensions
	// to the prior contents of the Matrix. If the Matrix was uninitialized, this method
	// will update the contents and convert it to a valid Matrix.
	// Null references, and non-rectangular arrays will not update the Matrix.
	public void setEntries(double [][] newEntries)
	{
		// Verify array references are valid.
		if (newEntries == null || newEntries[0] == null)
			return;

		// Verify the matrix is rectangular.
		int firstLength = newEntries[0].length;
		for (int i = 0; i < newEntries.length; i++)
			if (newEntries[i] == null || newEntries[i].length != firstLength)
				return;

		// Check dimensions against the valid matrix.
		if (isValid && (this.rows != newEntries.length || this.cols != newEntries[0].length))
			return;

		// Check dimensions against the current vectors
		if (isValid && (vectors[0].length() != newEntries.length || newEntries[0].length != vectors.length))
			return;

		// Essentially call the constructor and make this a valid object.
		if (!isValid)
		{
			this.rows = newEntries.length;
			this.cols = newEntries[0].length;
			this.isValid = true;
			this.vectors = new Vector[this.cols];
			this.entries = new double [newEntries.length][newEntries[0].length];

			// Initialize Vectors:
			for (int i = 0; i < this.cols; i++)
				this.vectors[i] = new Vector(this.rows);
		}

		// Populate Array
		for (int i = 0; i < this.rows; i++)
			System.arraycopy(newEntries[i], 0, this.entries[i], 0, this.cols);

		// Update the Array.
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
				this.entries[i][j] = newEntries[i][j];

		updateVectors();
	}

	// Update the contents of the Matrix at a specific row.
	// Invalid matricies, out of bounds rows, and different lengths arrays will not
	// update the matrix.
	public void setRow(int row, double [] newEntries)
	{
		if (!isValid || row < 0 || row > this.rows)
			return;

		if (newEntries == null || newEntries.length != this.cols)
			return;

		System.arraycopy(newEntries, 0, entries[row], 0, cols);

		// Update the Vectors.
		for (int i = 0; i < cols; i++)
			vectors[i].setEntry(row, newEntries[i]);
	}

	// Update the values of a Matrix with a new array of Vectors.
	// This requires a rectangular Vector array, which is properly initialized or
	// no update will occur.
	public void setVectors(Vector [] vecs)
	{
		if (vecs == null)
		return;

		// Verify every vectors has identical length.
		for (int i = 0; i < vecs.length; i++)
		{
			if (vecs[i] == null || vecs[i].length() == 0)
				return;
			if (vecs[i].length() != vecs[0].length())
				return;
		}

		// Check vecs dimensions against the valid array.
		if (isValid && (this.cols != vecs.length ||  this.rows != vecs[0].length()))
			return;

		// Initialize the Matrix fields.
		if (!isValid)
		{
			this.rows = vecs[0].length();
			this.cols = vecs.length;
			this.isValid = true;
			this.vectors = new Vector[this.cols];
			this.entries = new double[this.rows][this.cols];
		}

		for (int i = 0; i < this.cols; i++)
			this.vectors[i] = vecs[i].copy();

		updateArray();
	}

	// Update the contents of the Matrix at a specific column.
	// Invalid matricies, out of bounds columns, and a different length Vector
	// will not update the Matrix.
	public void setVector(int col, Vector newVec)
	{
		if (!isValid || col < 0 || col > this.cols)
			return;

		if (newVec == null || newVec.length() != this.rows)
			return;

		for (int i = 0; i < rows; i++)
		{
			double val = newVec.valueAt(i);
			entries[i][col] = val;
			vectors[col].setEntry(i, val);
		}
	}

	// Updates the decimal precision of the Matrix.
	// Arbitrary choice of 12 digit decimal precision.
	//For precisions greater than 12, maybe a different tool should be used.
	public void setPrecision(int n)
	{
		if (vectors == null || precision > 12)
			return;

		// Updates precision for all the vectors.
		vectors[0].setPrecision(n);
		this.format = vectors[0].format();

		this.precision = n;
		StringBuilder epString = new StringBuilder();
		epString.append("0.");

		for (int i = 0; i < n; i++)
			epString.append("0");

		epString.append("1");
		this.epsilon = Double.parseDouble(epString.toString());
	}

	// Private Helper Methods
	//============================================================================

	// Helper method which converts a 2D array into an array of Vectors.
	// It assumes the 2D array is initialized, else it would not have been called.
	private Vector [] arrayToVectors()
	{
		Vector [] vecs = new Vector [cols];
		for (int i = 0; i < cols; i++)
		{
			vecs[i] = new Vector(rows);

			for (int j = 0; j < rows; j++)
				vecs[i].setEntry(j, entries[i][j]);
		}

		return vecs;
	}

	// Helper method which converts an array of Vectors to a simple 2D array of numbers.
	// Assumes the Vectors are initialized and rectangular, or it would not have been called.
	private double [][] vectorsTo2DArray()
	{
		double [][] retVals = new double[rows][cols];
		for (int i = 0; i < cols; i++)
		{
			double [] vectorVals = vectors[i].entries();

			for (int j = 0, k = 0; j < rows; j++)
				retVals[j][i] = vectorVals[k++];
		}

		return retVals;
	}

	// Helper method which returns whether or not a 2D array and Vector array have
	// identical dimensions. It also checks that both objects are rectangular.
	// Any uninitialized value or difference in dimensions will return false.
	private static boolean identicalDimensions(double [][] entries, Vector [] vecs)
	{
		if (entries == null || vecs == null)
			return false;

		if (entries[0] == null || entries[0].length != vecs.length)
			return false;

		int numRows = entries.length;
		int numCols = entries[0].length;

		// Checking the 2D array.
		for (int i = 0; i < numRows; i++)
			if (entries[i] == null || entries[i].length != numCols)
				return false;

		if (vecs[0] == null)
				return false;

		// Checking the Vectors.
		for (int i = 0; i < vecs.length; i++)
			if (vecs[i] == null || vecs[i].length() != numRows)
				return false;

		return true;
	}

	// Helper which updates the entries field from the vectors array field.
	private void updateArray()
	{
		if (!isValid)
			return;

		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				entries[i][j] = vectors[j].valueAt(i);
	}

	// Helper which updates the vectors field from the entries array field.
	private void updateVectors()
	{
		if (!isValid)
			return;

		for (int i = 0; i < this.cols; i++)
			for (int j = 0; j < this.rows; j++)
				this.vectors[i].setEntry(j, entries[j][i]);
	}
}
