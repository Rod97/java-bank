package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.dao.BankUserDAO;
import com.bank.dao.BankUserDAOImpl;
import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.IncorrectPasswordException;
import com.bank.exceptions.UserNotFoundException;
import com.bank.util.ConnectionUtil;

public class UserService {

	public AccountsDAO accountsDAO;
	public BankUserDAO bankUserDAO;

	public UserService() {
		this.accountsDAO = new AccountsDAOImpl();
		this.bankUserDAO = new BankUserDAOImpl();
	}

	public boolean hasCorrectUsername(String username) throws SQLException, BlankEntryException, UserNotFoundException {
		
		try (Connection con = ConnectionUtil.getConnection()) {

			if (username.matches("^\\s*$") || username == null) {
				throw new BlankEntryException("Nothing was entered");
			} else if (bankUserDAO.hasCorrectUsername(username,con)) {
				return true;
			} else
				throw new UserNotFoundException("Username not found. Try again.");
		}
	}
	
	public boolean hasCorrectPassword(String username, String password) throws SQLException, BlankEntryException, IncorrectPasswordException {
		
		try (Connection con = ConnectionUtil.getConnection()){
			if (password.matches("^\\s*$") || password == null) {
				throw new BlankEntryException("Nothing was entered");
			}else if(bankUserDAO.hasCorrectPassword(username, password, con)) {
				return true;
			}else
				throw new IncorrectPasswordException("WRONG PASSWORD");
		
		}
	}

}
