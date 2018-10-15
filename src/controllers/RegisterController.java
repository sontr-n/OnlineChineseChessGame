package controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.table.TableModel;

import java.sql.PreparedStatement;

import controllers.networking.ClientController;
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
			ClientController.getInstance().sendData(new DataPackage(registerView.getUser(), ActionType.SIGN_UP));
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
		Connection con = dao.connect();
		try {
			PreparedStatement pstm = con.prepareStatement("INSERT INTO users (username, password) values (?, ?)"); 
			pstm.setString(1, u.getUsername());
			pstm.setString(2, String.valueOf(u.getPassword()));
			int count = pstm.executeUpdate();
			if (count > 0)
				return true;
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
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
