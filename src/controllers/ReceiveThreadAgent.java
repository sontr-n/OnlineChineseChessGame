package controllers;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import controllers.networking.client.ClientController;
import game.GameController;
import models.DataPackage;
import models.Destroy;
import models.Game;
import models.Movement;
import models.Record;
import models.User;

public class ReceiveThreadAgent extends Thread {
	List<User> users;
	@Override
	public void run() {
		for (;;) {
			DataPackage dp = ClientController.getInstance().receiveData();
			System.out.println(dp.getActionType());
			if (dp.getActionType() == ActionType.SIGN_IN) {
				if (dp.getData() != null) {
					LoginController.getInstance().showMessage("Sign In Successfully");
					LoginController.getInstance().hideView();
					
					UserController.getInstance().setLogedIn(true);
					UserController.getInstance().setUser((User)dp.getData());
					HomeController.getInstance().displayView();
					
				} else 
					LoginController.getInstance().showMessage("Invalid username/password");
			}
			
			if (dp.getActionType() == ActionType.SIGN_UP) {
				boolean isOK = (Boolean)dp.getData();
				if (isOK) {
					RegisterController.getInstance().showMessage("Sign Up Successfully");
					RegisterController.getInstance().hideView();
					
					UserController.getInstance().setUser(RegisterController.getInstance().getUser());
					
					HomeController.getInstance().displayView();
				}
				else 
					RegisterController.getInstance().showMessage("Sign Up Failed");
			}
			
			if (UserController.getInstance().getUser() != null && dp.getActionType() == ActionType.UPDATE) {
				users = (List<User>)dp.getData();
				HomeController.getInstance().updateTable(users);				
			}
			 
			if (dp.getActionType() == ActionType.SEND_INVITATION) {
				User sender = dp.getSender();
				PlayerController.getInstance().setUser(sender);
				InvitationController inviteCtl = new InvitationController();
				inviteCtl.addSender(sender);
			}
			
			if (dp.getActionType() == ActionType.RESPONSE_INVITATION) 
				HomeController.getInstance().rejectedChallenge();
					
			if (dp.getActionType() == ActionType.NEW_GAME) {
				Game game = (Game) dp.getData();
				GameController.getInstance().newGame(game.getId());
				HomeController.getInstance().hideView();
				if (UserController.getInstance().getUser().getUsername().equals(game.getPlayFirst())) {
					GameController.getInstance().mf.isMaster = true;
					GameController.getInstance().mf.normalNewGame();
				}
				else {
					GameController.getInstance().mf.reverseNewGame();
				}
			}
			
			if (dp.getActionType() == ActionType.REMATCH) {
				
			}
			
			if (dp.getActionType() == ActionType.MOVE) {
				Movement mov = (Movement)dp.getData();
				GameController.getInstance().mf.getMove(mov.chessIndex, mov.transportX, mov.transportY);
			}
			
			if (dp.getActionType() == ActionType.DESTROY) {
				Destroy des = (Destroy)dp.getData();
				GameController.getInstance().mf.getDestroy(des.hitChess, des.destroyedChess);
			}
			
			if (dp.getActionType() == ActionType.EXIT) {
				String name = PlayerController.getInstance().getUser().getUsername();
				GameController.getInstance().mf.stopTime();
				JOptionPane.showMessageDialog(GameController.getInstance().mf, name + " had quited, You won!");
				Record record = new Record(GameController.getInstance().mf.getId(), UserController.getInstance().getUser(), GameController.getInstance().mf.getMove(), true);
				DataPackage dp2 = new DataPackage(record, ActionType.END_GAME);
				ClientController.getInstance().sendData(dp2);
				GameController.getInstance().quit();
				HomeController.getInstance().displayView();
			}
			
			if (dp.getActionType() == ActionType.UPDATE_USER) {
				User u = (User) dp.getData();
				System.out.println(u.getScore());
				UserController.getInstance().setUser(u);
				HomeController.getInstance().updateView();
			}
			
			if (dp.getActionType() == ActionType.UPDATE_RANK) {
				ArrayList<User> users = (ArrayList<User>)dp.getData();
				RankingController.getInstance().updateTable1(users);
				RankingController.getInstance().updateTable2(users);
				RankingController.getInstance().updateTable3(users);
			}
			
			
			
			
		}
	}
}
