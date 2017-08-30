/**
 * (#)Bot.java 7/17/17 v1
 * 
 * This class is designed to streamline and concentrate many core functions of
 * the bot into one Object, which also eases the use of multiple accounts
 * for a single script. 
 * 
 * The code below is commented in a style that makes viewing comments from
 * the outside of the class much easier. I've also added '//'-style comments, 
 * just in case somebody needs to go through my source code
 * 
 * @author Insxnity
 * 
 */
package net.insxnity.jbotlib.bot;

import java.net.MalformedURLException;
import java.net.URL;

import net.dean.jraw.ApiException;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkException;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.managers.AccountManager;
import net.dean.jraw.managers.AccountManager.SubmissionBuilder;
import net.dean.jraw.managers.CaptchaHelper;
import net.dean.jraw.managers.InboxManager;
import net.dean.jraw.managers.LiveThreadManager;
import net.dean.jraw.managers.ModerationManager;
import net.dean.jraw.managers.MultiRedditManager;
import net.dean.jraw.models.Contribution;
import net.insxnity.jbotlib.exceptions.CredentialException;
import net.insxnity.jbotlib.util.Util;

/**
 * Represents and performs most of the core logic functions
 * of the bot. Handles main, redundant interactions with JRAW
 * 
 * @author Insxnity
 */
public class RedditBot {
	
	private UserAgent botUserAgent;
	
	protected Credentials botCredentials;

	private AccountManager accountManager;
	
	private InboxManager inboxManager;
	
	private LiveThreadManager liveThreadManager;
	
	private CaptchaHelper captchaHelper;
	
	private ModerationManager moderationManager;
	
	protected RedditClient redditClient;
	
	private MultiRedditManager multiRedditManager;
	
	// User Agent Information
	
	private String botPlatform = 
			// You almost always want to use 'desktop' here
			""
	;
	private String botGroupId = 
			""
	;
	private String botVersion = 
			""
	;
	private String botAuthor = 
			""
	;
	
	// OAUTH Credential Information
	
	private String botUsername =
			""
	;
	private String botPassword = 
			""
	;
	private String botPublicKey = 
			""
	;
	private String botPrivateKey = 
			""
	;
	

	public RedditBot() {
		
	}
	
	/**
	 * Configures most of the internal bot variables. 
	 * UserAgent and Credentials must be configured before run
	 * 
	 * @return This bot object, or null, if UserAgent and 
	 *         Credentials have not been configured 
	 * @throws CredentialException 
	 */
	public RedditBot setupBotVariables() throws CredentialException {
		if (botUsername.equals("") 
				|| botPassword.equals("") 
				|| botPublicKey.equals("") 
				|| botPrivateKey.equals("")) 
		{
			throw new CredentialException("No Credentials Provided");
		}
		
		this.createUserAgent();
		this.generateClient();
		this.generateManagers();
		return this;
	}
	
	/**
	 * Configures the user agent for the bot using the variables
	 * already set, or a default set, if none have been configured
	 * 
	 * @see setPlatform()
	 * @see setGroupId()
	 * @see setVersion()
	 * @see setAuthor()
	 * @return The Generated UserAgent
	 */
	public UserAgent createUserAgent() {
		if (botPlatform.equals("")) botPlatform = "desktop";
		if (botGroupId.equals("")) botGroupId = "net.insxnity.template";
		if (botVersion.equals("")) botVersion = "0.1.0";
		if (botAuthor.equals("")) botAuthor = "Insxnity";
		
		setUserAgent(UserAgent.of(botPlatform, botGroupId, botVersion, botAuthor));
		return botUserAgent;
	}
	
	/**
	 * Configures the user agent for the bot with the parameters 
	 * provided
	 * 
	 * 
	 * @param platform - System Platform, usually "desktop"
	 * @param groupId - Unique ID for your application
	 * @param version - Version of your script
	 * @param author - Author of your script
	 * @return The generated UserAgent
	 */
	public UserAgent createUserAgent(String platform, String groupId, String version, String author) {
		if (botPlatform.equals("")) platform = "desktop";
		if (botGroupId.equals("")) groupId = "net.insxnity.template";
		if (botVersion.equals("")) version = "0.1.0";
		if (botAuthor.equals("")) author = "Insxnity";
		
		setUserAgent(UserAgent.of(platform, groupId, version, author));
		return botUserAgent;
	}
	/**
	 * Configures Credentials object for bot using provided credentials
	 * 
	 * @return The generated Credentials, or null, if the required credentials
	 *         are not avaliable
	 * @throws CredentialException 
	 */
	public Credentials createCredentials() throws CredentialException {
		if (botUsername.equals("") || botPassword.equals("") || botPublicKey.equals("") || botPrivateKey.equals("")) {
			throw new CredentialException("No Credentials Provided");
		}
		setCredentials(Credentials.script(botUsername, botPassword, botPublicKey, botPrivateKey));
		return botCredentials;
	}	
	
