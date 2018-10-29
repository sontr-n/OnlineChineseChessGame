/**
 * class ChessGroup
 * @author Louis
 */
package game;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
//import java.util.Iterator;
import java.util.ListIterator;


public class ChessGroup implements Serializable {
	// the number of Chess in this group
	private int numChess = 0;
	// Chess list
	private ArrayList<Chess> chesses = null; 
	// red side or black side
	@SuppressWarnings("unused")
	private String color = null;
	// use to keep which chess is chosen
	private int index = -1; 
	// use to record how many chesses are chosen
	private int numTaken = 0;
	//private Point chessLocation = null; // the position of the first card in this cards group
	/**
	 * Constructor
	 */
	ChessGroup(){
		this.numChess = 0;
		this.index = -1;
		this.chesses = new ArrayList<Chess>();
		this.color = new String();
		this.numTaken = 0;
		//this.chessLocation = new Point();
	}
	
	/**
	 * Overload constructor. 
	 * @param str : input chess name list array. 
	 * we will add Chesses into the chess list by their name.
	 * @param color : red side or black side
	 */
	ChessGroup(String[] str, String color, Point p){
		this.numTaken = 0;
		this.index = -1;
		this.color = color;
		this.numChess = 16;
		this.chesses = new ArrayList<Chess>();
		int index = 0;
		//this.chessLocation = new Point(p);
		for(int i=0; i<str.length; i++){
			String strCmp = str[i]; // black-ma.gif
			int id = strCmp.indexOf('-'); // id = 5
			String strTmp = strCmp.substring(id+1,strCmp.length()-4); // ma
			//String strTmp2 = strCmp.substring(0, id); // black
			if(color.equals("red")&&color.equals(strCmp.substring(0, id))){
				if(strTmp.equals("bing")){ //five bing
					for(int j = 0; j< 5; j++){
						// their position are: (0,3),(2,3),(4,3),(6,3),(8,3)
						Chess c = new Chess(str[i], j*2 , 3);
						c.setId(index);
						index++;
						chesses.add(c);
					}
					
				}else if(strTmp.equals("jiang")){ //one jiang
					// its position is (0,4)
					Chess c = new Chess(str[i],4,0);
					c.setId(index);
					chesses.add(c);
					index ++;
				}else if(strTmp.equals("ju")){ // two ju
					for(int j = 0; j< 2; j++){
						// their position are: (0,0) and (8,0)
						Chess c = new Chess(str[i], 8*j, 0);
						c.setId(index);
						chesses.add(c);
						index++;
					}
					
				}else if(strTmp.equals("ma")){ // two ma
					for(int j = 0; j< 2; j++){
						// their position are: (1,0) and (7,0)
						Chess c = new Chess(str[i], 6*j+1, 0);
						c.setId(index);
						chesses.add(c);
						index++;
					}
				}else if(strTmp.equals("pao")){ // two pao
					for(int j = 0; j< 2; j++){
						// their position are: (1,2) and (7,2)
						Chess c = new Chess(str[i], 6*j+1, 2);
						c.setId(index);
						chesses.add(c);
						index++;
					}
				
				}else if(strTmp.equals("shi")){ // two shi
					for(int j = 0; j< 2; j++){
						// their position are: (3,0) and (5,0)
						Chess c = new Chess(str[i], j*2+3, 0);
						c.setId(index);
						chesses.add(c);
						index++;
					}
				
				}else if(strTmp.equals("xiang")){ // two xiang
					for(int j = 0; j< 2; j++){
						// their position are: (2,0) and (6,0)
						Chess c = new Chess(str[i], j*4+2, 0);
						c.setId(index);
						chesses.add(c);
						index++;
					}
					
				}else{
					System.out.println("error chess name here!");
				}
				
			}
			else if(color.equals("black")&&color.equals(strCmp.substring(0, id))){
				if(strTmp.equals("bing")){ //five bing
					for(int j = 0; j< 5; j++){
						// their position are: (0,6),(2,6),(4,6),(6,6),(8,6)
						Chess c = new Chess(str[i], j*2 , 6);
						c.setId(index);
						chesses.add(c);
						index++;
					}
					//index = index + 5;
				}else if(strTmp.equals("jiang")){ //one jiang
					// its position is (4,9)
					Chess c = new Chess(str[i],4,9);
					c.setId(index);
					chesses.add(c);
					index ++;
				}else if(strTmp.equals("ju")){ // two ju
					for(int j = 0; j< 2; j++){
						// their position are: (0,9) and (8,9)
						Chess c = new Chess(str[i], 8*j, 9);
						c.setId(index);
						chesses.add(c);
						index ++;
					}
					//index = index + 2;
				}else if(strTmp.equals("ma")){ // two ma
					for(int j = 0; j< 2; j++){
						// their position are: (1,9) and (7,9)
						Chess c = new Chess(str[i], 6*j+1, 9);
						c.setId(index);
						chesses.add(c);
						index ++;
					}
					//index = index + 2;
				}else if(strTmp.equals("pao")){ // two pao
					for(int j = 0; j< 2; j++){
						// their position are: (1,7) and (7,7)
						Chess c = new Chess(str[i], 6*j+1, 7);
						c.setId(index);
						chesses.add(c);
						index ++;
					}
					//index = index + 2;
				}else if(strTmp.equals("shi")){ // two shi
					for(int j = 0; j< 2; j++){
						// their position are: (3,9) and (5,9)
						Chess c = new Chess(str[i], j*2+3, 9);
						c.setId(index);
						chesses.add(c);
						index ++;
					}
					//index = index + 2;
				}else if(strTmp.equals("xiang")){ // two xiang
					for(int j = 0; j< 2; j++){
						// their position are: (2,9) and (6,9)
						Chess c = new Chess(str[i], j*4+2, 9);
						c.setId(index);
						chesses.add(c);
						index ++;
					}
					//index = index + 2;
				}else{
					System.out.println("error chess name here!");
				}
				
			}
		}
		System.out.println("index is:" + index);
	}
	
