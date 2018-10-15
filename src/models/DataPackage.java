package models;

import java.io.Serializable;

import controllers.ActionType;

public class DataPackage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object data;
	private ActionType actType;
	private User sender;
	private User receiver;
	
	public DataPackage() {
		
	}

	public DataPackage(Object o, ActionType a) {
		data = o;
		actType = a;
		sender = null;
		receiver = null;
	}
	
	public DataPackage(Object o, User s, User r, ActionType a) {
		data = o;
		actType = a;
		sender = s;
		receiver = r;
	}
	
	public DataPackage(User s, User r, ActionType a) {
		sender = s;
		receiver = r;
		actType = a;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public ActionType getActionType() {
		return actType;
	}

	public void setActionType(ActionType actType) {
		this.actType = actType;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}
	
	
	
	
}


