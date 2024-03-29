package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FileParser {
	/**
	 * parse le fichier texte, les elements sont separer par des '|' et les lignes finissent par un ';' ou ' '
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String[]> parse(File file) throws IOException {
		BufferedReader buffer = null;
		String[] tabLines = null;
		String str;
		
		try {
			buffer = new BufferedReader(new FileReader(file));
		} catch(FileNotFoundException e) {
			System.out.println("File unreacheable.");
		}
		
		ArrayList<String[]> array = new ArrayList<String[]>();
		while ((str = buffer.readLine()) != null) {
			tabLines = str.split("\\|",-1);
			tabLines[tabLines.length-1] = tabLines[tabLines.length-1].substring(0, tabLines[tabLines.length-1].length()-1);
			if (tabLines != null) {
				array.add(tabLines);
			}
		}
		buffer.close();
		
		return array;
	}
}
