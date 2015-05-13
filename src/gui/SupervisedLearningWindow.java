package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;

import logic.Training;

public class SupervisedLearningWindow {

	public JFrame windowLearning;
	private Training training_phrases;

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

		training_phrases = new Training();
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
		
		JTextArea phrasesArea = new JTextArea(20, 50);
		phrasesArea.setEditable(false);
		phrasesArea.setBounds(12, 80, 412, 60);
		phrasesArea.setBorder(BorderFactory.createEtchedBorder());
		windowLearning.getContentPane().add(phrasesArea);
		phrasesArea.setText(training_phrases.getNextPhrase());
		phrasesArea.setRows(25);
		phrasesArea.setColumns(25);
		phrasesArea.setWrapStyleWord(true);
		phrasesArea.setLineWrap(true);
		
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
		
		JLabel labelTotal = new JLabel("0");
		labelTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		labelTotal.setBounds(195, 12, 64, 25);
		windowLearning.getContentPane().add(labelTotal);
		
		windowLearning.getContentPane().setVisible(true);
		
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String phrase;
				if(!rdbtnPositive.isSelected() && !rdbtnNeutral.isSelected() && !rdbtnNegative.isSelected()) {
					JOptionPane.showMessageDialog(windowLearning,
							"You must classify the phrase.",
						    "",
						    JOptionPane.ERROR_MESSAGE);
				} else if((phrase = training_phrases.getNextPhrase()) != null) {
					phrasesArea.setText(phrase);
					int number = Integer.parseInt(labelTotal.getText()) + 1;
					labelTotal.setText(Integer.toString(number));
					
					rdbtnsPolarity.clearSelection();
				}
			}
		});
	}
}
