package controllers;

import models.User;

public class PlayerController {
	private User user;
	
	private PlayerController() {
		
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	private static PlayerController instance = new PlayerController();
	
	public static final PlayerController getInstance() {
		return instance;
	}
	
}
