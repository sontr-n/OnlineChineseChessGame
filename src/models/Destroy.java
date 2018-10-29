package models;

import java.io.Serializable;

import game.ChessGroup;

public class Destroy implements Serializable {

	public int hitChess;
	public int destroyedChess;
	
	public Destroy(int hc, int dc) {
		hitChess = hc;
		destroyedChess = dc;
	}
}
