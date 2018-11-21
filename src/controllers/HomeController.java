package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import controllers.networking.client.ClientController;
import game.GameController;
import models.DataPackage;
import models.User;
import views.HomeView;

public class HomeController implements BaseController {
	private HomeView view;
	private User user = null;
	
	private HomeController() {
		view = new HomeView();
		view.addSignOutListener(new SignOutListener());
		view.addRankingListener(new RankingListener());
		view.addInvitationListener(new InvitationListener());
		view.addMouseAdapterTable(new MouseAdapterTable());
	}
	
	class SignOutListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			UserController.getInstance().setLogedIn(false);
			ClientController.getInstance().sendData(new DataPackage(UserController.getInstance().getUser(), ActionType.SIGN_OUT));
			
			hideView();
			LoginController.getInstance().displayView();
		}
		
	}
	
	private class MouseAdapterTable extends MouseAdapter {
		@Override
	    public void mouseClicked(java.awt.event.MouseEvent evt) {
	        int row = view.getTable().rowAtPoint(evt.getPoint());
	        if (row >= 0) {
	        	String status = (String)view.getTable().getValueAt(row, 2);
	        	String username = (String)view.getTable().getValueAt(row, 0);
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
				JOptionPane.showMessageDialog(view, "Please pick player before request");
			else {
				if (user.isBusy()) {
	        		view.showMessage(user.getUsername() + " is playing");
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
			System.out.println("t");
			RankingController.getInstance().displayView();
			view.dispose();
		}
		
	}
	
	@Override
	public void displayView() {
		updateView();
		view.setVisible(true);
	}
	
	@Override
	public void updateView() {
		infoUpdate();
	}
	
	private void infoUpdate() {
		view.setUserInfo(UserController.getInstance().getUser());
	}
	
	

	@Override
	public DataPackage packData() {
		return null;
	}

	@Override
	public void hideView() {
		view.dispose();
	}
	
	public void updateTable(List<User> users) {
		DefaultTableModel model = (DefaultTableModel)view.getTable().getModel();
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
		view.showMessage("Your challenge was declined!");
	}
		
	public void accecptedChallenge() {
		DataPackage dp = new DataPackage(UserController.getInstance().getUser(), 
				PlayerController.getInstance().getUser(), ActionType.CHANGE_STATUS);
		ClientController.getInstance().sendData(dp);
	}
	

}
