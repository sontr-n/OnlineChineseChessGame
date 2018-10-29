package game;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import controllers.ActionType;
import controllers.PlayerController;
import controllers.UserController;
import controllers.networking.ClientController;
import game.ChessBoard.ChessPoint;
import models.DataPackage;
import models.Destroy;
import models.Movement;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


import java.util.*;
import java.text.DateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;




public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	public static final int FRAME_WIDTH = 565;
	public static final int FRAME_HEIGHT = 700;
	public static final Point POINT = new Point(10,400);
	private static final Point Point = null;
	public static String ipAddress = null;
	public static String port = null;
	private Image offScreenImage = null;
	private JPanel jContentPane = null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;
	private JPanel jPanel2 = null;
	private JPanel jPanel3 = null;
	private JPanel jPanel4 = null;
	private JPanel jPanel5 = null;
	private JTextArea stepShow = null;
	private JTextArea chatShow = null;
	private JTextArea sideFlag = null;//This is a text area which shows the side of the player and who is in turn now.
	private JScrollPane stepScroll = null;
	private JScrollPane chatScroll = null;
	private JTextField chatInput = null;
	private JButton jButton = null;
	private JButton jButton1 = null;
	private JButton jButtonSaveStep = null;
	private JButton jButtonSaveChat = null;
	private JButton jButtonSentChat = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	// "red" is used for red side chess
	private ChessGroup red = null; 
	// "black" is used for black side chess
	private ChessGroup black = null; 
	// "cb" is used for chess board
	private ChessBoard cb = null;  
	// for judging whether it's red side's turn to move now 
	private boolean RedPlay = false;
	// for judging game over
	private static boolean isOver = false;
	//public boolean isClick = false;
	Chess eatenChess;//A chess to store the dead one
	Chess eatenChessPre;//A chess to store the dead chess two steps earlier
	Chess movedChess;//A chess to store the moved one
	Chess movedChessPre;//A chess to store the moved chess two steps earlier
	int canPlayBack=0;//if there is a move,a play back step is allowed
	boolean canGo = false;//if it is true, than it is the player's turn
	public boolean isMaster = false;//if player is the master side, the first attacker
	boolean callNewGame = true;
	Thread getThread = null;//a thread to get the information from the next side
	Thread threadChessDestroy = null;//a thread to detect if it can do chess eaten move or one side is lose
	ChessDestroy detectChessDestroy = null;

		
		
	/**
	 * This is the default constructor
	 */
	public MainFrame() {
		super();
		initialize();
		// for double buffering
		new Thread(new PaintThread()).start();
		
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(900, 730);//565
		this.setContentPane(getJContentPane());
		this.setJMenuBar(createMenuBar());
		this.setTitle("[Chinese Chess] Designed by Project Group 11/ FALL 2011");
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
	 * This method create the controlling menuBar
	 * There is three menu in the menuBar, the Connect, Game Control and Save Documents
	 * In connect, you can choose menuItem start Connection or disconnect Connection
	 * In Game Control, you can choose New Game or Play Back for one step
	 * In Save Documents, you can save the step information or save the chat information 
	 * @return JMenuBar
	 */
	public JMenuBar createMenuBar(){
		JMenuBar menuBar;
		JMenu gameControl, saveDoc;
		JMenuItem tryNewGame, tryPlayBack, saveStep, saveChat;
		
		gameControl = new JMenu("Game Control");
		tryNewGame = new JMenuItem("New Game");
		tryNewGame.addActionListener(new NewGame());
		gameControl.add(tryNewGame);
		tryPlayBack = new JMenuItem("Play Back");
		tryPlayBack.addActionListener(new PlayBack());
		gameControl.add(tryPlayBack);
		
		
		saveDoc = new JMenu("Save Documents");
		saveStep =new JMenuItem("Save Steps");
		saveStep.addActionListener(new SaveStep());
		saveDoc.add(saveStep);
		saveChat = new JMenuItem("Save Charts");
		saveChat.addActionListener(new SaveChats());
		saveDoc.add(saveChat);
		
		
		menuBar = new JMenuBar();
		menuBar.add(gameControl);
		menuBar.add(saveDoc);
		
		return menuBar;
	}
	
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getJPanel1(), null);
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
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(384, 2, 171, 36));
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(0, 622, 558, 41));
			jPanel1.add(getJButton(), null);
			jPanel1.add(getJButton1(), null);
			jPanel1.add(jLabel1, null);
		}
		return jPanel1;
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
			jPanel2.setBounds(new Rectangle(582, 0, 318, 700));
			jPanel2.add(getJPanel3(), null);
			jPanel2.add(getJPanel4(), null);
			jPanel2.add(getJPanel5(), null);
		}
		return jPanel2;
	}
	/**
	 * This method initializes jPanel3
	 * It will add the Text Area sideFlag, which shows the side of the player and the current turn
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel3(){
		if(jPanel3 == null) {
			jPanel3 = new JPanel();
			jPanel3.setLayout(null);
			sideFlag = new JTextArea("Welcome to the Chinese Chess Game!\n",2, 20);
			sideFlag.setEditable(false);
			sideFlag.setBounds(new Rectangle(0, 5, 300, 36));
			jPanel2.add(sideFlag, null);
			jPanel3.setBounds(new Rectangle(0, 0, 318, 50));
		}
		return jPanel3;
	}
	
	/**
	 * This method initializes jPanel4
	 * It will add Text Area stepShow to itself, which can show the step information and some helpful advice
	 * A button can be used to save the information in the text Area
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel4(){
		if(jPanel4 == null) {
			jPanel4 = new JPanel();
			jPanel4.setLayout(null);
			stepShow = new JTextArea("This is A step Show \n", 5, 20);
			stepShow.setLineWrap(true);
			stepShow.setEditable(false);
			stepScroll = new JScrollPane(stepShow);
			stepScroll.setBounds(new Rectangle(0, 0, 300, 228));
			jPanel4.add(stepScroll, null);
			
			jButtonSaveStep = new JButton("Save Steps");
			jButtonSaveStep.setBounds(180, 235, 119,37);
			jButtonSaveStep.addActionListener(new SaveStep());
			jPanel4.add(jButtonSaveStep);
			
			jPanel4.setBounds(new Rectangle(0, 50, 318, 280));
			
		}
		return jPanel4;
	}
	
	/**
	 * This method initializes jPanel5
	 * Create a JPanel with a chat board and a input line
	 * A button can be use to sent the chat information to the other side, another button can be use to save the chat information
	 * @return javax.swing.JPanel
	 */
	private JPanel getJPanel5(){
		if(jPanel5 == null) {
			jPanel5 = new JPanel();
			jPanel5.setLayout(null);
			
			chatShow = new JTextArea("This is a chat board\nPleace click \"Start Connect\" to line up the server,\nand then click \"new game\" to start\n", 5, 20);
			chatShow.setEditable(false);
			chatShow.setLineWrap(true);
			chatScroll = new JScrollPane(chatShow);
			chatScroll.setBounds(new Rectangle(0, 0, 300, 233));
			jPanel5.add(chatScroll, null);
			
			chatInput = new JTextField("Please input chat, English only!", 20);
			chatInput.setEditable(true);
			chatInput.setBounds(new Rectangle(0, 240, 300, 45));
			jPanel5.add(chatInput, null);
			
			jButtonSentChat = new JButton("Sent Chats");
			jButtonSentChat.addActionListener(new SentChat());
			jButtonSentChat.setBounds(new Rectangle(180, 288, 119, 37));
			jPanel5.add(jButtonSentChat);
			
			jButtonSaveChat = new JButton("Save Chats");
			jButtonSaveChat.addActionListener(new SaveChats());
			jButtonSaveChat.setBounds(55, 288, 119,37);
			jPanel5.add(jButtonSaveChat);
			
			jPanel5.setBounds(new Rectangle(0, 335, 318, 330));
			
		}
		return jPanel5;
	}
	
	/**
	 * This method initializes jButton	
	 * 	The button is used to start a new turn of game
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(3, 2, 123, 37));
			jButton.setText("New Game");
			jButton.addActionListener(new NewGame());
		}
		return jButton;
	}
	
	/**
	 * This ActionListener Class implement the function of sending new game apply to another side player
	 * 
	 *
	 */
	private class NewGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
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
		stepShow.append("This is Black side\n");
		canGo = true;
		changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
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
		stepShow.append("This is Red side\n");
		canGo = false;
		changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
	}
	
	private class SentChat implements ActionListener{
		public void actionPerformed(ActionEvent e) {
        }	
	}
	
	/**
	 * This ActionListener Class implement the function of saving step information
	 * it will save the text on the step board to a file named by the current time 
	 *
	 */
	private class SaveStep implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			PrintWriter print = null;
			Date date = new Date(); 
			DateFormat mediumDateFormat = DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.MEDIUM); 
			String timeNow = mediumDateFormat.format(date);
			String[] splitTimeNow = timeNow.split("\\ ");
			String[] splitHour = splitTimeNow[1].split("\\:");
			String timeIn = new String(splitTimeNow[0]+"-"+splitHour[0]+"-"+splitHour[1]+"-"+splitHour[2]+"-");
			String sideText = null;
			if(isMaster){
				sideText = new String("BlackSide-");
			}else{
				sideText = new String("RedSide-");
			}
			String str = stepShow.getText();
			String fileName = new String(sideText+timeIn+"StepRecord" + ".txt");
			File f = new File(fileName);
			
			if(!f.exists()){
				try{
					f.createNewFile();
				}catch(IOException er){
					er.printStackTrace();
				}
			}
			
			try{       
	      		print = new PrintWriter( new BufferedWriter( new FileWriter( fileName ) ), true );
	      		stepShow.append("The chat information has been saved\nin the root direction at\n"+ timeNow + "\n");
	    	}catch ( IOException iox ){
	      		System.out.println("Problem writing " + fileName );
	    	}
	    	
	    	print.print(str);
	    	print.close();
        }	
	}
	
	/**
	 * This ActionListener Class implement the function of saving chat information
	 * it will save the text on the chat board to a file named by the current time 
	 *
	 */
	private class SaveChats implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			PrintWriter print = null;
			Date date = new Date(); 
			DateFormat mediumDateFormat = DateFormat.getDateTimeInstance( DateFormat.MEDIUM, DateFormat.MEDIUM); 
			String timeNow = mediumDateFormat.format(date);
			String[] splitTimeNow = timeNow.split("\\ ");
			String[] splitHour = splitTimeNow[1].split("\\:");
			String timeIn = new String(splitTimeNow[0]+"-"+splitHour[0]+"-"+splitHour[1]+"-"+splitHour[2]+"-");
			String sideText = null;
			if(isMaster){
				sideText = new String("BlackSide-");
			}else{
				sideText = new String("RedSide-");
			}
			String str = chatShow.getText();
			String fileName = new String(sideText+timeIn+"ChatRecord" + ".txt");
			File f = new File(fileName);
			
			if(!f.exists()){
				try{
					f.createNewFile();
				}catch(IOException er){
					er.printStackTrace();
				}
			}
			
			try{       
	      		print = new PrintWriter( new BufferedWriter( new FileWriter( fileName ) ), true );
	      		chatShow.append("The chat information has been saved\nin the root direction at\n"+ timeNow + "\n");
	    	}catch ( IOException iox ){
	      		System.out.println("Problem writing " + fileName );
	    	}
	    	
	    	print.print(str);
	    	print.close();
        }	
	}

	/**
	 * This method initializes jButton1	
	 * 	The button is used to use play back function, it will set message for agreement for play pack move
	 * @return javax.swing.JButton	
	 */
	private JButton getJButton1() {
		if (jButton1 == null) {
			jButton1 = new JButton();
			jButton1.setBounds(new Rectangle(128, 2, 123, 37));
			jButton1.setText("Play Back");
			jButton1.addActionListener(new PlayBack());
		}
		return jButton1;
	}
	
	/**
	 * This ActionListener Class implement the function of sending Play Back apply
	 * it will sent the apply to the opponent through the chess server
	 *
	 */
	private class PlayBack implements ActionListener{
		public void actionPerformed(ActionEvent e) {
        }		
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
		for (int i = 0; i < str.length; i++) {
			System.out.println(str[i]);
		}
		
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
		
		//4. disable the play back function when new game start
		canPlayBack = 0;
		
		//5. Show on the step board that new game have start
		stepShow.append("A new game turn has started\n");
		
		System.out.println("isOver is "+ isOver);
		
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
	
	/**
	 * This method do play back work, 
	 *it will read the last two step of moved chess and chess been eaten, and recover them
	 */
	public void playback(){
		//0. decide whether we can take a play back
		if(canPlayBack>=2){
			//1. set all the chess unchosen
			red.disableChosen();
			black.disableChosen();
			jLabel.setVisible(false);
			jPanel.remove(jLabel);
			
			//2. If a chess is eaten, recover it
			if(eatenChess != null){		
				System.out.println("Try to recover the dead chess");
				//Now we add the eaten chess on top of the chess board label						
				jPanel.add(eatenChess);
				eatenChess.setVisible(true);
				eatenChess.setAlive(true);
				eatenChess.draw();
				//We set the chess point taken by the recovered chess 
				ListIterator<ChessPoint> lit = cb.chessPoints.listIterator();
				while(lit.hasNext()){
					ChessPoint cp = lit.next();						
						if(cp.getX() == eatenChess.getIndX() && cp.getY() == eatenChess.getIndY()){
							cp.setTaken(true);
							break;
						}	
				}
			}		
			//3. Pull back the moved chess
			int tmp_indX = movedChess.getIndX();
			int tmp_indY = movedChess.getIndY();
			movedChess.setIndX(movedChess.getPre_x());
			movedChess.setIndY(movedChess.getPre_y());
			movedChess.setPre_x(tmp_indX);
			movedChess.setPre_y(tmp_indY);
			ChessRule.ChangeStatus(cb, movedChess, movedChess.getIndX(), movedChess.getIndY());
			movedChess.draw();		
			//4. If there is a chess eaten two steps earlier, try to recover it
			if(eatenChessPre != null){		
				System.out.println("Try to recover the dead chess");
				//Now we add the eaten chess on top of the chess board label					
				jPanel.add(eatenChessPre);
				eatenChessPre.setVisible(true);
				eatenChessPre.setAlive(true);
				eatenChessPre.draw();							
				//We set the chess point taken by the recovered chess 
				ListIterator<ChessPoint> lit = cb.chessPoints.listIterator();
				while(lit.hasNext()){
					ChessPoint cp = lit.next();						
						if(cp.getX() == eatenChessPre.getIndX() && cp.getY() == eatenChessPre.getIndY()){
							cp.setTaken(true);
							break;
						}	
				}
			}		
			//5. Pull back the moved chess two steps earlier
			tmp_indX = movedChessPre.getIndX();
			tmp_indY = movedChessPre.getIndY();
			movedChessPre.setIndX(movedChessPre.getPre_x());
			movedChessPre.setIndY(movedChessPre.getPre_y());
			movedChessPre.setPre_x(tmp_indX);
			movedChessPre.setPre_y(tmp_indY);
			ChessRule.ChangeStatus(cb, movedChessPre, movedChessPre.getIndX(), movedChessPre.getIndY());
			movedChessPre.draw();
			jPanel.add(jLabel,null);
			jLabel.setVisible(true);

		}
		//Erase two steps from the step board
		eraseStep();	
		//Every time we have clicked a play back, no second play back movement can be taken before the second move
		canPlayBack=0;
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
	 * @author Louis
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
							//2.2 as there is no chess be destroyed, the eatenChess is null and we use a movedChess to store the moved chess
							eatenChessPre = eatenChess;
							eatenChess = null;
							movedChessPre = movedChess;
							movedChess = c;
							//2.3 after the move, we can used the play back for one step
							canPlayBack++;
							//Append the step on the step board
							moveStep(c, c.getIndX(), c.getIndY());
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
							
							//2.2 as there is no chess be destroyed, the eatenChess is null and we use a movedChess to store the moved chess
							eatenChessPre = eatenChess;
							eatenChess = null;
							movedChessPre = movedChess; 
							movedChess = c;
							//2.3 after the move, we can used the play back for one step
							canPlayBack++;
							//Append the step on the step board
							moveStep(c, c.getIndX(), c.getIndY());
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
					//2.3 after the move, we can used the play back for one step					
					canPlayBack++;
					//2.4 set player can more the chess when receive command
					canGo = true;
					changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
					//Append the step on the step board
					reverseMoveStep(c, c.getIndX(), c.getIndY());
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
					canPlayBack++;
					//2.4 set player can more the chess when receive command
					canGo = true;
					changeSideFlag();//Set information for the label on the upper-right corner to acknowledge the player to move
					//Append the step on the step board
					reverseMoveStep(c, c.getIndX(), c.getIndY());
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
			// TODO Auto-generated method stub
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
						
						
						b.dead();
						//Remove the black chess component from the chess board panel
						jPanel.remove(b);
						//we use the eatenChess and the movedChess to store the chess eaten or moved
						movedChessPre = movedChess;
						movedChess = r;
						//after a move, a step of play back is allowed
						canPlayBack++;
						//put the destroyed step on the step board
						destroyedStep(r,b);
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
				System.out.println("Game over!");
				//If the red chess died, then a message dialog will jump out to show us the red loses, and game is over 
				if (!this.cg1.isAlive()) {
					JOptionPane.showMessageDialog(null, "Red side LOSE", "Game Over!", -1);		
					stepShow.append("The Black Side has win the game!\n");
					chatShow.append("The Black Side has win the game!\n");
					sideFlag.setText("The Black Side wins, Game is over!\nPlease click \"New Game\" to start a new turn");
				 } 
				 else{//If the black chess died, then a message dialog will jump out to show us the black loses, and game is over 
					JOptionPane.showMessageDialog(null, "Black side LOSE", "Game Over!", -1);
					stepShow.append("The Red Side has win the game!\n");
					chatShow.append("The Red Side has win the game!\n");
					sideFlag.setText("The Red Side wins, Game is over!\nPlease click \"New Game\" to start a new turn");
				 }	
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
					//after a move, a step of play back is allowed
					canPlayBack++;
					//put the destroyed step on the step board
					reverseDestroyedStep(r,b);
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
					//after a move, a step of play back is allowed
					canPlayBack++;		
					//put the destroyed step on the step board
					reverseDestroyedStep(b,r);
				}					 
			}	
		//  judge cg1 and cg2 still alive or not. if one of the side doesn't alive any more, a pop out dialogue will show up.
		// after you click the dialogue, the program will exit.
		// write your code here!
		//If one of the chess group die, then the game is over
		if ((!red.isAlive()) || (!black.isAlive())) {
			isOver = true;//game over
			System.out.println("Game over!");
			//If the red chess died, then a message dialog will jump out to show us the red loses, and game is over 
			if (!red.isAlive()) {
				JOptionPane.showMessageDialog(null, "Red side LOSE", "Game Over!", -1);
				stepShow.append("The Black Side has win the game\n");
				chatShow.append("The Black Side has win the game\n");
				sideFlag.setText("The Black Side wins, Game is over!\nPlease click \"New Game\" to start a new turn");
				//System.exit(0);//the system will exit safely
			 } 
			 else{//If the black chess died, then a message dialog will jump out to show us the black loses, and game is over 
				JOptionPane.showMessageDialog(null, "Black side LOSE", "Game Over!", -1);
				stepShow.append("The Red Side has win the game!\n");
				chatShow.append("The Red Side has win the game!\n");
				sideFlag.setText("The Red Side wins, Game is over!\nPlease click \"New Game\" to start a new turn");				
				//System.exit(0);//the system will exit safely
			 }	
		 }
	}
	
	/**
	 * the method print the chess movement on the step board
	 */
	public void moveStep(Chess c, int x, int y){
		String name = c.getName();
		String[] splitName = name.split("\\.");
		String str = new String(splitName[0] + " " + c.getPre_x()+ " " + c.getPre_y() + " go to " + x + " " + y);
		stepShow.append(str + "\n");
	}
	
	/**
	 * the method print the chess movement on the step board for the slave side
	 */
	public void reverseMoveStep(Chess c, int x, int y){
		String name = c.getName();
		String[] splitName = name.split("\\.");
		String str = new String(splitName[0] + " " + (8-c.getPre_x())+ " " + (9-c.getPre_y()) + " go to " + (8-x) + " " + (9-y));
		stepShow.append(str + "\n");
	}
	/**
	 * the method print the chess destroyed movement on the step board 
	 */
	public void destroyedStep(Chess hitC, Chess eatC){
		String hitName = hitC.getName();
		String[] splitHitName = hitName.split("\\.");
		String eatName = eatC.getName();
		String[] splitEatName = eatName.split("\\.");
		String str = new String(splitHitName[0] + " " + hitC.getPre_x()+ " " + hitC.getPre_y() + " destroyed " + splitEatName[0] + " " + hitC.getIndX()+ " " + hitC.getIndY());
		stepShow.append(str + "\n");
	}

	/**
	 * the method print the chess destroyed movement on the step board for the slave side
	 */
	public void reverseDestroyedStep(Chess hitC, Chess eatC){
		String hitName = hitC.getName();
		String[] splitHitName = hitName.split("\\.");
		String eatName = eatC.getName();
		String[] splitEatName = eatName.split("\\.");
		String str = new String(splitHitName[0] + " " + (8-hitC.getPre_x())+ " " + (9-hitC.getPre_y()) + " destroyed " + splitEatName[0] + " " + (8-hitC.getIndX())+ " " + (9-hitC.getIndY()));
		stepShow.append(str + "\n");
	}
	
	/**
	 * when the play back movement is taken, the method clean the last two step on the step board
	 */
	public void eraseStep(){
		String origin = stepShow.getText();
		String[] steps = origin.split("\\\n");
		int  stepSize = steps.length;
		stepShow.setText("");
		for(int i = 0; i < stepSize-2; i++){
			stepShow.append(steps[i]+"\n");
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
	 
	

	 
	 
	 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainFrame mf = new MainFrame();
		mf.setVisible(true);
		
	}
	
	
} 


