package controller;

import java.util.ArrayList;
import java.util.HashMap;

import model.Candidat;
import model.CloseModel;
import model.Line;

public class CloseAlgorithme {
	private CloseModel closeModel;
	private double support;
	private int nbItems;
	private ArrayList<ArrayList<Candidat>> generateurs;
	private ArrayList<Line> items;
	
	public CloseAlgorithme(CloseModel c, double s, int n) {
		closeModel = c;
		support = n*s;
		nbItems = n;

		generateurs = new ArrayList<ArrayList<Candidat>>();
		
		items = new ArrayList<Line>();
		for (Integer key : ((HashMap<Integer, Line>) c.getLines()).keySet())
		{
			items.add(c.getLines().get(key));
		}
	}
	
	public void generateSizeOne() {
		ArrayList<Candidat> generateur = new ArrayList<Candidat>();
		
		for(Line line:items) {
			for(String item:line.getItems()) {
				boolean contains = false;
				for (Candidat c:generateur) {
					if (!contains && c.getNom().equals(item)) {
						contains = true;
					}
				}
				if (!contains) {
					Candidat candidat = new Candidat(item, this.getFerme(item), support);
					generateur.add(candidat);
				}
			}
		}
				
		generateurs.add(generateur);
	}
	
	public Line getFerme(String candidat) {
		String ferme = null;
		Line itemsFerme = null;
		
		for (Line line : items) {
			 if (line.getItems().contains(candidat)) {
				 itemsFerme = line;
			 }
		}
		if (itemsFerme != null) {
			ArrayList<Line> itemsFermeAll = new ArrayList<Line>();  
			
			while(itemsFerme.getItems().size() > 0) {
				itemsFermeAll.add(itemsFerme);
				
				if(!itemsFerme.getItems().get(0).equals(candidat)) {
					itemsFerme.getItems().remove(0);
				} else if (itemsFerme.getItems().size() > 1) {
					itemsFerme.getItems().remove(1);
				} else {
					itemsFerme.getItems().remove(0);
				}
			}
			
			int itemCible=0;
			for (int i=0; i<items.size(); i++) {
				if (items.get(i).getItems().contains(candidat)) {
					if (!itemsFermeAll.get(itemCible).getItems().containsAll(items.get(i).getItems())) {
						itemsFerme = items.get(i);
					} else {
						i=0;
						itemCible++;
					}
				}
			}
		}
		
		return itemsFerme;
	}
	
	public String toString() {
		String s= "";
		s += "candidats | fermé | support\n";
		
		this.generateSizeOne();
		
		for(ArrayList<Candidat> c:generateurs) {
			s += c.toString() + "\n";
		}
		
		return s;
	}
}
