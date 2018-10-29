/**
 * ChessRule class
 * This class is used to define the rule on playing Chinese Chess
 * @author Louis
 * 
 */
package game;

public class ChessRule {
	/**
	 * Rule for playing soldier("bing"). Please read this code carefully
	 * @param cb : Chess Board object
	 * @param c : Chess object
	 * @param x : horizontal position of the destination
	 * @param y : vertical position of the destination
	 */
	public static void soldierRule(ChessBoard cb, Chess c, int x, int y){
		if(c.getRank().equals("bing")){
			int index_x = c.getIndX();
			int index_y = c.getIndY();
			int diff_x = Math.abs(x - index_x);
			int diff_y = Math.abs(y - index_y);
			// only one step is allowed for arms
			if((diff_x + diff_y)== 1){
				// for upper red side
				if(c.getSuit() == Chess.Suits.Red){
					// must move forward
					if( y < index_y){
						c.setChosen(false);
						return;
					}else{
						if( index_y <=4 && diff_x == 1){ //must move forward before cross the river
							c.setChosen(false);
							return;
						}else{
							ChangeStatus(cb, c, x, y);
						}
					}
				}// for below black side
				else{
					// must move forward
					if( y > index_y){
						c.setChosen(false);
						return;
					}else{
						if( index_y >=5 && diff_x == 1){ //must move forward before cross the river
							c.setChosen(false);
							return;
						}else{
							ChangeStatus(cb, c, x, y);
						}
					}
				}
			}else{
				System.out.println("give the wrong destination to soldier");
				c.setChosen(false);
			}
			
		}else{
			System.out.println("apply wroing rule on soldier!");
		}
	}
	
	
	/**
	 * Rule for playing chariot("ju") or cannon("pao"). 
	 * Read the "Rule of Playing Chinese Chess" carefully, and try to find the rule by yourself.
	 * In this code, we need to consider the case that a Chess may be blocked by another one.
	 * @param cb: Chess Board object
	 * @param c: Chess object
	 * @param x: horizontal position of the destination
	 * @param y: vertical position of the destination
	 */
	public static void chariotRule(ChessBoard cb, Chess c, int x, int y){
		if(c.getRank().equals("ju")||c.getRank().equals("pao")){
			int index_x = c.getIndX();
			int index_y = c.getIndY();
			int diff_x = Math.abs(x - index_x);
			int diff_y = Math.abs(y - index_y);
			// must walk straight
			if( diff_x ==0 || diff_y == 0){
				// haven't considered the block case
				if(diff_x == 0){ // walk vertically
					int min_y = Math.min(index_y, y);
					int max_y = Math.max(index_y, y);
					boolean flag = false; // the position is not taken by default 
					for(int i= min_y+1; i<max_y; i++){
						if(isBlocked(cb,x,i)){
							flag = true;
						}
					}
					if(flag == true){
						System.out.println("the vertical path is blocked");
						c.setChosen(false);
						return;
					}
					else{
						ChangeStatus(cb, c, x, y);
					}
				}
				else{ // walk horizontally
					
					int min_x = Math.min(index_x, x);
					int max_x = Math.max(index_x, x);
					boolean flag = false; // the position is not taken by default 
					for(int i= min_x+1; i<max_x; i++){
						if(isBlocked(cb,i,y)){
							flag = true;
						}
					}
					if(flag == true){
						System.out.println("the horizontal path is blocked");
						c.setChosen(false);
						return;
					}
					else{
						ChangeStatus(cb, c, x, y);
					}
				}
				
			}else{
				System.out.println("give the wrong destination to chariot/cannons");
				c.setChosen(false);
			}
		}else{
			System.out.println("apply wroing rule on chariot/cannon!");
			c.setChosen(false);
		}
	}
	
