package controllers;

import models.User;

public class UserController {
	private User user;
	private boolean logedIn;
	public boolean isTurn;
	
	private UserController() {
		logedIn = false;
		user = null;
		isTurn = false;
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
