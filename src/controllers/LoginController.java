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
		updateView();
		loginView.setVisible(true);
	}
	
	public DataPackage getUserDAO(User u) {
		//get user data from DB and check 
		DAO dao = new DAO();
		DataPackage dp = null;
		try {
			String sql = "SELECT * FROM users WHERE username=? AND password=?";
			Object[] params = new Object[] {u.getUsername(), u.getPassword()};
			ResultSet rs = dao.executeQuery(sql, params);
			if (rs.first())	
				dp = new DataPackage(u, ActionType.SIGN_IN);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		dao.closeConnection();
		return dp;
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
	public void hiddenView() {
		loginView.dispose();
	}
	
}
