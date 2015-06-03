package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import logic.Converter;

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
		lblTheme.setBounds(70, 12, 58, 31);
		mainFrame.getContentPane().add(lblTheme);

		textTheme = new JTextField();
		textTheme.setBounds(131, 12, 108, 31);
		mainFrame.getContentPane().add(textTheme);
		textTheme.setColumns(10);

		JButton btnStartAnalysis = new JButton("Start Polarity Analysis");

		btnStartAnalysis.setBounds(244, 136, 144, 31);
		mainFrame.getContentPane().add(btnStartAnalysis);

		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Python starting...");
				try {
					Runtime r = Runtime.getRuntime();
					Process p = r.exec("python ./twitterPython/app.py " + textTheme.getText() + " 200");
					p.waitFor();
					BufferedReader b = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = "";

					while ((line = b.readLine()) != null) {
						System.out.println(line);
					}

					b.close();

				} catch (IOException | InterruptedException e1) {
					e1.printStackTrace();
				}
				System.out.println("Python ended");
				System.out.println("Generating arff...");

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				Converter c = new Converter(textTheme.getText());
				c.generateArff();
				System.out.println("Generating arff ended");
			}
		});
		btnDownload.setBounds(261, 15, 117, 25);
		mainFrame.getContentPane().add(btnDownload);

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

		btnStartAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Classifying data...");
				Converter.classify();
				System.out.println("Classifying data ended");
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
