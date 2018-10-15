package models;

import java.io.ObjectOutputStream;

public class Client {
	private User user;
	private ObjectOutputStream oos;
	
	public Client(User u, ObjectOutputStream o) {
		user = u;
		oos = o;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ObjectOutputStream getStream() {
		return oos;
	}

	public void setStream(ObjectOutputStream oos) {
		this.oos = oos;
	}
	
	
}
