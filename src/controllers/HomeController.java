package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import controllers.networking.ClientController;
import models.DataPackage;
import models.User;
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
			ClientController.getInstance().sendData(new DataPackage(UserController.getInstance().getUser(), ActionType.SIGN_OUT));
			
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
	}
	
	private void infoUpdate() {
		homeView.setUserInfo(UserController.getInstance().getUser());
	}
	
	

	@Override
	public DataPackage packData() {
		return null;
	}

	@Override
	public void hiddenView() {
		homeView.dispose();
	}
	
	public void updateTable(List<User> users) {
		DefaultTableModel model = (DefaultTableModel)homeView.getTable().getModel();
		int rows = model.getRowCount();
		for (int i = rows-1; i >= 0; --i)
			model.removeRow(i);
		System.out.println("2");
		for (User u : users) {
			if (!u.getUsername().equals(UserController.getInstance().getUser().getUsername()))
			model.addRow(u.toObject());
		}
	}
	
	private static HomeController instance = new HomeController();
	
	public static final HomeController getInstance() {
		return instance;
	}
	

}
