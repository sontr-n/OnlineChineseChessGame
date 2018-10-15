package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controllers.networking.ClientController;
import models.DataPackage;
import models.User;
import views.InvitationView;

public class InvitationController {
	InvitationView view;
	User sender;
	User receiver;
	public InvitationController() {
		view = new InvitationView();
		view.addAcceptListener(new AcceptListener());
		view.addRejectListener(new RejectListener());
		sender = UserController.getInstance().getUser();
		receiver = PlayerController.getInstance().getUser();
		view.setVisible(true);
	}

	class AcceptListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ClientController.getInstance().sendData(new DataPackage(new Boolean(true), sender, receiver, ActionType.RESPONSE_INVITATION));
			view.dispose();
		}
		
	}
	
	class RejectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ClientController.getInstance().sendData(new DataPackage(new Boolean(false), sender, receiver, ActionType.RESPONSE_INVITATION));
			view.dispose();
		}
	}
	
	
	public void addSender(User u) {
		view.addSender(u);
	}

}
