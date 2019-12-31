package core;

import java.util.Arrays;
import java.lang.StringBuilder;

// This class is a non-dynamic array wrapper.
// I decided to creat is as a way of abscrating arrays into Vectors. Since some
// Matrix operations are better though of as operations on collections of Vectors.

public class Vector
{
  private int length;
  private double [] entries;
  private static int precision = 4; // Default 4 decimal precision
  private static String format = "%.4f";
  private static double epsilon = 1e-5;

  private static final int MAX_PRECISION = 12;

  // Constructors:
  //============================================================================

  // Creates a Vector of length 10 with values initialized to zero.
  public Vector()
  {
    this(10);
  }

  // Creates a Vector of length n, with all values initialized to zero.
  // Lengths below one will return an uninitialized Vector. (a null array with 0 length)
  public Vector(int n)
  {
    if (n <= 0)
    {
      this.length = 0;
      this.entries = null;
      return;
    }

    this.entries = new double [n];
    this.length = n;
  }

  // Creates a Vector from an array of existing values.
  // Passing a null reference will return an uninitialized Vector.
  public Vector(double [] entries)
  {
    if (entries == null)
    {
      this.length = 0;
      this.entries = null;
      return;
    }

    this.length = entries.length;
    this.entries = new double[this.length];
    System.arraycopy(entries, 0, this.entries, 0, this.length);
  }

  // Vector Operations:
  //============================================================================

  // Prints the outputs of a Vector to the console as a column Vector.
  // Uninitialized Vectors will not generate output.
  public void print()
  {
    for (int i = 0; i < length; i++)
      System.out.println("| " + String.format(format, this.entries[i]) + " |");
  }

  // Prints the outputs of a Vector to the console as a row Vector (Transposed).
  // Again, unitialized Vectors will print nothing.
  public void printRow()
  {
    for (int i = 0; i < length; i++)
      System.out.print("| " + String.format(format, this.entries[i]) + "| ");
  }

  // Creates a clone of the current Vector, returning a unique object with identical entries.
  public Vector copy()
  {
    if (length <= 0)
      return new Vector(0);

    return new Vector(entries);
  }

  // Returns a boolean representing whether two vectors are identical in dimension
  // and if they hold identical values. The operation is done through a fuzzy comparison.
  @Override
  public boolean equals(Object o)
  {
    if (!(o instanceof Vector))
      return false;

    Vector v = (Vector) o;
    if (this.length != v.length)
      return false;

    boolean equal = true;
    for (int i = 0; i < this.length; i++)
      if (!nearlyEqual(this.entries[i], v.entries[i], this.epsilon))
        equal = false;

    return equal;
  }

  // Arrays which have completely identical elements will have identical hashcodes.
  // The hashCode is dependent on the user-set precision. It will truncate each
  // entry to the precision set before computing the hashcode.
  @Override
  public int hashCode()
  {
    double [] approximateEntries = new double[length];
    for (int i = 0; i < length; i++)
    {
      String temp = String.format(format, entries[i]);
      approximateEntries[i] = Double.parseDouble(temp);
    }

    return Arrays.hashCode(approximateEntries);
  }

  // Computes and returns a unique Vector with entries this + v.
  // Requires a valid argument with identical dimensions or null will be returned.
  public Vector add(Vector v)
  {
    if (notValid(v, "Vector.add()", "null"))
      return null;

    if (invalidDimensions(this, v, "add()", "null"))
      return null;

    double [] newEntries = new double[this.length];
    for (int i = 0; i < this.length; i++)
			newEntries[i] = entries[i] + v.entries[i];

    return new Vector(newEntries);
  }

  // Returns a unique Vector with entries this - v.
  // Requires a valid argument with identical dimensions or null will be returned.
  public Vector subtract(Vector v)
  {
    if (notValid(v, "subtract()", "null"))
      return null;

    if (invalidDimensions(this, v, "subtract()", "null"))
      return null;

    double [] newEntries = new double[this.length];
    for (int i = 0; i < this.length; i++)
      newEntries[i] = entries[i] - v.entries[i];

    return new Vector(newEntries);
  }

  // Returns a unique Vector where each entry is scaled by the input.
  // If an uninitialized Vector is passed, that Vector is returned.
  public Vector scale(double scalar)
  {
    if (notValid(this, "scale()", "original vector"))
      return this;

    Vector v = this.copy();
    for (int i = 0; i < length; i++)
      v.entries[i] *= scalar;

    return v;
  }

  // Computes and returns the dot product of two Vectors.
  // If the dimensions of the vectors are not identical, or one of them
  // is uninitialized, Double.NaN will be returned.
  public double dotProduct(Vector v)
  {
    Double result = calculateDotProduct(v);
    if (result != null)
      return result.doubleValue();

    return Double.NaN;
  }

