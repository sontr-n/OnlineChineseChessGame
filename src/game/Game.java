package game;

public class Game {
	public MainFrame mf;
	private Game() {
		mf = new MainFrame();
		mf.setVisible(true);
	}
	
	private static final Game instance = new Game();
	
	public static final Game getInstance() {
		return instance;
	}
}
