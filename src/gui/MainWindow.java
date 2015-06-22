package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import logic.WekaConnection;
import logic.Python;
import settings.Constants;

public class MainWindow {

	private JFrame mainFrame;
	private JTextField textTheme;
	private JTextField textModelPath;
	private JFileChooser modelFileChooser = new JFileChooser();
	private JFileChooser arffFileChooser = new JFileChooser();
	private JFileChooser arffFileSaver= new JFileChooser();
	private JTextField textNoResults;
	public static JTextField textArffPath;
	private JTextField textArffLearning;
	private JLabel resultLabel;
	private MainPanel panelResult;


	class MainPanel extends JPanel implements IMainPanel {
		private static final long serialVersionUID = 1L;

		private static final String PATH_LOADING = "resources/loader.gif";
		private static final String PATH_POSITIVE = "resources/pos.png";
		private static final String PATH_NEGATIVE = "resources/neg.png";
		private static final String PATH_NEUTRAL = "resources/neu.png";
		private static final String PATH_SUCCESS = "resources/success.png";
		private static final String PATH_INSUCCESS = "resources/insuccess.png";

		private Image loading;
		private Image positive;
		private Image neutral;
		private Image negative;
		private Image success;
		private Image insuccess;
		private Image showImage;


		public MainPanel() {
			loading = Toolkit.getDefaultToolkit().createImage(PATH_LOADING);
			positive = Toolkit.getDefaultToolkit().createImage(PATH_POSITIVE);
			neutral = Toolkit.getDefaultToolkit().createImage(PATH_NEUTRAL);
			negative = Toolkit.getDefaultToolkit().createImage(PATH_NEGATIVE);
			success = Toolkit.getDefaultToolkit().createImage(PATH_SUCCESS);
			insuccess = Toolkit.getDefaultToolkit().createImage(PATH_INSUCCESS);
			showImage = null;
			repaint();
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(showImage != null)
				//g.drawImage(showImage, 110, 10,50,50,this);
				g.drawImage(showImage, 90, 0,80,80,this);
		}
		
		public void setResult(String result, float percentage) {
			resultLabel.setVisible(true);
			String text = "";
			switch(result) {
			case Constants.NEGATIVE: 
				panelResult.showImage = panelResult.negative;
				text = "Negative";
				break;
			case Constants.POSITIVE:
				panelResult.showImage = panelResult.positive;
				text = "Positive";
				break;
			case Constants.NEUTRAL:
				panelResult.showImage = panelResult.neutral;
				text = "Neutral";
				break;
			default:
				panelResult.showImage = panelResult.positive;
				resultLabel.setText(result);
				return;
			}
			
			panelResult.repaint();
			resultLabel.setText(text + ": " + Math.round(percentage*100) + "%");
		}

		@Override
		public void setLoading() {
			showImage = loading;
			resultLabel.setVisible(false);
			repaint();
		}

		@Override
		public void clear() {
			resultLabel.setVisible(true);
			resultLabel.setText("no results");
			showImage = null;
			repaint();
		}

		@Override
		public void setMessage(String txt, int icon) {
			resultLabel.setVisible(true);
			switch (icon) {
			case Constants.SUCCESS:
				showImage = success;
				break;
			case Constants.INSUCCESS:
				showImage = insuccess;
				break;
			}
			
			resultLabel.setText(txt);
		}
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
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
		mainFrame.setIconImage(Toolkit.getDefaultToolkit().getImage("./resources/twitter.png"));
		mainFrame.setTitle("Twitter Polarity Analysis");
		mainFrame.setBounds(100, 100, 800, 350);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Supervised Learning", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(12, 167, 457, 139);
		mainFrame.getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnNewLearning = new JButton("Start or Resume");
		btnNewLearning.setBounds(91, 67, 171, 25);
		panel.add(btnNewLearning);

		JButton btnExport = new JButton("Export Model");
		btnExport.setBounds(274, 67, 171, 25);
		panel.add(btnExport);

		textArffLearning = new JTextField();
		textArffLearning.setColumns(10);
		textArffLearning.setBounds(162, 25, 283, 25);
		panel.add(textArffLearning);

		JButton button = new JButton("Load Arff");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = arffFileChooser.showOpenDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = arffFileChooser.getSelectedFile();
					textArffLearning.setText(file.getAbsolutePath());
				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		button.setBounds(22, 25, 117, 25);
		panel.add(button);

		//file chooser - set current directory and accept only model files
		modelFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MODEL FILES", "model", "text");
		modelFileChooser.setFileFilter(filter);

		arffFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		filter = new FileNameExtensionFilter("ARFF FILES", "arff", "text");
		arffFileChooser.setFileFilter(filter);

		arffFileSaver.setCurrentDirectory(new File(System.getProperty("user.dir")));
		filter = new FileNameExtensionFilter("ARFF FILES", "arff", "text");
		arffFileSaver.setFileFilter(filter);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Polarity Analysis", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_1.setBounds(12, 12, 457, 139);
		mainFrame.getContentPane().add(panel_1);

		JButton btnNewButton = new JButton("Load Model");
		btnNewButton.setBounds(22, 25, 117, 25);
		panel_1.add(btnNewButton);

		textModelPath = new JTextField();
		textModelPath.setBounds(162, 25, 283, 25);
		panel_1.add(textModelPath);
		textModelPath.setColumns(10);

		JButton btnStartAnalysis = new JButton("Start Polarity Analysis");
		btnStartAnalysis.setBounds(245, 96, 200, 25);
		panel_1.add(btnStartAnalysis);

