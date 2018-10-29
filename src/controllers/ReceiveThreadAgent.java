package controllers;

import java.util.List;

import javax.swing.JOptionPane;

import controllers.networking.ClientController;
import game.Game;
import models.DataPackage;
import models.Destroy;
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
			
			if (dp.getActionType() == ActionType.RESPONSE_INVITATION) {
				User sender = dp.getSender();
				boolean data = (Boolean)dp.getData();
				PlayerController.getInstance().setUser(sender);
				//if accecpted
				if (data) {
					HomeController.getInstance().accecptedChallenge();
					HomeController.getInstance().hiddenView();
				}
				//if rejected
				else 
					HomeController.getInstance().rejectedChallenge();
					
			}
			
			if (dp.getActionType() == ActionType.NEW_GAME) {
				
			}
			
			if (dp.getActionType() == ActionType.MOVE) {
				Movement mov = (Movement)dp.getData();
				Game.getInstance().mf.getMove(mov.chessIndex, mov.transportX, mov.transportY);
			}
			
			if (dp.getActionType() == ActionType.DESTROY) {
				Destroy des = (Destroy)dp.getData();
				Game.getInstance().mf.getDestroy(des.hitChess, des.destroyedChess);
			}
			
		}
	}
}
