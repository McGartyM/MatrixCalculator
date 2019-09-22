package core;

import java.util.Scanner;
import java.lang.Math;
import java.lang.String;

// Class could be abstracted to allow for different object vectors. Strictly doubles for now.
public class Vector
{
  private double [] entries;
  private double magnitude;
  private int length;
	private static int precision;
  private String format;

  // Default constructor:
  public Vector(double [] entries)
  {
    this.precision = 4; // default precision need to implement
    this.format = setFormat();

    if (entries == null)
      return;

    this.entries = entries;
    this.length = entries.length;
    this.magnitude = this.calculateMagnitude();
  }

  // Vector Operations.
  //===============================================================================================

  // Display the Vector entries as a column Vector.
  public void printVector()
  {
    if (nullCheck(this, "printVector() [this]"))
    return;

    // Can implement some regex solution so that everything is properly alligned.
    for (int i = 0; i < length; i++)
    System.out.println("| " + String.format(format, this.entries[i]) + " |");
  }

  // Add two Vectors of identical dimension and return a new Vector.
  public Vector addVector(Vector v)
  {
    if (nullCheck(this, "addVectors() [this]") || nullCheck(v, "addVectors() [arg]"))
      return null;

    if (dimensionCheck(this, v))
    {
      errorStatement("addVectors()", this.length, v.length);
      return null;
    }


    double [] newEntries = new double[this.length];

    for (int i = 0; i < this.length; i++)
      newEntries[i] = entries[i] + v.entries[i];

    return new Vector(newEntries);
  }

  // Scale a Vector by a double without creating a new object.
  public void scaleVector(double scalar)
  {
    if (nullCheck(this, "scaleVector() [this]"))
      return;

    for (int i = 0; i < length; i++)
      entries[i] *= scalar;
  }

  // Compute the dot product of two Vectors of identical dimensions.
  public Double dotProduct(Vector v)
  {
    double dotProduct = 0;

    if (nullCheck(this, "addVectors() [this]") || nullCheck(v, "addVectors() [arg]"))
      return null;

    if (dimensionCheck(this, v))
    {
      errorStatement("dotProduct()", this.length, v.length);
      return null;
    }

    for (int i = 0; i < this.length; i++)
      dotProduct += (this.entries[i] * v.entries[i] );

    return dotProduct;
  }

  // Utility functions.
  //===============================================================================================

  // Error checking function if a Vector object is null and if it was properly initialized.
  private boolean nullCheck(Vector v, String function)
  {
    if (v == null)
    {
      System.out.println("Vector object is null in " + function + '.');
      return true;
    }

    if (v.entries == null || v.length == 0)
    {
      System.out.println("Vector fields null in " + function + '.');
      return true;
    }

    return false;
  }

  // Check if the dimensions of two Vectors are equal.
  private boolean dimensionCheck(Vector x, Vector y)
  {
    return (x.length != y.length);
  }

  // Helper function for dimension check. Abstracting purpose to allow for custom messages.
  private void errorStatement(String message, int x, int y)
  {
    System.err.println("Invalid dimensions in function " + message + ".");
    System.err.println("This.length = " + x + ". Vector.length = " + y + ".\n");
    return;
  }

  // Calculates the magnitude to be stored in the objects magnitude field.
  private double calculateMagnitude()
  {
    double magnitude = 0.0;

    for (int i = 0; i < length; i++)
    magnitude += (entries[i] * entries[i]);

    return Math.sqrt(magnitude);
  }

  // Updates the string representing to console format / printing of the Vector entries.
  public static String setFormat()
  {
    String temp = Integer.toString(precision);
    int accuracy = temp.length();
    char [] preformat = new char [2 + accuracy + 1];

    preformat[0] = '%';
    preformat[1] = '.';
    preformat[2 + accuracy] = 'f';

    for (int j = 0; j < accuracy; j++)
    preformat[j + 2] = temp.charAt(j);

    return new String(preformat);
  }

  // Getters and setters:
  //===============================================================================================

  // Retrieve vector entries.
  public double [] getValues()
  {
    return this.entries;
  }

  // Retrieve vector dimension.
  public int length()
  {
    return this.length;
  }

  // Retrieve vector magnitude.
  public double magnitude()
  {
    return this.magnitude;
  }

	public int getPrecision()
	{
		return this.precision;
	}
  // Set and update the decimal precision of the Vector.
  public void setPrecision(int precision)
  {
    this.precision = precision;
    this.format = setFormat();
    return;
  }

  // Replace the Vector entries with a new array of values. (Updates fields accordingly)
  public boolean setVector(double [] values)
  {
    if (values == null)
    return false;

    this.entries = values;
    this.length = values.length;
    this.magnitude = calculateMagnitude();

    return true;
  }

  // Updates a specific index of the Vector.
  public boolean setEntry(int index, double value)
  {
    if (index < 0 || index > this.length)
    return false;

    this.entries[index] = value;
    return true;
  }
}
