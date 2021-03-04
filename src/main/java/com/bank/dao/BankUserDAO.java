package com.bank.dao;

import java.sql.Connection;
import java.sql.SQLException;

import com.bank.model.BankUser;

public interface BankUserDAO {

	public boolean hasCorrectUsername(String username, Connection con) throws SQLException;

	public boolean hasCorrectPassword(String username, String password, Connection con) throws SQLException;

	public BankUser createNewUser(String username, String password, Connection con) throws SQLException;
	
	

}
