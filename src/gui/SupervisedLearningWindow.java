package gui;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;

public class SupervisedLearningWindow {

	public JFrame windowLearning;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SupervisedLearningWindow window = new SupervisedLearningWindow();
					window.windowLearning.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SupervisedLearningWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		windowLearning = new JFrame();
		windowLearning.setResizable(false);
		windowLearning.setTitle("Supervised learning");
		windowLearning.setBounds(100, 100, 442, 220);
		windowLearning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		windowLearning.getContentPane().setLayout(null);
		
		JTextArea phrasesArea = new JTextArea();
		phrasesArea.setEditable(false);
		phrasesArea.setBounds(12, 80, 412, 60);
		phrasesArea.setBorder(BorderFactory.createEtchedBorder());
		windowLearning.getContentPane().add(phrasesArea);
		
		JButton btnPrevious = new JButton("Previous");
		btnPrevious.setBounds(12, 150, 117, 25);
		windowLearning.getContentPane().add(btnPrevious);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(307, 150, 117, 25);
		windowLearning.getContentPane().add(btnNext);
		
		ButtonGroup rdbtnsPolarity = new ButtonGroup();
		
		JRadioButton rdbtnPositive = new JRadioButton("Positive");
		rdbtnPositive.setForeground(Color.GREEN);
		rdbtnPositive.setBounds(311, 49, 149, 23);
		windowLearning.getContentPane().add(rdbtnPositive);
		rdbtnsPolarity.add(rdbtnPositive);
		
		JRadioButton rdbtnNeutral = new JRadioButton("Neutral");
		rdbtnNeutral.setBounds(169, 49, 140, 24);
		windowLearning.getContentPane().add(rdbtnNeutral);
		rdbtnsPolarity.add(rdbtnNeutral);
		
		JRadioButton rdbtnNegative = new JRadioButton("Negative");
		rdbtnNegative.setForeground(Color.RED);
		rdbtnNegative.setBounds(30, 49, 136, 25);
		windowLearning.getContentPane().add(rdbtnNegative);
		rdbtnsPolarity.add(rdbtnNegative);
		
		JLabel labelTotal = new JLabel("15/30");
		labelTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		labelTotal.setBounds(195, 12, 64, 25);
		windowLearning.getContentPane().add(labelTotal);
	}
}
