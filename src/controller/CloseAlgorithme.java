package controller;

import java.util.ArrayList;
import java.util.HashMap;

import model.CloseModel;
import model.Line;

public class CloseAlgorithme {
	private CloseModel closeModel;
	private double support;
	private int nbItems;
	private ArrayList<ArrayList<String>> generateur1;
	
	public CloseAlgorithme(CloseModel c, double s, int n) {
		closeModel = c;
		support = n*s;
		nbItems = n;
		System.out.println(support);
	}
	
	public void generateSizeOne() {
		generateur1 = new ArrayList<ArrayList<String>>();
		
		for (int i=0; i<nbItems; i++) {
			for(String s:closeModel.getLine(i).getItems()) {
				ArrayList<String> array = new ArrayList<String>();
				String candidat = s;
				
				array.add(candidat);
				String ferme = this.getFerme(candidat, closeModel.getLines());
				
				generateur1.add(array);
			}
				
		}
	}
	
	public String getFerme(String candidat, HashMap<Integer,Line> lines) {
		for (Integer key : lines.keySet()) {
			 
		}
	
		return candidat;
	}
	
	public String toString() {
		String s= "";
		s += "candidats | fermé | support\n";
		
		this.generateSizeOne();
		
		for(ArrayList<String> a:generateur1) {
			for (String str:a) {
				s += str+"|";
			}
			s += "\n";
		}
		
		return s;
	}
}
