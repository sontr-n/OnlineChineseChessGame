package controllers.Main;

import controllers.LoginController;
import controllers.networking.ClientController;

public class ClientRun {
	public static void main(String[] args) {
		LoginController.getInstance().displayView();
	}
}
