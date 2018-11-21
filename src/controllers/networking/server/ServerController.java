package controllers.networking.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import controllers.ActionType;
import controllers.DAO;
import controllers.LoginController;
import controllers.RegisterController;
import models.DataPackage;
import models.Game;
import models.Record;
import models.Rematch;
import models.User;
import utils.ServerConfig;
import models.Client;

public class ServerController {
	private ServerSocket server;
	private int port = ServerConfig.PORT;
	
	private List<Client> clients = Collections.synchronizedList(new ArrayList<Client>());
	private List<Game> games = Collections.synchronizedList(new ArrayList<Game>());
	
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
				clients.add(new Client(null, oos, ois));
				for (;;) {
					try {
						DataPackage dp = (DataPackage) ois.readObject();
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
						if (dp.getActionType() == ActionType.REMATCH) {
							handleRematch(dp);
						}
						if (dp.getActionType() == ActionType.MOVE) {
							handleMovement(dp);
						}
						if (dp.getActionType() == ActionType.DESTROY) {
							handleDestruction(dp);
						}
						if (dp.getActionType() == ActionType.EXIT) {
							handleExit(dp);
						}
						if (dp.getActionType() == ActionType.END_GAME) {
							handleEndGame(dp);
						}
					} catch (ClassNotFoundException ex) {
							ex.printStackTrace();
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
				boolean isOK = addUserDAO((User)dp.getData());
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
		
		
		private synchronized void handleSignIn(DataPackage dp) {
			User u = (User)dp.getData();
			try {
				DataPackage re = getUserDAO(u);
				if (re != null) {
					oos.reset();
					oos.writeObject(re);
					addUser(u, oos);
					handleUpdateRank();
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
						c.getOutputStream().reset();
						c.getOutputStream().writeObject(new DataPackage(data, ActionType.MOVE));
					} catch (IOException ex) {
						System.err.println(ex);
					}
				}
			}
		}
		
		public void handleExit(DataPackage dp) {
			User receiver = dp.getReceiver();
			User sender = dp.getSender();
			Iterator<Client> iter = clients.iterator();
			try {
				while (iter.hasNext()) {
					Client c = iter.next();
					if (c.getUser().getUsername().equals(receiver.getUsername())) {
						c.getUser().setBusy(false);
						c.getOutputStream().reset();
						c.getOutputStream().writeObject(dp);		
					}
					else if (c.getUser().getUsername().equals(sender.getUsername()))
						c.getUser().setBusy(false);
				}
			handleUpdateTable();
			} catch (IOException ex) {
				System.err.println(ex);	
			}
		}
		
		private void handleResponseInvitation(DataPackage dp) {
			User sender = dp.getSender();
			User receiver = dp.getReceiver();
			Boolean data = (Boolean) dp.getData();
			Iterator<Client> iter = clients.iterator();
			if (!data)
				while (iter.hasNext()) {
					Client c = iter.next();
					if (c.getUser().getUsername().equals(receiver.getUsername())) {
						try {
							c.getOutputStream().reset();
							c.getOutputStream().writeObject(new DataPackage(data, sender, receiver, ActionType.RESPONSE_INVITATION));
						} catch (IOException ex) {
							System.err.println(ex);
						}
					}
				}
			else {
				handleChangeStatus(dp);
				handleNewGame(dp);
			}
		}

		private void handleNewGame(DataPackage dp) {
			User sender = dp.getSender();
			User receiver = dp.getReceiver();
			Iterator<Client> iter = clients.iterator();
			Game game;
			if (sender.getUsername().compareTo(receiver.getUsername()) < 0)
				game = new Game(sender, receiver);
			else
				game = new Game(receiver, sender);
			game.setPlayFirst(sender.getUsername());
			games.add(game);
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser().getUsername().equals(receiver.getUsername()) || 
						c.getUser().getUsername().equals(sender.getUsername()))
						try {
							c.getOutputStream().reset();
							c.getOutputStream().writeObject(new DataPackage(game, ActionType.NEW_GAME));
						} catch (IOException ex) {
							System.err.println(ex);
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
						c.getOutputStream().reset();
						c.getOutputStream().writeObject(new DataPackage(sender, receiver, ActionType.SEND_INVITATION));
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
						c.getOutputStream().reset();
						c.getOutputStream().writeObject(new DataPackage(data, ActionType.DESTROY));
					} catch (IOException ex) {
						System.err.println(ex);
					}
				}
			}
		}
		
		private void handleChangeStatus(DataPackage dp) {
			User u_1 = dp.getSender();
			User u_2 = dp.getReceiver();
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (u_1.getUsername().equals(c.getUser().getUsername())) 
					c.getUser().setBusy(!c.getUser().isBusy());
				if (u_2.getUsername().equals(c.getUser().getUsername())) 
					c.getUser().setBusy(!c.getUser().isBusy());
			}
			handleUpdateTable();
		}
		private void handleRematch(DataPackage dp) {
			Rematch rm = (Rematch) dp.getData();
			Game game = null;
			if (rm.isRematch()) {
				for (Game g : games) {
					if (g.getId() == rm.getGameId()) {
						game = g;
						if (g.getP1Rematch() == -1) g.setP1Rematch(1);
						else g.setP2Rematch(1);
					}
				}
			}
			Iterator<Client> iter = clients.iterator();
			if (game.getP1Rematch() == 1 && game.getP2Rematch() == 1) {
				game.switchPlayer();
				game.reset();
				while (iter.hasNext()) {
					Client c = iter.next();
					if (c.getUser().getUsername().equals(game.getPlayer1().getUsername()) ||
							c.getUser().getUsername().equals(game.getPlayer2().getUsername()))
						try {
							c.getOutputStream().reset();
							c.getOutputStream().writeObject(new DataPackage(game, ActionType.NEW_GAME));
						} catch (IOException e) {
							e.printStackTrace();
						}
				}
			}
//			else {
//				DataPackage dp2 = new DataPackage(game.getPlayer1(), game.getPlayer2(), ActionType.CHANGE_STATUS);
//				handleChangeStatus(dp2);
//			}
		}
		
		
		//updating table when someone change 
		private synchronized void handleUpdateTable() {
			System.out.println("update table");
			ArrayList<User> users = new ArrayList<User>();
			for (Client c : clients) {
				if (c.getUser() != null) 
					users.add(c.getUser());
			}
			try {
				DataPackage dp = new DataPackage(users, ActionType.UPDATE);
				for (Client c : clients) {
					c.getOutputStream().reset();
					c.getOutputStream().writeObject(dp);
				}
			} catch (IOException ex) {
				System.err.println(ex);
			}
		}
		
		private void handleEndGame(DataPackage dp) {
			Record record = (Record) dp.getData();
			Iterator<Game> iter = games.iterator();
			Game game = null;
			while (iter.hasNext()) {
				Game g = iter.next();
				if (g.getId() == record.getId()) {
					game = g;
					if (g.getPlayer1().getUsername().equals(record.getUser().getUsername())) {
						g.setP1Moved(record.getMove());
						if (record.isWin())
							g.setWinner(record.getUser().getUsername());
					}
					else {
						g.setP2Moved(record.getMove());
						if (record.isWin())
							g.setWinner(record.getUser().getUsername());
					}
				}
			}
			String user1 = game.getPlayer1().getUsername();
			String user2 = game.getPlayer2().getUsername();
			String loser = (user1.equals(game.getWinner())) ? user2 : user1;
			Iterator<Client> iter2 = clients.iterator();
			while (iter2.hasNext()) {
				Client c = iter2.next();
				if (c.getUser().getUsername().equals(game.getWinner())) {
					c.getUser().win();
				}
			}
			handleUpdateUser(game.getWinner());
			handleUpdateTable();
			if (game.getP1Moved() != -1 && game.getP2Moved() != -1) {
				DAO dao = new DAO();
				//insert into games
				String sql = "INSERT INTO games (user1, user2, winner) VALUES (?, ?, ?)";
				Object[] params;
				params = new Object[] {user1, user2, game.getWinner()};
				dao.executeUpdate(sql, params);
				//update users
				sql = "UPDATE users SET win=win+1 WHERE username=?";
				params = new Object[] {game.getWinner()};
				dao.executeUpdate(sql, params);
				sql = "UPDATE users SET lose=lose+1 WHERE username=?";
				params = new Object[] {loser};
				dao.executeUpdate(sql, params);
				//get ID game
				int gameId = -1;
				sql = "SELECT * FROM games WHERE (time=(SELECT MAX(time) FROM games WHERE (user1=? AND user2=?)))";
				params = new Object[] {user1, user2};
				ResultSet rs = dao.executeQuery(sql, params);
				try {
					rs.next();
					gameId = rs.getInt("gameId");
					//insert moves into record
					sql = "INSERT INTO records (gameId, user, move) VALUES (?, ?, ?)";
					params = new Object[] {gameId, user1, game.getP1Moved()};
					dao.executeUpdate(sql, params);
					params = new Object[] {gameId, user2, game.getP2Moved()};
					dao.executeUpdate(sql, params);
				} catch (SQLException e) {
					e.printStackTrace();
				}
				dao.closeConnection();
			}
			handleUpdateRank();
		}
		
		private void handleUpdateUser(String username) {
			Iterator<Client> iter = clients.iterator();
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getUser().getUsername().equals(username))
					try {
						c.getOutputStream().writeObject(new DataPackage(c.getUser(), ActionType.UPDATE_USER));
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		private void handleUpdateRank() {
			ArrayList<User> users = new ArrayList<User>();
			DAO dao = new DAO();
			String sql = "SELECT * FROM users";
			Object[] params = null;
			ResultSet rs = dao.executeQuery(sql);
			try {
				while (rs.next()) {
					User user = new User(rs.getString("username"));
					user.setGameDraw(rs.getInt("draw"));
					user.setGameWin(rs.getInt("win"));
					user.setGameLose(rs.getInt("lose"));
					sql = "SELECT SUM(move) FROM games g INNER JOIN records r ON g.gameid = r.gameId AND g.winner = r.user WHERE r.user = ?";
					params = new Object[] {user.getUsername()};
					ResultSet rs2 = dao.executeQuery(sql, params);
					rs2.first();
					user.setMoveWin(rs2.getInt("SUM(move)"));
					System.out.println(user.getMoveWin());
					sql = "SELECT SUM(move) FROM records WHERE user=?";
					rs2 = dao.executeQuery(sql, params);
					rs2.first();
					user.setMoveLose(rs2.getInt("SUM(move)") - user.getMoveWin());
					users.add(user);
				}
				Iterator<Client> iter = clients.iterator();
				while (iter.hasNext()) {
					Client c = iter.next();
					for (User u : users) {
						if (u.getUsername().equals(c.getUser().getUsername()))
							c.setUser(u);
					}
				}
				dao.closeConnection();
				DataPackage dp = new DataPackage(users, ActionType.UPDATE_RANK);
				for (Client c : clients) {
					c.getOutputStream().reset();
					c.getOutputStream().writeObject(dp);
				}
			} catch (SQLException | IOException e) {
				e.printStackTrace();
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
				if (c.getOutputStream().equals(oos)) {
					iter.remove();
				}
			}
		}
		
		public boolean addUserDAO(User u) {
			//connect DB
			DAO dao = new DAO();
			String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
			Object[] params = new Object[] {u.getUsername(), u.getPassword()};
			int count = dao.executeUpdate(sql, params);
			dao.closeConnection();
			if (count > 0)
				return true;
			return false;
		}
		
		
		public DataPackage getUserDAO(User u) {
			DAO dao = new DAO();
			DataPackage dp = null;
			try {
				String sql = "SELECT * FROM users WHERE username=? AND password=?";
				Object[] params = new Object[] {u.getUsername(), u.getPassword()};
				ResultSet rs = dao.executeQuery(sql, params);
				if (rs.first())	{
					u.setGameLose(rs.getInt("lose"));
					u.setGameWin(rs.getInt("win"));
					u.setGameDraw(rs.getInt("draw"));
					dp = new DataPackage(u, ActionType.SIGN_IN);
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			dao.closeConnection();
			return dp;
		}
		
		
		
		private synchronized void addUser(User u, ObjectOutputStream oos) {
			Iterator<Client> iter = clients.iterator();
			
			while (iter.hasNext()) {
				Client c = iter.next();
				if (c.getOutputStream().equals(oos)) {
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