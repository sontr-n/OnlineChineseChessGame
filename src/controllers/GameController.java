package controllers;

import game.MainFrame;
import models.Game;

public class GameController {
	public MainFrame mf = null;
	private Game game;
	
	private GameController() {

	}
	
	public void newGame(Game g) {
		mf = new MainFrame();
		mf.setVisible(true);
	}
	
	public Game getGame() {
		return game;
	}
	
	private static final GameController instance = new GameController();
	
	public void quit() {
		mf.isOver = true;
		mf.dispose();
		mf = null;
	}
	
	
	public static final GameController getInstance() {
		return instance;
	}
}
