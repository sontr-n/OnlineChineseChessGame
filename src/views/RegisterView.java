package views;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import models.User;

public class RegisterView extends JFrame {
	
	public RegisterView() {
		
	}
	
	public User getUser() {
		return new User();
	}

}
