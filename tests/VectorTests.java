import core.Vector;

// This file verifies the behavior of Vector creation in Vector.java
public class vectorConstructor
{
  public static void nullCheck()
  {
    Vector v = new Vector(null);
		System.out.println(v.length());
		System.out.println(v.precision());
		System.out.println(v.format());

		v.printVector();
  }

  // Verify the memory address of a passed double [] is different
  // from the double [] created in the Vector.
  public static void uniquePointers()
  {
    double [] values = new double[]{1, 2, 3, 4, 5};
    Vector v = new Vector(values);

    // Should not be the same.
    System.out.println(values);
    System.out.println(v.entries());

    System.out.println(v.length());
    v.printVector();
  }

  public static void cloneVector()
  {
    double [] values = new double []{1, 2, 3, 4, 5};
    Vector v = new Vector(values);
    Vector w = v.copy();

    v.printVector();
    w.printVector();

    // Check that fields are identical.
    System.out.println("Length: " + (v.length() == w.length()));
    System.out.println("Entries: " + (v.entries() != w.entries()));
  }

  public static void equals()
  {
    ;
  }

  public static void adding()
  {
    // Cases:
    // Adding it with something null.
    // Different dimensions.

    // Adding a vector with itself. (Does not copy array reference.)
    // Adding it with its inverse.

    double [] a = new double []{1, 2, 3, 4, 5};
    double [] b = new double []{1, 2, 3, 4};
    double [] c = new double []{-1, -2, -3, -4};
    Vector x, y, z;


    // Commutativity
    x = new Vector(a);
    y = new Vector(a);

    // Different entries references.
    System.out.println(x.entries() != y.entries());

    z = x.add(y);
    System.out.println(z.equals(y.add(x)));

    // Different Dimensions
    x = new Vector(a);
    y = new Vector(b);

    z = x.add(y);
    System.out.println(z == null);

    // Adding with null.
    x = null;
    y = new Vector(b);

    z = y.add(x);
    System.out.println(z == null);

    x = new Vector(b);
    y = new Vector(c);

    z = y.add(x);
    System.out.println(z.equals(new Vector(4)));
  }

  // Subtract method differs only by a single subtraction.
  // Tests would give identical results.

  public void scale()
  {
    // Scaling an uninitialized Vector.
    // -> Should do nothing.
    double [] a = new double []{1, 2, 3, 4, 5};
		double [] b = new double []{1, 2, 3, 4};
		double [] c = new double []{-1, -2, -3, -4};
		Vector x, y, z;

		x = new Vector(null);
		x.scale(5);

		System.out.println(x.equals(x));

		x = new Vector(a);
		y = x.scale(2);

		System.out.println(y.equals(x.add(x)));
  }

  public void dotProduct()
  {
    // input == null
    // input has different dimensions
    // input is zero vector {0, 0, 0} -> always results in zero.
    double [] a = new double [] {1, 2, 3, 4, 5};
		double [] b = new double [] {0, 0, 0, 0, 0};
		double [] c = new double [] {1, 2, 3};

		Vector x, y, z;
		double result = 0;

		x = new Vector(a);
		y = new Vector(b);
		z = new Vector(c);

		// With null vector.
		y = null;
		result = x.dotProduct(y);
		System.out.println(Double.isNaN(result));

		// Different dimensions -> NaN
		result = x.dotProduct(z);
		System.out.println(Double.isNaN(result));

		y = new Vector(b);

		// With 0 vector -> zero
		result = x.dotProduct(y);
		System.out.println(result == 0);

		// Commutivity.
		result = y.dotProduct(x);
		System.out.println(result == 0);
  }

  public void projection()
  {
    // null , null
    // Either one is null
    // Neither is null
    // -> One is the zero vector.

    double [] a = new double [] {3, 4};
    double [] b = new double [] {5, -12};
    double [] c = new double [] {0, 0};
    double [] d = new double [] {1, 2, 3};
    Vector x, y, z, projection;

    // Null checks:
    y = new Vector(a);
    projection = Vector.projection(null, y);
    System.out.println(projection == null);

    x = new Vector(a);
    projection = Vector.projection(x, null);
    System.out.println(projection == null);

    projection = Vector.projection(null, null);
    System.out.println(projection == null);

    // Dimension Checks:
    x = new Vector(a);
    y = new Vector(d);
    projection = Vector.projection(x, y);
    System.out.println(projection == null);

    // Dot with zero vector:
    x = new Vector(a);
    z = new Vector(c);

    projection = Vector.projection(x, z);
    System.out.println(projection.equals(z));

    x = new Vector(b);
    projection = Vector.projection(z, z);
    System.out.println(projection.equals(z));

    // Dot product with itself should equal itself.
    x = new Vector(a);
    projection = Vector.projection(x,x);
    System.out.println(projection.equals(x));

    // Commutivity, <u, v> == <v, u>
    x = new Vector(a);
    y = new Vector(b);
    projection = Vector.projection(x, y);
    projection.print();

    x = new Vector(b);
    y = new Vector(a);
    projection = Vector.projection(y, x);
    projection.print();

  }

  public void setEntry()
  {
    // Setting with a null array,
    // Setting with a different length
    // Checking that the values were updated.
    double [] a = new double [] {1, 2, 3};
    double [] b = null;
    double [] c = new double []{1};
    double [] d = new double []{4, 5, 6};


    Vector v = new Vector(a);
    Vector x = new Vector(a);

    v.setEntries(a);
    System.out.println(v.entries());
    System.out.println(v.equals(v));
    System.out.println(v.entries());

    v.setEntries(b);
    System.out.println(v.entries());
    System.out.println(v.equals(v));
    System.out.println(v.entries());

    v.setEntries(c);
    System.out.println(v.entries());
    System.out.println(v.equals(v));
    System.out.println(v.entries());

    v.setEntries(d);
    System.out.println(v.entries());
    System.out.println(!v.equals(x));
    System.out.println(v.entries());
  }

  public void setEntries()
  {
    ;
  }

  public static void main (String [] args)
  {
    nullCheck();
    uniquePointers();
  }
}
