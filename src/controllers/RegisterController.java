package controllers;
import models.DataPackage;
import models.User;
import views.RegisterView;


public class RegisterController implements BaseController {
	private RegisterView registerView;
	private User user;
	
	public RegisterController(RegisterView view) {
		registerView = view;
		
	}

	@Override
	public void updateView() {
		
	}

	@Override
	public void displayView() {
		
	}

	@Override
	public DataPackage packData() {
		return new DataPackage(registerView.getUser(), ActionType.SIGN_UP);
	}

	@Override
	public void hiddenView() {
		registerView.dispose();
	}
}
