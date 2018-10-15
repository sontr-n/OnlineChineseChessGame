package controllers.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import models.DataPackage;
import utils.ServerConfig;

public class ClientController {
	private Socket socket;
	private int port = ServerConfig.PORT;
	private String host = ServerConfig.HOST;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	
	private ClientController() {
		openConnection();
	}
	
	
	private void openConnection() {
		try {
			socket = new Socket(host, port);
			if (socket != null) {
				System.out.println("Successful Connection");
				oos = new ObjectOutputStream(socket.getOutputStream());
				ois = new ObjectInputStream(socket.getInputStream());
			}
			else System.out.println("Failed Connection");
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	
	
	
	public DataPackage receiveData() {
		System.out.println("receive");
		try {
			DataPackage dp = (DataPackage)ois.readObject();
			return dp;
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	public void sendData(DataPackage dp) {
		System.out.println("send");
		try {
			oos.writeObject(dp);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}	
	
	private static ClientController instance = new ClientController();
	
	public static final ClientController getInstance() {
		return instance;
	}
	
}
