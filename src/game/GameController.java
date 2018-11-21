package game;

public class GameController {
	public MainFrame mf;
	private GameController() {

	}
	
	public void newGame(int id) {
		mf = new MainFrame(id);
		mf.setVisible(true);
	}
	
	private static final GameController instance = new GameController();
	
	public void quit() {
		mf.isOver = true;
		mf.setVisible(false);
	}
	
	
	public static final GameController getInstance() {
		return instance;
	}
}
