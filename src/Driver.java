import gui.MatrixGUI;
import core.Vector;
import core.Matrix;
import core.Operations;

public class Driver
{
	public static void main(String[] args)
	{
		Vector a = new Vector(new double[]{1, 2, 3});
		Vector b = new Vector(new double[]{4, 5, 6});
		Vector c = new Vector(new double[]{7, 8, 9});

		Vector [] x = new Vector[]{a, b, c};

		Matrix m = new Matrix(x);
		m.printMatrix(false);
		//n.printMatrix(false);

		Matrix o = Operations.transpose(m);
		o.printMatrix(false);
	}
}
// BUGS:
// Apparently there is a mini bug when it comes to dealing with Matrix references.
// Scaling a matrix by -1 then adding it to itself breaks.
// Doing this same process with unique vectors does not cause any issue.
// Potential solution. If it turns out there references are the same at the start of the function.
// Find some way to let the Operations class interact directly with a matrix.
// Or absolve the Operations java files into the matrix class. <- Better solution.


// Before running, make sure core and GUI directory
// are updated.

// Need to use the -cp ../ to know that the files will be
// (packages) are in the directory above.

/*
	From src file Use javac -d ../ -cp ../ Matrix.java or MatrixGUI.javac
	use javac -cp ../ Driver.java
*/


// In bash: echo "export DISPLAY=localhost:0.0" >> ~/.bashrc
// audo apt-get install x11-apps.
