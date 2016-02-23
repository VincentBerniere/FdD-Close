package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;

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
		frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		
		openButton = new JButton("Ouvrir");
		
		startButton = new JButton("START");
		
		results = new JTextArea();
		jscroll = new JScrollPane(results);
		filePath = new JLabel("");
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
		frame.add(resultPanel, BorderLayout.EAST);
	}
	
	public static void main(String[] args) {
		Close close = new Close();
		close.setVisible(true);
	}

}