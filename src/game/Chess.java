package game;

import javax.swing.*;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Chess extends JLabel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	public static final int START_X = 25;
//	public static final int START_Y = 25;
//	public static final int SIZE = 55; //Chess size
//	public static final int GAP = 27; // Chess gap
//	public static final int UNIT = 57; // Chess board unit size
	
	public static int START_X = 25;
	public static int START_Y = 25;
	public static int SIZE = 55; //Chess size
	public static int GAP = 27; // Chess gap
	public static int UNIT = 57; // Chess board unit size
	
	public enum Suits {Red, Black};
	private int id = 0;
	// we will use the given image file name as the Chess name. e.g.: red-ma.jpg
	private String name = null;
	// the rank of the Chess. E.g.: for a given name "red-ma.jpg", the string "ma" will be extracted as the rank.
	private String rank = null;
	// Chess suit. for red side, it can take the value as Suits.Red. And for black side, it can take the value as Suits.Black
	// E.g.: for a given name "red-ma.jpg", the string "red" will be extracted out first. Then we will know this Chess comes from the red side. 
	// And the corresponding Suits.Red will be assigned as its rank.
	private Suits suit = null;
	// Current horizontal position of the Chess
	private int index_x = 0;
	// Current vertical position of the Chess
	private int index_y = 0;
	
	private Point location = null;
	// previous horizontal position of the Chess
	private int pre_x = 0;
	// previous vertical position of the Chess
	private int pre_y = 0;
	private String url = null;
	// This Chess is chosen or not
	private boolean isChosen = false;
	private boolean isAlive = true;

	/**
	 * empty constructor
	 */
	Chess(){
		super();
	}
	
	/**
	 * Overloaded Constructor
	 * @param str: input string, e.g.: red-ma.jpg. 
	 * The first part "red" will be extracted out as the value of suit, and "ma" will be extracted as the value of rank
	 * @param x: horizontal position
	 * @param y: vertical position
	 */
	Chess(String str, int x, int y){
		this.name = new String(str);
		//System.out.println(str);
		String seprator = new String("-");
		String[] sub = str.split(seprator);
		if(sub.length != 2){
			System.out.println("input strname has problem!");
			System.out.println(str);
			System.out.println(sub[0]);
			System.out.println(sub[1]);
			System.exit(-1);
		}
		//this.figure = Integer.parseInt(sub[1]);
		switch(name.charAt(0)){
		case 'b':
			this.suit = Suits.Black;
			break;
		case 'r':
			this.suit = Suits.Red;
			break;
		}
		this.index_x = x;
		this.index_y = y;
		//Point p = new Point(x,y);
		this.location = new Point();
		//this.preLocation = new Point();
		calPosition(this.index_x, this.index_y);
		this.pre_x = this.index_x;
		this.pre_y = this.index_y;
		this.url = new String("images/" + name);
		int id = name.indexOf('-');
		this.rank = new String(name.substring(id+1,name.length()-4));
		System.out.println("url is:" + url);
		System.out.println("name is:" + name);
		this.setIcon(new ImageIcon(url));
        this.setSize(55, 55);
        this.setLocation(location);
        this.setVisible(true);
        this.addMouseListener(new Moniter());
        System.out.println("chess has been initilized");
	}
	
	// Convert the matrix index to the real pixels
	public void calPosition(int index_x, int index_y){
		double tmp_x = index_x*UNIT + START_X;
		double tmp_y = index_y*UNIT + START_Y;
		this.location.setLocation(tmp_x, tmp_y);
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Suits getSuit() {
		return suit;
	}
	public void setSuit(Suits suit) {
		this.suit = suit;
	}
	
	public String getRank(){
		return rank;
	}
	public int getIndX(){
		return index_x;
	}
	public int getIndY(){
		return index_y;
	}
	public void setIndX(int index_x){
		this.index_x = index_x;
	}
	public void setIndY(int index_y){
		this.index_y = index_y;
	}
	
	public int getPre_x() {
		return pre_x;
	}
	public void setPre_x(int pre_x) {
		this.pre_x = pre_x;
	}
	public int getPre_y() {
		return pre_y;
	}
	public void setPre_y(int pre_y) {
		this.pre_y = pre_y;
	}
	public void draw(){
		setIcon(new ImageIcon(url));
        setSize(55, 55);
        setLocation(location);
        setVisible(true);
	}
	
	public void setId(int id){
		this.id = id;
	}
	public int getId(){
		return this.id;
	}

	public boolean isChosen() {
		return isChosen;
	}
	public void setChosen(boolean isChosen) {
		this.isChosen = isChosen;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}
	
	public static void setSTART_X(int start_x){
		START_X = start_x;		
	}
	
	public static void setSTART_Y(int start_y){
		START_Y = start_y;		
	}
	
	public static void setUNIT(int unit){
		UNIT = unit;
	}
	
	public static int getSTART_X(){
		return START_X;
	}
	
	public static int getSTART_Y(){
		return START_Y;
	}
	
	public static int getUNIT(){
		return UNIT;
	}
	
	/**
	 * Override toString method. the string presentation of this object will be as following, E.g.:
	 * Suit is: red
	 * Rank is: ma
	 * Position is: 2 3
	 */
	public String toString() {
			
			//write your code here!
			String tmp = null;
			tmp = (suit == Suits.Red)? new String("red")
				: (suit == Suits.Black)? new String("black")
				: null;
			
			double tmp_x = this.location.getX();
			double tmp_y = this.location.getY();
			
			return Double.valueOf(tmp_x).toString() + ":" + Double.valueOf(tmp_y) + ":" + tmp;
		}
	
	public void dead(){
		this.setChosen(false);
		this.setAlive(false);
		this.setVisible(false);
	}
	/**
	 * Inner class. Used to respond mouse click
	 * @author Louis
	 *
	 */
	class Moniter extends MouseAdapter {
		
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			if(!MainFrame.getIsOver()){
				Chess c = (Chess)e.getSource();
				c.isChosen = true;
				new Blink(c);
			}
		}

		
	}
	
	/**
	 * Inner class. Used to display blinking effect when clicking
	 * @author Louis
	 *
	 */
	class Blink implements Runnable{
		Chess c = null;
		public Blink(Chess c){
			this.c = c;
			(new Thread(this)).start();
		}
		@Override
		public void run() {
			while (c.isChosen){
				//begin to blink when first click
				System.out.println(Thread.currentThread().getName());
				if (c.isChosen){
					c.setVisible(false);

					//time control
					try{
						Thread.sleep(200);
					}
					catch(Exception e){
						e.printStackTrace();
					}
					
					c.setVisible(true);
					
					try{
						Thread.sleep(350);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
	}
}
