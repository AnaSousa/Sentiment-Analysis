package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import logic.Training;
import logic.Training.EvaluatedPhrase;

public class SupervisedLearningWindow {

	public JFrame windowLearning;
	
	private Training training;
	private Vector<String> training_phrases = new Vector<String>();
	private Vector<EvaluatedPhrase> evaluated_phrases = new Vector<EvaluatedPhrase>();
	private EvaluatedPhrase currentPhrase = null;
	private int count = 0;
	
	private int NEUTRAL_VALUE = 0;
	private int POSITIVE_VALUE = 1;
	private int NEGATIVE_VALUE = -1;
	

	/**
	 * Launch the application.
	 */
	/*public static void main(String[] args) {
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
	}*/
	
	/**
	 * Create the application.
	 */
	public SupervisedLearningWindow() {
		
		try {
			UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		training = new Training();
		training_phrases = training.getPhrases();
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

		final JTextArea phrasesArea = new JTextArea(20, 50);
		phrasesArea.setEditable(false);
		phrasesArea.setBounds(12, 80, 412, 60);
		phrasesArea.setBorder(BorderFactory.createEtchedBorder());
		windowLearning.getContentPane().add(phrasesArea);

		//EvaluatedPhrase ep = null;
		if(count < training_phrases.size()) {
			currentPhrase = training.getValue(count, training_phrases.get(count));
			
			if(currentPhrase != null)
				evaluated_phrases.addElement(currentPhrase);
			else
				currentPhrase = evaluated_phrases.get(count);
			
			phrasesArea.setText(currentPhrase.str);
			count++;
		} else {
			phrasesArea.setText("Ficheiro vazio, tente outra vez");
		}
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

		final ButtonGroup rdbtnsPolarity = new ButtonGroup();

		final JRadioButton rdbtnPositive = new JRadioButton("Positive");
		rdbtnPositive.setForeground(Color.GREEN);
		rdbtnPositive.setBounds(311, 49, 149, 23);
		windowLearning.getContentPane().add(rdbtnPositive);
		rdbtnsPolarity.add(rdbtnPositive);

		final JRadioButton rdbtnNeutral = new JRadioButton("Neutral");
		rdbtnNeutral.setBounds(169, 49, 140, 24);
		windowLearning.getContentPane().add(rdbtnNeutral);
		rdbtnsPolarity.add(rdbtnNeutral);

		final JRadioButton rdbtnNegative = new JRadioButton("Negative");
		rdbtnNegative.setForeground(Color.RED);
		rdbtnNegative.setBounds(30, 49, 136, 25);
		windowLearning.getContentPane().add(rdbtnNegative);
		rdbtnsPolarity.add(rdbtnNegative);

		if(currentPhrase.has_value)
		{
			if(currentPhrase.value == 0)
				rdbtnNeutral.setSelected(true);
			else if(currentPhrase.value < 0)
				rdbtnNegative.setSelected(true);
			else rdbtnPositive.setSelected(true);
		}
		
		rdbtnPositive.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	currentPhrase.value = POSITIVE_VALUE;
	        }
		});
		
		rdbtnNeutral.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	currentPhrase.value = NEUTRAL_VALUE;
	        }
		});
		
		rdbtnNegative.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	currentPhrase.value = NEGATIVE_VALUE;
	        }
		});
		
		
		
		final JLabel labelTotal = new JLabel("1 / " + Integer.toString(training_phrases.size()));
		labelTotal.setFont(new Font("Dialog", Font.BOLD, 16));
		labelTotal.setBounds(195, 12, 64, 25);
		windowLearning.getContentPane().add(labelTotal);
		
		JButton btnFinish = new JButton("Finish");
		btnFinish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				System.out.println("Writing to arff file training results..");
				training.saveToArffFile(evaluated_phrases, training_phrases, count - 1);
				System.out.println("Finished writing to arff file training results..");
				
			}
		});
		btnFinish.setBounds(164, 150, 117, 25);
		windowLearning.getContentPane().add(btnFinish);

		windowLearning.getContentPane().setVisible(true);

		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(count - 2 >= 0) {
					count--; count--;
				
					currentPhrase = training.getValue(count, training_phrases.get(count));
					
					if(currentPhrase != null)
						evaluated_phrases.addElement(currentPhrase);
					else
						currentPhrase = evaluated_phrases.get(count);
					
					phrasesArea.setText("" + currentPhrase.str);

					rdbtnsPolarity.clearSelection();
					if(currentPhrase.has_value)
					{
						if(currentPhrase.value == 0)
							rdbtnNeutral.setSelected(true);
						else if(currentPhrase.value < 0)
							rdbtnNegative.setSelected(true);
						else rdbtnPositive.setSelected(true);
					}
					
					count++;
					labelTotal.setText(Integer.toString(count) + " / " + Integer.toString(training_phrases.size()));
				}
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

				if(!rdbtnPositive.isSelected() && !rdbtnNeutral.isSelected() && !rdbtnNegative.isSelected()) {
					JOptionPane.showMessageDialog(windowLearning,
							"You must classify the phrase.",
							"",
							JOptionPane.ERROR_MESSAGE);

				} else if(count < training_phrases.size() && count >= 0) {
					
					/*if(rdbtnNegative.isSelected())
						currentPhrase.value = NEGATIVE_VALUE;
					else if(rdbtnNeutral.isSelected())
						currentPhrase.value = NEUTRAL_VALUE;
					else if(rdbtnPositive.isSelected())
						currentPhrase.value = POSITIVE_VALUE;*/
					
					currentPhrase = training.getValue(count, training_phrases.get(count));
					
					if(currentPhrase != null)
						evaluated_phrases.addElement(currentPhrase);
					else
						currentPhrase = evaluated_phrases.get(count);
					
					phrasesArea.setText(currentPhrase.str);

					rdbtnsPolarity.clearSelection();

					if(currentPhrase.has_value)
					{
						if(currentPhrase.value == 0)
							rdbtnNeutral.setSelected(true);
						else if(currentPhrase.value < 0)
							rdbtnNegative.setSelected(true);
						else rdbtnPositive.setSelected(true);
					}

					count++;
					labelTotal.setText(Integer.toString(count) + " / " + Integer.toString(training_phrases.size()));
					
				}
			}
		});
	}
}
