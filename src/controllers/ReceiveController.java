package controllers;

import java.util.List;

import controllers.networking.ClientController;
import models.DataPackage;
import models.User;

public class ReceiveController extends Thread {
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
			
			if(dp.getActionType() == ActionType.RESPONSE_INVITATION) {
				User sender = dp.getSender();
				boolean data = (Boolean)dp.getData();
				System.out.println(data);
				//if accecpted
				
				//if rejected
			}
		}
	}
}
