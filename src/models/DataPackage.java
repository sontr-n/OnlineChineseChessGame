package models;

import java.io.Serializable;

import controllers.ActionType;

public class DataPackage implements Serializable {

	private Object data;
	private ActionType actType;
	
	
	public DataPackage() {
		
	}

	public DataPackage(Object o, ActionType a) {
		data = o;
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
	
	
}


