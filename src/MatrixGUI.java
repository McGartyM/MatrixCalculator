package gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import javax.swing.event.*;
import core.Matrix;
import core.Operations;

public class MatrixGUI
{
	// State member Variables
	private Matrix matrixInput;

	// UI Component Variables.
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu settings, about;
	private JMenuItem precision, help, version;
	private JPanel inputPane, buttonPane, sizePane;
	private JTextArea inputArea;
	private JTextField rowArea, colArea;
	private JScrollPane inputField;
	private JButton displayButton, gsButton, sizeButton, matrixButton;

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

		// Setting up the initial root frame.
		frame = new JFrame("Matrix Calculator");

		frame.setMinimumSize(new Dimension(460, 260));
		frame.setPreferredSize(new Dimension(460, 260));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Initializing the menu.
		initMenu();

		// Initializeing the top header of the GUI.
		initHeader();

		// Setting the user matrix input pane and the button selection.
		initText();

		// Initiating the Panel holding all the button operations.
		initButtons();

		// Adding the main JPanels onto the root frame.
		frame.setJMenuBar(menuBar);
		frame.add(sizePane, BorderLayout.NORTH);
		frame.add(inputPane, BorderLayout.LINE_START);
		frame.add(buttonPane, BorderLayout.CENTER);

		// Finalizing frame initialization.
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public void initMenu()
	{
		// MenuBar is the top level container for the menu bar.
		menuBar = new JMenuBar();

		// Settings tab.
		//==============================================================================================
		settings = new JMenu("Settings");

		precision = new JMenuItem("Precision");
		precision.putClientProperty("menuItem", "precision");
		precision.addActionListener(new MenuListener());

		settings.add(precision);
		// Help tab.
		//=============================================================================================
		about = new JMenu("About");

		help = new JMenuItem("Help");
		help.putClientProperty("menuItem", "help");
		help.addActionListener(new MenuListener());

		version = new JMenuItem("Version");
		version.putClientProperty("menuItem", "version");
		version.addActionListener(new MenuListener());

		// Appending up the JMenuBar.
		about.add(help);
		about.add(version);

		menuBar.add(settings);
		menuBar.add(about);
	}

	public void initHeader()
	{
		// Initializing JPanel for the GUI "header".
		sizePane = new JPanel();
		sizePane.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		rowArea = new JTextField();
		colArea = new JTextField();

		// Setup for row and column input textboxes.
		rowArea.setPreferredSize(new Dimension(50, 20));
		rowArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
		rowArea.getDocument().putProperty("field", "setup");

		colArea.setPreferredSize(new Dimension(50, 20));
		colArea.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GRAY));
		colArea.getDocument().putProperty("field", "setup");

		// Document listener to allow parsing and extraction of data.
		rowArea.getDocument().addDocumentListener(new DocListener());
		colArea.getDocument().addDocumentListener(new DocListener());

		// Blank JLabels to help with look and feel of the header.
		JLabel blankRows = new JLabel("Rows: "), blankCols = new JLabel("Columns:");
		JLabel blankSize = new JLabel();

		blankRows.setPreferredSize(new Dimension(50, 20));
		blankCols.setPreferredSize(new Dimension(75, 20));
		blankSize.setPreferredSize(new Dimension(80, 0));

		// Setup of button to submit matrix parameters for matrix constructor.
		matrixButton = new JButton("Set Matrix");
		matrixButton.addActionListener(new InputListener());
		matrixButton.setMinimumSize(new Dimension(50, 15));
		matrixButton.putClientProperty("operation", "setup");
		matrixButton.setEnabled(false);

		// Appending JLabels and text fields onto the header JPanel.
		sizePane.add(blankRows);
		sizePane.add(rowArea);
		sizePane.add(blankCols);
		sizePane.add(colArea);
		sizePane.add(blankSize);
		sizePane.add(matrixButton);
	}

	public void initText()
	{
		// Setup of the matrix entries input JPanel.
		inputPane = new JPanel();
		inputPane.setLayout(new FlowLayout());

		inputArea = new JTextArea();
		inputArea.getDocument().putProperty("field", "setup");
		inputArea.getDocument().addDocumentListener(new DocListener());

		// Recall that an textPane is created from an textarea.
		inputField = new JScrollPane(inputArea);

		inputField.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputField.setPreferredSize(new Dimension(300, 200));
		inputField.setBorder(new TitledBorder("Matrix Input Field"));

		inputPane.add(inputField);
	}

	public void initButtons()
	{
		// Setup of JPanel holding the operations buttons.
		buttonPane = new JPanel();
		buttonPane.setMinimumSize(new Dimension(160, 260));
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));
		buttonPane.setBorder(new TitledBorder("Operations Field"));

		// Note to add a button here to display the current saved matrix into MatrixGUI.
		displayButton = new JButton("Display Saved");
		displayButton.addActionListener(new InputListener());
		displayButton.setMinimumSize(new Dimension(50, 15));
		displayButton.putClientProperty("operation", "display");

		// Gram-Schmidt Button.
		gsButton = new JButton("Gram-Schmidt");
		gsButton.addActionListener(new InputListener());
		gsButton.setMinimumSize(new Dimension(50, 15));
		gsButton.putClientProperty("operation", "gram");

		buttonPane.add(displayButton);
		buttonPane.add(gsButton);
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
			else if (operation.equals("display"))
			{
				System.out.println("Display button Pressed.");
				Matrix.print2DArray(matrixInput.getArray(), matrixInput.getRows(), matrixInput.getCols());
			}
			else if (operation.equals("gram"))
			{
				System.out.println("Gram-Schmidt Button Pressed.");
				Operations.gramSchmidt(matrixInput);
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

				String text = "The current decimal precision is ..." +
									  "lease enter the new precision in the" +
										"textbox below, if desired.";

				Object [] possibilities = {"1", "2", "3", "4", "5", "6"};

				//String s = (String) JOptionPane.showInputDialog(frame, text, "Change decimal Precision", null, possibilities, "1");
				JDialog d = new JDialog(frame, "Example", true);
				JLabel instructions = new JLabel(text);

				d.setLayout(new FlowLayout());
				d.setSize(300,225);
				d.setLocationRelativeTo(null);

				d.add(instructions);
				d.setVisible(true);
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
						//System.out.println("Valid Entries: " + validEntries);
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
   }
}
