package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileParser {
	public void parse(File file) throws IOException {
		BufferedReader buffer = null;
		String[] tabLines = null;
		String str;
		
		try {
			buffer = new BufferedReader(new FileReader(file));
		} catch(FileNotFoundException e) {
			System.out.println("File unreacheable.");
		}
		while ((str = buffer.readLine()) != null) {
			tabLines = str.split("|");
			
		}
		buffer.close();
	}
}
