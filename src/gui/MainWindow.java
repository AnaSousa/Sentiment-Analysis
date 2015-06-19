package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import logic.Converter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainWindow {

	private JFrame mainFrame;
	private JTextField textTheme;
	private JTextField textModelPath;
	private JFileChooser modelFileChooser = new JFileChooser();
	private JFileChooser arffFileChooser = new JFileChooser();
	private JTextField textNoResults;
	private JTextField textArffPath;

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
		mainFrame.setTitle("Supervised learning");
		mainFrame.setBounds(100, 100, 483, 400);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Supervised Learning", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel.setBounds(12, 182, 212, 150);
		mainFrame.getContentPane().add(panel);
		panel.setLayout(null);

		JButton btnNewLearning = new JButton("New");
		btnNewLearning.setBounds(20, 44, 171, 33);
		panel.add(btnNewLearning);

		JButton btnResumeLearning = new JButton("Resume");
		btnResumeLearning.setBounds(20, 88, 171, 33);
		panel.add(btnResumeLearning);
		
		//file chooser - set current directory and accept only model files
		modelFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		FileNameExtensionFilter filter = new FileNameExtensionFilter("MODEL FILES", "model", "text");
		modelFileChooser.setFileFilter(filter);
		
		arffFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
		filter = new FileNameExtensionFilter("MODEL FILES", "arff", "text");
		arffFileChooser.setFileFilter(filter);
		
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
				btnStartAnalysis.setBounds(245, 96, 200, 31);
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
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										System.out.println("Classifying data...");
										Converter.classify();
										System.out.println("Classifying data ended");
									}
								}).start();
								
							}
						});
		
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Download", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(257, 182, 212, 150);
		mainFrame.getContentPane().add(panel_2);
		
				textTheme = new JTextField();
				textTheme.setBounds(79, 27, 108, 31);
				panel_2.add(textTheme);
				textTheme.setColumns(10);
				
						JLabel lblTheme = new JLabel("Theme:");
						lblTheme.setBounds(16, 27, 58, 31);
						panel_2.add(lblTheme);
						
								JButton btnDownload = new JButton("Download");
								btnDownload.setBounds(57, 102, 117, 25);
								panel_2.add(btnDownload);
								
								JLabel lblNumberOfResults = new JLabel("No/Nº:");
								lblNumberOfResults.setBounds(16, 59, 58, 31);
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
								textNoResults.setBounds(79, 59, 108, 31);
								panel_2.add(textNoResults);
								btnDownload.addActionListener(new ActionListener() {
									public void actionPerformed(ActionEvent e) {
										
										if(textTheme.getText().equals("") || textNoResults.getText().equals("") || textTheme.getText() == null || textNoResults.getText() == null)
											JOptionPane.showMessageDialog(mainFrame,
												    "You must select a theme and the number of results.",
												    "Incomplete",
												    JOptionPane.WARNING_MESSAGE);
										System.out.println("Python starting...");
										try {
											Runtime r = Runtime.getRuntime();
											Process p = r.exec("python ./twitterPython/app.py " + textTheme.getText() + " " + textNoResults.getText());
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
