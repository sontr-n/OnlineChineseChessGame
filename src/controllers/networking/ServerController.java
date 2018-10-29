package controllers.networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import controllers.ActionType;
import controllers.LoginController;
import controllers.RegisterController;
import models.DataPackage;
import models.Movement;
import models.User;
import utils.ServerConfig;
import models.Client;

public class ServerController {
	private ServerSocket server;
	private int port = ServerConfig.PORT;
	
//	private HashSet<ObjectOutputStream> players = new HashSet<ObjectOutputStream>();
	private List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
	private List<User> users = Collections.synchronizedList(new ArrayList<User>());
	
	public ServerController() {
		open();
		
		//listening
		for (;;) {
			try {
				new Handler(server.accept()).start();
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
	}
	
	//open connection 
	public void open() {
			
		try {
			server = new ServerSocket(port);
			if (server != null) System.out.println("Connecting on port: " + port);
			else System.out.println("somthing wrong");
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
			
	}	
	
	private class Handler extends Thread {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private Socket socket;
		
		public Handler(Socket s) {
			socket = s;
		}
		
		@Override
		public void run() {
			listening();
		}

		public void listening() {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				clients.add(new Client(null, oos));
				for (;;) {
					try {
						DataPackage dp = (DataPackage)ois.readObject();
						if (dp.getActionType() == ActionType.SIGN_IN) {
							handleSignIn(dp);
						}
						
						if (dp.getActionType() == ActionType.SIGN_UP) {
							handleSignUp(dp);
						}
						
						if (dp.getActionType() == ActionType.SIGN_OUT) {
							handleSignOut(dp);
						}
						
						if (dp.getActionType() == ActionType.RESPONSE_INVITATION) {
							handleResponseInvitation(dp);
						}
						
						if (dp.getActionType() == ActionType.SEND_INVITATION) {
							handleRequestInvitation(dp);
						}
						
						if (dp.getActionType() == ActionType.CHANGE_STATUS) {
							handleChangeStatus(dp);
						}
						
						if (dp.getActionType() == ActionType.NEW_GAME) {
							handleNewGame(dp);
						}
						
						if (dp.getActionType() == ActionType.MOVE) {
							handleMovement(dp);
						}
						
						if (dp.getActionType() == ActionType.DESTROY) {
							handleDestruction(dp);
						}
						
						
		//				} catch (ClassNotFoundException ex) {
		//					ex.printStackTrace();
					} catch (Exception ex) {
						System.err.println(ex);
						removeClient(oos);
						handleUpdateTable();
						break;
					}
				}
//				client.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} 
		}
		
		private void handleSignUp(DataPackage dp) {
			System.out.println("sign up");
			User u = (User)dp.getData();
			try {
					boolean isOK = RegisterController.getInstance().addUser((User)dp.getData());
					if (isOK) {
						addUser(u, oos);
						oos.reset();
						oos.writeObject(new DataPackage(new Boolean(true), ActionType.SIGN_UP));
						handleUpdateTable();
					}
					else {
						oos.reset();
						oos.writeObject(new DataPackage(new Boolean(false), ActionType.SIGN_UP));
					}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		//
		private synchronized void handleSignIn(DataPackage dp) {
			System.out.println(clients.size());
			System.out.println("sign in");
			User u = (User)dp.getData();
			try {
				DataPackage re = LoginController.getInstance().getUserDAO(u);
				if (re != null) {
					oos.reset();
					oos.writeObject(re);
					addUser(u, oos);
					handleUpdateTable();
				}
				else {
					oos.writeObject(new DataPackage(null, ActionType.SIGN_IN));
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		private void handleMovement(DataPackage dp) {
			User receiver = dp.getReceiver();
			Object data = dp.getData();
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser().getUsername().equals(receiver.getUsername())) {
					try {
						c.getStream().reset();
						c.getStream().writeObject(new DataPackage(data, ActionType.MOVE));
					} catch (IOException ex) {
						System.err.println(ex);
					}
				}
			}
		}
		
		private void handleResponseInvitation(DataPackage dp) {
			User sender = dp.getSender();
			User receiver = dp.getReceiver();
			Object data = dp.getData();
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser().getUsername().equals(receiver.getUsername())) {
					try {
						c.getStream().reset();
						c.getStream().writeObject(new DataPackage(data, sender, receiver, ActionType.RESPONSE_INVITATION));
					} catch (IOException ex) {
						System.err.println(ex);
					}
				}
			}
		}
		
		private void handleRequestInvitation(DataPackage dp) {
			User sender = dp.getSender();
			User receiver = dp.getReceiver();
			Iterator<Client> iter = clients.iterator();
			
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser().getUsername().equals(receiver.getUsername())) {
					try {
						c.getStream().reset();
						c.getStream().writeObject(new DataPackage(sender, receiver, ActionType.SEND_INVITATION));
					} catch (IOException ex) {
						System.err.println(ex);
					}
				}
			}
		}
		
		private void handleDestruction(DataPackage dp) {
			User receiver = dp.getReceiver();
			Object data = dp.getData();
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser().getUsername().equals(receiver.getUsername())) {
					try {
						c.getStream().reset();
						c.getStream().writeObject(new DataPackage(data, ActionType.DESTROY));
					} catch (IOException ex) {
						System.err.println(ex);
					}
				}
			}
		}
		
		private void handleChangeStatus(DataPackage dp) {
			User u_1 = dp.getSender();
			User u_2 = dp.getReceiver();
			for (User user : users) {
				if (u_1.getUsername().equals(user.getUsername())) 
					user.setBusy(!user.isBusy());
				if (u_2.getUsername().equals(user.getUsername())) 
					user.setBusy(!user.isBusy());
			}
			handleUpdateTable();

		}
		
		private void handleNewGame(DataPackage dp) {
			
		}
		
		
		//updating table when someone change 
		private synchronized void handleUpdateTable() {
			System.out.println("update table");
			users.clear();
			for (Client c : clients) {
				if (c.getUser() != null) 
					users.add(c.getUser());
			}
			try {
				DataPackage dp = new DataPackage(users, ActionType.UPDATE);
				for (Client c : clients) {
					c.getStream().reset();
					c.getStream().writeObject(dp);
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
		
		//removing signed out user from users 
		private void handleSignOut(DataPackage dp) {
			User u = (User)dp.getData();
			removeUser(u.getUsername());
			handleUpdateTable();
		}
		
		
		
		private synchronized void removeClient(ObjectOutputStream oos) {
			Iterator<Client> iter = clients.iterator();
			
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getStream().equals(oos)) {
					iter.remove();
				}
			}
		}
		
		
		
		private synchronized void addUser(User u, ObjectOutputStream oos) {
			Iterator<Client> iter = clients.iterator();
			
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getStream().equals(oos)) {
					c.setUser(u);
				}
			}
		}
		
		private synchronized void removeUser(String username) {
			Iterator<Client> iter = clients.iterator();
			
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser() != null) 
					if (c.getUser().getUsername().equals(username))
						c.setUser(null);
			}
		}
		
	}
	
	
}
