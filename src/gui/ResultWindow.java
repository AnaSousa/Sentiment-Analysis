package gui;


import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import settings.Constants;



public class ResultWindow {
	private MainPanel panel;
	public JFrame frmResult;

	
	private static final String PATH_LOADING = "resources/loader.gif";
	private static final String PATH_POSITIVE = "resources/pos.png";
	private static final String PATH_NEGATIVE = "resources/neg.png";
	private static final String PATH_NEUTRAL = "resources/neu.png";
	private JLabel resultLabel;

	class MainPanel extends JPanel {
		private static final long serialVersionUID = 1L;
		private Image loading;
		private Image positive;
		private Image neutral;
		private Image negative;
		private Image showImage;
		

		public MainPanel() {
			loading = Toolkit.getDefaultToolkit().createImage(PATH_LOADING);
			positive = Toolkit.getDefaultToolkit().createImage(PATH_POSITIVE);
			neutral = Toolkit.getDefaultToolkit().createImage(PATH_NEUTRAL);
			negative = Toolkit.getDefaultToolkit().createImage(PATH_NEGATIVE);
			showImage = loading;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(showImage, 100, 10,100,100,this);
		}
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultWindow window = new ResultWindow();
					window.frmResult.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ResultWindow() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmResult = new JFrame();
		frmResult.setResizable(false);
		//frmResult.setIconImage(Toolkit.getDefaultToolkit().getImage("resources\\truckIcon.png"));
		frmResult.setTitle("Result");
		frmResult.setBounds(100, 100, 305, 180);
		
		frmResult.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel = new MainPanel();
		frmResult.getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		frmResult.getContentPane().add(panel_1, BorderLayout.NORTH);
		
		JPanel panel_2 = new JPanel();
		frmResult.getContentPane().add(panel_2, BorderLayout.SOUTH);
		
		resultLabel = new JLabel("cenas");
		resultLabel.setVisible(false);
		panel_2.add(resultLabel);
	}
	
	public void setResult(String result, float percentage) {
		resultLabel.setVisible(true);
		String text = "";
		switch(result) {
		case Constants.NEGATIVE: 
			panel.showImage = panel.negative;
			text = "Negative";
			break;
		case Constants.POSITIVE:
			panel.showImage = panel.positive;
			text = "Positive";
			break;
		case Constants.NEUTRAL:
			panel.showImage = panel.neutral;
			text = "Neutral";
			break;
		}
		panel.repaint();
		resultLabel.setText(text + ": " + Math.round(percentage*100) + "%");
	}
}