package controllers;

import models.DataPackage;

public interface BaseController {
	public void updateView();
	public void displayView();
	public DataPackage packData();
	public void hideView();
}
