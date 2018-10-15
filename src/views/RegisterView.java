package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import models.User;

public class RegisterView extends JFrame {
	private User user;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnRegister;
	private JButton btnReturn;
	
	public RegisterView() {
		JPanel content = new JPanel(null);
		this.setBounds(100, 100, 460, 300);
		this.setTitle("Sign up");
		this.setResizable(false);
		
		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setBounds(102, 80, 69, 16);
		content.add(lblUsername);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(183, 77, 116, 22);
		content.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setBounds(102, 122, 69, 16);
		content.add(lblPassword);
		
		txtPassword = new JPasswordField();
		txtPassword.setEchoChar('*');
		txtPassword.setBounds(183, 119, 116, 22);
		content.add(txtPassword);
		txtPassword.setColumns(10);
		
		
		btnRegister = new JButton("Register");
		btnRegister.setBounds(101, 182, 97, 25);
		content.add(btnRegister);
		
		btnReturn = new JButton("Return");
		btnReturn.setBounds(221, 182, 97, 25);
		content.add(btnReturn);
		
		this.setContentPane(content);
		
		
		//click exit window
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public User getUser() {
		return new User(txtUsername.getText(), txtPassword.getPassword());
	}
	
	public void addRegisterListener(ActionListener reg) {
		btnRegister.addActionListener(reg);
	}
	
	public void addReturnListener(ActionListener ret) {
		btnReturn.addActionListener(ret);
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
}
