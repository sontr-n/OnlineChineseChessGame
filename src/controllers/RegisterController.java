package controllers;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controllers.networking.ClientController;
import models.DataPackage;
import models.User;
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
			DataPackage dp = ClientController.getInstance().receiveData();
			boolean isOK = (Boolean)dp.getData();
			if (isOK) {
				registerView.showMessage("Sign Up Successfully");
				hiddenView();
				HomeController.getInstance().displayView();
			}
			else 
				registerView.showMessage("Sign Up Failed");

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
		//connect Database
		if (u.getUsername().equals("son"))
			return true;
		return false;
	}
	
	private static RegisterController instance = new RegisterController();
	
	public static final RegisterController getInstance() {
		return instance;
	}
}
