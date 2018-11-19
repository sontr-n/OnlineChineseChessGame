package game;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextArea;

import controllers.ActionType;
import controllers.HomeController;
import controllers.PlayerController;
import controllers.UserController;
import controllers.networking.client.ClientController;
import models.DataPackage;
import models.Destroy;
import models.Movement;
import models.User;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;




public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int FRAME_WIDTH = 565;
	public static final int FRAME_HEIGHT = 700;
	public static final Point POINT = new Point(10,400);
	private static final Point Point = null;
	private Image offScreenImage = null;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JTextArea sideFlag = null;//This is a text area which shows the side of the player and who is in turn now.
	private JButton jButton = null;
	private JLabel jLabel = null;
	// "red" is used for red side chess
	private ChessGroup red = null; 
	// "black" is used for black side chess
	private ChessGroup black = null; 
	// "cb" is used for chess board
	private ChessBoard cb = null;  
	// for judging whether it's red side's turn to move now 
	private boolean RedPlay = false;
	// for judging game over
	public static boolean isOver = false;
	//public boolean isClick = false;
	Chess eatenChess;//A chess to store the dead one
	Chess eatenChessPre;//A chess to store the dead chess two steps earlier
	Chess movedChess;//A chess to store the moved one
	Chess movedChessPre;//A chess to store the moved chess two steps earlier
	boolean canGo = false;//if it is true, than it is the player's turn
	public boolean isMaster = false;//if player is the master side, the first attacker
	boolean callNewGame = true;
	Thread getThread = null;//a thread to get the information from the next side
	Thread threadChessDestroy = null;//a thread to detect if it can do chess eaten move or one side is lose
	ChessDestroy detectChessDestroy = null;
	JLabel lblTimer;
	public int countDown = 30;
	private TimeThread timeThread;

	public void stopTime() {
		timeThread.stop();
	}
	
	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
		// for double buffering
		timeThread = new TimeThread();
		new Thread(new PaintThread()).start();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	
	private class TimeThread extends Thread {
		@Override
		public void run() {
			runTimer();
			this.setDaemon(true);
		}
		
		public void runTimer() {
			while (countDown > 0 || !isOver) {
				try {
					Thread.sleep(1000L);
					countDown--;
					lblTimer.setText("0:"+countDown);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	private void initialize() {
		this.setSize(750, 660);//565
		this.setContentPane(getJContentPane());
		this.setTitle("Chinese Chess");
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		
	}
	
	// for double buffering
	public void update(Graphics g) {
		if(offScreenImage == null) {
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		Color c = gOffScreen.getColor();
		gOffScreen.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);
		gOffScreen.setColor(c);
		paint(gOffScreen);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	
	// for double buffering
	private class PaintThread implements Runnable {

		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * This method initializes jContentPane
	 * j
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getJPanel2(), null);
		}
		// draw chess board background image
		jLabel = new JLabel();
		jLabel.setIcon(new ImageIcon("images/main.gif"));
		jLabel.setBounds(0, 0, 582, 620);//582,620
		jLabel.setVisible(true);
		jPanel.add(jLabel,null);	
		return jContentPane;
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(0, 0, 558, 620));//558
			jPanel.addMouseListener(new Moniter());
		}
		return jPanel;
	}

	
	
	/**
	 * This method initializes jPanel2
	 * It will add three small panel into itself
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel2(){
		if(jPanel2 == null) {
			jPanel2 = new JPanel();
			jPanel2.setLayout(null);									
			jPanel2.setBounds(new Rectangle(582, 0, 150, 620));
			jPanel2.add(getSideFlag());
			jPanel2.add(getExitButton());
			jPanel2.add(getLblTime());
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel3
	 * It will add the Text Area sideFlag, which shows the side of the player and the current turn
	 * @return javax.swing.JPanel
	 */
	private JPanel getSideFlag(){
		if(jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(null);
			sideFlag = new JTextArea("Welcome to the Chinese Chess Game!\n", 2, 20);
			sideFlag.setEditable(false);
			sideFlag.setBounds(new Rectangle(0, 5, 300, 36));
			jPanel2.add(sideFlag, null);
			jPanel3.setBounds(new Rectangle(0, 0, 318, 50));
		}
		return jPanel3;
	}
	
	private JLabel getLblTime() {
		lblTimer = new JLabel("0:" + countDown);
		lblTimer.setBounds(new Rectangle(25, 300, 123, 37));
		lblTimer.setFont(new Font("Tahoma", Font.BOLD, 40));
		return lblTimer;
	}
	
	
//Exit button	
	private JButton getExitButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(0, 580, 123, 37));
			jButton.setText("Exit");
			jButton.addActionListener(new ExitButton());
		}
		return jButton;
	}
	
	/**
	 * This ActionListener Class implement the function of sending new game apply to another side player
	 * 
	 *
	 */
	private class ExitButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
        	int chose = JOptionPane.showConfirmDialog(null, "Are you a Cower?", "Exit", JOptionPane.YES_NO_OPTION);
        	if (chose == JOptionPane.YES_OPTION) {
        		DataPackage dp = new DataPackage(UserController.getInstance().getUser(), PlayerController.getInstance().getUser(), ActionType.EXIT);
        		ClientController.getInstance().sendData(dp);
        		dispose();
        		HomeController.getInstance().displayView();
        	}
        }
    } 
	
	/**
	 * This method start new game for the master side, all the chess are in normal look
	 * 
	 * @return void
	 */
	public void normalNewGame(){
		jLabel.setVisible(false);
		jPanel.remove(jLabel);
		newGame();
		jLabel.setVisible(true);
		jPanel.add(jLabel,null);
		canGo = true;
		changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
		timeThread.start();
	}
	
	/**
	 * This method start new game for the slave side, all the chess are in reverse look
	 * 
	 * @return void
	 */
	public void reverseNewGame(){
		jLabel.setVisible(false);
		jPanel.remove(jLabel);
		Chess.setSTART_X(558-25-57);//setting the error of the corner index
		Chess.setSTART_Y(620-25-57);
		Chess.setUNIT(-57);		
		newGame();
		jLabel.setVisible(true);
		jPanel.add(jLabel,null);
		canGo = false;
		changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
		timeThread.start();
	}
	
	
	/**
	 * start a new game
	 */
	public void newGame(){
		//1. clear the content in "red", "black" and "cb"
		if (red != null) {//clear the red side
			ListIterator<Chess> lit = red.getChess().listIterator();
			while (lit.hasNext()) {
				Chess c = (Chess)lit.next();
				jPanel.remove(c);
			}
			red = null;
		}	
		if (black != null) {//clear the black side
			ListIterator<Chess> lit = black.getChess().listIterator();
			while (lit.hasNext()) {
				Chess c = (Chess)lit.next();
				jPanel.remove(c);
			}
			black = null;
		}
		
		if (cb != null) {//null
			cb = null;
		}
		
		// 2. read the image folder to get all Chess name list
		// and assign the new value for "red", "black", and "cb".
		// and draw chess on the chess board.
		String[] str = readImageFolder("images/");//read the image into the chessBoard
		
		
		black = new ChessGroup(str, "black",Point);//read black side into the chessBoard
		drawChess(black);//display
		red = new ChessGroup(str, "red",Point);//read red side into the chessBoard
		drawChess(red);//display
		
		cb = new ChessBoard(9, 10);//get the chessBoard
		cb.SetPositionTaken(black);
		cb.SetPositionTaken(red);
		
		// 3. start the ChessDestory thread
		//detectChessDestroy = null;
		detectChessDestroy = new ChessDestroy(red, black);//run
		isOver = false;	
		RedPlay = false;
		
		
		
  }

	public void disableChosen(ChessGroup cg) {
		Iterator<Chess> it = cg.getChess().iterator();
		while (it.hasNext()) {
			Chess c = (Chess)it.next();
			if (c.isChosen())
				c.setChosen(false);
		}
	}

	public boolean findChosen(ChessGroup cg) {
		Iterator<Chess> it = cg.getChess().iterator();
		boolean flag = false;
		while (it.hasNext()) {
			Chess c = (Chess)it.next();
			if (c.isChosen()) {
				cg.setIndex(c.getId());
				flag = true;
			}
		}
		return flag;
	}
	
	
	public static String[] readImageFolder(String url){
		String tmp = new String();
		String[] str = null;
		Pattern p = Pattern.compile("[a-z]{3,5}-[a-z]{2,5}.gif");
		HashSet<String> hs = new HashSet<String>();
		File f = new File(url);
		if(!f.exists()|| !f.isDirectory()){
			System.out.println("please input an valid directory name!");
			return null;
		}
		File[] fArray = f.listFiles();
		for(int i=0; i<fArray.length; i++){
			if(fArray[i].isFile()){
				tmp = fArray[i].getName();
				Matcher m = p.matcher(tmp);
				if(m.matches()){
					hs.add(tmp);
				}
			}
		}
		if(str == null){
			str = new String[hs.size()];
			Iterator<String> it = hs.iterator();
			int j = 0;
			while(it.hasNext()){
				tmp = it.next();
				str[j] = new String(tmp);
				j++;
			}
		}
		return str;
	}
	

	
	public void drawChess(ChessGroup cg){	
		Iterator<Chess> it = cg.getChess().iterator();
		while(it.hasNext()){
			Chess c = it.next();
			jPanel.add(c,null);
		}
	}
	

	/**
	 * This method is used to convert the coordinates of mouse click to be the {row, column} pair on the chess board
	 * @param mx: horizontal coordinate of mouse click
	 * @param my: vertical coordinate of mouse click
	 * @return: return {-1,-1} if the mouse click outside the chess board, or the position is too far away from the intersection point on the chess board.
	 * otherwise, it will return the {row, column} pair.
	 */
	public int[] convertIndex(int mx, int my){
	    // 0. local variables declaration
		int[] back= new int[2];//make an array
		int range = 15;	
		int index_x = -1;//initialize the value
		int index_y = -1;	
		int start_x = 51;//the beginning position
		int start_y = 53;		
		int reminder_x = (mx - start_x)%Chess.getUNIT();
		int reminder_y = (my - start_y)%Chess.UNIT;
		
		// 1. if mouse clicking at the outside of the chess board, return {-1,-1}
		// write your code here!
		if ((mx < start_x - range) || (mx > FRAME_WIDTH - start_x + range) || (my < start_y - range) || (my > FRAME_HEIGHT - 2*start_y - range)) {//the situation
			back[0] = -1;//out of the chess board
			back[1] = -1;
			return back;
		}
		
		// 2. if mouse clicking position is within 15x15 area which has one intersection as its center, then return the {row, column} pair of that intersection point.
		// otherwise, return {-1,-1}.
		// only response in 10 X 10 local area
		// write your code here!
		if (((reminder_x <= range) || (reminder_x >= Chess.UNIT - range)) && ((reminder_y <= range) || (reminder_y >= Chess.UNIT - range)))
		{
			if (reminder_x <= range)
				index_x = (mx - start_x) / Chess.UNIT;
			else if (reminder_x >= Chess.UNIT - range) {
				index_x = (mx - start_x + range) / Chess.UNIT;
			}
			back[0] = index_x;//get the value of different position
      
			if (reminder_y <= range)
				index_y = (my - start_y) / Chess.UNIT;
			else if (reminder_y >= Chess.UNIT - range) {
				index_y = (my - start_y + range) / Chess.UNIT;
			}
			back[1] = index_y;//get the value of different position

			return back;
		}
		else{
			back[0] = -1;
			back[1] = -1;
			return back;
		}	
	}
	
	/**
	 * This method is used to convert the coordinates of mouse click to be the {row, column} pair on the chess board for the slave side
	 * it will reverse the mouse click coordinate to the normal coordinate
	 * @param mx: horizontal coordinate of mouse click
	 * @param my: vertical coordinate of mouse click
	 * @return: return {-1,-1} if the mouse click outside the chess board, or the position is too far away from the intersection point on the chess board.
	 * otherwise, it will return the {row, column} pair.
	 */
	public int[] reverseConvertIndex(int mx, int my){
	    // 0. local variables declaration
		int[] back= new int[2];//make an array
		int range = 15;
		int unit = 57;
		
		int index_x = -1;//initialize the value
		int index_y = -1;
		
		int start_x = 51;//the beginning position
		int start_y = 53;
		
		int reminder_x = (558-(mx + start_x))%unit;
		int reminder_y = (620-(my + start_y))%unit;
		
		// 1. if mouse clicking at the outside of the chess board, return {-1,-1}
		// write your code here!
		if ((mx < start_x - range) || (mx > FRAME_WIDTH - start_x + range) || (my < start_y - range) || (my > FRAME_HEIGHT - 2*start_y - range)) {//the situation
			back[0] = -1;//out of the chess board
			back[1] = -1;
			return back;
		}
		
		// 2. if mouse clicking position is within 15x15 area which has one intersection as its center, then return the {row, column} pair of that intersection point.
		// otherwise, return {-1,-1}.
		// only response in 10 X 10 local area
		// write your code here!
		if (((reminder_x <= range) || (reminder_x >= unit - range)) && ((reminder_y <= range) || (reminder_y >= unit - range)))
		{
			if (reminder_x <= range)
				index_x = (558-(mx + start_x)) / unit;
			else if (reminder_x >= unit - range) {
				index_x = ((558-(mx + start_x)) + range) / unit;
			}
			back[0] = index_x;//get the value of different position
      
			if (reminder_y <= range)
				index_y = (620-(my + start_y)) / unit;
			else if (reminder_y >= unit - range) {
				index_y = ((620-(my + start_y)) + range) / unit;
			}
			back[1] = index_y;//get the value of different position

			return back;
		}
		else{
			back[0] = -1;
			back[1] = -1;
			return back;
			
		}	
	}
	
	/**
	 * Inner class. Used to respond mouse click on the Panel
	 *
	 */
	class Moniter extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {
			
			
			// 1. get the mouse clicking coordinates, and convert it to {row, column} pair
			int[] temp = new int[2];
			//1.1 if it is the slave side, force to convert to the true clicking coordinates
			if(isMaster){
				temp = convertIndex(e.getX(),e.getY());//get the position

			}else{
				temp = reverseConvertIndex(e.getX(),e.getY());
			}
			int transportX = temp[0];
			int transportY = temp[1];
			
			// 2. if tmp dose not equal to {-1,-1}, we will continue with our work
				// 2.1. if it is red side's turn to move, and the given destination follows the rule of playing chess, the red chess will move
			    // otherwise, it will stop blinking.
			    // change the value of "RedPlay" to indicate the other side is going to move now
			    // change the displayed message on the panel
			    // write your code here!
			if ((temp[0] != -1) && (temp[1] != -1)){
				if (RedPlay) {
					System.out.println("red side walks!");
					
					//Calculte how many chess have been chosen for each side
					black.CalIndex();
					red.CalIndex();
					
					//If there is a chess chosen, than we will calculate whether it can move and make changes according to the chess rules
					if (red.getIndex() != -1) {
						Chess c = (Chess)red.getChess().get(red.getIndex());//get the position
						int chessIndex = red.getIndex();
						int old_x = c.getIndX();
						int old_y = c.getIndY();
						//check whether the red chess can move according to the chess rule
						ChessRule.AllRules(cb, c, temp[0], temp[1]);
						
						red.disableChosen();
						
						//If the chess can really move, than the control was given to the other side of player and print the information
						//to advertise it should be the black side's term
						if ((old_x != c.getIndX()) || (old_y != c.getIndY())) {
							
							RedPlay = false;//change the player's side, let black chess move
							canGo = false;
							changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
							Movement mov = new Movement(chessIndex, transportX, transportY);
							DataPackage dp = new DataPackage(mov, UserController.getInstance().getUser(), PlayerController.getInstance().getUser(), ActionType.MOVE);
							ClientController.getInstance().sendData(dp);
							countDown = 30;
							//2.2 as there is no chess be destroyed, the eatenChess is null and we use a movedChess to store the moved chess
							eatenChessPre = eatenChess;
							eatenChess = null;
							movedChessPre = movedChess;
							movedChess = c;
						}
					}
					if (black.getIndex() != -1){
						black.disableChosen();
					}
				}
				// 2.1. if it is black side's turn to move, and the given destination follows the rule of playing chess, the black chess will move
			    // otherwise, it will stop blinking.
			    // change the value of "RedPlay" to indicate the other side is going to move now
			    // change the displayed message on the panel
			    // write your code here!
				else{
					System.out.println("black side walks!");

					//Calculate how many chess have been chosen for each side
					black.CalIndex();
					red.CalIndex();
					
					//If a red chess is chosen, then it should be changed status into unchosen, because now it is the black side's term
					if (red.getIndex() != -1){
						red.disableChosen();
					}
					
					//If a black chess is chosen, then it should check whether this chess can move by the chess rule
					if (black.getIndex() != -1) {
						Chess c = (Chess)black.getChess().get(black.getIndex());//get the position
						int chessIndex = black.getIndex();
						int old_x = c.getIndX();
						int old_y = c.getIndY();
	             
						ChessRule.AllRules(cb, c, temp[0], temp[1]);//check the rule
	                
						black.disableChosen();
						if ((old_x != c.getIndX()) || (old_y != c.getIndY())) {
							RedPlay = true;//change the value
							
							canGo = false;
							changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
							Movement mov = new Movement(chessIndex, transportX, transportY);
							DataPackage dp = new DataPackage(mov, UserController.getInstance().getUser(), PlayerController.getInstance().getUser(), ActionType.MOVE);
							ClientController.getInstance().sendData(dp);
							countDown = 30;
							//2.2 as there is no chess be destroyed, the eatenChess is null and we use a movedChess to store the moved chess
							eatenChessPre = eatenChess;
							eatenChess = null;
							movedChessPre = movedChess; 
							movedChess = c;
							//2.3 after the move, we can used the play back for one step
						}
					}
					if (red.getIndex() != -1){
						red.disableChosen();
					}
	          	}
	  		}
		}
	}
	
	/**
	 * This method is used to get to movement from the opponent and move the chess on the current side
	 * @param chessIndex: the index of the moved chess
	 * @param transportX: The horizontal index of the chess' destination
	 * @param transportY: The vertical index of the chess' destination
	 * @return: void
	 */
	public void getMove(int chessIndex, int transportX, int transportY) {
		//reset time
		countDown = 30;
		// 2.1. if it is red side's turn to move, and the given destination follows the rule of playing chess, the red chess will move
		// otherwise, it will stop blinking.
		// change the value of "RedPlay" to indicate the other side is going to move now
		// change the displayed message on the panel
		// write your code here!
		if (RedPlay) {
			System.out.println("red side walks!");
				Chess c = (Chess)red.getChess().get(chessIndex);//get the chess we need to move
				int old_x = c.getIndX();
				int old_y = c.getIndY();
				//check whether the red chess can move according to the chess rule
				ChessRule.GetRules(cb, c, transportX, transportY);						
				red.disableChosen();
						
				//to advertise it should be the black side's term
				if ((old_x != c.getIndX()) || (old_y != c.getIndY())) {
					RedPlay = false;//change the player's side, let black chess move
					//2.2 as there is no chess be destroyed, the eatenChess is null and we use a movedChess to store the moved chess
					eatenChessPre = eatenChess;
					eatenChess = null;
					movedChessPre = movedChess;
					movedChess = c;
					//2.4 set player can more the chess when receive command
					canGo = true;
					changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
				}
		}else{ //If it is black side to walk
			System.out.println("black side walks!");
			Chess c = (Chess)black.getChess().get(chessIndex);//get the position
			int old_x = c.getIndX();
			int old_y = c.getIndY();
			ChessRule.GetRules(cb, c, transportX, transportY);//check the rule	                
			black.disableChosen();
			if ((old_x != c.getIndX()) || (old_y != c.getIndY())) {
					RedPlay = true;//change the value
					//2.2 as there is no chess be destroyed, the eatenChess is null and we use a movedChess to store the moved chess
					eatenChessPre = eatenChess;
					eatenChess = null;
					movedChessPre = movedChess; 
					movedChess = c;
					//2.3 after the move, we can used the play back for one step
					canGo = true;
					changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
			}					
	    }
	  		
	}
	
	/**
	 * Inner class to handle chess destroy
	 */
	class ChessDestroy implements Runnable{
		
		private ChessGroup cg1 = null;
		private ChessGroup cg2 = null;
		
		
		ChessDestroy(ChessGroup cg1,ChessGroup cg2 ){
			this.cg1 = cg1;
			this.cg2 = cg2;
			threadChessDestroy = new Thread(this);
			(threadChessDestroy).start();//two thread start to check the situation of two chosen chess
		}
		
		public void run() {
			while(!isOver){
			// 0, if it is not the turn to play, disable all chosen
			if(!canGo){
				red.disableChosen();
				black.disableChosen();
			}
				
			// 1, count how many chess have been chosen for each side 
			this.cg1.CalIndex();
			this.cg2.CalIndex();
			
			// 2. if the number of chosen chess for one side is larger than 1, then we will change the status of these chess from chosen to be unchosen
			// actually, after changing the status of chess to be unchosen, those chess will stop blinking.
			if (this.cg1.getNumTaken() > 1) {//check the number of chosen chess
				this.cg1.disableChosen();
			}
			
			if (this.cg2.getNumTaken() > 1) {
				this.cg2.disableChosen();
			}
			
			// 3. if the number of chosen chess for both side equals to 1, we need to consider eating chess problem
			// 3.1. if it is red side's turn to move, then the chosen chess from black side will be eaten. 
			// And the movement of the chess should following the rule of playing this chess.
			// 3.2. if it is black side's turn to move, then the chosen chess from red side will be eaten.
			// And the movement of the chess should following the rule of playing this chess.			
				if (RedPlay) {//If it is the term for red side to play
					//In red chess's term, if no red chess has been chosen, no black chess can be chosen
					if(cg1.getNumTaken() != 1){
						cg2.disableChosen();
					}
					
					//In the normal situation
					if ((this.cg1.getNumTaken() == 1) && (this.cg2.getNumTaken() == 1)){
					Chess r = (Chess)this.cg1.getChess().get(this.cg1.getIndex());//chess r is the chosen red chess
					Chess b = (Chess)this.cg2.getChess().get(this.cg2.getIndex());//chess b is the chosen black chess, in this term, the eaten one 
					int hitChess = this.cg1.getIndex(), destroyedChess = this.cg2.getIndex();
					//Set the index of the black chess as the target index 
					int x = b.getIndX();
					int y = b.getIndY();
					//set the index of the red chess as the source index
					int old_x = r.getIndX();
					int old_y = r.getIndY();
					
					//We will use the chess rule to determine whether the destroy of chess can take place
					if (r.getRank().equals("pao"))
						ChessRule.cannonRule(cb, r, b);
					//If the general move into the same column and there is no chess between them, the latter coming general will dead
					else if(r.getRank().equals("jiang") && b.getRank().equals("jiang")){
						ChessRule.generaFlylRule(cb, r, b);
					}
					else{
						ChessRule.AllRules(cb, r, x, y);
					}
					
					// 3.3. after eating chess, you should do some clean up work
					//3.3.1 remove the eaten chess from the chess board
					//3.3.2 change the value of "RedPlay" to indicate the other side is going to move now
					//3.3.3 change the displayed message on the panel
					// write your code here!
					if ((old_x != r.getIndX()) || (old_y != r.getIndY())) {//eat and print code 
						//We use b to represent the eaten chess for the play back function
						eatenChessPre = eatenChess;
						eatenChess = b;
						//Make changes for the black side to walk
						RedPlay = false; 
						
						canGo = false;
						changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
						Destroy des = new Destroy(hitChess, destroyedChess);
						DataPackage dp = new DataPackage(des, UserController.getInstance().getUser(), PlayerController.getInstance().getUser(), ActionType.DESTROY);
						ClientController.getInstance().sendData(dp);
						countDown = 30;
						
						b.dead();
						//Remove the black chess component from the chess board panel
						jPanel.remove(b);
						//we use the eatenChess and the movedChess to store the chess eaten or moved
						movedChessPre = movedChess;
						movedChess = r;
						}
					}
				 }
				 else {//If it is the term for black side to play
					//In red chess's term, if no red chess has been chosen, no black chess can be chosen
					if(cg2.getNumTaken() != 1){
						cg1.disableChosen();
					}
					//In the normal situation
					if ((this.cg1.getNumTaken() == 1) && (this.cg2.getNumTaken() == 1)){
					Chess r = (Chess)this.cg1.getChess().get(this.cg1.getIndex());//chess r is the chosen red chess, in this term, the eaten one 
					Chess b = (Chess)this.cg2.getChess().get(this.cg2.getIndex());//chess b is the chosen black chess
					int hitChess = this.cg2.getIndex(), destroyedChess = this.cg1.getIndex();
					//Set the index of the red chess as the target index 
					int x = r.getIndX();
					int y = r.getIndY();
					//set the index of the black chess as the source index
					int old_x = b.getIndX();
					int old_y = b.getIndY();
					//We will use the chess rule to determine whether the destroy of chess can take place
					if (b.getRank().equals("pao"))
						ChessRule.cannonRule(cb, b, r);
					//If the general move into the same column and there is no chess between them, the latter coming general will dead
					else if(b.getRank().equals("jiang") && r.getRank().equals("jiang")){
						ChessRule.generaFlylRule(cb, b, r);
					}
					else{
						ChessRule.AllRules(cb, b, x, y);
					}
					
					if ((old_x != b.getIndX()) || (old_y != b.getIndY())) {//eat and print code
						//Remove the red chess component from the chess board panel
						eatenChessPre = eatenChess;
						eatenChess = r;
						//Make changes for the red side to walk
						RedPlay = true;
						//give the turn to the opponent
						canGo = false;
						changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
						Destroy des = new Destroy(hitChess, destroyedChess);
						DataPackage dp = new DataPackage(des, UserController.getInstance().getUser(), PlayerController.getInstance().getUser(), ActionType.DESTROY);
						ClientController.getInstance().sendData(dp);		
						countDown = 30;
						r.dead();
						//Remove the red chess component from the chess board panel
						jPanel.remove(r);				
						//we use the eatenChess and the movedChess to store the chess eaten or moved
						movedChessPre = movedChess;
						movedChess = b;
						
					}
					}
				 }
					
			// 4. judge cg1 and cg2 still alive or not. if one of the side doesn't alive any more, a pop out dialogue will show up.
			// after you click the dialogue, the program will start a new game turn .
			// write your code here!
			//If one of the chess group die, then the game is over
			if ((!this.cg1.isAlive()) || (!this.cg2.isAlive())) {
				isOver = true;//game over
				stopTime();
				//If the red chess died, then a message dialog will jump out to show us the red loses, and game is over 
				if (!this.cg1.isAlive()) {
					if (isMaster) 
						JOptionPane.showMessageDialog(null, "Congats, You Won!", "Game Over!", -1);		
					else 
						JOptionPane.showMessageDialog(null, "Unfortunately ,You Lose!", "Game Over!", -1);
				 } 
				 else{//If the black chess died, then a message dialog will jump out to show us the black loses, and game is over 
					 if (isMaster) 
						 JOptionPane.showMessageDialog(null, "Congats, You Won!", "Game Over!", -1);		
					else 
						JOptionPane.showMessageDialog(null, "Unfortunately ,You Lose!", "Game Over!", -1);
				 }	
				int option = JOptionPane.showConfirmDialog(null, "Do you want to rematch?", "Rematch", JOptionPane.YES_NO_OPTION);
//				if (option == JOptionPane.YES_OPTION) 
//					ClientController.getInstance().sendData(new DataPackage(true, ActionType.REMATCH));
//				else 
//					ClientController.getInstance().sendData(new DataPackage(false, ActionType.REMATCH));
			 }
				
			 try{//check the sentence
				Thread.sleep(500L);
			 }
			 catch (InterruptedException e) {
				e.printStackTrace();
			 	}
			}		
		}
	}
	
	/**
	 * This method is used to get the chess eaten movement from the opponent and move the chess on the current side
	 * @param cg1: the chess group of the current side, usually it is the red side
	 * @param cg2: the chess group of the current side, usually it is the black side
	 * @param hitChess: the index of the hit Chess
	 * @param destroyedChess: the index of the destroyedChess
	 * @return: void
	 */
	public void getDestroy(int hitChess, int destroyedChess){
		//reset time
		countDown = 30;
		//  if it is red side's turn to move, then the chosen chess from black side will be eaten. 
		// And the movement of the chess should following the rule of playing this chess.
		//  if it is black side's turn to move, then the chosen chess from red side will be eaten.
		// And the movement of the chess should following the rule of playing this chess.		
			if (RedPlay) {//If it is the term for red side to play
				Chess r = (Chess)red.getChess().get(hitChess);//chess r is the chosen red chess
				Chess b = (Chess)black.getChess().get(destroyedChess);//chess b is the chosen black chess, in this term, the eaten one 
				//Set the index of the black chess as the target index 
				int x = b.getIndX();
				int y = b.getIndY();
				//set the index of the red chess as the source index
				int old_x = r.getIndX();
				int old_y = r.getIndY();			
				//We will use the chess rule to determine whether the destroy of chess can take place
				if (r.getRank().equals("pao"))
					ChessRule.cannonRule(cb, r, b);
				//If the general move into the same column and there is no chess between them, the latter coming general will dead
				else if(r.getRank().equals("jiang") && b.getRank().equals("jiang")){
					ChessRule.generaFlylRule(cb, r, b);
				}
				else{
					ChessRule.GetRules(cb, r, x, y);
				}			
				// 3.3. after eating chess, you should do some clean up work
				//3.3.1 remove the eaten chess from the chess board
				//3.3.2 change the value of "RedPlay" to indicate the other side is going to move now
				//3.3.3 change the displayed message on the panel
				// write your code here!
				if ((old_x != r.getIndX()) || (old_y != r.getIndY())) {//eat and print code 
					//We use b to represent the eaten chess for the play back function
					eatenChessPre = eatenChess;
					eatenChess = b;
					//Make changes for the black side to walk
					RedPlay = false; 
					canGo = true;
					changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
					b.dead();
					//Remove the black chess component from the chess board panel
					jPanel.remove(b);
					//we use the eatenChess and the movedChess to store the chess eaten or moved
					movedChessPre = movedChess;
					movedChess = r;
					}
				
			 }
			 else {//If it is the term for black side to play

				Chess r = (Chess)red.getChess().get(destroyedChess);//chess r is the chosen red chess, in this term, the eaten one 
				Chess b = (Chess)black.getChess().get(hitChess);//chess b is the chosen black chess
				//Set the index of the red chess as the target index 
				int x = r.getIndX();
				int y = r.getIndY();
				//set the index of the black chess as the source index
				int old_x = b.getIndX();
				int old_y = b.getIndY();
				//We will use the chess rule to determine whether the destroy of chess can take place
				if (b.getRank().equals("pao"))
					ChessRule.cannonRule(cb, b, r);
				//If the general move into the same column and there is no chess between them, the latter coming general will dead
				else if(b.getRank().equals("jiang") && r.getRank().equals("jiang")){
					ChessRule.generaFlylRule(cb, b, r);
				}
				else{
					ChessRule.GetRules(cb, b, x, y);
					}
				
				if ((old_x != b.getIndX()) || (old_y != b.getIndY())) {//eat and print code
					//Remove the red chess component from the chess board panel
					eatenChessPre = eatenChess;
					eatenChess = r;
					//Make changes for the red side to walk
					RedPlay = true;
					canGo = true;
					changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
					r.dead();
					//Remove the red chess component from the chess board panel
					jPanel.remove(r);			
					//we use the eatenChess and the movedChess to store the chess eaten or moved
					movedChessPre = movedChess;
					movedChess = b;
				}					 
			}	
		//  judge cg1 and cg2 still alive or not. if one of the side doesn't alive any more, a pop out dialogue will show up.
		// after you click the dialogue, the program will exit.
		// write your code here!
		//If one of the chess group die, then the game is over
		if ((!red.isAlive()) || (!black.isAlive())) {
			stopTime();
			isOver = true;//game over
			//If the red chess died, then a message dialog will jump out to show us the red loses, and game is over 
			if (!red.isAlive()) {
				if (isMaster) 
				JOptionPane.showMessageDialog(this, "Congats, You Won!", "Game Over!", -1);		
			else 
				JOptionPane.showMessageDialog(this, "Unfortunately ,You Lose!", "Game Over!", -1);
		 } 
			else {//If the black chess died, then a message dialog will jump out to show us the black loses, and game is over 
				if (isMaster) 
					JOptionPane.showMessageDialog(this, "Congats, You Won!", "Game Over!", -1);		
				else 
					JOptionPane.showMessageDialog(this, "Unfortunately ,You Lose!", "Game Over!", -1);
			}
			int option = JOptionPane.showConfirmDialog(null, "Do you want to rematch?", "Rematch", JOptionPane.YES_NO_OPTION);
//			if (option == JOptionPane.YES_OPTION) 
//				ClientController.getInstance().sendData(new DataPackage(true, ActionType.REMATCH));
//			else 
//				ClientController.getInstance().sendData(new DataPackage(false, ActionType.REMATCH));
		 }
	}
	
	
	
	 public void changeSideFlag(){
		String yourSide = null;
		String yourTurn = null;
		if(isMaster){
			yourSide = new String("You are Black side\n");
		}else{
			yourSide = new String("You are Red side\n");
		}
		 if(canGo){
			 yourTurn = new String("Now is Your turn to play!\n");
		 }else{
			 yourTurn = new String("Please wait for your opponent to move.\n");
		 }
		 sideFlag.setText(yourTurn + yourSide);
	 }
	 
	 /**
		 * the method tell the  whether the game is over, it is used for disabling choosing chess when the game is over 
		 */
	 public static boolean getIsOver(){
		 return isOver;
		 
	 }
	 
	 public static void main(String[] args) {
		 new MainFrame().setVisible(true);
	 }
	

	 
	 
	 
	
	
	
} 


