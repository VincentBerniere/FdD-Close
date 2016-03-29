package model;

import java.util.ArrayList;

public class Candidat {
	private String nom;
	private Line ferme;
	private double support;
	
	public Candidat(String n, Line f, double s) {
		nom = n;
		ferme = f;
		support = s;
	}
	
	public String getNom() {
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
	
	public String toString() {
		return nom + " | " + ferme.toString() + " | " + support + "\n";
	}
}
