package controllers;

import models.User;

public class UserController {
	private User user;
	private boolean logedIn;
	
	private UserController() {
		logedIn = false;
		user = null;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public boolean isLogedIn() {
		return logedIn;
	}
	public void setLogedIn(boolean logedIn) {
		this.logedIn = logedIn;
	}

	private static UserController instance = new UserController();
	
	public static final UserController getInstance() {
		return instance;
	}
	

}
