package views;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;


public class RankingView extends JFrame {
	private JTable tbl1, tbl2, tbl3, tbl4;
	private JSplitPane splitPane;
	private JPanel topPanel;
	private JPanel bottomPanel;
	private JButton btnReturn;
	public RankingView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTabbedPane tabbedPane = new JTabbedPane();
		setPreferredSize(new Dimension(400, 400)); 
		this.setResizable(false);
		splitPane = new JSplitPane();
		topPanel = new JPanel(new GridLayout(0, 1));
		bottomPanel = new JPanel(null);
		//configure splitPane
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setDividerLocation(300);
		splitPane.setTopComponent(topPanel);
		splitPane.setBottomComponent(bottomPanel);
		
		btnReturn = new JButton("RETURN");
		btnReturn.setBounds(140, 13, 97, 25);
		bottomPanel.add(btnReturn);
		splitPane.setEnabled(false);
				
		tabbedPane.addTab("Player Scores", null, tab1Content(), null);
		tabbedPane.addTab("Win", null, tab2Content(), null);
		tabbedPane.addTab("Lose", null, tab3Content(), null);
		topPanel.add(tabbedPane);
		this.setContentPane(splitPane);
		pack();
		
	}
	
	public JPanel tab1Content() {
		JPanel content;
		JScrollPane scrollPane = new JScrollPane();
		content = new JPanel(new GridLayout(0, 1));
		Object[] cols = {"STT", "Name", "Score"};
		Object[][] rows = {{"1", "test", "0"}};
		TableModel tblModel = new DefaultTableModel(rows, cols) {
			public boolean isCellEditable(int row, int col) {
				 return false;
			}
		};
		tbl1 = new JTable(tblModel);
		tbl1.setRowSelectionAllowed(false);
		tbl1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		scrollPane.setViewportView(tbl1);
		content.add(scrollPane);
		return content;
	}
	
	public JPanel tab2Content() {
		JPanel content;
		JScrollPane scrollPane = new JScrollPane();
		content = new JPanel(new GridLayout(0, 1));
		Object[] cols = {"STT", "Name", "Move"};
		Object[][] rows = {{"1", "test", "0"}};
		 TableModel tblModel = new DefaultTableModel(rows, cols) {
			public boolean isCellEditable(int row, int col) {
				 return false;
			 }
		 };
		 tbl2 = new JTable(tblModel);
		 tbl2.setRowSelectionAllowed(false);
		 tbl2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 scrollPane.setViewportView(tbl2);
		 content.add(scrollPane);
		return content;
	}
	
	public JPanel tab3Content() {
		JPanel content;
		JScrollPane scrollPane = new JScrollPane();
		content = new JPanel(new GridLayout(0, 1));
		Object[] cols = {"STT", "Name", "Move"};
		 Object[][] rows = {{"1", "test", "0"}};
		 TableModel tblModel = new DefaultTableModel(rows, cols) {
			public boolean isCellEditable(int row, int col) {
				 return false;
			 }
		 };
		 tbl3 = new JTable(tblModel);
		 tbl3.setRowSelectionAllowed(false);
		 tbl3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		 scrollPane.setViewportView(tbl3);
		 content.add(scrollPane);
		return content;
	}
	
	
	
	public JTable getTable1() {
		return tbl1;
	}
	public JTable getTable2() {
		return tbl2;
	}
	public JTable getTable3() {
		return tbl3;
	}
	
	public void addReturnListener(ActionListener rl) {
		btnReturn.addActionListener(rl);
	}
	
	public static void main(String[] args) {
		RankingView rank = new RankingView();
		rank.setVisible(true);
	}
}