		textArffPath = new JTextField();
		textArffPath.setColumns(10);
		textArffPath.setBounds(162, 59, 283, 25);
		panel_1.add(textArffPath);

		JButton btnLoadArff = new JButton("Load Arff");
		btnLoadArff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = arffFileChooser.showOpenDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = arffFileChooser.getSelectedFile();
					//This is where a real application would open the file.
					System.out.println("Opening: " + file.getName());
					textArffPath.setText(file.getAbsolutePath());
				} else {
					System.out.println("Open command cancelled by user.");
				}
			}
		});
		btnLoadArff.setBounds(22, 59, 117, 25);
		panel_1.add(btnLoadArff);

		btnStartAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textModelPath.getText().equals("") || textArffPath.getText().equals("")) {
					JOptionPane.showMessageDialog(mainFrame,
							"You must select a valid model and a valid arff file!",
							"Incomplete",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				File f1 = new File(textModelPath.getText());
				File f2 = new File(textArffPath.getText());
				if(!f1.exists() || !f2.exists()) {
					JOptionPane.showMessageDialog(mainFrame,
							"You must select a valid model and a valid arff file!",
							"Incomplete",
							JOptionPane.WARNING_MESSAGE);
					return;
				}

				new Thread(new Runnable() {

					@Override
					public void run() {
						System.out.println("Classifying data...");
						try {
							WekaConnection.classify(textModelPath.getText(), textArffPath.getText(), panelResult);
						} catch (Exception e) {
							JOptionPane.showMessageDialog(mainFrame,
									"There is a problem with your model!",
									"Error",
									JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
						System.out.println("Classifying data ended");
					}
				}).start();

			}
		});

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(new BorderLayout());
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Result", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(478, 167, 296, 139);
		mainFrame.getContentPane().add(panel_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setOpaque(false);
		panel_3.add(panel_4, BorderLayout.SOUTH);
		
		panelResult = new MainPanel();
		panelResult.setBounds(478, 167, 296, 139);
		panel_3.add(panelResult, BorderLayout.CENTER);
		
		resultLabel = new JLabel("no results");
		panel_4.add(resultLabel, BorderLayout.SOUTH);
		


		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Download", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(478, 12, 296, 139);
		mainFrame.getContentPane().add(panel_2);

		textTheme = new JTextField();
		textTheme.setBounds(166, 26, 108, 25);
		panel_2.add(textTheme);
		textTheme.setColumns(10);

		JLabel lblTheme = new JLabel("Hashtag:");
		lblTheme.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTheme.setBounds(12, 26, 142, 31);
		panel_2.add(lblTheme);

		JButton btnDownload = new JButton("Download");
		btnDownload.setBounds(101, 101, 117, 25);
		panel_2.add(btnDownload);

		JLabel lblNumberOfResults = new JLabel("Number of results:");
		lblNumberOfResults.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNumberOfResults.setBounds(12, 58, 142, 31);
		panel_2.add(lblNumberOfResults);

		textNoResults = new JTextField();
		textNoResults.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		textNoResults.setColumns(10);
		textNoResults.setBounds(166, 58, 108, 25);
		panel_2.add(textNoResults);
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(textTheme.getText().equals("") || textNoResults.getText().equals("") || textTheme.getText() == null || textNoResults.getText() == null){
					JOptionPane.showMessageDialog(mainFrame,
							"You must select a theme and the number of results.",
							"Incomplete",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				panelResult.setLoading();
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						arffFileSaver.setSelectedFile(new File(textTheme.getText() + "_" + textNoResults.getText() + ".arff"));

						int returnVal = arffFileSaver.showSaveDialog(mainFrame);

						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = arffFileSaver.getSelectedFile();
							System.out.println("Opening: " + file.getName());
							textArffPath.setText(file.getAbsolutePath());
							try {
								if(!Python.getData(textTheme.getText(), textNoResults.getText(), file.getAbsolutePath())) {
									panelResult.setMessage("Something went wrong!", Constants.INSUCCESS);
									return;
								}
							} catch (IOException | InterruptedException e1) {
								e1.printStackTrace();
								panelResult.setMessage("Something went wrong!", Constants.INSUCCESS);
								return;
							}
						} else {
							System.out.println("Open command cancelled by user.");
							panelResult.clear();
							return;
						}
						panelResult.setMessage("Download successfull", Constants.SUCCESS);
					}
				}).start();
				
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int returnVal = modelFileChooser.showOpenDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = modelFileChooser.getSelectedFile();
					//This is where a real application would open the file.
					System.out.println("Opening: " + file.getName());
					textModelPath.setText(file.getAbsolutePath());
				} else {
					System.out.println("Open command cancelled by user.");
				}


			}
		});

		btnNewLearning.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				startSupervisedWindow();
			}
		});

		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if(textArffLearning.getText().equals("")) {
					JOptionPane.showMessageDialog(mainFrame,
							"You must select a valid arff file!",
							"Incomplete",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				
				String modelPath = null;
				int returnVal = modelFileChooser.showSaveDialog(mainFrame);

				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = modelFileChooser.getSelectedFile();
					System.out.println("Opening: " + file.getName());
					modelPath = file.getAbsolutePath();
				} else {
					System.out.println("Open command cancelled by user.");
					return;
				}	
				
				try {
					WekaConnection.generateModel(modelPath, textArffLearning.getText(), panelResult);
				} catch (Exception e) {
					panelResult.setMessage("Something went wrong!", Constants.INSUCCESS);
					JOptionPane.showMessageDialog(mainFrame,
							"Something went wrong, maybe there are problems in the configuration file!",
							"Error",
							JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
				
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
