package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
			ArrayList<String> array = (ArrayList<String>)line.getItems().clone();
			if (array != null) {
				for(String item:array) {
					if (item != null) {
						boolean contains = false;
						for (Candidat c:generateur) {
							if (!contains && c.getNom().equals(item)) {
								contains = true;
							}
						}
						if (!contains) {
							ArrayList<String> a = new ArrayList<String>();
							a.add(item);
							Line ferme = this.getFerme(a);
							double supportCandidat = this.getSupport(a);
							
							if (supportCandidat >= support) {
								Candidat candidat = new Candidat(item, ferme, supportCandidat/nbItems);
								generateur.add(candidat);
							}
						}
					}
				}
			}
		}
		Collections.sort(generateur, new Comparator<Candidat>() {
			@Override
			public int compare(Candidat o1, Candidat o2) {
				return  o1.getNom().compareTo(o2.getNom());
			}
	    });
		
		generateurs.add(generateur);
	}
	
	public Line getFerme(ArrayList<String> candidat) {
		String ferme = null;
		Line itemsFerme = new Line();
		
		for (Line l : items) {
			 if (itemsFerme.getItems().size() == 0 && l.getItems().containsAll(candidat)) {
				 itemsFerme.setItems((ArrayList<String>)l.getItems().clone());
			 }
		}
		
		if (itemsFerme != null) {
			for (int i=0; i<items.size(); i++) {
				if (items.get(i).getItems().containsAll(candidat)) {
					if (!items.get(i).getItems().containsAll(itemsFerme.getItems())) {
						ArrayList<String> itemsFermeTmp = (ArrayList<String>)itemsFerme.getItems().clone();
						for (String str:itemsFerme.getItems()) {
							if (!items.get(i).getItems().contains(str)) {
									itemsFermeTmp.remove(str);
							}
						}
						itemsFerme.setItems(itemsFermeTmp);
					}
				}
			}
		}
		
		return itemsFerme;
	}
	
	public double getSupport(ArrayList<String> candidat) {
		double supportCandidat = 0;
		
		for(Line line:items) {
			if (line.getItems().containsAll(candidat)) {
				supportCandidat++;
			}
		}
		
		return supportCandidat;
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
