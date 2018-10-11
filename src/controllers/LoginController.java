package controllers;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
			if (dp.getData() != null) {
				loginView.showMessage("Sign In Successfully");
				hiddenView();
				
				UserController.getInstance().setLogedIn(true);
				UserController.getInstance().setUser(loginView.getUser());
				
				HomeController.getInstance().displayView();
				
			} else 
				loginView.showMessage("Check your username/password");
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
	
	public DataPackage getUser(User u) {
		//get user data from DB and check 
		DAO dao = new DAO();
		Connection con = dao.connect();
		DataPackage dp = null;
		try {
			Statement stm = con.createStatement();
			ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username= '" + u.getUsername() + "'");
			if (rs.first()) {
				String pw = rs.getString("password");
				if (pw.equals(String.valueOf(u.getPassword()))) {
					User user = new User(rs.getInt(1), rs.getString(2), rs.getString(3).toCharArray(), rs.getDouble(4));
					dp = new DataPackage(user, ActionType.SIGN_IN);
				}
			}
			
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


	@Override
	public void hiddenView() {
		loginView.dispose();
	}
	
}
