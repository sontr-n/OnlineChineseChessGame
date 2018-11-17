package views;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import models.User;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class InvitationView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtInvite;
	private JTextField txtName;
	private JButton btnAccept;
	private JButton btnReject;
	private JTextField txtCountDown;
	private int time;
	/**
	 * Create the panel.
	 */
	public InvitationView() {
		JPanel content = new JPanel(null);
		this.setBounds(100, 100, 460, 300);
		this.setResizable(false);
		time = 10;
		
		
		txtInvite = new JTextField();
		txtInvite.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtInvite.setEditable(false);
		txtInvite.setText("Challenges you");
		txtInvite.setBounds(157, 84, 137, 48);
		content.add(txtInvite);
		txtInvite.setBorder(BorderFactory.createEmptyBorder());
		txtInvite.setColumns(10);
		
		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtName.setEditable(false);
		txtName.setBounds(147, 29, 137, 42);
		txtName.setHorizontalAlignment(JTextField.CENTER);
		txtName.setBorder(BorderFactory.createEmptyBorder());
		content.add(txtName);
		txtName.setColumns(10);
		
		btnAccept = new JButton("Accept");
		btnAccept.setBounds(76, 188, 113, 42);
		content.add(btnAccept);
		
		btnReject = new JButton("Reject");
		btnReject.setBounds(233, 188, 119, 42);
		content.add(btnReject);
		
		txtCountDown = new JTextField();
		txtCountDown.setFont(new Font("Tahoma", Font.PLAIN, 18));
		txtCountDown.setEditable(false);
		txtCountDown.setBounds(136, 145, 189, 22);
		txtCountDown.setBorder(BorderFactory.createEmptyBorder());
		content.add(txtCountDown);
		txtCountDown.setColumns(10);
		txtCountDown.setText("Time Remaining: " + time + "s");
		this.setContentPane(content);
			
	}



	public int getTime() {
		return time;
	}



	public void countDown() {
		time--;
	}
	
	public void updateTime() {
		txtCountDown.setText("Time Remaining: " + time + "s");
	}




	public void addSender(User u) {
		txtName.setText(u.getUsername());
	}
	
	public void addAcceptListener(ActionListener a) {
		btnAccept.addActionListener(a);
	}
	
	public void addRejectListener(ActionListener r) {
		btnReject.addActionListener(r);
	}
}