	/**
	 * Rule for using cannon("pao") to eat chess from other side. 
	 * Read the "Rule of Playing Chinese Chess" carefully, and try to find the rule by yourself.
	 * In this code, we need to consider under which situation cannon can eat the other chess
	 * the other chess been eaten will be removed from the chess board, and cannon will take its position
	 * @param cb: Chess Board object
	 * @param c: Chess object
	 * @param cd: the other Chess object
	 */
	public static void cannonRule(ChessBoard cb, Chess c, Chess cd){
		// write your code here!
		if (c.getRank().equals("pao")) {//check the name of chess
			int index_x = c.getIndX();//the beginning place
			int index_y = c.getIndY();
		      
			int x = cd.getIndX();//the destination place
			int y = cd.getIndY();
		      
			int diff_x = Math.abs(x - index_x);//the middle range of the beginning and destination
			int diff_y = Math.abs(y - index_y);

			if ((diff_x == 0) || (diff_y == 0)){
					if (diff_x == 0) {
						int min_y = Math.min(index_y, y);
						int max_y = Math.max(index_y, y);
						int numBlock = 0;
						boolean flag = false;//initialize the flag
						for (int i = min_y + 1; i < max_y; i++) {
							if (isBlocked(cb, x, i)) {
								numBlock++;
							}
						}
						System.out.println("apply cannon rule, and the numblock is:" + numBlock);
						if ((numBlock != 1) || (c.getSuit() == cd.getSuit())) {//the situation to check if cannon can move  
							flag = true;
						}
						if (flag) {
							System.out.println("the vertical path is blocked!");//other situation
							cd.setChosen(false);
							c.setChosen(false);
							return;
						}

						ChangeStatus(cb, c, x, y);
						c.draw();
						cd.dead();//finish and dead
					}
					else{
						int min_x = Math.min(index_x, x);
						int max_x = Math.max(index_x, x);
						boolean flag = false;
						int numBlock = 0;
						for (int i = min_x + 1; i < max_x; i++) {
							if (isBlocked(cb, i, y)) {
								numBlock++;
							}
						}
						System.out.println("apply cannon rule, and the numblock is:" + numBlock);
						if ((numBlock != 1) || (c.getSuit() == cd.getSuit())) {
							flag = true;
						}
						if (flag) {
							System.out.println("the horizontal path is blocked!");
							cd.setChosen(false);
							c.setChosen(false);
							return;
						}

						ChangeStatus(cb, c, x, y);
						c.draw();
						cd.dead();
					}
		    }
		    else{
		    	System.out.println("give the wrong destination to cannons");//the destination are wrong
		    	cd.setChosen(false);
				c.setChosen(false);
		    }
		} 
		else {
			System.out.println("apply wroing rule on cannon!");//wrong rule
			cd.setChosen(false);
			c.setChosen(false);
		}
	}
	
	/**
	 * Rule for playing elephant("xiang").
	 * Read the "Rule of Playing Chinese Chess" carefully, and try to find the rule by yourself.
	 * In this code, we need to consider the case that a Chess may be blocked by another one.
	 * Pay attention that elephant can not cross the river.
	 * Suppose Red Chess is put on the upper side of the chess board.
	 * @param cb: Chess Board object
	 * @param c: Chess object
	 * @param x: horizontal position of the destination
	 * @param y: vertical position of the destination
	 */
	public static void elephantRule(ChessBoard cb, Chess c, int x, int y){
		if(c.getRank().equals("xiang")){
			int index_x = c.getIndX();
			int index_y = c.getIndY();
			int diff_x = Math.abs(x - index_x);
			int diff_y = Math.abs(y - index_y);
			if(diff_x == 2 && diff_y == 2){
				// for red side
				if(c.getSuit() == Chess.Suits.Red){
					// elephant can not cross the river
					if( y<=4 ){
						int middle_x = (x + index_x)/2;
						int middle_y = (y + index_y)/2;
						// path is blocked
						if(!isBlocked(cb,middle_x,middle_y)){
							ChangeStatus(cb, c, x, y);
						}else{
							System.out.println("the path is blocked");
							c.setChosen(false);
							return;
						}
						
					}else{
						c.setChosen(false);
						return;
					}
				}else{ // for black side
					if( y>=5 ){
						int middle_x = (x + index_x)/2;
						int middle_y = (y + index_y)/2;
						// path is blocked
						if(!isBlocked(cb,middle_x,middle_y)){
							ChangeStatus(cb, c, x, y);
						}else{
							System.out.println("the path is blocked");
							c.setChosen(false);
							return;
						}
					}else{
						c.setChosen(false);
						return;
					}
				}
			}else{
				System.out.println("elephant can not move in that way!");
				c.setChosen(false);
			}
		}else{
			System.out.println("apply wroing rule on elephant!");
			c.setChosen(false);
		}

	}
	
