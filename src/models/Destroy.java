package models;

import java.io.Serializable;


public class Destroy implements Serializable {

	private static final long serialVersionUID = 1L;
	public int hitChess;
	public int destroyedChess;
	
	public Destroy(int hc, int dc) {
		hitChess = hc;
		destroyedChess = dc;
	}
}
