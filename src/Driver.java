import gui.MatrixGUI;
import core.Matrix;

public class Driver
{
	public static void main(String[] args)
	{
		MatrixGUI a = new MatrixGUI();
	}
}

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
