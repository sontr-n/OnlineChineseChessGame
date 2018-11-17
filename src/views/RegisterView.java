package views;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import models.User;

public class RegisterView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	private JButton btnRegister;
	private JButton btnReturn;
	private JPasswordField txtConfirmedPassword;
	
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
		txtPassword.setBounds(183, 119, 116, 22);
		content.add(txtPassword);
		txtPassword.setColumns(10);
		
		
		btnRegister = new JButton("Register");
		btnRegister.setBounds(110, 212, 97, 25);
		content.add(btnRegister);
		
		btnReturn = new JButton("Return");
		btnReturn.setBounds(244, 212, 97, 25);
		content.add(btnReturn);
		
		JLabel lblRepassword = new JLabel("Confirmed password:");
		lblRepassword.setBounds(38, 159, 123, 16);
		content.add(lblRepassword);
		
		txtConfirmedPassword = new JPasswordField();
		txtConfirmedPassword.setBounds(183, 156, 116, 22);
		content.add(txtConfirmedPassword);
		txtConfirmedPassword.setColumns(10);
		
		this.setContentPane(content);
		
		
		
		//click exit window
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
	
	
	
	
	public JPasswordField getTxtPassword() {
		return txtPassword;
	}




	public JPasswordField getTxtConfirmedPassword() {
		return txtConfirmedPassword;
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
