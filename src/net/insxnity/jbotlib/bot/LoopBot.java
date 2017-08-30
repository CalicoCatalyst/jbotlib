package net.insxnity.jbotlib.bot;

import java.net.URL;
import java.util.Timer;

import net.dean.jraw.http.NetworkException;
import net.dean.jraw.models.Contribution;
import net.insxnity.jbotlib.util.LoopTask;
import net.insxnity.jbotlib.util.TimeUtils;
import net.insxnity.jbotlib.util.Util;

/**
 * This object represents a bot that operates cyclicly. It repeats 
 * the mainLoop method every 30 seconds. 
 * 
 * @author Insxnity
 */
public class LoopBot extends RedditBot {
	
	/**
	 * Timer Object that we use to schedule the cycles
	 */
	protected static Timer timer = null;
	
	protected static long cycleTime = TimeUtils.SECOND * 30;
	
	/**
	 * This is the text that is run by the Loop class. 
	 * 
	 * The LoopTask class extends a TimerTask. 
	 * 
	 * The Loop run method calls this method.
	 */
	public void ranByLoop() {
		try {
			mainLoop();
		} catch (Exception e) {
			Util.log("reauthenticating");
			setupBot();
			mainLoop();
		}
	}
	
	/** 
	 * This is the code that is run when the bot loops
	 */
	public void mainLoop() {
		Util.log("Error 4: You have not configured any code to run! Exiting...");
		System.exit(4);
	}
	
	/**
	 * Create a Timer to run the code in Loop
	 */
	public void startLoop() {
		timer = new Timer();
		timer.schedule(new LoopTask(), 0, cycleTime);
	}
	
	/**
	 * End the timer and complete all tasks
	 */
	public void stopLoop() {
		timer.cancel();
	}
	
	/**
	 * Cancel's Timer Tasks, says "Clean Exit", and terminates the task
	 */
	public void endAndClose() {
		stopLoop();
		Util.log("Clean Exit");
		System.exit(0);
	}
	

	
	@Override
	public void reply(Contribution c, String text) {
		try {
			super.reply(c, text);
		} catch (NetworkException e) {
			Util.log("Attempting to Reauthenticate...");
			setupBot();
		}
		
	}
	/**
	 * Performs the variety of tasks required to post a URL. Also attempts to reauthenticate
	 * if there are any errors that indicate the OAUTH cert has expired.
	 * 
	 * @param subreddit
	 * @param title
	 * @param url
	 */
	public void postUrl(String subreddit, String title, String url) throws NetworkException {
		try {
			super.postUrl(subreddit, title, url);
		} catch (NetworkException e) {
			Util.log("Attempting to Reauthenticate...");
			setupBot();
		}
	}
	
	/**
	 * Performs the variety of tasks required to post a URL. Also attempts to reauthenticate
	 * if there are any errors that indicate the OAUTH cert has expired.
	 * 
	 * @param subreddit
	 * @param title
	 * @param url
	 */
	public void postUrl(String subreddit, String title, URL url) throws NetworkException {
		try {
			super.postUrl(subreddit, title, url);
		} catch (NetworkException e) {
			Util.log("Attempting to Reauthenticate...");
			setupBot();
		}
	}

	/**
	 * Performs the variety of tasks required to post a Text post. Also attempts to reauthenticate
	 * if there are any errors that indicate the OAUTH cert has expired.
	 * 
	 * @param subreddit
	 * @param title
	 * @param url
	 */
	public void postText(String subreddit, String title, String text) throws NetworkException {
		try {
			super.postText(subreddit, title, text);
		} catch (NetworkException e) {
			Util.log("Attempting to Reauthenticate...");
			setupBot();
		}
	}
}