	/**
	 * Setup the bots. Credentials need to be predifined or included in the override
	 * 
	 * @return
	 */
	public RedditBot setupBot() {
		Util.log("Generating Credentials");
		
		try {
			createCredentials();
		} catch (CredentialException e) {
			e.printStackTrace();
		}
		createUserAgent();
		
		// Will return null if credentials aren't provided
		try {
			return setupBotVariables();
		} catch (CredentialException e) {
			Util.log("Error 2: CredentialException:");
			Util.log(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Create Credentials using parameters provided
	 * 
	 * @param u - Username
	 * @param p - Password
	 * @param pu - Public Key
	 * @param pr - Private Key
	 * @return The generated Credentials, or null, if required credentials 
	 *         are not available
	 * @throws CredentialException 
	 */
	public Credentials createCredentials(String u, String p, String pu, String pr) throws CredentialException {
		
		if (u.equals("") || p.equals("") || pu.equals("") || pr.equals("")) {
			throw new CredentialException("No Credentials Provided");
		}
		
		setCredentials(Credentials.script(u, p, pu, pr));
		return botCredentials;
	}
	/**
	 * Generates and openes a reddit client using the generated UserAgent
	 * and Credentials
	 * 
	 * @return The generated RedditClient, or null, if one could not be 
	 *         generated
	 */
	public RedditClient generateClient(){

		setRedditClient(new RedditClient(this.getUserAgent()));

		OAuthData authData = null;
		try {
			authData = redditClient.getOAuthHelper().easyAuth(botCredentials);
		} catch (NetworkException | OAuthException e) {
			e.printStackTrace();
		}
		
		redditClient.authenticate(authData);
		
		
		
		return redditClient;
	}
	public RedditBot generateManagers() {
		this.setAccountManager(new AccountManager(this.getRedditClient()));
		this.setInboxManager(new InboxManager(this.getRedditClient()));
		this.setModerationManager(new ModerationManager(this.getRedditClient()));
		liveThreadManager = new LiveThreadManager(this.getRedditClient());
		captchaHelper = new CaptchaHelper(this.getRedditClient());
		multiRedditManager = new MultiRedditManager(this.getRedditClient());
		
		return this;
	}
	
	
	public static void everyHour() throws NetworkException, OAuthException {
		//OAuthData newAuthData = botRedditClient.getOAuthHelper().refreshToken(credentials);
		//botRedditClient.authenticate(newAuthData);
	}
	
	
	public void cleanup() {
		 //OAuthHelper.revokeToken(botCredentials);
	}
	
	
	
	
	
	
	// Start getters and setters

	/**
	 * @return The Bot's RedditClient variable
	 */
	public RedditClient getRedditClient() {
		return redditClient;
	}
	/**
	 * Set the RedditClient. This usually does not need to be done by the 
	 *         end user.
	 * 
	 * @param redditClient - Set the RedditClient to...
	 */
	private void setRedditClient(RedditClient redditClient) {
		this.redditClient = redditClient;
	}
	/** 
	 * @return the Bot's UserAgent variable
	 */
	public UserAgent getUserAgent() {
		return botUserAgent;
	}
	/**
	 * Set the UserAgent. This usually does not need to be done by the
	 *         end user.
	 * 
	 * @param botUserAgent - Set the RedditClient to...
	 */
	private void setUserAgent(UserAgent botUserAgent) {
		this.botUserAgent = botUserAgent;
	}
	/**
	 * Get the bot's credentials. 
	 * 
	 * @return Credentials variable for bot
	 */
	public Credentials getCredentials() {
		return botCredentials;
	}
	/**
	 * Set the credentials for the bot
	 * 
	 * @param botCredentials - Set the Credentials to...
	 */
	private void setCredentials(Credentials botCredentials) {
		this.botCredentials = botCredentials;
	}

	/**
	 * The AccountManager for the Bot. Used to manage most tasks
	 *         that tend to interact with Reddit.
	 * 
	 * @return The Bot's AccountManager variable
	 */
	public AccountManager getAccountManager() {
		return accountManager;
	}
	/**
	 * Set the AccountManager. The end user does not generally need to use
	 * this function.
	 * 
	 * @param accountManager - Set the AccountManager to...
	 */
	private void setAccountManager(AccountManager accountManager) {
		this.accountManager = accountManager;
	}
	
	/**
	 * The InboxManager for the Bot. Used to send mail and shit.
	 * 
	 * @return - The InboxManager for the Bot
	 */
	public InboxManager getInboxManager() {
		return inboxManager;
	}
	/**
	 * Set the InboxManager for the Bot. The end user does not generally need to 
	 *         use this task
	 * @param inboxManager - Set the InboxManager to...
	 */
	private void setInboxManager(InboxManager inboxManager) {
		this.inboxManager = inboxManager;
	}
	/**
	 * Get the ModerationManager for the bot. Assist with moderation duties 
	 *         like deleting, distinguishing, etc.
	 * 
	 * @return - The ModerationManager for the Bot
	 */
	public ModerationManager getModerationManager() {
		return moderationManager;
	}
	/**
	 * Set the ModerationManager for the Bot. The end user does not generally need to 
	 *         use this task
	 * @param moderationManager - Set the ModerationManager to...
	 */
	private void setModerationManager(ModerationManager moderationManager) {
		this.moderationManager = moderationManager;
	}

	public String getPlatform() {
		return botPlatform;
	}

	public void setPlatform(String botPlatform) {
		this.botPlatform = botPlatform;
	}

	
	public String getGroupId() {
		return botGroupId;
	}

	public void setGroupId(String botGroupId) {
		this.botGroupId = botGroupId;
	}

	public String getVersion() {
		return botVersion;
	}

	public void setVersion(String botVersion) {
		this.botVersion = botVersion;
	}

	public String getAuthor() {
		return botAuthor;
	}

	public void setAuthor(String botAuthor) {
		this.botAuthor = botAuthor;
	}

	public String getUsername() {
		return botUsername;
	}

	public void setUsername(String botUsername) {
		this.botUsername = botUsername;
	}

	public String getPassword() {
		return botPassword;
	}

	public void setPassword(String botPassword) {
		this.botPassword = botPassword;
	}

	public String getPublicKey() {
		return botPublicKey;
	}

	public void setPublicKey(String botPublicKey) {
		this.botPublicKey = botPublicKey;
	}

	public String getPrivateKey() {
		return botPrivateKey;
	}

	public LiveThreadManager getLiveThreadManager() {
		return liveThreadManager;
	}

	public CaptchaHelper getCaptchaHelper() {
		return captchaHelper;
	}

	public MultiRedditManager getMultiRedditManager() {
		return multiRedditManager;
	}

	public void setPrivateKey(String botPrivateKey) {
		this.botPrivateKey = botPrivateKey;
	}

	public void reply(Contribution c, String text) throws NetworkException{
		try {
			getAccountManager().reply(c, text);
		} catch (ApiException e) {
			System.out.println("Failed to Reply To Contribution " + c.getId());
			e.printStackTrace();
		}
		
	}
	/**
	 * Performs the variety of tasks required to submit a URL post
	 * 
	 * @param subreddit - The subreddit that you would like to submit to (no "/r/")
	 * @param title - The Title of the post
	 * @param url - The <code>String</code> url which should be posted
	 */
	public void postUrl(String subreddit, String title, String url) throws NetworkException {
		SubmissionBuilder submission = null;
		try {
			submission = new SubmissionBuilder(new URL(url), subreddit, title);
		} catch (MalformedURLException e1) {
			Util.log("Failed to Submit " + title);
			Util.log("There was an error with the URL.");
			
			e1.printStackTrace();
		}
		
		try {
			getAccountManager().submit(submission);
		} catch (ApiException e) {
			Util.log("Failed to Submit " + title);
			Util.log("There was an error regarding JRAW");
			Util.log("This can be due to submitting to a bad subreddit, one you are banned from,"
					+ " or a variety of other issues. Message /u/Insxnity on reddit if this problem"
					+ " persists");
			Util.log("Here's the stacktrace:");
			Util.log(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Performs the variety of tasks required to submit a URL post
	 * 
	 * @param subreddit - The subreddit that you would like to submit to (no "/r/")
	 * @param title - The Title of the post
	 * @param url - The url which should be posted
	 */
	public void postUrl(String subreddit, String title, URL url) throws NetworkException {
		SubmissionBuilder submission = null;
		submission = new SubmissionBuilder(url, subreddit, title);
		
		try {
			getAccountManager().submit(submission);
		} catch (ApiException e) {
			Util.log("Failed to Submit " + title);
			Util.log("There was an error regarding JRAW");
			Util.log("This can be due to submitting to a bad subreddit, one you are banned from,"
					+ " or a variety of other issues. Message /u/Insxnity on reddit if this problem"
					+ " persists");
			Util.log("Here's the stacktrace:");
			Util.log(e.getMessage());
			e.printStackTrace();
		}
	}
	/**
	 * Performs the variety of tasks required to submit a Text post (no "/r/")
	 * 
	 * @param subreddit
	 * @param title
	 * @param text
	 * @throws NetworkException
	 */
	public void postText(String subreddit, String title, String text) throws NetworkException{
		SubmissionBuilder submissionBuilder = new SubmissionBuilder(text, subreddit, title);
		
		try {
			getAccountManager().submit(submissionBuilder);
		} catch (ApiException e) {
			Util.log("Failed to Submit " + title);
			Util.log("There was an error regarding JRAW");
			Util.log("This can be due to submitting to a bad subreddit, one you are banned from,"
					+ " or a variety of other issues. Message /u/Insxnity on reddit if this problem"
					+ " persists");
			Util.log("Here's the stacktrace:");
			Util.log(e.getMessage());
			e.printStackTrace();
		}
	}
	
	
	
}
