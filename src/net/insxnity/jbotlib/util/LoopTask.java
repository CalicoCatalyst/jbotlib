package net.insxnity.jbotlib.util;

import java.util.TimerTask;

import net.insxnity.jbotlib.bot.LoopBot;

public class LoopTask extends TimerTask {
	
	private LoopBot botToLoop = null;
	
	@Override
	public void run() {
		botToLoop.ranByLoop();
	}
	
	public void setLoopingObject(LoopBot bot) {
		botToLoop = bot;
	}
	
}
