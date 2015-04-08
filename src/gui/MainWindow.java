package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSplitPane;

public class MainWindow {

	private JFrame frmXdffd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frmXdffd.setVisible(true);
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
		frmXdffd = new JFrame();
		frmXdffd.setTitle("Supervised learning");
		frmXdffd.setBounds(100, 100, 474, 290);
		frmXdffd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmXdffd.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("New Supervised Learning");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(0, 183, 212, 39);
		frmXdffd.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Resume Supervised Learning");
		btnNewButton_1.setBounds(0, 206, 212, 45);
		frmXdffd.getContentPane().add(btnNewButton_1);
	}
}
