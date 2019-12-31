import core.Matrix;
import core.Operations;
import core.Vector;
import core.GUI;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Application;

// Class to run the Application
public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Main.java:");
		Application.launch(GUI.class, "");
	}
}

// To Compile
// javac -cp ../ Main.java

// To run
// java -cp ../ Main.java
/*
 * BUGS:
 *
 * Find some way to let the Operations class interact directly with a matrix. Or
 * absolve the Operations java files into the matrix class.
 *
 * Directions: Before running, make sure core and GUI directory are updated.
 *
 * Need to use the -cp ../ to know that the files will be (packages) are in the
 * directory above.
 *
 *
 * From src file Use javac -d ../ -cp ../ Matrix.java or MatrixGUI.javac use
 * javac -cp ../ Driver.java
 *
 *
 * In bash: echo "export DISPLAY=localhost:0.0" >> ~/.bashrc sudo apt-get
 * install x11-apps.
 */
