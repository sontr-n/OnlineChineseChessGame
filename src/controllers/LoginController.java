package controllers;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.xml.crypto.Data;

import controllers.networking.ClientController;
import models.DataPackage;
import models.User;
import views.LoginView;

public class LoginController implements BaseController {
	private LoginView loginView;
	private User user;
	
	
	private LoginController() {
		loginView = new LoginView();
		loginView.addLoginListener(new LoginListener());
		loginView.addRegisterListener(new RegisterListener());
	}
	
	class LoginListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			ClientController.getInstance().sendData(new DataPackage(loginView.getUser(), ActionType.SIGN_IN));
			DataPackage dp = ClientController.getInstance().receiveData();
//			System.out.println(dp == null);
			if ((Boolean)dp.getData()) {
				hiddenView();
				HomeController.getInstance().displayView();
			}
		}
		
	}
	
	class RegisterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			RegisterController.getInstance().displayView();
			hiddenView();
		}
		
	}
	
	
	//Updating View
	@Override
	public void updateView() {
	
	}
	
	
	//Representation View
	@Override
	public void displayView() {
		loginView.setVisible(true);
	}
	
	public boolean checkUser(User u) {
		//get user data from DB and check 
		if (u.getUsername().equals("son"))
			return true;
		return false;
	}
	
	public DataPackage packData() {
		return new DataPackage(loginView.getUser(), ActionType.SIGN_IN);
	}
	

	private static LoginController instance = new LoginController();
	
	public static final LoginController getInstance() {
		return instance;
	}


	@Override
	public void hiddenView() {
		loginView.dispose();
	}
	
}
