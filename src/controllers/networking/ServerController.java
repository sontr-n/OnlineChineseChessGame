package controllers.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import controllers.ActionType;
import controllers.HomeController;
import controllers.LoginController;
import models.DataPackage;
import models.User;
import utils.ServerConfig;

public class ServerController {
	private ServerSocket server;
	private int port = ServerConfig.getInstance().PORT;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	
	
	public ServerController() {
		//opening connection
		open();
		
		for (;;) {
			listening();
		}
	}
	
	public void open() {
		
		try {
			server = new ServerSocket(port);
			if (server != null) System.out.println("Connecting on port: " + port);
			else System.out.println("somthing wrong");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
	}
	
	//
	private void handleSignIn(DataPackage dp) {
		System.out.println("sign in");
		User u = (User)dp.getData();
		try {
				boolean isCorrect = LoginController.getInstance().checkUser((User)dp.getData());
				if (isCorrect)
					oos.writeObject(new DataPackage(new Boolean(true), ActionType.SIGN_IN));
				else 
					oos.writeObject(new DataPackage(new Boolean(false), ActionType.SIGN_IN));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	public void listening() {
		Socket client;
		try {
			client = server.accept();
			ois = new ObjectInputStream(client.getInputStream());
			oos = new ObjectOutputStream(client.getOutputStream());
			for (;;) {
				try {
					DataPackage dp = (DataPackage)ois.readObject();
					if (dp.getActionType() == ActionType.SIGN_IN) {
						handleSignIn(dp);
					}
	//					
					//continue
					
	//				} catch (ClassNotFoundException ex) {
	//					ex.printStackTrace();
				} catch (Exception ex) {
					ex.printStackTrace();
					break;
				}
			}
			client.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} 
	}

	
}
