package model;

import java.util.ArrayList;

public class Candidat {
	private ArrayList<String> nom;
	private Line ferme;
	private double support;
	private String exactRule;
	
	/**
	 * Element d'un generateur
	 * @param nom
	 * @param Line
	 * @param Support
	 */
	public Candidat(ArrayList<String> n, Line f, double s) {
		nom = n;
		ferme = f;
		support = s;
		
		// règle exact
		ArrayList<String> array = (ArrayList<String>)f.getItems().clone();
		array.removeAll(n);
		if (array.size() == 0) {
			exactRule = "-----";
		} else {
			exactRule = n + " => " + new Line(array);
		}
	}
	
	public ArrayList<String> getNom() {
		return nom;
	}
	public Line getFerme() {
		return ferme;
	}
	public double getSupport() {
		return support;
	}
	public void setFerme(Line f) {
		ferme = f;
	}
	public void setSupport(double d) {
		support = d;
	}
	/**
	 * retourne la règle exacte à partir du support
	 * @return
	 */
	public String getExactRule() {
		return exactRule;
	}
	
	public String toString() {
		if (ferme == null) { ferme = new Line(); }
		String s = "";
		if (exactRule == "-----") {
			return nom + " | " + exactRule + " | " + " " + "\n";
		}
		return nom + " | " + exactRule + " | " + support + "\n";
	}
}
