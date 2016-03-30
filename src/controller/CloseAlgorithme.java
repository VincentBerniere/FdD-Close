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
	
	/**
	 * Model de base de l'algorithme
	 * @param fichier parser CloseModel
	 * @param support double
	 * @param nombres de lignes int
	 */
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
	/**
	 * genere le generateur de taille 1
	 */
	public void generatorOneSize() {
		ArrayList<Candidat> generateur = new ArrayList<Candidat>();
		
		for(Line line:items) {
			ArrayList<String> array = (ArrayList<String>)line.getItems().clone();
			if (array != null) {
				for(String item:array) {
					if (item != null) {
						boolean contains = false;
						for (Candidat c:generateur) {
							if (!contains && c.getNom().contains(item)) {
								contains = true;
							}
						}
						if (!contains) {
							ArrayList<String> a = new ArrayList<String>();
							a.add(item);
							Line ferme = this.getFerme(a);
							double supportCandidat = this.getSupport(a);
							
							if (supportCandidat >= support) {
								ArrayList<String> candidatName = new ArrayList<String>();
								candidatName.add(item);
								Candidat candidat = new Candidat(candidatName, ferme, supportCandidat/nbItems);
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
				Collections.sort(o1.getNom());
				Collections.sort(o2.getNom());
				
				for(String s1:o1.getNom()) {
					for(String s2:o2.getNom()) {
						if (s1.compareTo(s2) != 0) {
							return  s1.compareTo(s2);
						}
					}
				}
				return 0;
			}
	    });
		
		generateurs.add(generateur);
	}
	/**
	 * genere le generateur à partir de la taille 2
	 * @param size
	 * @return
	 */
	public boolean generatorSize(int size) {
		ArrayList<Candidat> generateur = new ArrayList<Candidat>();
		ArrayList<ArrayList<String>> candidatsName = new ArrayList<ArrayList<String>>();
		
		for(int i=0; i<generateurs.get(size-2).size()-1; i++) {
			ArrayList<String> candidatName = (ArrayList<String>)generateurs.get(size-2).get(i).getNom().clone();
			ArrayList<String> candidatNameTmp = (ArrayList<String>) candidatName.clone();
			
			for(int j=1; j<generateurs.get(size-2).size(); j++) {
				ArrayList<String> candidatNameNext = (ArrayList<String>)generateurs.get(size-2).get(j).getNom().clone();
				
				for (String item:candidatNameNext) {
					if (!candidatName.contains(item)) {
						candidatName.add(item);
						Collections.sort(candidatName);
						
						if (!candidatsName.contains(candidatName)) {
							boolean isFerme = false;
							for(Candidat candidat:generateurs.get(size-2)) {
								if(candidat.getFerme().getItems().containsAll(candidatName)) {
									isFerme = true;
								}
							}
							if(!isFerme) {
								double supportCandidat = this.getSupport(candidatName);
								if (supportCandidat >= support) {
									Candidat candidat = new Candidat(candidatName,this.getFerme(candidatName), supportCandidat/nbItems);
									generateur.add(candidat);
									candidatsName.add(candidatName);
								}
							}
						}
						candidatName = (ArrayList<String>) candidatNameTmp.clone();
					}
				}
			}
		}
		
		if (generateur.size() == 0) {
			return false;
		} else {
			Collections.sort(generateur, new Comparator<Candidat>() {
				@Override
				public int compare(Candidat o1, Candidat o2) {
					
					for(int i=0; i<o1.getNom().size(); i++) {
						if (o1.getNom().size() != o2.getNom().size()) {
							if (i >= o2.getNom().size()) {
								return  -1;
							}
						}
						if (o1.getNom().get(i).compareTo(o2.getNom().get(i)) != 0) {
							return  o1.getNom().get(i).compareTo(o2.getNom().get(i));
						}
					}
					return 0;
				}
		    });
			
			generateurs.add(generateur);
			this.generatorSize(generateurs.size()+1);
			return true;
		}
	}
	/**
	 * retourne le ferme du candidat donné en paramètre
	 * @param candidat
	 * @return
	 */
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
	/**
	 * retourne le support du candidat
	 * @param candidat
	 * @return
	 */
	public double getSupport(ArrayList<String> candidat) {
		double supportCandidat = 0;
		
		for(Line line:items) {
			if (line.getItems().containsAll(candidat)) {
				supportCandidat++;
			}
		}
		
		return supportCandidat;
	}
	/**
	 * retourne les sur ensembles fermes
	 * @param candidat
	 * @return
	 */
	public ArrayList<ArrayList<String>> getApproxRules(Candidat candidat) {
		ArrayList<ArrayList<String>> rules = new ArrayList<ArrayList<String>>();
		
		for (ArrayList<Candidat> candidats:generateurs) {
			for (Candidat c:candidats) {
				if (!candidat.getNom().containsAll(c.getNom()) && 
						c.getFerme().getItems().containsAll(candidat.getFerme().getItems())) {
					if (!rules.contains(c.getFerme().getItems()) && 
							!candidat.getFerme().getItems().containsAll(c.getFerme().getItems())) {
						rules.add(c.getFerme().getItems());
					}
				}
			}
		}
		
		return rules;
	}
	/**
	 * retourne la confiance avec 2 supports
	 * @param supp1
	 * @param supp2
	 * @return
	 */
	public double getConfiance(double supp1, double supp2) {
		return supp2/supp1;
	}
	/**
	 * Affichage console
	 */
	public String toString() {
		String s= "";
		
		this.generatorOneSize();
		this.generatorSize(generateurs.size()+1);
		
		s += "Générateurs | Règles exactes | Supports\n";
		for(ArrayList<Candidat> c:generateurs) {
			for(Candidat cand:c) {
				s += cand.toString();
			}
		}
		
		s += "\nGénérateurs | Règles App. | Supports | Confiances | Lift\n";
		for(ArrayList<Candidat> c:generateurs) {
			for(Candidat cand:c) {
				ArrayList<ArrayList<String>> rules = this.getApproxRules(cand);
				for (ArrayList<String> ensFerm:rules) {
					ArrayList<String> ruleApp = (ArrayList<String>)ensFerm.clone();
					ruleApp.removeAll(cand.getNom());
					double supp = this.getSupport(ensFerm)/nbItems;
					
					s 	+= cand.getNom().toString() + " | ";
					if(cand.getFerme().getItems().containsAll(ensFerm)) {
						s += "-----" + " | "
						+ "-----" + " | " 
						+ "-----" 
						+ "-----" + "\n";
					} else {
						double lift = this.getSupport(ensFerm)/(this.getSupport(cand.getNom()) * this.getSupport(ruleApp));
						s+= cand.getNom().toString() + " => " + ruleApp.toString() + " | "
							+ supp + " | " 
							+ this.getConfiance(cand.getSupport(), supp) + " | "
							+ lift
							+ "\n";
					
					}
				}
			}
		}
		
		//System.out.println(s);
		
		return s;
	}
}
