package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.deploy.uitoolkit.impl.fx.Utils;

import controller.CloseAlgorithme;
import controller.FileParser;
import model.CloseModel;
import model.Line;
import model.StdCloseModel;

public class Close extends JFrame {
	
	//private CloseModel model;
	private JFrame frame;
	private JPanel optionPanel;
	private JPanel resultPanel;
	private JButton openButton;
	private JButton startButton;
	private JTextArea results;
	private JScrollPane jscroll;
	private JLabel filePath;
	private JSlider supportSlider;
	private ArrayList<String[]> array;
	
	public Close() {
		createView();
		placeComponents();
		createModel();
	}
	
	public void createModel() {
		openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                fc.setFileFilter(filter);
                
                int returnVal = fc.showOpenDialog(Close.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    filePath.setText("Path: "+file.getPath());
                    System.out.println("Opening: " + file.getName());
                    
                    // parsing du fichier
                    array = new ArrayList<String[]>();
                    try {
    					array = new FileParser().parse(file);
    				} catch (IOException io) {
    					io.printStackTrace();
    				}
                    
                    startButton.setEnabled(true);
                } else {
                	System.out.println("Open command cancelled by user.");
                	 startButton.setEnabled(false);
                }
            }
        });
		
		startButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                // creation du model utilisant une hashMap
                CloseModel closeModel = new StdCloseModel();
                for(int i=0; i<array.size(); i++) {
                	Line line = new Line();
                	for(int j=1; j<array.get(i).length; j++) {
                		line.addItem(array.get(i)[j]);
                	}
                	closeModel.addLine(Integer.parseInt(array.get(i)[0].replaceAll(" +", "")), line);
                }
                
                // lancement de l'algorithme Close
                CloseAlgorithme closeAlgo = new CloseAlgorithme(closeModel, 0.02, array.size());
                results.setText(closeAlgo.toString());
			}
		});
		
	}
	
	public void createView() {
		final int frameWidth = 800;
		final int frameHeight = 500;
		
		frame = new JFrame();
		frame.setLayout(new BorderLayout());
		frame.setTitle("Algorithme Close");
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		
		openButton = new JButton("Ouvrir");
		filePath = new JLabel("Path :");
		
		results = new JTextArea();
		results.setText("Résultat");
		results.setLineWrap(true);
		results.setWrapStyleWord(true);
		results.setEditable(false);
		
		jscroll = new JScrollPane(results);
		jscroll.setPreferredSize(new Dimension(400, frameHeight));
		
		startButton = new JButton("START");
		startButton.setEnabled(false);
		
		supportSlider = new JSlider(0,1000,500);
	}
	
	public void placeComponents() {
		optionPanel = new JPanel(new GridLayout(4, 1));
		optionPanel.add(openButton);	
		optionPanel.add(filePath);
		optionPanel.add(supportSlider);
		optionPanel.add(startButton);
		
		frame.add(optionPanel, BorderLayout.WEST);
		frame.add(jscroll, BorderLayout.CENTER);
	}
	
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Close().display();
			}
		});
	}

}
