package models;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Client {
	private User user;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	public Client(User u, ObjectOutputStream o, ObjectInputStream i) {
		user = u;
		oos = o;
		ois = i;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ObjectOutputStream getOutputStream() {
		return oos;
	}

	public ObjectInputStream getInputStream() {
		return ois;
	}
	
	
}