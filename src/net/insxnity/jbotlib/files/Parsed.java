package net.insxnity.jbotlib.files;

import java.io.File;
import java.io.IOException;

import net.dean.jraw.models.Contribution;
import net.insxnity.jbotlib.util.Util;


public class Parsed {
	
	private static String parsedBlob = "";
	
	public static File parsedFile = new File("jbotlib/parsed.txt");
	
	public static boolean isParsed(Contribution c) {
		return isParsed(c.getId());
	}
	
	public static boolean isParsed(String i) {
		return parsedBlob.contains(i);
	}
	
	public static void addParsed(Contribution c) {
		addParsed(c.getId());
	}
	
	public static void addParsed(String i) {
		parsedBlob = parsedBlob + i + "\n";
	}
	
	public static void loadParsed() {
		// Creates new file IF AND ONLY IF one does not exist
		try {
			parsedFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		parsedBlob = Util.readFile(parsedFile);
	}
	
	
	public static void saveParsed() {
		// Creates new file IF AND ONLY IF one does not exist
		try {
			parsedFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Util.overwriteFile(new File("jbotlib/parsed.txt"), parsedBlob);
	}
	
	
}
