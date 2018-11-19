package controllers;

import java.util.List;

import javax.swing.JOptionPane;

import controllers.networking.client.ClientController;
import models.DataPackage;
import models.Destroy;
import models.Game;
import models.Movement;
import models.User;

public class ReceiveThreadAgent extends Thread {
	List<User> users;
	@Override
	public void run() {
		for (;;) {
			DataPackage dp = ClientController.getInstance().receiveData();
			if (dp.getActionType() == ActionType.SIGN_IN) {
				if (dp.getData() != null) {
					LoginController.getInstance().showMessage("Sign In Successfully");
					LoginController.getInstance().hiddenView();
					
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
					RegisterController.getInstance().hiddenView();
					
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
				GameController.getInstance().newGame(game);
				
				HomeController.getInstance().hiddenView();
				if (UserController.getInstance().getUser().getUsername().equals(game.getPlayer1().getUsername())) {
					GameController.getInstance().mf.isMaster = false;
					GameController.getInstance().mf.reverseNewGame();
					PlayerController.getInstance().setUser(game.getPlayer2());
					
				}
				else {
					GameController.getInstance().mf.isMaster = true;
					GameController.getInstance().mf.normalNewGame();
					PlayerController.getInstance().setUser(game.getPlayer1());
				}
				
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
				GameController.getInstance().quit();
				HomeController.getInstance().displayView();
			}
			
			
			
			
		}
	}
}
