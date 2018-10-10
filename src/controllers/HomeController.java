package controllers;

import models.DataPackage;
import views.HomeView;

public class HomeController implements BaseController {
	private HomeView homeView;
	
	
	private HomeController() {
		homeView = new HomeView();
	}
	
	@Override
	public void displayView() {
		homeView.setVisible(true);
	}
	
	@Override
	public void updateView() {
		
	}
	


	@Override
	public DataPackage packData() {
		return null;
	}

	@Override
	public void hiddenView() {
		homeView.dispose();
	}
	
	private static HomeController instance = new HomeController();
	
	public static final HomeController getInstance() {
		return instance;
	}

}
