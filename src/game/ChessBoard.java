
package game;

import java.util.ArrayList;
import java.util.ListIterator;

public class ChessBoard {
	// the width of the Chess Board
	private int width;
	// the height of the Chess Board
	private int height;
	// Chess Board consists of a quantity of chess points
	ArrayList<ChessPoint> chessPoints = null;
	//private int index; 
	
	
	/**
	 * Constructor
	 */
	ChessBoard(int width, int height){
		this.width = width;
		this.height = height;
		//this.index = -1;
		this.chessPoints = new ArrayList<ChessPoint>();
		for(int i=0; i<this.width; i++){
			for(int j=0; j<this.height; j++){
			ChessPoint cp = new ChessPoint(i,j);
			this.chessPoints.add(cp);
			}
		}
	}
	
	
	public ArrayList<ChessPoint> getChessPoints(){
		return this.chessPoints;
	}
	
	/**
	 * Go through each Chess in a Chess group, and record their position information by using chess points
	 * @param cg : input chess group
	 */
	public void SetPositionTaken(ChessGroup cg){
		ListIterator<ChessPoint> lit = this.chessPoints.listIterator();
		while(lit.hasNext()){
			ChessPoint cp = lit.next();
			ListIterator<Chess> ic = cg.getChess().listIterator();
			while(ic.hasNext()){
				Chess c = ic.next();
				if(cp.getX() == c.getIndX() && cp.getY() == c.getIndY()){
					cp.setTaken(true);
					break;
				}
			}
		}
	}
	

	/**
	 * Find the index of the chess point by the given position information
	 * @param index_x : horizontal position of the chess point
	 * @param index_y : vertical position of the chess point
	 * @return : return the index of the chess point
	 */
	public int FindByIndex(int index_x, int index_y){
		ListIterator<ChessPoint> lit = this.chessPoints.listIterator();
		int index = -1;
		while(lit.hasNext()){
			ChessPoint cp = lit.next();
			if(cp.getX() == index_x && cp.getY() == index_y){
				index = this.chessPoints.indexOf(cp);
				break;
			}
		}
		return index;
	}
	
	/**
	 * Used to indicate the given position on the Chess Board is taken or not
	 * @param index_x : horizontal position of the chess point
	 * @param index_y : vertical position of the chess point
	 * @return : return true if the position has been taken by a chess
	 */
	public boolean isTaken(int index_x, int index_y){
		boolean flag = false;
		int index = FindByIndex(index_x, index_y);
		if(index == -1){
			System.out.println("index can not be -1");
			System.exit(-1);
		}else{ // find the specific position
			ChessPoint cp = (ChessPoint)this.chessPoints.get(index);
			flag = cp.isTaken();
		}
		return flag;
	}
	
	
	/**
	 * This method is used to update the chess point status when a chess moving to the other position
	 * @param c : chess object
	 */
	public void ChangeTaken(Chess c){
		int old_indx = FindByIndex(c.getPre_x(),c.getPre_y());
		int new_indx = FindByIndex(c.getIndX(),c.getIndY());
		if(old_indx!=-1 && new_indx!=-1){
			ChessPoint cpold = this.chessPoints.get(old_indx);
			cpold.setTaken(false);
			ChessPoint cpnew = this.chessPoints.get(new_indx);
			cpnew.setTaken(true);	
		}else{
			System.out.println("Wrong here!");
			System.exit(-1);
		}
	}
	
	/**
	 * Inner class ChessPoint
	 * @author Louis
	 */
	class ChessPoint{
		// horizontal position of the chess point
		private int x = 0;
		// vertical position of the chess point
		private int y = 0;
		// used for indicating this chess point is taken by a chess or not
		private boolean isTaken = false;
		
		ChessPoint(){
			super();
		}
		
		/**
		 * Constructor
		 * @param x : horizontal position
		 * @param y : vertical position
		 */
		ChessPoint(int x, int y){
			this.x = x;
			this.y = y;
			this.isTaken = false;
		}
		
		/**
		 * overload Constructor
		 * @param x : horizontal position
		 * @param y : vertical position
		 * @param isTaken : this chess point is taken or not
		 */
		ChessPoint(int x, int y, boolean isTaken){
			this(x,y);
			this.isTaken = isTaken;
		}
		
		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}




		public boolean isTaken() {
			return isTaken;
		}

		public void setTaken(boolean isTaken) {
			this.isTaken = isTaken;
		}
		
		
	}
}
