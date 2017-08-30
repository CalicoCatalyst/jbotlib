# jbotlib

## Contents:

[Introduction](#introduction)  
[Getting Started](#getting-started)  
[Creating a Looping Bot](#creating-a-looping-bot)  
[- Looping Your Code](#looping-your-code)  
[Creating a Post](#creating-a-post)  
[Replying to Something](#replying-to-something)  
[Using the full extent of JRAW](#using-the-full-extent-of-jraw)  
[Installation](#installation)  
[Example](#example-program)

### Introduction
JBotLib is an extremely simple library designed to make creating reddit bots from JRAW much easier.  
Creating reddit bots with JRAW can be redundant. This library assists with logging in, posting, replying, and other things that JRAW, being just a Wrapper, does not include

### Getting Started
At the core of JBotLib is the `RedditBot` object. This represents a physical bot object. You tell it it's login info, tell it to reply, submit, and do other basic tasks.  

You can also grab any of the Managers jraw provides, which are automatically generated upon login. 

Additionally, you can create as many of these bot objects, with as many different identities as you would like.

To make a bot with this, although it can be done externally, I would recomend creating a subclass (extends RedditBot). All examples in this documentation will assume you're using this as a superclass

### Creating a Looping Bot
Most of the time, bots need to loop to function properly. This Library comes with a `LoopBot` object, which handles all of the requirements for creating a looping bot

To use this class, you'll want to create your own bot class, and extend `LoopBot`

#### Looping your code

To have your code loop, you'll need to create a method named `public void mainLoop()`. It should look something like this:

    @Override
    public void mainLoop() {
        // Your Code Here
    }
    
#### Starting the Loop  
This will run the code inside `mainLoop()`

    startLoop();
    
#### Stopping the Loop  
This will stop the code from cycling until started again

    stopLoop();

### Authenticating Your Bot

Authenticating your bot takes five lines of code. This Lib supports authenticating with OAuth2 using the script class

Authenticating should be done using the following syntax

    setUsername("botUsername");
    setPassword("botPassword");
    setPublicKey("publicKey");
    setPrivateKey("privateKey");
    // Once You have configured credentials
    
    // This configures most of the main variables for the bot
    setupBot();
    
### Creating a Post

To create a post, 3 methods exist:

    postUrl(String subreddit, String title, String url);
    
    postUrl(String subreddit, String title, URL url);
    
    postText(String subreddit, String title, Strint text);
    
They will throw an error if you try to post to a subreddit that cant be posted to, or that you are banned from.

### Replying to Something

Replying to something is very simple. Use the following syntax:

    reply(Contribution contribution, String text);
    
## Using the Full Extent of JRAW

Using the full extent of JRAW, with the variables included, can be done, easily.

You can get a bot's RedditClient, which is what JRAW uses to create the rest of it's managers, using:

    // Assuming rb is an object that extends or is RedditBot
    rb.getRedditClient();
    
You can also get the following managers using the following methods:

    // Assuming rb is an object that extends or is RedditBot
    rb.getAccountManager();
    rb.getInboxManager();
    rb.getModerationManager();
    rb.getLiveThreadManager();
    rb.getCaptchaHelper();
    rb.getMultiRedditManager();
    
## Installation

The first step is to download both the JRAW 0.9.0 and the JBotLib .jar libraries. You can find both of them here:

http://www.insxnity.net/jbotlib.html

Next, Add these to your build path. 

Once you've done that, follow the instructions above to use them in your projects.
    
# Example Program

    package net.insxnity.redditbot.jbotlibtest;

    import java.util.List;

    import net.dean.jraw.models.Submission;
    import net.dean.jraw.paginators.SubredditPaginator;
    import net.insxnity.jbotlib.bot.LoopBot;

    public class JBotLibTest extends LoopBot {

      /**
       * This method can be called by any outside class, and doing so will run the bot
       */
      public void run() {
        setUsername("username");
        setPassword("password");
        setPublicKey("publickey");
        setPrivateKey("privatekey");
        
        // Use the provided credentials to configure all of the bot variables we'll need
        setupBot();

        // Start running mainLoop();
        startLoop();
      }

      @Override
      public void mainLoop() {
        // Get a Paginator of the front page of redditdev
        SubredditPaginator sp = new SubredditPaginator(redditClient, "redditdev");
        // Make a list of Submission objects, grabbing one page's worth (25)
        List<Submission> spList = sp.accumulateMerged(1);
        // Reply to the first one (0) with the text
        reply(spList.get(0), "This is some reply text");

        // Make a .self post
        postText("exampleSubreddit", "This is the Title", "The text is here");
      }
    }


    
