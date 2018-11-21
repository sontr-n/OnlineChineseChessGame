package models;

import java.io.Serializable;

public class Rematch implements Serializable {
	private int gameId;
	private boolean rematch;
	
	
	public Rematch(int id, boolean rm) {
		gameId = id;
		rematch = rm;
	}
	
	public int getGameId() {
		return gameId;
	}
	public boolean isRematch() {
		return rematch;
	}
	
	
}
