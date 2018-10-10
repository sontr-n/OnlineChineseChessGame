package utils;

public class ServerConfig {
	public static final int PORT = 9000;
	public static final String HOST = "localhost";
	
	private ServerConfig() {
		
	}
	
	public static final ServerConfig getInstance() {
		return new ServerConfig();
	}
	
}
