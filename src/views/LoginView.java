package views;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import models.User;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
	private static final long serialVersionUID = 1L;
	private User user;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnLogin;
	private JButton btnRegister;
	
	/**
	 * Create the panel.
	 */
	public LoginView() {
		JPanel content = new JPanel(null);
		this.setBounds(100, 100, 460, 300);
		this.setResizable(false);
		this.setTitle("Log in");
		
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
		
		btnLogin = new JButton("Login");
		
		btnLogin.setBounds(95, 199, 97, 25);
		content.add(btnLogin);
		
		btnRegister = new JButton("Register");
		btnRegister.setBounds(225, 199, 97, 25);
		content.add(btnRegister);

		this.setContentPane(content);
		
		
		//click exit window
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	public User getUser() {
		user = new User(txtUsername.getText(), txtPassword.getPassword());
		return user;
	}
	
	public void addLoginListener(ActionListener log) {
		btnLogin.addActionListener(log);
	}
	
	public void addRegisterListener(ActionListener reg) {
		btnRegister.addActionListener(reg);
	}
	
	public void showMessage(String msg) {
		JOptionPane.showMessageDialog(this, msg);
	}
}
