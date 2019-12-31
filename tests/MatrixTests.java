public class MatrixTests
{
  // Cases:
  // Input is negative
  // Anything else.

  public static MatrixConstructs()
  {
    double [][] a = null;
		double [][] b = new double [3][];

		double [] x = new double[3];
		double [] y = new double[4];
		double [] z = new double[3];

		Matrix m;

		// Invalid array index
		m = new Matrix(-1);
		System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());

		// Null 2D array reference
		m = new Matrix(a);
		System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());

		// 2D array with no rows instantiated
		m = new Matrix(b);
		System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());

		// 2D Array with different rows lengths;
		b[0] = x;
		b[1] = y;
		b[2] = z;

		m = new Matrix(b);
		System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());

		b = new double [][]{
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};

		m = new Matrix(b);
		System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());
		m.print();
		for (Vector v : m.vectors())
			v.print();

  }

  public void matrixVecConstructor()
  {
    Vector [] vecs;
    Vector w = new Vector(new double[]{1, 2});
    Vector x = new Vector(new double[]{1, 2, 3});
    Vector y = new Vector(new double[]{1, 2, 3});
    Vector z = new Vector(new double[]{1, 2, 3});

    Matrix m;

    // Null parameter
    vecs = null;
    m = new Matrix(vecs);
    System.out.println(m == null);

    // Different vector lengths;
    vecs = new Vector[] {w, x, w};
    m = new Matrix(vecs);
    System.out.println(m == null);

    // One null vectors
    vecs = new Vector[] {w, null, w};
    m = new Matrix(vecs);
    System.out.println(m == null);

    // One zero length one.
    vecs = new Vector[] {w, new Vector(), w};
    m = new Matrix(vecs);
    System.out.println(m == null);

    // All identical vectors.
    vecs = new Vector[] {x, x, x};
    m = new Matrix(vecs);
    System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());
    m.print();
    for (Vector v : m.vectors())
        System.out.println(v);

    vecs = new Vector []{x, y, z};
    m = new Matrix(vecs);
    System.out.println(m.rows() + " " + m.cols() + " " + m.entries() + " " + m.vectors() + " " + m.isValid());
    m.print();
    for (Vector v : m.vectors())
      System.out.println(v);
  }

  // The copy function should not need any testing.
  public void swapTest()
  {
    double[][] a = new double[][] {
     { 1, 2, 3 },
     { 4, 5, 6 },
     { 7, 8, 9 }
    };

    double [][] b = null;

    // Testing Swap Rows:
    Matrix m = new Matrix(b);
    matrixDebug(m);

    // Null matrix, should not update.
    m.swapRows(-1, 0);
    matrixDebug(m);

    m.swapRows(0, 1); // Should swap values.
    matrixDebug(m);

    m = new Matrix(a);
    matrixDebug(m);

    // No update.
    m.swapRows(-1, 0);
    matrixDebug(m);

    // Works
    m.swapRows(1, 0);
    matrixDebug(m);

    // No update
    m.swapRows(2, 4);
    matrixDebug(m);
  }

  public void equals()
  {
    double[][] a = new double[][] {
     { 1, 2, 3 },
     { 4, 5, 6 },
     { 7, 8, 9 }
    };

    double [][] b = null;

    // Testing Swap Rows:
    Matrix m = new Matrix(a);
    matrixDebug(m);

    Matrix n = new Matrix(b);
    matrixDebug(m);

    Matrix o = new Matrix(b);
    matrixDebug(m);

    // One null reference. F
    System.out.println(n.equals(m));

    // Both null references. T
    System.out.println(n.equals(o));

    // Both non-null references, generated from same array. T
    System.out.println(m.equals(m));
  }

  public void setEntries()
  {
    // Cases:
    // Null double array
    // Non-initialized Rows
    // Non-rectangular matrix
    // Array with different dimensions to current array.
    // Updating an invalid array
    // Non rectangular Vectors.

    double[][] a = new double[][] {
     { 1, 2, 3 },
     { 4, 5, 6 },
     { 7, 8, 9 }
    };

    double [][] b = new double [][]{
      {1, 2, 3},
      {1, 2, 3, 4},
      {1, 2}
    };

    double [][] c = new double [3][];
    c[0] = new double []{1, 2, 3};

    double [][] d = new double [][] {
      {1, 2},
      {3, 4}
    };

    double [][] e = null;

    Vector [] f = null;
    Vector v, w, x, y, z, zz;
    v = new Vector(new double []{1, 2});
    w = new Vector (new double[]{1, 2, 3});
    x = new Vector(new double[] {4, 5, 6});
    y = new Vector(new double[] {7, 8, 9});
    z = null;
    zz = new Vector(-4);

    Vector [] g = new Vector []{w, x, z};
    Vector [] h = new Vector []{v,x, y};
    Matrix m, n, o;
    m = new Matrix(e);
    n = new Matrix(e);

    // Initialized null - updating with null
    // No update.
    m.setEntries(e);
    matrixDebug(m);

    // Updating an invalid array with something.
    m.setEntries(a);
    matrixDebug(m);

    // Trying to invert back to an invalid state does not update.
    m.setEntries(e);
    matrixDebug(m);

    // Non-rectangular matrix argument. -> NO update
    m.setEntries(b);
    matrixDebug(m);

    // Non-initialized matrix rows, no update occurs.
    m.setEntries(c);
    matrixDebug(m);

    // Arguments with different dimensions No update
    m.setEntries(d);
    matrixDebug(m);

    //Same set of testings with invalid start arrays: Should always update except if invalid.
    System.out.println("First ------------------");
    n.setEntries(a); // updates
    matrixDebug(n);

    System.out.println("Second ------------------");
    n = new Matrix(e); // updates
    n.setEntries(b);
    matrixDebug(n);

    System.out.println("Third ------------------");
    n = new Matrix(e); // nope
    n.setEntries(c);
    matrixDebug(n);

    System.out.println("Fourth ------------------");
    n = new Matrix(e); // nope
    n.setEntries(d);
    matrixDebug(n);

    System.out.println("Fifth ------------------");
    n = new Matrix(e); // nope
    n.setEntries(e);
    matrixDebug(n);
  }

  public void setVectors()
  {
    // Cases:
    // Null vectors array.
    // Checking some vectors arent null in the array.
    // Non-rectangular Vector array.
    // Different dimensions to current vec array.
    // Starting matrix is invalid.

    Vector a, b, c, d, e, f, g , h;
    a = new Vector(new double []{1, 2});
    b = new Vector(new double []{3, 4});
    c = new Vector(new double[] {1, 2, 3});
    d = new Vector(new double[] {4, 5, 6});
    e = new Vector(new double[] {7, 8, 9});
    f = null;
    g = new Vector(-4); // Uninitialized

    Vector [] r, s, t, u, v, w, x, y, z;

    // Non-Rectuangular
    r = new Vector[]{a, b, c};

    // All invalid matricies.
    t = null;
    u = new Vector []{f, a, b};
    v = new Vector []{g, a, b};

    w = new Vector []{a, b};
    x = new Vector []{c, d, e};


    Matrix m, n, o;
    m = new Matrix(t);

    // Initialized null - updating with null
    // No update.
    m.setVectors(t);
    matrixDebug(m);

    // Updating an invalid array with something. Becomes 2x2
    m.setVectors(w);
    matrixDebug(m);

    // // Trying to invert back to an invalid state. No update.
    m.setVectors(t);
    matrixDebug(m);

    // Initialized null, some null vectors in the matrix array.
    // No update.
    m = new Matrix(t);
    m.setVectors(u);
    matrixDebug(m);

    m = new Matrix(t);
    m.setVectors(v);
    matrixDebug(m);

    // // Non-rectangular matrix argument. -> NO update
    m = new Matrix(w);
    m.setVectors(x);
    matrixDebug(m);

  }
  
  public void setVector()
  {

  }
  public void setRow()

  public static void main (String [] args)
  {
    ;
  }
}
