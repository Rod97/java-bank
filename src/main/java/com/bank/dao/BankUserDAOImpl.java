package com.bank.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bank.model.BankUser;

public class BankUserDAOImpl implements BankUserDAO {

	@Override
	public boolean hasCorrectUsername(String username, Connection con) throws SQLException {
		// Here we will use a sql Query to check username
		String sql = "Select username FROM bank_app_data.bank_user WHERE username = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, username);

		ResultSet rs = pstmt.executeQuery();
		String returnedUser = null;
		if (rs.next()) {
			returnedUser = rs.getString("username");
		}
		if (returnedUser != null) {
			return true;
		}

		return false;
	}

	@Override
	public boolean hasCorrectPassword(String username, String password, Connection con) throws SQLException {
		// Validating password for given username
		String sql = "SELECT username, user_pass from bank_app_data.bank_user WHERE (username = ? AND user_pass = crypt(?, user_pass));";



		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, username);
		pstmt.setString(2, password);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			String un = rs.getString("username");
			
			return un.equals(username);
		}
		
		return false;
	}

	@Override
	public BankUser createNewUser(String username, String password, Connection con) throws SQLException {
		BankUser newUser = null;
		String sql = "INSERT INTO bank_app_data.bank_user(username, user_pass) VALUES (?,crypt(?, gen_salt('bf')));";
		
		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setString(1, username);
		pstmt.setString(2,password);
		
		pstmt.executeUpdate(); //Use executeUpdate for DML (i.e. INSERT)
		
		sql = "SELECT username FROM bank_app_data.bank_user WHERE username = ?";
		pstmt = con.prepareStatement(sql);
		pstmt.setString(1, username);
		
		ResultSet rs = pstmt.executeQuery();
		
		if(rs.next()) {
			String un = rs.getString("username");
			
			newUser = new BankUser(un);
		}
			

		 return newUser;
	}

}
