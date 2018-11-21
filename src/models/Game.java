package models;

import java.io.Serializable;

public class Game implements Serializable {
	private User player1;
	private User player2;
	private String winner;
	private int id;
	private int p1Moved;
	private int p2Moved;
	private static int static_id = 0;
	private int p1Rematch;
	private int p2Rematch;
	private String playFirst;
	
	
	public int getP1Moved() {
		return p1Moved;
	}

	public int getP2Moved() {
		return p2Moved;
	}

	public void setP1Moved(int p1Moved) {
		this.p1Moved = p1Moved;
	}

	public void setP2Moved(int p2Moved) {
		this.p2Moved = p2Moved;
	}

	public Game(User p1, User p2) {
		id = static_id;
		static_id++;
		player1 = p1;
		player2 = p2;
		reset();
	}
	
	public void reset() {
		winner = "";
		p1Moved = -1;
		p2Moved = -1;
		p1Rematch = -1;
		p2Rematch = -1;
	}

	public User getPlayer1() {
		return player1;
	}

	public User getPlayer2() {
		return player2;
	}

	public int getId() {
		return id;
	}

	public String getWinner() {
		return winner;
	}


	public void setWinner(String winner) {
		this.winner = winner;
	}

	public String getPlayFirst() {
		return playFirst;
	}
	
	public void setPlayFirst(String u) {
		playFirst = u;
	}
	
	public void switchPlayer() {
		if (playFirst.equals(player1.getUsername()))
			playFirst = player2.getUsername();
		else 
			playFirst = player1.getUsername();
	}

	public int getP1Rematch() {
		return p1Rematch;
	}

	public int getP2Rematch() {
		return p2Rematch;
	}

	public void setP1Rematch(int p1Rematch) {
		this.p1Rematch = p1Rematch;
	}

	public void setP2Rematch(int p2Rematch) {
		this.p2Rematch = p2Rematch;
	}

	

}