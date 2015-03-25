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

	private JFrame frmSupervisionLearning;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SupervisedLearningWindow window = new SupervisedLearningWindow();
					window.frmSupervisionLearning.setVisible(true);
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
		frmSupervisionLearning = new JFrame();
		frmSupervisionLearning.setResizable(false);
		frmSupervisionLearning.setTitle("Supervised learning");
		frmSupervisionLearning.setBounds(100, 100, 442, 220);
		frmSupervisionLearning.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSupervisionLearning.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(12, 80, 412, 60);
		textArea.setBorder(BorderFactory.createEtchedBorder());
		frmSupervisionLearning.getContentPane().add(textArea);
		
		JButton btnPrevi = new JButton("Previous");
		btnPrevi.setBounds(12, 150, 117, 25);
		frmSupervisionLearning.getContentPane().add(btnPrevi);
		
		JButton btnNext = new JButton("Next");
		btnNext.setBounds(307, 150, 117, 25);
		frmSupervisionLearning.getContentPane().add(btnNext);
		
		ButtonGroup rdbtnsPolarity = new ButtonGroup();
		
		JRadioButton rdbtnPositive = new JRadioButton("Positive");
		rdbtnPositive.setForeground(Color.GREEN);
		rdbtnPositive.setBounds(311, 49, 149, 23);
		frmSupervisionLearning.getContentPane().add(rdbtnPositive);
		rdbtnsPolarity.add(rdbtnPositive);
		
		JRadioButton rdbtnNeutral = new JRadioButton("Neutral");
		rdbtnNeutral.setBounds(169, 49, 149, 23);
		frmSupervisionLearning.getContentPane().add(rdbtnNeutral);
		rdbtnsPolarity.add(rdbtnNeutral);
		
		JRadioButton rdbtnNegative = new JRadioButton("Negative");
		rdbtnNegative.setForeground(Color.RED);
		rdbtnNegative.setBounds(30, 49, 149, 23);
		frmSupervisionLearning.getContentPane().add(rdbtnNegative);
		rdbtnsPolarity.add(rdbtnNegative);
		
		JLabel label = new JLabel("15/30");
		label.setFont(new Font("Dialog", Font.BOLD, 16));
		label.setBounds(195, 12, 64, 25);
		frmSupervisionLearning.getContentPane().add(label);
	}
}
