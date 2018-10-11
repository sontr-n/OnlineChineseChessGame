package controllers;

import java.awt.Desktop.Action;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
			if ((Boolean)dp.getData()) {
				loginView.showMessage("Sign In Successfully");
				hiddenView();
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
		loginView.setVisible(true);
	}
	
	public boolean checkUser(User u) {
		//get user data from DB and check 
		DAO dao = new DAO();
		Statement stm = dao.connect();
		boolean isOK = false;
		try {
			ResultSet rs = stm.executeQuery("SELECT * FROM users WHERE username= '" + u.getUsername() + "'");
			if (rs.first()) {
				String pw = rs.getString("password");
				if (pw.equals(String.valueOf(u.getPassword())))
					isOK = true;
			}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		dao.closeConnection();
		return isOK;
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
