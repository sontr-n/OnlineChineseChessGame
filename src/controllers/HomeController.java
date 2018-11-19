package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import controllers.networking.client.ClientController;
import models.DataPackage;
import models.User;
import views.HomeView;

public class HomeController implements BaseController {
	private HomeView homeView;
	private User user = null;
	
	private HomeController() {
		homeView = new HomeView();
		homeView.addSignOutListener(new SignOutListener());
		homeView.addRankingListner(new RankingListener());
		homeView.addInvitationListener(new InvitationListener());
		homeView.addMouseAdapterTable(new MouseAdapterTable());
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
	
	private class MouseAdapterTable extends MouseAdapter {
		@Override
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
	        int row = homeView.getTable().rowAtPoint(evt.getPoint());
	        if (row >= 0) {
	        	String status = (String)homeView.getTable().getValueAt(row, 2);
	        	String username = (String)homeView.getTable().getValueAt(row, 0);
	        	user = new User(username);
	        	if (status.equals("busy")) 
	        		user.setBusy(true);
	        }
	        else 
	        	user = null;

	    }	
	 }
	
	class InvitationListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (user == null) 
				JOptionPane.showMessageDialog(homeView, "Please pick player before request");
			else {
				if (user.isBusy()) {
	        		homeView.showMessage(user.getUsername() + " is playing");
	        	}
				
				else {
					User sender = UserController.getInstance().getUser();
		        	User receiver = new User(user.getUsername()); 
		        	ClientController.getInstance().sendData(new DataPackage(sender, receiver, ActionType.SEND_INVITATION));
				}
			}			
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
		for (User u : users) {
			if (!u.getUsername().equals(UserController.getInstance().getUser().getUsername()))
			model.addRow(u.toObject());
		}
	}
	
	private static HomeController instance = new HomeController();
	
	public static final HomeController getInstance() {
		return instance;
	}
	
	public void rejectedChallenge() {
		homeView.showMessage("Your challenge was declined!");
	}
		
	public void accecptedChallenge() {
		DataPackage dp = new DataPackage(UserController.getInstance().getUser(), 
				PlayerController.getInstance().getUser(), ActionType.CHANGE_STATUS);
		ClientController.getInstance().sendData(dp);
	}
	

}
