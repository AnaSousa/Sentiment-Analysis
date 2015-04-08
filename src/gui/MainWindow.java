package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.JLabel;

public class MainWindow {

	private JFrame mainFrame;
	private JTextField textTheme;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.mainFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		mainFrame = new JFrame();
		mainFrame.setTitle("Supervised learning");
		mainFrame.setBounds(100, 100, 422, 290);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Supervised Learning", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(10, 74, 212, 150);
		mainFrame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JButton btnNewLearning = new JButton("New Supervised Learning");
		btnNewLearning.setBounds(20, 44, 171, 33);
		panel.add(btnNewLearning);
		
		JButton btnResumeLearning = new JButton("Resume Supervised Learning");
		btnResumeLearning.setBounds(20, 88, 171, 33);
		panel.add(btnResumeLearning);
		
		JLabel lblTheme = new JLabel("Theme:");
		lblTheme.setBounds(144, 21, 58, 31);
		mainFrame.getContentPane().add(lblTheme);
		
		textTheme = new JTextField();
		textTheme.setBounds(191, 21, 108, 31);
		mainFrame.getContentPane().add(textTheme);
		textTheme.setColumns(10);
		
		JButton btnStartAnalysis = new JButton("Start Polarity Analysis");
		btnStartAnalysis.setBounds(244, 136, 144, 31);
		mainFrame.getContentPane().add(btnStartAnalysis);

		btnNewLearning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				startSupervisedWindow();
			}
		});
		
		btnResumeLearning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				startSupervisedWindow();
			}
		});
	}
	
	private void startSupervisedWindow() {
		if(!textTheme.getText().equals("")) {
			SupervisedLearningWindow learningWindow = new SupervisedLearningWindow();
			learningWindow.windowLearning.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(mainFrame,
					"You must specify a theme.",
				    "Empty theme",
				    JOptionPane.ERROR_MESSAGE);
		}
	}
}
