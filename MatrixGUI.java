package guimatrix;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;
import java.awt.event.*;

public class MatrixGUI
{
	// State member Variables
	private int row, col;
	private String stream;

	// UI Component Variables.
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu menu;
	private JMenuItem help, version;
	private JPanel inputPane;
	private JPanel buttonPane;
	private JTextArea inputArea;
	private JScrollPane inputField;
	private JButton gsButton;


	public MatrixGUI(int row, int col)
	{
		this.row = row;
		this.col = col;

		initComponents();
	}

	// incorporate adding num of columns and rows.
	private void initComponents()
	{
		//Arbitrary way to determine the size of the JPanel
		frame = new JFrame("Matrix Calculator");

		frame.setMinimumSize(new Dimension(460, 230));
		frame.setPreferredSize(new Dimension(460, 230));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Setting the Menu Bar and its items.
		menuBar = new JMenuBar();
		menu = new JMenu("About");

		help = new JMenuItem("Help");
		help.addActionListener(new HelpListener());

		version = new JMenuItem("Version");
		version.addActionListener(new VersionListener());

		menu.add(help);
		menu.add(version);

		menuBar.add(menu);

		// Setting the user matrix input pane and the button selection.
		inputPane = new JPanel();
		inputPane.setLayout(new FlowLayout());

		inputArea = new JTextArea();
		inputField = new JScrollPane(inputArea);

		inputField.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputField.setPreferredSize(new Dimension(300, 200));
		inputField.setBorder(new TitledBorder("Matrix Input Field"));

		inputPane.add(inputField);

		buttonPane = new JPanel();
		buttonPane.setMinimumSize(new Dimension(160, 230));
		buttonPane.setLayout(new FlowLayout(FlowLayout.CENTER));

		gsButton = new JButton("Gram-Schmidt");
		gsButton.addActionListener(new InputListener());
		gsButton.setMinimumSize(new Dimension(50, 15));

		buttonPane.add(gsButton);
		buttonPane.setBorder(new TitledBorder("Operations Field"));

		frame.setJMenuBar(menuBar);
		frame.add(inputPane, BorderLayout.LINE_START);
		frame.add(buttonPane, BorderLayout.CENTER);

		// There we go!
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

 private class InputListener implements ActionListener
 {
		private String string;
		public void actionPerformed(ActionEvent event)
		{
			System.out.println("Gram-Schmidt Pressed.");
			// Some function to get all the data from the input Field.
		}
 }

	private class HelpListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JOptionPane.showMessageDialog(frame, "Here is my help menuItem.");
		}
	}

	private class VersionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			JOptionPane.showMessageDialog(frame, "Here is my Version menuItem");
		}
	}
}

// In bash: echo "export DISPLAY=localhost:0.0" >> ~/.bashrc
// audo apt-get install x11-apps.