  // Computes and returns a Vector that is the projection of v onto u.
  // Uninitialized Vectors and unidentical dimensions will return null.
  public static Vector projection(Vector v, Vector u)
  {
    if (notValid(v, "Vector.projection()", "null") || notValid(u, "Vector.projection()", "null"))
      return null;

    if (invalidDimensions(v, u, "Vector.projectio()", "null"))
      return null;

    // If u is the zero Vector, v projects to the zero vector.
    double magnitude = u.dotProduct(u);
    if (magnitude == 0)
      return new Vector(v.length());

    Vector projected = u.copy();
    return projected.scale((double) v.dotProduct(u) / magnitude);
  }

  // Getters and setters:
  //============================================================================

  public int length()
  {
    return this.length;
  }

  public int precision()
	{
		return this.precision;
	}

  // Returns a String of the decimal format. -> "%.nf"
  public String format()
  {
      return this.format;
  }

  public double [] entries()
  {
    return this.entries;
  }

  // Returns the value stored at the specific index of a Vector.
  // Indicies out of bounds will throw an OutOfBounds exception.
  public double valueAt(int index)
  {
    return entries[index];
  }

  // Set and update the decimal precision of ALL Vectors.
  public void setPrecision(int precision)
  {
    if (precision > MAX_PRECISION)
      return;

    this.precision = precision;
    this.format = setFormat();
  }

  // Replace a Vector's contents with a new array of identical size.
  // If the array has a different length, or it null, no update will occur.
  public void setEntries(double [] values)
  {
    if (values == null)
    {
      System.err.println("-- Null reference in setVector() -- ");
      return;
    }

    if (values.length != entries.length)
    {
      System.err.println(" -- Attempting to set Vector ( " + this + " ) to an invalid " +
                        "dimension in setVector() -- ");
      return;
    }

    // Update contents of the current array, without modifying parameter.
    System.arraycopy(values, 0, this.entries, 0, this.length);
  }

  // Updates a specific index of the Vector with a new value.
  // Out of bounds indicies will not update Vector.
  public void setEntry(int index, double value)
  {
    if (index < 0 || index > this.length)
    {
      System.err.println("Invalid index for Vector (" + this.toString() +
                         ") in setEntry()");
      return;
    }

    this.entries[index] = value;
  }

  // Class-specific helper methods
  //============================================================================
  // Helper method which actually computes the dot product.
  private Double calculateDotProduct(Vector v)
  {
    if (notValid(v, "addVectors()", "null"))
      return null;

    if (invalidDimensions(this, v, "dotProduct()", "null"))
      return null;

    double dotProduct = 0;
    for (int i = 0; i < this.length; i++)
      dotProduct += (this.entries[i] * v.entries[i]);

    return dotProduct;
  }

  // Returns whether or not two floating point numbers are equal to a certain precision.
  // Source:   http://floating-point-gui.de
  public static boolean nearlyEqual(double a, double b, double epsilon)
  {
    final double absA = Math.abs(a);
    final double absB = Math.abs(b);
    final double diff = Math.abs(a - b);

    if (a == b)
      return true;
    else if (a == 0 || b == 0 || (absA + absB) < epsilon)
      return diff < epsilon;
    else
      return diff / Math.min((absA + absB), Double.MAX_VALUE) < (epsilon);
  }

  // Creates a String representing the decimal format of the Vector..
  // For example, a decimal precision of 4 is formated as %.4f
  private static String setFormat()
  {
    StringBuilder str = new StringBuilder();
    str.append("%.");
    str.append(precision);
    str.append('f');

    return str.toString();
  }

  // Error-checking methods
  //============================================================================

  // Returns true if given Vector is not properly initialied, false otherwise.
  // Proper initialization is defined as a valid object and entries reference.
  private static boolean notValid(Vector v, String function, String retVal)
  {
    if (v == null)
    {
      System.err.printf("-- Null parameter passed into %s. --\n", function );
      System.err.printf("-- Returning %s. --\n", retVal);
      return true;
    }

    if (v.entries == null)
    {
      System.err.printf("-- Vector ( %s ).entries() is null in %s. --\n", v, function );
      System.err.printf("-- Returning %s. --\n", retVal);
      return true;

    }
    if (v.length == 0)
    {
      System.err.printf("-- Vector ( %s ) is uninitialized in %s. --\n", v, function );
      System.err.printf("-- Returning %s. --\n", retVal);
      return true;
    }

    return false;
  }

  // Returns a boolean if the dimensions of the two vectors are identical.
  private static boolean invalidDimensions(Vector u, Vector v, String function, String retVal)
  {
    if (u.length != v.length)
    {
      System.err.printf("-- Different Vector dimensions in %s. --\n",  function);
      System.err.printf("-- Vector u ( %s ) has length %d | Vector v ( %s ) has length %d\n", u, u.length, v, v.length);
      System.err.printf("Returning %s\n", retVal);
      return true;
    }

    return false;
  }
}