	/**
	 * Rule for playing horse("ma").
	 * Read the "Rule of Playing Chinese Chess" carefully, and try to find the rule by yourself.
	 * In this code, we need consider the case that a Chess may be blocked by another one.
	 * @param cb: Chess Board
	 * @param c: Chess object
	 * @param x: horizontal position of the destination
	 * @param y: vertical position of the destination
	 */
	public static void horseRule(ChessBoard cb, Chess c, int x, int y){
		if (c.getRank().equals("ma")) {//judge if it is the Chess which we want
			int index_x = c.getIndX();//get the position of x
			int index_y = c.getIndY();//get the position of y
			
			int diff_x = Math.abs(x - index_x);//get the position of center
			int diff_y = Math.abs(y - index_y);
			//the pace of "ma" is moved by "ri"
					if((diff_x + diff_y) != 3){
						c.setChosen(false);
						System.out.println("give the wrong destination to horse");
						return;
					}
					else{	
						if(diff_x == 2){
							if(isBlocked(cb, (x+index_x)/2, index_y)){//check if there is a chess block off the "ma"
								c.setChosen(false);
								System.out.println("the path is blocked");
								return;
							}
							else{
								ChangeStatus(cb, c, x, y);
							}
						}
						if(diff_y == 2){
							if(isBlocked(cb, index_x, (y+index_y)/2)){//check if there is a chess block off the "ma"
								c.setChosen(false);
								System.out.println("the path is blocked");
								return;
							}
							else{
								ChangeStatus(cb, c, x, y);
							}
						}
					}					
		}
		else {
			System.out.println("apply wrong rule on horse!");
		}
	}
	
	/**
	 * Rule for playing advisor("shi").
	 * Read the "Rule of Playing Chinese Chess" carefully, and try to find the rule by yourself.
	 * Pay attention that the advisor can not go out of the Palace.
	 * Suppose Red Chess is put on the upper side of the chess board.
	 * @param cb: Chess Board object
	 * @param c: Chess object
	 * @param x: horizontal position of the destination
	 * @param y: vertical position of the destination
	 */
	public static void advisorRule(ChessBoard cb, Chess c, int x, int y){
		if(c.getRank().equals("shi")){
			int index_x = c.getIndX();
			int index_y = c.getIndY();
			int diff_x = Math.abs(x - index_x);
			int diff_y = Math.abs(y - index_y);
			if(diff_x == 1 && diff_y == 1){
				// for red side
				if(c.getSuit() == Chess.Suits.Red){
					// chap's movement is limited
					if(x>=3 && x<=5 && y>=0 && y<=2){
						ChangeStatus(cb, c, x, y);
					}else{
						c.setChosen(false);
						return;
					}
				}else{ // for black side
					if(x>=3 && x<=5 && y>=7 && y<=9){
						ChangeStatus(cb, c, x, y);
					}else{
						c.setChosen(false);
						return;
					}
				}
			}else{
				System.out.println("advisor can not move in that way!");
				c.setChosen(false);
			}
		}else{
			System.out.println("apply wroing rule on advisor!");
			c.setChosen(false);
		}
	}
	
	/**
	 * Rule for playing general("jiang").
	 * Read the "Rule of Playing Chinese Chess" carefully, and try to find the rule by yourself.
	 * Pay attention that the general can not go out of the Palace
	 * Suppose Red Chess is put on the upper side of the chess board.
	 * @param cb: Chess Board object
	 * @param c: Chess object
	 * @param x: horizontal position of the destination
	 * @param y: vertical position of the destination
	 */
	public static void generalRule(ChessBoard cb, Chess c, int x, int y){
		if(c.getRank().equals("jiang")){
			int index_x = c.getIndX();
			int index_y = c.getIndY();
			int diff_x = Math.abs(x - index_x);
			int diff_y = Math.abs(y - index_y);
			// only one step is allowed for arms
			if((diff_x + diff_y)== 1){
				// for red side
				if(c.getSuit() == Chess.Suits.Red){
					// chap's movement is limited
					if(x>=3 && x<=5 && y>=0 && y<=2){
						ChangeStatus(cb, c, x, y);
					}else{
						c.setChosen(false);
						return;
					}
				}else{ // for black side
					if(x>=3 && x<=5 && y>=7 && y<=9){
						ChangeStatus(cb, c, x, y);
					}else{
						c.setChosen(false);
						return;
					}
				}
			}else{
				System.out.println("general can not move in that way!");
				c.setChosen(false);
			}
		}else{
			System.out.println("apply wroing rule on general!");
			c.setChosen(false);
		}
	}
	
