package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.deploy.uitoolkit.impl.fx.Utils;

import controller.CloseAlgorithme;
import controller.FileParser;
import model.CloseModel;
import model.Line;
import model.StdCloseModel;

public class Close extends JFrame {
	
	private JFrame frame;
	private JPanel optionPanel;
	private JPanel resultPanel;
	private JButton openButton;
	private JButton startButton;
	private JButton saveButton;
	private JTextArea results;
	private JScrollPane jscroll;
	private JLabel filePath;
	private JSlider supportSlider;
	private JTextField supportText;
	private ArrayList<String[]> array;
	
	public Close() {
		createView();
		placeComponents();
		createModel();
		createController();
	}
	
	/**
	 * ajoutes toutes les actions (listener) aux composants de l'ihm
	 */
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
                    filePath.setText(file.getPath());
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
		
		supportSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                supportText.setText(String.valueOf((double)supportSlider.getValue()/1000));
            }
        });
		
		supportText.addActionListener(new ActionListener() {
			 
            @Override
            public void actionPerformed(ActionEvent e) {
                try
                {
                	double value = Double.parseDouble(supportText.getText()) * 1000;
                    supportSlider.setValue((int)value);
                }
                catch(Exception ex)
                {
                	supportText.setText("ERROR");
                	supportText.setToolTipText("Set Value in Range between 0 - 1 ") ;
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
                CloseAlgorithme closeAlgo = new CloseAlgorithme(closeModel, (double)supportSlider.getValue()/1000, array.size());
                results.setText(closeAlgo.toString());
                
                saveButton.setEnabled(true);
			}
		});
		
		saveButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fc = new JFileChooser();
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                
                int returnVal = fc.showOpenDialog(Close.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
	                File file = fc.getSelectedFile();
	                
					try{
						File ff=new File(file.getPath()+"\\sortie.txt"); 
						ff.createNewFile();
						FileWriter ffw=new FileWriter(ff);
						ffw.write(results.getText());
						ffw.close(); 
						System.out.println("Save in : "+ff.getPath());
					} catch (Exception io) { System.out.println("Erreur d'écriture du fichier");}
                }
			}
		});
	}
	
	/**
	 * Creer l'ihm avec tous ses composants, seulement le visuel
	 */
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
		
		startButton = new JButton("Exécuter");
		saveButton = new JButton("Sauvegarder");
		startButton.setEnabled(false);
		saveButton.setEnabled(false);
		
		supportSlider = new JSlider(JSlider.HORIZONTAL,0, 1000, 500);
		supportText = new JTextField("0.5");
		supportText.setPreferredSize(new Dimension(50, 25));
		supportSlider.setFont(new Font("Tahoma",Font.BOLD,12));
		supportSlider.setMajorTickSpacing(100);
		supportSlider.setMinorTickSpacing(25);
		supportSlider.setPaintLabels(true);
		supportSlider.setPaintTicks(true);
		supportSlider.setPaintTrack(true);
		supportSlider.setAutoscrolls(true);
		
		Hashtable labelTable = new Hashtable();
		for (int i=0; i<=1000; i++) {
			if (i%200 == 0) {
				labelTable.put( new Integer( i ), new JLabel((double)i/1000+"") );
			}
		}
		supportSlider.setLabelTable( labelTable );
		supportSlider.setPaintLabels(true);
	}
	/**
	 * place les composants de l'ihm de manière structurer
	 */
	public void placeComponents() {
		optionPanel = new JPanel(new GridLayout(5, 1));
		
		JPanel p1 = new JPanel(new GridLayout(2, 1));
		optionPanel.add(filePath);
		optionPanel.add(openButton);	
		
		JPanel p2 = new JPanel();
		p2.add(supportSlider);
		p2.add(supportText);
		optionPanel.add(p2);
		
		optionPanel.add(startButton);
		optionPanel.add(saveButton);
		
		frame.add(optionPanel, BorderLayout.WEST);
		frame.add(jscroll, BorderLayout.CENTER);
	}
	
	/**
	 * gère l'affichage de l'ihm
	 */
	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public void createController() {
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	/**
	 * Client du programme
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Close().display();
			}
		});
	}

}
