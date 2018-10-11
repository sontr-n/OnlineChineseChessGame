package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import models.DataPackage;
import views.HomeView;

public class HomeController implements BaseController {
	private HomeView homeView;
	
	
	private HomeController() {
		homeView = new HomeView();
		homeView.addSignOutListener(new SignOutListener());
		homeView.addRankingListner(new RankingListener());
	}
	
	class SignOutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UserController.getInstance().setLogedIn(false);
			hiddenView();
			LoginController.getInstance().displayView();
		}
		
	}
	
	class RankingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
		}
		
	}
	
	@Override
	public void displayView() {
		updateView();
		homeView.setVisible(true);
	}
	
	@Override
	public void updateView() {
		infoUpdate();
		tableUpdate();
	}
	
	private void infoUpdate() {
		homeView.setUserInfo(UserController.getInstance().getUser());
	}
	private void tableUpdate() {
		
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
