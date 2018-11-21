package models;

import java.io.Serializable;

public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private char[] password;
	private double score;
	private int moveWin;
	private int moveLose;
	private int gameWin;
	private int gameLose;
	private int gameDraw;
	
	
	public int getGameWin() {
		return gameWin;
	}

	public int getGameLose() {
		return gameLose;
	}

	public int getGameDraw() {
		return gameDraw;
	}

	public void setGameWin(int gameWin) {
		this.gameWin = gameWin;
	}

	public void setGameLose(int gameLose) {
		this.gameLose = gameLose;
	}

	public void setGameDraw(int gameDraw) {
		this.gameDraw = gameDraw;
	}

	public int getMoveWin() {
		return moveWin;
	}

	public int getMoveLose() {
		return moveLose;
	}

	public void setMoveWin(int moveWin) {
		this.moveWin = moveWin;
	}

	public void setMoveLose(int moveLose) {
		this.moveLose = moveLose;
	}

	private int id;
	private boolean isBusy;

	public User(String u, char[] p) {
		this.username = u;
		this.password = p;
		isBusy = false;
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
	public String getPassword() {
		return String.valueOf(password);
	}
	public void setPassword(char[] password) {
		this.password = password;
	}

	public double getScore() {
		score = gameWin + gameDraw/2;
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public boolean isBusy() {
		return isBusy;
	}
	
	public double getAverageMoveWin() {
		if (gameWin == 0) return 0;
		return Math.round(((double)moveWin) / gameWin);
	}
	
	public double getAverageMoveLose() {
		if (gameLose == 0) return 0;
		return ((double)moveLose) / gameLose;
	}
	

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}
	
	public String[] toObject() {
		String busy = "busy";
		if (!isBusy) busy = "free";
		String[] s = {username, String.valueOf(getScore()), busy};
		return s;
	}
	
	public void win() {
		gameWin++;
	}
	
	
}
