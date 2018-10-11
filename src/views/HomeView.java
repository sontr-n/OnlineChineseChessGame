package views;


import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.UIManager;

import models.User;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class HomeView extends JFrame {
	private JSplitPane splitPane;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JScrollPane scrollPane;
	private JTable tblPlayer;
	private JButton btnSignOut;
	private JButton btnRanking;
	private JTextArea txtUsername;
	private JTextArea txtScore;
	
	public HomeView() {
		splitPane = new JSplitPane();
		
		topPanel = new JPanel(null);
		bottomPanel = new JPanel();
		scrollPane = new JScrollPane();
		
		//set window size and its layout
		setPreferredSize(new Dimension(600, 500)); 
		getContentPane().setLayout(new GridLayout());
		getContentPane().add(splitPane);
		setTitle("Login");
		
		//configure splitPane
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(150);
		splitPane.setTopComponent(topPanel);
		splitPane.setBottomComponent(bottomPanel);
		splitPane.setEnabled(false);
		
		
		//configure headPanel
		btnSignOut = new JButton("Sign Out");
		btnSignOut.setBounds(471, 13, 97, 25);
		topPanel.add(btnSignOut);
		
		txtUsername = new JTextArea();
		txtUsername.setText("123123");
		txtUsername.setBackground(UIManager.getColor("Button.background"));
		txtUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtUsername.setEditable(false);
		txtUsername.setBounds(124, 16, 97, 22);
		topPanel.add(txtUsername);
		
		txtScore = new JTextArea();
		txtScore.setText("132");
		txtScore.setBackground(UIManager.getColor("Button.background"));
		txtScore.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtScore.setEditable(false);
		txtScore.setBounds(124, 47, 97, 22);
		topPanel.add(txtScore);
		
		btnRanking = new JButton("Ranking");
		btnRanking.setBounds(356, 13, 97, 25);
		topPanel.add(btnRanking);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsername.setBounds(37, 17, 86, 21);
		topPanel.add(lblUsername);
		
		JLabel lblScore = new JLabel("Score:");
		lblScore.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblScore.setBounds(64, 50, 48, 16);
		topPanel.add(lblScore);
		
		
		//configure bottomPanel
		 bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
		 bottomPanel.add(scrollPane); 
		 Object[] cols = {"Name", "Score", "Satus"};
		 Object[][] rows = {
				 {"name1", "0", "Playing"},
				 {"name2", "0", "Free"}
				 };
		 TableModel tblModel = new DefaultTableModel(rows, cols) {
			 public boolean isCellEditable(int row, int col) {
				 return false;
			 }
		 };
		 tblPlayer = new JTable(tblModel);
		 tblPlayer.getColumnModel().getColumn(0).setPreferredWidth(109);
		 tblPlayer.getColumnModel().getColumn(1).setPreferredWidth(49);
		 tblPlayer.getColumnModel().getColumn(2).setPreferredWidth(43);
		 tblPlayer.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 scrollPane.setViewportView(tblPlayer);
		 bottomPanel.add(scrollPane);
	

		 pack();
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
	}
	
	public void setUserInfo(User u) {
		txtUsername.setText(u.getUsername());
		txtScore.setText(Double.toString(u.getScore()));
	}
	
	public void addSignOutListener(ActionListener so) {
		btnSignOut.addActionListener(so);
	}
	
	public void addRankingListner(ActionListener r) {
		btnSignOut.addActionListener(r);
	}
}
