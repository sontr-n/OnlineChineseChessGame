package controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import controllers.networking.client.ClientController;

import java.sql.PreparedStatement;

import models.DataPackage;
import models.User;
import views.HomeView;
import views.RegisterView;


public class RegisterController implements BaseController {
	private RegisterView registerView;
	private User user;
	
	public RegisterController() {
		registerView = new RegisterView();
		registerView.addRegisterListener(new RegisterListener());
		registerView.addReturnListener(new ReturnListener());
		
	}
	
	class RegisterListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String pass = String.valueOf(registerView.getTxtPassword().getPassword());
			String rePass = String.valueOf(registerView.getTxtConfirmedPassword().getPassword());
			if (pass.equals(rePass))
				ClientController.getInstance().sendData(new DataPackage(registerView.getUser(), ActionType.SIGN_UP));
			else 
				JOptionPane.showMessageDialog(registerView, "Password and Confirmed password didn't match");
		}
	}
	
	class ReturnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			hiddenView();
			LoginController.getInstance().displayView();
			
		}
		
	}

	@Override
	public void updateView() {
	}

	@Override
	public void displayView() {
		updateView();
		registerView.setVisible(true);
	}
	

	@Override
	public DataPackage packData() {
		return new DataPackage(registerView.getUser(), ActionType.SIGN_UP);
	}
	

	@Override
	public void hiddenView() {
		registerView.dispose();
	}
	
	public boolean addUser(User u) {
		//connect DB
		DAO dao = new DAO();
		String sql = "INSERT INTO users (username, password) values (?, ?)";
		Object[] params = new Object[] {u.getUsername(), u.getPassword()};
		int count = dao.executeUpdate(sql, params);
		dao.closeConnection();
		if (count > 0)
			return true;
		return false;
	}
	
	public void showMessage(String msg) {
		registerView.showMessage(msg);
	}
	
	public User getUser() {
		return registerView.getUser();
	}
	
	
	
	
	private static RegisterController instance = new RegisterController();
	
	public static final RegisterController getInstance() {
		return instance;
	}
}