	public static void generaFlylRule(ChessBoard cb, Chess c, Chess cd){
		if(c.getRank().equals("jiang") && cd.getRank().equals("jiang")){
			int index_x = c.getIndX();//the beginning place
			int index_y = c.getIndY();
			
			int x = cd.getIndX();//the destination place
			int y = cd.getIndY();
			
			int diff_x = Math.abs(x - index_x);
			
			
			if (diff_x == 0) {
								int min_y = Math.min(index_y, y);
								int max_y = Math.max(index_y, y);
								int numBlock = 0;
								boolean flag = false;
								for (int i = min_y + 1; i < max_y; i++) {
									if (isBlocked(cb, x, i)) {
										numBlock++;
									}
								}
								System.out.println("apply general rule, and the numblock is:" + numBlock);
								if ((numBlock == 0) && (c.getSuit() != cd.getSuit())) {  
									flag = true;
								}
								if(flag){
								c.draw();
								cd.dead();//finish and dead
								}
			}
			else{
				System.out.println("general can not move in that way!");
				c.setChosen(false);
			}
		}
		else{
			System.out.println("apply wroing rule on general!");
			c.setChosen(false);
		}
		
	}
	
	public static boolean isBlocked(ChessBoard cb, int x, int y){
		boolean flag = false;
		flag = cb.isTaken(x, y);
		return flag;
	}
	
	
	public static void ChangeStatus(ChessBoard cb, Chess c, int x, int y){
		c.setChosen(false);
		c.setPre_x(c.getIndX());
		c.setPre_y(c.getIndY());
	    c.calPosition(x, y);
	    c.setIndX(x);
	    c.setIndY(y);
	    cb.ChangeTaken(c);
	}
	
	/**
	 * The function combine all rules together
	 * @param cb: Chess Board object
	 * @param c: Chess object
	 * @param x: horizontal position of the destination
	 * @param y: vertical position of the destination
	 */
	public static void AllRules(ChessBoard cb, Chess c, int x, int y){
		if(c.isChosen()){
			c.setChosen(false);
			if(c.getRank().equals("bing")){
				ChessRule.soldierRule(cb, c, x, y);
			}
			if(c.getRank().equals("shi")){
				ChessRule.advisorRule(cb, c, x, y);
			}
			if(c.getRank().equals("xiang")){
				ChessRule.elephantRule(cb, c, x, y);
			}
			if(c.getRank().equals("ma")){
				ChessRule.horseRule(cb, c, x, y);
			}
			if(c.getRank().equals("jiang")){
				ChessRule.generalRule(cb, c, x, y);
			}
			if(c.getRank().equals("pao")||c.getRank().equals("ju")){
				ChessRule.chariotRule(cb, c, x, y);
			}
			c.draw();
		}
	}
	
	public static void GetRules(ChessBoard cb, Chess c, int x, int y){
			c.setChosen(false);
			if(c.getRank().equals("bing")){
				ChessRule.soldierRule(cb, c, x, y);
			}
			if(c.getRank().equals("shi")){
				ChessRule.advisorRule(cb, c, x, y);
			}
			if(c.getRank().equals("xiang")){
				ChessRule.elephantRule(cb, c, x, y);
			}
			if(c.getRank().equals("ma")){
				ChessRule.horseRule(cb, c, x, y);
			}
			if(c.getRank().equals("jiang")){
				ChessRule.generalRule(cb, c, x, y);
			}
			if(c.getRank().equals("pao")||c.getRank().equals("ju")){
				ChessRule.chariotRule(cb, c, x, y);
			}
			c.draw();
		
	}
	
}
