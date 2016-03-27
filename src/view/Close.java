package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
	
	public Close() {
		createView();
		placeComponents();
	}
	
	public void createModel() {
		//model = new CloseModel("Algorithme Close");
	}
	
	public void createView() {
		final int frameWidth = 800;
		final int frameHeight = 500;
		
		frame = new JFrame();
		frame.setTitle("Algorithme Close");
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		
		openButton = new JButton("Ouvrir");
		filePath = new JLabel("/");
		results = new JTextArea();
		openButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
                fc.setFileFilter(filter);
                
                int returnVal = fc.showOpenDialog(Close.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    filePath.setText(file.getPath());
                    System.out.println("Opening: " + file.getName());
                    
                    // parsing du fichier
                    ArrayList<String[]> array = new ArrayList<String[]>();
                    try {
						array = new FileParser().parse(file);
					} catch (IOException e) {
						e.printStackTrace();
					}
                    
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
                    CloseAlgorithme closeAlgo = new CloseAlgorithme(closeModel, 0.3333, array.size());
                    results.setText(closeAlgo.toString());
                    
                } else {
                	System.out.println("Open command cancelled by user.");
                }
            }
        });
		
		startButton = new JButton("START");
		jscroll = new JScrollPane(results);
		
		supportSlider = new JSlider(0,1000,500);
	}
	
	public void placeComponents() {
		optionPanel = new JPanel(new GridLayout(4, 1));
		optionPanel.add(openButton);	
		optionPanel.add(filePath);
		optionPanel.add(supportSlider);
		optionPanel.add(startButton);
		frame.add(optionPanel, BorderLayout.WEST);
		
		resultPanel = new JPanel();
		resultPanel.add(jscroll);
		frame.add(resultPanel, BorderLayout.EAST);
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
