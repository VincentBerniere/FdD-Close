package model;

import java.util.ArrayList;

public class Line {
	private ArrayList<String> listItems;
	
	public Line() {
		listItems = new ArrayList<String>();
	}
	
	public void addItem(String str) {
		listItems.add(str);
	}
	
	public ArrayList<String> getItems() { return listItems; }
	public String getItem(int i) { return listItems.get(i); }
	public void setItems(ArrayList<String> l) { listItems = l; }
	public String toString() {
		String s = "";
		
		if (listItems.size() == 1) {
			s = listItems.get(0);
		} else if(listItems.size() > 1) {
			for (int i=0; i<listItems.size()-1; i++) {
				s += listItems.get(i) + ", ";
			}
			s += listItems.get(listItems.size()-1);
		}
		
		return s;
	}
}
