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
}
