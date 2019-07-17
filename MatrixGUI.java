package guimatrix;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.event.*;

public class MatrixGUI
{
	private JFrame frame;
	private JPanel inputPane;
	private JPanel outputPane;
	private JScrollPane inputField;
	private JTextArea inputArea;
	public MatrixGUI()
	{

		initComponents();
	}

	private void initComponents()
	{
		//System.out.println("Made it to the constructor");

		frame = new JFrame("Matrix Calculator");
		inputPane = new JPanel();
		inputArea = new JTextArea();
		inputField = new JScrollPane(inputArea);
		// Setting of the inputPane data.
		inputField.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		inputField.setPreferredSize(new Dimension(200, 200));
		inputField.setBorder(new TitledBorder("Matrix Input Field"));

		inputPane.setLayout(new FlowLayout());
		inputPane.add(inputField);

		// Setting of the JFrame properties.
		frame.setMinimumSize(new Dimension(300, 200));
		frame.setPreferredSize(new Dimension(400, 300));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());

		frame.add(inputPane, BorderLayout.WEST);
		frame.setVisible(true);
	}
}
