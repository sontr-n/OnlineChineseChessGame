package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import controllers.networking.client.ClientController;
import models.DataPackage;
import models.User;
import views.LoginView;

public class LoginController implements BaseController {
	private LoginView loginView;
	
	
	private LoginController() {
		loginView = new LoginView();
		loginView.addLoginListener(new LoginListener());
		loginView.addRegisterListener(new RegisterListener());
	}
	
	class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ClientController.getInstance().sendData(packData());
		}
		
	}
	
	class RegisterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			RegisterController.getInstance().displayView();
			hideView();
		}
		
	}
	
	
	//Updating View
	@Override
	public void updateView() {
		
	}
	
	
	
	//Representation View
	@Override
	public void displayView() {
		updateView();
		loginView.setVisible(true);
	}
	
	
	public DataPackage packData() {
		return new DataPackage(loginView.getUser(), ActionType.SIGN_IN);
	}
	

	private static LoginController instance = new LoginController();
	
	public static final LoginController getInstance() {
		return instance;
	}
	
	public void showMessage(String msg) {
		loginView.showMessage(msg);
	}

	public User getUser() {
		return loginView.getUser();
	}

	@Override
	public void hideView() {
		loginView.dispose();
	}
	
}
