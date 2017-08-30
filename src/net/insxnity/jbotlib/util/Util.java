/**
 * (#)Util.java 7/17/17 Version jbl1.0 
 * 
 * This class is designed to compliment Insxnity's Bot Template. For most
 * functions to work, it requires the Bot class and the Main class. I would
 * not recommend using it outside of the template, as it'd be kinda useless. 
 * 
 * The code below is commented in a style that makes viewing comments from
 * the outside of the class much easier. I've also added '//'-style comments, 
 * just in case somebody needs to go through my source code
 * 
 * @author Insxnity
 */
package net.insxnity.jbotlib.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.managers.ModerationManager;
import net.dean.jraw.models.Contribution;
import net.insxnity.jbotlib.bot.RedditBot;

/**
 * The Utility Class; Contains many functions that have uses across different
 * bots. Requires a {@code bot} variable in the {@code Main} class for some
 * methods.
 * 
 * @author Insxnity
 * @see RedditBot
 *
 */
public class Util {
	public static Boolean debug = false;

	static final long DAY = 24 * 60 * 60 * 1000;
	
	public static boolean inLastDay(Date aDate) {
	    return aDate.getTime() > System.currentTimeMillis() - DAY;
	}
	/**
	 * Translates the contents of a text file into a string
	 * 
	 * @param file - The file that will be translated into String
	 * @return {@code String} containing file contents
	 */
	public static String readFile(File file) {
			  byte[] encoded = null;
			try {
				// Gets the raw bytes
				encoded = Files.readAllBytes(Paths.get(file.toURI()));
			} catch (IOException e) {
				// If this goes fucky, it's because it's using a file not in BotFiles.java, as
				// that class is designed to create files if they dont exist. So it'd probably be
				// some sort of critical error, in which case, the bot shouldn't be running
				log("Critical Error, file could not be read");
				log("Check if " + file.getName() + " exists");

				System.exit(1);
			}
			// Take those raw bytes and turn them into some shit I can read  
			return new String(encoded, Charset.defaultCharset());
	}
	/**
	 * Append a String onto the contents of a text file
	 * 
	 * @param file - The file to be modified
	 * @param txt - The text to be appended to the file
	 * @return {@code true}, if operation completes successfully 
	 */
	public static boolean appendToFile(File file, String txt) {
		// This allows us to append easily
		FileWriter fw = null;
		try {
			// the 'true' puts the FileWriter in "append" mode. 
			fw = new FileWriter(file, true);
			fw.append(txt);
			fw.close();
		} catch (IOException e) {
			// shoot
			return false;
		}
		return true;
	}
	
	public static boolean overwriteFile(File file, String txt) {
		// This allows us to append easily
		FileWriter fw = null;
		try {
			// the 'false' sets it to overwrite mode
			fw = new FileWriter(file, false);
			fw.write(txt);
			fw.close();
		} catch (IOException e) {
			// shoot
			return false;
		}
		return true;
	}
	
	/**
	 * One-Liner for logging/printing out system information
	 * 
	 * @param s - Text to log
	 */
	public static void log(String s) {
		System.out.println(s);
	}

	public static Contribution getContributionByID(RedditClient r, String id) {
		return (Contribution) (r.get(id)).get(0);
	}
	
	public static void deleteContribution(RedditBot r, Contribution c) {
		ModerationManager mm = new ModerationManager(r.getRedditClient());
		try {
			mm.delete(c.getFullName());
		} catch (NetworkException | ApiException e) {
			Util.log("Attempting to Reauthenticate...");
			r.setupBot();
		}
	}

}
