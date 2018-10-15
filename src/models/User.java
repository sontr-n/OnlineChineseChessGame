package models;

import java.io.Serializable;
import java.net.Socket;

public class User implements Serializable {
	private String username;
	private char[] password;
	private double score;
	private int id;
	private boolean isBusy;
	
	
	public User(String u, char[] p) {
		this.username = u;
		this.password = p;
		isBusy = true;
		score = 0;
	}
	
	public User(int id, String u, char[] p, double s) {
		this.id = id;
		username = u;
		password = p;
		score = s;
		isBusy = false;
	}
	

	
	public User(String u) {
		username = u;
		isBusy = false;
		score = 0;
	}
	
	public int getId() {
		return id;
	}
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public char[] getPassword() {
		return password;
	}
	public void setPassword(char[] password) {
		this.password = password;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	
	public String[] toObject() {
		String busy = "busy";
		if (!isBusy) busy = "free";
		String[] s = {username, String.valueOf(score), busy};
		return s;
	}
	
	
}
