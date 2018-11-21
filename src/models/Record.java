package models;

import java.io.Serializable;

public class Record implements Serializable {
	private int id;
	private User user;
	private boolean win;
	private int move;
	
	public Record(int i, User u, int m, boolean w) {
		id = i;
		user = u;
		win = w;
		move = m;
	}

	public int getId() {
		return id;
	}

	public User getUser() {
		return user;
	}
	
	public boolean isWin() {
		return win;
	}
	
	public int getMove() {
		return move;
	}
}
