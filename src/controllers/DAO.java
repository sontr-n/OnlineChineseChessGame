package controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import utils.DatabaseConfig;

public class DAO {
	private String className = DatabaseConfig.DATABASE_CLASS;
	private String dbName = DatabaseConfig.DATABASE_NAME;
	private String username = DatabaseConfig.USERNAME;
	private String password = DatabaseConfig.PASSWORD;
	private Connection con;
	public DAO() {
		
	}
	
	private void openConnection() {
		try {
			Class.forName(className);
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+dbName, username, password);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public ResultSet executeQuery(String sql, Object[] params) {
		try {
			openConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			for (int i = 0; i < params.length; ++i) {
				if (params[i] instanceof String) 
					stm.setString(i+1, (String)params[i]);
				if (params[i] instanceof Double) 
					stm.setDouble(i+1, (Double)params[i]);
			}
			ResultSet rs = stm.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public int executeUpdate(String sql, Object[] params) {
		int count = -1;
		try {
			openConnection();
			PreparedStatement stm = con.prepareStatement(sql);
			for (int i = 0; i < params.length; ++i) {
				if (params[i] instanceof String)
					stm.setString(i+1, (String)params[i]);
				if (params[i] instanceof Double)
					stm.setDouble(i+1, (Double)params[i]);
			}
			count = stm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	
	public void closeConnection() {
		try {
			con.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
}
