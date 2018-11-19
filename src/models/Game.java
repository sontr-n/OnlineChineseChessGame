package models;

import java.io.Serializable;

public class Game implements Serializable {
	private User player1;
	private User player2;
	private int id;
	private int p1MoveTotal;
	private int p2MoveTotal;
	private static int static_id = 0;
	
	public Game(User p1, User p2) {
		player1 = p1;
		player2 = p2;
		p1MoveTotal = 0;
		p2MoveTotal = 0;
		id = static_id;
		static_id++;
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
	public void setId(int id) {
		this.id = id;
	}
	public int getP1MoveTotal() {
		return p1MoveTotal;
	}
	public void setP1MoveTotal(int p1MoveTotal) {
		this.p1MoveTotal = p1MoveTotal;
	}
	public int getP2MoveTotal() {
		return p2MoveTotal;
	}
	public void setP2MoveTotal(int p2MoveTotal) {
		this.p2MoveTotal = p2MoveTotal;
	}
	
	public void player1Move() {
		p1MoveTotal++;
	}
	
	public void player2Move() {
		p2MoveTotal++;
	}
	
	
}