	public ArrayList<Chess> getChess(){
		return this.chesses;
	}
	
	/**
	 * This method used to calculate how many chesses are chosen
	 */
	public void CalIndex(){
		this.index = -1;
		this.numTaken = 0;
		ListIterator<Chess> lit = this.chesses.listIterator();
		while(lit.hasNext()){
			Chess c = lit.next();
			if(c.isChosen()){
				this.index = this.chesses.indexOf(c);
				this.numTaken ++;
			}
		}
	}
	
	
	public int getNumTaken() {
		return numTaken;
	}

	public void setNumTaken(int numTaken) {
		this.numTaken = numTaken;
	}

	public int getIndex(){
		return this.index;
	}
	
	public void setIndex(int index){
		this.index = index;
	}
	
	public boolean isAlive(){
		boolean flag = true;
		ListIterator<Chess> lit = this.chesses.listIterator();
		while(lit.hasNext()){
			Chess c = lit.next();
			if(c.getRank().equals("jiang")){
				if(!c.isAlive()){
					flag = false;
					break;
				}
			}
		}
		return flag;
	}
	
	
	//public void drawChess(){
		//ListIterator<Chess> lit = this.chesses.listIterator();
		//while(lit.hasNext()){
			//Chess c = lit.next();
			//if(c.isAlive()){
				//c.draw();
			//}
		//}
	//}
	
	
	/**
	 * Go through each Chess in this group, and set its status to be unchosen 
	 */
	public void disableChosen(){
		ListIterator<Chess> lit = this.chesses.listIterator();
		while(lit.hasNext()){
			Chess c = lit.next();
			if(c.isChosen()){
				c.setChosen(false);
			}
		}
		this.setIndex(-1);
	}

	public int getChessNumber() {
		// TODO Auto-generated method stub
		return this.numChess;
		
	}

	public void shuffleChess() {
		// TODO Auto-generated method stub
		Collections.shuffle(this.getChess());
	}

	public Point getChessLocation() {
		// TODO Auto-generated method stub
		return this.getChessLocation();
	}

	
}
