package controllers.networking.server;

import java.io.IOException;

import models.Client;
import models.DataPackage;

public class GameController {
	private Client p1, p2;
	private int winner = -1;
	
	public GameController(Client c1, Client c2) {
		p1 = c1;
		p2 = c2;
		
		startGame();
	}

	public void startGame() {
		PlayerThread p1Thread = new PlayerThread(p1, p2);
		PlayerThread p2Thread = new PlayerThread(p2, p1);
		p1Thread.start();
		p2Thread.start();
		
		for (;;) {
			System.out.print("");
			if (winner != -1) break;
		}
	}
	
	
	private class PlayerThread extends Thread {
		private Client me;
		private Client enemy;
		public PlayerThread(Client m, Client e) {
			me = m;
			enemy = e;
		}
		
		@Override
		public void run() {
			for (;;) {
				try {
					DataPackage dp = (DataPackage) me.getInputStream().readObject();
					System.out.println("test");
				} catch (ClassNotFoundException | IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
	

}
