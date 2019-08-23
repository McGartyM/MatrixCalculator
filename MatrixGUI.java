//package guimatrix;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;
//import Matrix.matrix;

public class MatrixGUI
{
	// State member Variables
	private Matrix matrixInput;

	// UI Component Variables.
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu settings;
	private JMenu menu;
	private JMenuItem precision, help, version;
	private JPanel inputPane;
	private JPanel buttonPane;
	private JPanel sizePane;
	private JTextArea inputArea;
	private JTextField rowArea, colArea;
	private JScrollPane inputField;
	private JButton gsButton;
	private JButton sizeButton;
	private JButton matrixButton;

	public MatrixGUI()
	{
		this.matrixInput = null;

		initComponents();

		System.out.println("END OF INIT");
	}

	// incorporate adding num of columns and rows.
	private void initComponents()
	{
		String operation;
		//Arbitrary way to determine the size of the JPanel
		frame = new JFrame("Matrix Calculator");

		frame.setMinimumSize(new Dimension(460, 260));
		frame.setPreferredSize(new Dimension(460, 260));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Setting the Menu Bar and its items.
		menuBar = new JMenuBar();
		// Settings menu bar first;
		settings = new JMenu("Settings");

		precision = new JMenuItem("Precision");
		precision.putClientProperty("menuItem", "precision");
		precision.addActionListener(new MenuListener());

		settings.add(precision);

		// The remaining menu bar
		menu = new JMenu("About");

		help = new JMenuItem("Help");
		help.putClientProperty("menuItem", "help");
		help.addActionListener(new MenuListener());

		version = new JMenuItem("Version");
		version.putClientProperty("menuItem", "version");
		version.addActionListener(new MenuListener());

		menu.add(help);
		menu.add(version);

		menuBar.add(settings);
		menuBar.add(menu);

		// Setting the row and column textfields.
		sizePane = new JPanel();
		sizePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		rowArea = new JTextField();
		colArea = new JTextField();

		rowArea.setPreferredSize(new Dimension(50, 20));
		rowArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
		rowArea.getDocument().putProperty("field", "setup");

		colArea.setPreferredSize(new Dimension(50, 20));
		colArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
		colArea.getDocument().putProperty("field", "setup");

		rowArea.getDocument().addDocumentListener(new DocListener());
		colArea.getDocument().addDocumentListener(new DocListener());

		JLabel blankRows = new JLabel("Rows: "), blankCols = new JLabel("Columns:");
		JLabel blankSize = new JLabel();

		blankRows.setPreferredSize(new Dimension(50, 20));
		blankCols.setPreferredSize(new Dimension(75, 20));
		blankSize.setPreferredSize(new Dimension(80, 0));

		matrixButton = new JButton("Set Matrix");
		matrixButton.addActionListener(new InputListener());
		matrixButton.setMinimumSize(new Dimension(50, 15));
		matrixButton.putClientProperty("operation", "setup");
		matrixButton.setEnabled(false);

		sizePane.add(blankRows);
		sizePane.add(rowArea);
		sizePane.add(blankCols);
		sizePane.add(colArea);
		sizePane.add(blankSize);
		sizePane.add(matrixButton);

		// Setting the user matrix input pane and the button selection.
		inputPane = new JPanel();
		inputPane.setLayout(new FlowLayout());

		inputArea = new JTextArea();
		inputArea.getDocument().putProperty("field", "setup");
		inputArea.getDocument().addDocumentListener(new DocListener());

		inputField = new JScrollPane(inputArea);

		inputField.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputField.setPreferredSize(new Dimension(300, 200));
		inputField.setBorder(new TitledBorder("Matrix Input Field"));

		inputPane.add(inputField);

		buttonPane = new JPanel();
		buttonPane.setMinimumSize(new Dimension(160, 260));
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		gsButton = new JButton("Gram-Schmidt");
		gsButton.addActionListener(new InputListener());
		gsButton.setMinimumSize(new Dimension(50, 15));
		gsButton.putClientProperty("operation", "GS");

		buttonPane.add(gsButton);
		buttonPane.setBorder(new TitledBorder("Operations Field"));

		frame.setJMenuBar(menuBar);
		frame.add(sizePane, BorderLayout.NORTH);
		frame.add(inputPane, BorderLayout.LINE_START);
		frame.add(buttonPane, BorderLayout.CENTER);

		// There we go!
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void printArgs()
	{
		Matrix temp = this.matrixInput;

		System.out.printf("\nSize: (%d, %d)\n", temp.getRows(), temp.getCols());

		System.out.println("Printing Matrix Entries:\n\n");
		for (int i = 0; i < temp.getRows(); i++)
		{
			for (int j = 0; j < temp.getCols(); j++)
				System.out.printf("%.3f ", temp.getArray()[i][j]);
			System.out.println();
		}
	}

 private class InputListener implements ActionListener
 {
		private String data;
		public void actionPerformed(ActionEvent event)
		{
			JButton button = (JButton) event.getSource();
			String operation = (String) button.getClientProperty("operation");

			if (operation.equals("setup"))
			{
				int rows, cols;
				String data;
				System.out.println("Setting Matrix Parameters:");

				rows = Integer.parseInt(rowArea.getText().trim());
				cols = Integer.parseInt(colArea.getText().trim());
				data = inputArea.getText().trim();

				matrixInput = new Matrix(rows, cols, data);

				printArgs();
			}
			else if (operation.equals("GS"))
			{
				System.out.println("Gram-Schmidt Button Pressed.");
			}
		}
 }

// Can condense the menu listeners with client properties.
private class MenuListener implements ActionListener
{
	public void actionPerformed(ActionEvent event)
	{
		JMenuItem item = (JMenuItem) event.getSource();
		String subItem = (String) item.getClientProperty("menuItem");

		if (subItem.equals("precision"))
		{
			System.out.println("Precision menuItem pressed.");
		}
		else if (subItem.equals("help"))
		{
			JOptionPane.showMessageDialog(frame, "Here is my help menuItem");
		}
		else if (subItem.equals("version"))
		{
			JOptionPane.showMessageDialog(frame, "Here is my Version menuItem");
		}
	}
}
	// private class HelpListener implements ActionListener
	// {
	// 	public void actionPerformed(ActionEvent event)
	// 	{
	// 		JOptionPane.showMessageDialog(frame, "Here is my help menuItem.");
	// 	}
	// }
	//
	// private class VersionListener implements ActionListener
	// {
	// 	public void actionPerformed(ActionEvent event)
	// 	{
	// 		JOptionPane.showMessageDialog(frame, "Here is my Version menuItem");
	// 	}
	// }

	// May need another documentListener for
	private class DocListener implements DocumentListener
	{
		public void removeUpdate(DocumentEvent e)
		{
			checkForText(e);
		}

		public void insertUpdate(DocumentEvent e)
		{
			checkForText(e);
		}

		public void changedUpdate(DocumentEvent e)
		{
			checkForText(e);
		}

		private void checkForText(DocumentEvent event)
		{

			if (event.getDocument().getProperty("field").equals("setup"))
			{
				String a, b, c;
				boolean validSize = false, validEntries = false, enable;
				int row = 0, col = 0, count = 0;

				a = rowArea.getText().trim();
				b = colArea.getText().trim();
				c = inputArea.getText().trim();

				validSize = (!a.isEmpty() && !b.isEmpty()) && (isNum(a) && isNum(b));
				//enable = (validSize && (c.length() > 0));
				if (validSize)
				{
					row = Integer.parseInt(a);
					col = Integer.parseInt(b);
				}

				if (c.length() > 0)
				{
					validEntries = Matrix.checkEntries(c, row * col);
				}

				enable = validSize && validEntries;
				//System.out.println(validSize + " " + enable + " c.length(): " + c.length());

				matrixButton.setEnabled(enable);
			}
		}

		public boolean isNum(String x)
		{
			try
			{
				Integer.parseInt(x);
				return true;
			}
			catch(NumberFormatException exception)
			{
				//Logging.log(exception);
				//System.out.println("Exception!");
				return false;
			}
		}
	};
}

// In bash: echo "export DISPLAY=localhost:0.0" >> ~/.bashrc
// audo apt-get install x11-apps.
