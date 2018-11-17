package controllers.main;

import controllers.LoginController;
import controllers.ReceiveThreadAgent;
import controllers.networking.client.ClientController;

public class ClientRun {
	public static void main(String[] args) {
		LoginController.getInstance().displayView();
		new ReceiveThreadAgent().start();
	}
}
