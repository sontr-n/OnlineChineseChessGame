package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;

import controllers.networking.client.ClientController;
import models.DataPackage;
import models.User;
import views.InvitationView;

public class InvitationController {
	InvitationView view;
	User sender;
	User receiver;
	boolean isPicked = false;
	public InvitationController() {
		view = new InvitationView();
		view.addAcceptListener(new AcceptListener());
		view.addRejectListener(new RejectListener());
		sender = UserController.getInstance().getUser();
		receiver = PlayerController.getInstance().getUser();
		new CountDownThread().start();
		view.setVisible(true);
	}

	private class AcceptListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isPicked = true;
			ClientController.getInstance().sendData(new DataPackage(true, sender, receiver, ActionType.RESPONSE_INVITATION));
			view.dispose();
		}
		
	}
	
	private class CountDownThread extends Thread {
		
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				view.countDown();
				view.updateTime();
				if (isPicked)
					break;
				if (view.getTime() == 0) {
					ClientController.getInstance().sendData(new DataPackage(new Boolean(false), sender, receiver, ActionType.RESPONSE_INVITATION));
					view.dispose();
					break;
				}
			}
		}
	}
	
	class RejectListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			isPicked = true;
			ClientController.getInstance().sendData(new DataPackage(new Boolean(false), sender, receiver, ActionType.RESPONSE_INVITATION));
			view.dispose();
		}
	}
	
	
	public void addSender(User u) {
		view.addSender(u);
	}

}
