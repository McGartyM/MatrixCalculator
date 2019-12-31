public class OperationTests
{
  public void scaleTest()
  {
    // Cases:
    // Scaling an uninitialized matrix.
    // Anything else.
    double [][] a = new double [][]
		{
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};

		double [][] b = null;

		Matrix m = new Matrix(a);
		Matrix n = new Matrix(b);

		matrixDebug(m);
		m = Operations.scaleMatrix(m, -1);
		matrixDebug(m);

		matrixDebug(n);
		n = Operations.scaleMatrix(n, 3);
	  matrixDebug(n);
  }

  public void addTest()
  {
    double [][] a = new double [][]
    {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    double [] x = new double []{1, 4, 7};
    double [] y = new double []{2, 5, 8};
    double [] z = new double []{3, 6, 9};
    Vector [] vecs = new Vector []{new Vector(x), new Vector(y), new Vector(z)};

    double [][] b = null;

    double [][] c = new double [][]
    {
      {1, 2},
      {3, 4}
    };

    Matrix m = new Matrix(a);
    Matrix n = new Matrix(a);
    Matrix o = new Matrix(b);
    Matrix p = new Matrix(c);
    Matrix q = new Matrix(vecs);

    // Any add call with null or invalid should return null.
    System.out.println(Operations.add(m, o));
    System.out.println(Operations.add(o, o));
    System.out.println(Operations.add(m, p));

    // Adding M with its negative, should give zero matrix.
    n = Operations.scale(n, i);
    Matrix r = Operations.add(m, n);

    System.out.println("------------------- m ----------------");
    matrixDebug(m);
    System.out.println("------------------- n ----------------");
    matrixDebug(n);
    System.out.println("------------------- r ----------------");
    matrixDebug(r);


  }

  public void subtractTest()
  {
    double [][] a = new double [][]
    {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    double [] x = new double []{1, 2, 3};
    double [] y = new double []{4, 5, 6};
    double [] z = new double []{7, 8, 9};
    Vector [] vecs = new Vector []{new Vector(x), new Vector(y), new Vector(z)};

    double [][] b = null;

    double [][] c = new double [][]
    {
      {1, 2},
      {3, 4}
    };

    Matrix m = new Matrix(a);
    Matrix n = new Matrix(a);
    Matrix o = new Matrix(b);
    Matrix p = new Matrix(c);
    Matrix q = new Matrix(vecs);

    // Any add call with null -> null
    System.out.println(Operations.subtract(m, o));
    System.out.println(Operations.subtract(o, o));

    // Invalid Dimension -> null
    System.out.println(Operations.subtract(m, p));

    // Subtracting n with itself gives the zero matrix.
    Matrix r = Operations.subtract(m, q);
    System.out.println("------------------- m ----------------");
    matrixDebug(m);
    System.out.println("------------------- q ----------------");
    matrixDebug(q);
    System.out.println("------------------- r ----------------");
    matrixDebug(r);

  }

  public void multiplyTest()
  {
    // Checkin for invalidness
    // Non-valid dimension parameters
    double [][] a = new double [][]
		{
			{1, 2, 3},
			{4, 5, 6},
			{7, 8, 9}
		};

		double [] x = new double []{1, 2, 3};
		double [] y = new double []{4, 5, 6};
		double [] z = new double []{7, 8, 9};
		Vector [] vecs = new Vector []{new Vector(x), new Vector(y), new Vector(z)};

		double [][] b = null;

		double [][] c = new double [][]
		{
			{1, 2},
			{3, 4}
		};

		double [][] d = new double [][]{
			{0, 0},
			{0, 0}
		};

		Matrix m, n, o, p, q;
		m = new Matrix(c);
		n = new Matrix(c);

		o = Operations.multiply(m, n);
		matrixDebug(o);

		o = Operations.multiply(o, new Matrix(d));
		matrixDebug(o);

		p = new Matrix(b);
		o = Operations.multiply(o, p);

		System.out.println(o);
		if (o != null)
			matrixDebug(o);
  }

  public void powerTest()
  {
    // Come back to after fixing / checking Inversion.
  }
  public void transposeTest()
  {
    double [][] a = new double [][]
    {
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    double [] x = new double []{1, 2, 3};
    double [] y = new double []{4, 5, 6};
    double [] z = new double []{7, 8, 9};
    Vector [] vecs = new Vector []{new Vector(x), new Vector(y), new Vector(z)};

    Vector [] nullz = null;
    double [][] b = null;

    double [][] c = new double [][]
    {
      {1, 2},
      {3, 4}
    };

    double [][] d = new double [][]{
      {0, 0},
      {0, 0}
    };

    Matrix m, n, o, p;

    m = new Matrix(nullz);
      System.out.println(Operations.transpose(m));
    matrixDebug(m);

    // Normal transpose operation.
    m = new Matrix(vecs);
    matrixDebug(m);
    n = Operations.transpose(m);
    matrixDebug(n);
    matrixDebug(m);
  }

  public void traceTest()
  {
    // Trace Cases:
    double [][] a;
    Matrix m;

    // Non-square matrix.
    a = new double [][]{
      {1, 2, 3},
      {4, 5, 6}
    };

    m = new Matrix (a);
    System.out.println(Operations.trace(m)); // -1, with error messages.

    // invalid matrix.
    a = null;
    m = new Matrix(a);

    System.out.println(Operations.trace(m)); // -1, with error messages.

    // Valid Case.
    a = new double [][]{
      {1, 2, 3},
      {2, 1, 3},
      {3, 2, 1}
    };
    m = new Matrix(a);
    System.out.println(Operations.trace(m)); // 3, no errors.
  }

  public void identityTests()
  {
    // Identity tests
    Matrix a = Operations.identity(-1);
    System.out.println(a);

    Matrix b = Operations.identity(1);
    matrixDebug(b);

    Matrix c = Operations.identity(5);
    matrixDebug(c);
  }

  public void appendTest()
  {
    double [][] a = null;
    Vector [] b = null;
    double [][] c = new double [][]{
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    double [][] d = new double [][]{
      {1, 2},
      {3, 4}
    };

    Matrix A = new Matrix(a);
    Matrix B = new Matrix(b);
    Matrix C = new Matrix(c);
    Matrix D = new Matrix(d);

    // Invalid Parameter. Should all be null returns.
    System.out.println(Operations.append(A, C));
    System.out.println(Operations.append(B, C));
    System.out.println(Operations.append(A, B));

    // Difference in Rows: Null
    System.out.println(Operations.append(C, D));

    Matrix x = Operations.append(C, Operations.identity(3));
    matrixDebug(C);
    matrixDebug(x);

  }

  public void splitTest()
  {
    double [][] a;
    Matrix A;
    Matrix [] split;

    a = new double [][] {
      {1, 2, 3, 0},
      {4, 5, 6, 0},
      {7, 8, 9, 0}
    };

    A = new Matrix(a);
    split = Operations.split(A, -1);
    split = Operations.split(A, 6);
    split = Operations.split(A, 2);

    split[0].print();
    split[1].print();

    A = null;
    split = Operations.split(A, 2);

    Vector [] nullz = null;
    A = new Matrix(nullz);
    split = Operations.split(A, 2);
  }

  public void RankAndRREFTest()
  {
    double [][] a;

    a = new double [][] {
      {3, 2, -5, 4},
      {1, 1, -2, 1},
      {5, 3, -8, 6}
    };

    double [][] b = new double [][]{
      {1, 2, 3},
      {4, 5, 6},
      {7, 8, 9}
    };

    double [][] c = new double [][]{
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0}
    };

    double [][] d = new double [][]{
      {0, 3, -6, 6, 4, -5},
      {3, -7, 8, -5, 8, 9},
      {3, -9, 12, -9, 6, 15}
    };

    double [][] e = new double [][]{
      {0, 3, 3},
      {3, -7, -9},
      {-6, 8, 12},
      {6, -5, -9},
      {4, 8, 6},
      {-5, 9, 15}
    };

    // Rank Test:
    System.out.println(Operations.rank(new Matrix(a))); // 3
    System.out.println(Operations.rank(new Matrix(b))); // 2
    System.out.println(Operations.rank(new Matrix(c))); // 0
    System.out.println(Operations.rank(new Matrix(d))); // 3
    System.out.println(Operations.rank(new Matrix(e))); // 3
    System.out.println(Operations.rank(new Matrix(new double [][]{{1.0}}))); // 1
  }

  public void isLinDep()
  {
    double [][] a = null;
    Matrix A = new Matrix(a);

    System.out.println(Operations.isInconsistent(A)); // Null -> False

    a = new double [][]{
      {1, 2},
      {3, 4}
    };

    A = new Matrix(a);
    System.out.println(Operations.isInconsistent(A)); // False

    a = new double [][]{
      {0, 1},
      {0, 0}
    };

    A = new Matrix(a);
    System.out.println(Operations.isInconsistent(A)); // True

    a = new double [][]{
      {0, 0},
      {0, 1}
    };

    A = new Matrix(a);
    System.out.println(Operations.isInconsistent(A)); // True


    a = new double [][]{
      {0, 0},
      {0, 0}
    };

    A = new Matrix(a);
    System.out.println(Operations.isInconsistent(A)); // False
  }

  public void invertTest()
  {
    // Inversion Tests:
    double [][] a;
    Matrix A;
    a = new double [][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 0}
    };

    // Matrix A = new Matrix(a);
    // matrixDebug(A);
    // System.out.println("Inverted: ");
    // Matrix B = Operations.invert(A); // null
    // matrixDebug(A);
    //
    // matrixDebug(B);

    // a = new double [][]{
    // 	{2, 3},
    // 	{2, 2}
    // };
    //
    // A = new Matrix(a);
    // matrixDebug(A);
    // Matrix B = Operations.invert(A);
    // System.out.println("Post Inversion: ");
    // matrixDebug(A);
    // matrixDebug(B);

    // No Update Occurs:
    // a = new double [][]{
    // 	{0, 0},
    // 	{0, 0}
    // };
    //
    // A = new Matrix(a);
    // matrixDebug(A);
    // Matrix B = Operations.invert(A);
    // System.out.println("Post Inversion: ");
    // matrixDebug(A);
    // matrixDebug(B);
    // A = Operations.identity(3);
    // matrixDebug(Operations.invert(A));


  }

  public void cofactorTest()
  {		// Cofactor Cases:
  		Vector [] v;
  		double [][] d;
  		Matrix m, result;

  		// Null input, returns Null
  		//Operations.cofactor(null);

  		// Invalid Matrix, returns null
  		v = null;
  		m = new Matrix(v);
  		//Operations.cofactor(m);

  		// Non-Square Matrix, returns original Matrix
  		d = new double [][]{
  			{1, 2, 3},
  			{4, 5, 6}
  		};

  		m = new Matrix(d);
  		//result = Operations.cofactor(m);

  		// Zero-Matrix, returns itself.
  		d = new double [][]{
  			{0, 0, 0},
  			{0, 0, 0},
  			{0, 0, 0}
  		};

  		m = new Matrix(d);
  		//matrixDebug(m);
  		//result = Operations.cofactor(m);
  		//matrixDebug(m);

  		// non-invertible matrix -> No cofactor matrix.
  		d = new double [][]{
  			{1, 0, 0},
  			{0, 1, 0},
  			{0, 0, 0}
  		};

  		// m = new Matrix(d);
  		// matrixDebug(m);
  		// result = Operations.cofactor(m);


  		d = new double [][]{
  			{1, 2, 3},
  			{0, 4, 5},
  			{1, 0, 6}
  		};

  		m = new Matrix(d);
  		matrixDebug(m);
  		result = Operations.cofactor(m);
  		matrixDebug(result);
  		matrixDebug(m);
  		// Essentially Anything else.
  }

  public void orthogonalizeTest()
  {
    // Othogonalize Cases:
    Vector [] v;
    double [][] d;
    Matrix m, result;

    // Null input, returns Null
    Operations.orthogonalize(null);

    // Invalid Matrix, returns null
    v = null;
    m = new Matrix(v);
    Operations.orthogonalize(m);

    // Zero-Matrix, returns itself.
    d = new double [][]{
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0}
    };

    // m = new Matrix(d);
    // matrixDebug(m);
    // result = Operations.orthogonalize(m);
    // matrixDebug(m);
    // matrixDebug(result);

    // Retruns itself.
    d = new double [][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 0}
    };

    // m = new Matrix(d);
    // matrixDebug(m);
    // result = Operations.orthogonalize(m);
    // matrixDebug(m);
    // matrixDebug(result);


    d = new double [][]{
      {1, 2, 3},
      {0, 4, 5},
      {1, 0, 6}
    };

    // m = new Matrix(d);
    // matrixDebug(m);
    // result = Operations.orthogonalize(m);
    // matrixDebug(result);
    // matrixDebug(m);
    // Essentially Anything else.

    d = new double [][]{
      {1.3, 32.1, 44},
      {0, -3, 17},
      {23, 0, 2}
    };

    m = new Matrix(d);
    matrixDebug(m);
    result = Operations.orthogonalize(m);
    matrixDebug(m);
    matrixDebug(result);
  }

  public void normalizeTest()
  {
      ;
  }

  public void qrTest()
  {
    // Othogonalize Cases:
    Vector [] v;
    double [][] d;
    Matrix m;
    Matrix [] result;

    // Null input, returns Null
    Operations.qr(null);
    //
    // // Invalid Matrix, returns null
    v = null;
    m = new Matrix(v);
    Operations.qr(m);

    // Zero-Matrix, returns itself.
    d = new double [][]{
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0}
    };

    //m = new Matrix(d);
    // matrixDebug(m);
    // result = Operations.qr(m);
    // matrixDebug(m);
    // matrixDebug(result[0]);
    // matrixDebug(result[1]);


    // Retruns itself.
    d = new double [][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 0}
    };

    // Q == R
    // m = new Matrix(d);
    // matrixDebug(m);
    // result = Operations.qr(m);
    // matrixDebug(m);
    // System.out.println("Q: ");
    // matrixDebug(result[0]);
    // System.out.println("R: ");
    // matrixDebug(result[1]);


    d = new double [][]{
      {1, 2, 3},
      {0, 4, 5},
      {1, 0, 6}
    };

    m = new Matrix(d);
    matrixDebug(m);
    result = Operations.qr(m);
    matrixDebug(m);
  }

  public void luTest()
  {
    double [][] d;
    Matrix m;
    Matrix [] ret;

    Operations.lu(null);


    d = null;
    m = new Matrix(d);
    Operations.lu(m);

    d = new double [][]{
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0}
    };

    // m = new Matrix(d);
    // System.out.println("Original: ");
    //
    // matrixDebug(m);
    // ret = Operations.lu(m); // No LU for zero matrix.
    // matrixDebug(m);
    //
    // for (Matrix n : ret)
    // 	matrixDebug(n);

    d = new double [][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 0}
    };

    // m = new Matrix(d);
    // System.out.println("Original: ");
    //
    // matrixDebug(m);
    // ret = Operations.lu(m); // Technically no LU decomp, but technically returns a valid result.
    // matrixDebug(m);
    //
    // for (Matrix n : ret)
    // 	matrixDebug(n);

    d = new double [][]{
      {1, 2, 4},
      {3, 8, 14},
      {2, 6, 13}
    };

    m = new Matrix(d);
    System.out.println("Original: ");

    matrixDebug(m);
    ret = Operations.lu(m); // Technically no LU decomp, but technically returns a valid result.
    matrixDebug(m);

    for (Matrix n : ret)
      matrixDebug(n);
  }

  public void EigenVectors()
  {
    double [][] a;
    Vector [] v;
    Matrix m;

    // a = new double [][]{
    // 	{1, 2, 1},
    // 	{6, -1, 0},
    // 	{-1, -2, -1}
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();
    //
    // // EVs: -4, -3, 0 ->
    // // -1  -1,  -0.0769
    // //  2  -1.5 -0.4615
    // //  1   1    1
    //
    a = new double [][]{
      {1, 0, 0},
      {0, 1, 0},
      {0, 0, 1}
    };

    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();
    //
    // // EVs: 1, 1, 1 ->
    // // 1, 0, 0
    // // 0, 1, 0
    // // 0, 0, 1
    //
    a = new double [][]{
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0}
    };

    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();
    //
    // // EVs: 0, 0, 0 ->
    // // 1, 0, 0
    // // 0, 1, 0
    // // 0, 0, 1
    //
    // a = new double [][]{
    // 	{0, 2, 0},
    // 	{0, 0, 0},
    // 	{0, 0, 0}
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();
    //
    // // EVs: 1, 1, 1 ->
    // // 1, 0
    // // 0, 0
    // // 0, 1
    //
    // a = new double [][]{
    // 	{3, 2, 4},
    // 	{2, 0, 2},
    // 	{4, 2, 3}
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();
    //
    // // EVs: 8, -1, -1
    // // 2 -2 -1
    // // 1  0  2
    // // 2  2  0
    //
    // a = new double [][]{
    // 	{1, -4},
    // 	{4, -7}
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();
    //
    // // Evs 1, 1
    // // 1
    // // 1
    //
    // // INCORRECT CASE:
    // a = new double [][]{
    // 	{0, 1, 0, 1, 0},
    // 	{1, 0, 1, 0, 0},
    // 	{0, 1, 0, 1, 0},
    // 	{0, 0, 1, 0, 1},
    // 	{0, 1, 0, 1, 0},
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();
    // // Evs: 0, 0, 0, -2, 2
    // //  1  0  1 1
    // //  0 -1 -1 1
    // // -1  0  1 1
    // //  0  1 -1 1
    // //  1  0  1 1
    //
    // a = new double [][]{
    // 	{4, 4, 2, 3, -2},
    // 	{0, 1, -2, -2, 2},
    // 	{6, 12, 11, 2, -4},
    // 	{9, 20, 10, 10, -6},
    // 	{15, 28, 14, 5, -3},
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();

    // a = new double [][]{
    // 	{1, 3},
    // 	{2, 2}
    // };
    //
    // v = Operations.eigenvectors(new Matrix(a));
    // m = new Matrix(v);
    // m.print();

    // EVs: 4, -1
    // 1 -1.5
    // 1  1

    //Application.launch(GUI.class);
    a = new double [][]{
        {1, 2, 1},
        {6, -1, 0},
        {-1, -2, -1}
    };

    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();

    a = new double [][]{
        {-2, -4, 2},
        {-2, 1, 2},
        {4, 2, 5}
    };

    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();

    a = new double [][]{
        {1, 3},
        {3, 2}
    };

    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();

    a = new double [][]{
      {0, 0, 0},
      {0, 0, 0},
      {0, 0, 0}
    };
    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();

    // Needs more debugging!
    a = new double [][]{
      {1, 2, 0},
      {0, 1, 0},
      {0, 0, 1}
    };
    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();

    // Same eigenvector equation.
    a = new double [][]{
      {8, -9},
      {4, -4}
    };
    v = Operations.eigenvectors(new Matrix(a));
    m = new Matrix(v);
    m.print();
  }
  }

  public void rankDecomp()
  {

    		
    		double [][] a;
    		Matrix m;
    		Matrix [] o;

    		a = new double [][]
    		{
    			{1, 2, 3},
    			{4, 5, 6},
    			{7, 8, 9}
    		};

    		// 1 2     1 0 -1
    		// 4 5     0 1  2
    		// 7 8

    		m = new Matrix(a);
    		o = Operations.rankDecomp(m);

    		for (Matrix n : o)
    			n.print();
    		System.out.println("-------------------------------------------------");

    		a = new double [][]
    		{
    			{1},
    			{2},
    			{3}
    		};

    		m = new Matrix(a);
    		o = Operations.rankDecomp(m);

    		for (Matrix n : o)
    			n.print();

    		// 1   1
    		// 2
    		// 3
    		System.out.println("-------------------------------------------------");

    		a = new double [][]
    		{
    			{1}
    		};

    		m = new Matrix(a);
    		o = Operations.rankDecomp(m);

    		for (Matrix n : o)
    			n.print();

    		// 1   1

    		System.out.println("-------------------------------------------------");

    		a = new double [][]
    		{
    			{0, 0, 0}
    		};

    		m = new Matrix(a);
    		o = Operations.rankDecomp(m);

    		for (Matrix n : o)
    			n.print();
    		// 0 0 0    0
    		System.out.println("-------------------------------------------------");

    		a = new double [][]
    		{
    			{1, 3, 1, 4},
    			{2, 7, 3, 9},
    			{1, 5, 3, 1},
    			{1, 2, 0, 8}
    		};

    		m = new Matrix(a);
    		o = Operations.rankDecomp(m);

    		for (Matrix n : o)
    			n.print();
    		System.out.println("-------------------------------------------------");

    		a = new double [][]
    		{
    			{1, 3, 1, 4},
    			{2, 7, 3, 9},
    			{1, 5, 3, 1},
    			{1, 2, 0, 8}
    		};

    		m = new Matrix(a);
    		o = Operations.rankDecomp(m);

    		for (Matrix n : o)
    			n.print();
    		System.out.println("-------------------------------------------------");
  }
  public static void main (String [] args)
  {
    ;
  }
}
