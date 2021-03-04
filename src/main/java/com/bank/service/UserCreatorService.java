package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.bank.dao.BankUserDAO;
import com.bank.dao.BankUserDAOImpl;
import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.UnableToCreateUserException;
import com.bank.model.BankUser;
import com.bank.util.ConnectionUtil;

public class UserCreatorService {

	public BankUserDAO bankUserDAO;

	public UserCreatorService() {

		this.bankUserDAO = new BankUserDAOImpl();
	}

	//this is to be used with JUnit
	public UserCreatorService(BankUserDAO bankUserDAO) {
		this.bankUserDAO = bankUserDAO;
	}

	public BankUser createUser(String username, String password)
			throws SQLException, BlankEntryException, UnableToCreateUserException {
		BankUser user = new BankUser();
		try (Connection con = ConnectionUtil.getConnection()) {
			if (username.matches("^\\s*$") || username == null) {
				throw new BlankEntryException("Invalid username. Username must have a maximum of 16 characters"); // add
																													// minimum?
			} else if (password.matches("^\\s*$") || password == null) {
				throw new BlankEntryException("Invalid password."); // contain a
																											// special
																											// character?
			} else {
				user = bankUserDAO.createNewUser(username, password, con);
			}

			if (user == null) {
				throw new UnableToCreateUserException("Sorry, we were unable to create a user account");
			}

			return user;
		}
	}
}
