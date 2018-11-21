package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import models.DataPackage;
import models.User;
import views.RankingView;

public class RankingController implements BaseController {
	private RankingView view;
	
	public RankingController() {
		view = new RankingView();
		view.addReturnListener(new ReturnListener());
	}
	
	private class ReturnListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			HomeController.getInstance().displayView();
			view.dispose();
		}
		
	}
	

	@Override
	public void updateView() {
		
	}
	
	public void updateTable1(ArrayList<User> us) {
		ArrayList<User> users = us;
		for (int j = 0; j < users.size(); ++j) {
			for (int i = 0; i < users.size()-1; ++i) {
				if (users.get(i).getScore() < users.get(i+1).getScore()) {
					User tmp = users.get(i);
					users.set(i, users.get(i+1));
					users.set(i+1, tmp);
				}
			}
		}
		DefaultTableModel model = (DefaultTableModel) view.getTable1().getModel();
		int rows = model.getRowCount();
		for (int i = rows-1; i >= 0; --i)
			model.removeRow(i);
		int i = 1;
		for (User u : users) {
			model.addRow(new Object[] {i, u.getUsername(), u.getScore()});
			i++;
		}
	}
	
	public void updateTable2(ArrayList<User> us) {
		ArrayList<User> users = us;
		for (int j = 0; j < users.size(); ++j) {
			for (int i = 0; i < users.size()-1; ++i) {
				if (users.get(i).getAverageMoveWin() > users.get(i+1).getAverageMoveWin()) {
					User tmp = users.get(i);
					users.set(i, users.get(i+1));
					users.set(i+1, tmp);
				}
			}
		}
		DefaultTableModel model = (DefaultTableModel) view.getTable2().getModel();
		int rows = model.getRowCount();
		for (int i = rows-1; i >= 0; --i)
			model.removeRow(i);
		int i = 1;
		for (User u : users) {
			model.addRow(new Object[] {i, u.getUsername(), u.getAverageMoveWin()});
			i++;
		}
	}
	
	public void updateTable3(ArrayList<User> us) {
		ArrayList<User> users = us;
		for (int j = 0; j < users.size(); ++j) {
			for (int i = 0; i < users.size()-1; i++) {
				if (users.get(i).getAverageMoveLose() < users.get(i+1).getAverageMoveLose()) {
					User tmp = users.get(i);
					users.set(i, users.get(i+1));
					users.set(i+1, tmp);
				}
			}
		}
		DefaultTableModel model = (DefaultTableModel) view.getTable3().getModel();
		int rows = model.getRowCount();
		for (int i = rows-1; i >= 0; --i)
			model.removeRow(i);
		int i = 1;
		for (User u : users) {
			model.addRow(new Object[] {i, u.getUsername(), u.getAverageMoveLose()});
			i++;
		}
	}

	@Override
	public void displayView() {
		view.setVisible(true);
	}

	@Override
	public DataPackage packData() {
		return null;
	}

	@Override
	public void hideView() {
		view.dispose();
	}
	
	private static final RankingController instance = new RankingController();
	
	public static RankingController getInstance() {
		return instance;
	}
	
}
