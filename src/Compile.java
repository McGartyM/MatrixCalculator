import java.util.regex.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class Compile
{
	public static void main (String [] args)
	{
		Instant starts = Instant.now();
		try
		{
			runProcess("javac -d ../ -cp ../ Vector.java");
			runProcess("javac -d ../ -cp ../ Matrix.java");
			runProcess("javac -d ../ -cp ../ Operations.java");
			runProcess("javac -d ../ -cp ../ MatrixGUI.java");
			runProcess("javac -cp ../ Driver.java");

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		Instant ends = Instant.now();
		Duration span = Duration.between(starts, ends);
		System.out.println("Time Elapsed: " + 		span.getSeconds() + " second(s).");
	}
	private static void runProcess(String command) throws Exception
	{
		Process p = Runtime.getRuntime().exec(command);
		String name = stripFilename(command);

		System.out.printf("Compiling: %s\n", name);
		p.waitFor();

		if (p.exitValue() == 0)
			System.out.println(name + " has successfully compiled.");
		else
			System.out.println(name + " has experienced an error while compiling.");

		System.out.println();
	}

	private static String stripFilename(String command)
	{
		Pattern p = Pattern.compile("(\\w*\\.java)");
		Matcher m = p.matcher(command);

		if (!m.find())
			return "Could not find filename..";
		else
			return m.group();
	}
}
