package models;

import java.io.Serializable;


public class Movement implements Serializable {
	public int chessIndex;
	public int transportX;
	public int transportY;
	
	public Movement(int ci, int tx, int ty) {
		chessIndex = ci;
		transportX = tx;
		transportY = ty;
	}
}
