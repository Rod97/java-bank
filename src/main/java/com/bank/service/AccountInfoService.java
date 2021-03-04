package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.exceptions.InvalidInputException;
import com.bank.exceptions.NoAccountsFoundException;
import com.bank.model.Account;
import com.bank.util.ConnectionUtil;

public class AccountInfoService {
	
	private AccountsDAO accountsDAO = new AccountsDAOImpl();
	
	public AccountInfoService() {}
	
	public AccountInfoService(AccountsDAO accountsDAO) {
		this.accountsDAO = accountsDAO;
	}

	public ArrayList<Account> getAccountsByUsername(String username) throws SQLException, NoAccountsFoundException {
		ArrayList<Account> userAccounts = new ArrayList<>();
		try (Connection con = ConnectionUtil.getConnection()) {
			userAccounts = accountsDAO.getActiveAccountsByUsername(username, con);
			
			if(userAccounts == null) {
				throw new NoAccountsFoundException("No active accounts were found for this  username");
			}

			return userAccounts;
		}
	}

	public Account getAccountByAccountNumber(String accountNumber, String username) throws SQLException, NoAccountsFoundException, InvalidInputException {
		Account account = null;
		try (Connection con = ConnectionUtil.getConnection()) {
			
			if(accountNumber.length() != 9) {
				throw new InvalidInputException("Not a valid account number");
			}
			account = accountsDAO.getAccountByAccountNumber(Integer.parseInt(accountNumber), username, con);
			
			if(account == null) {
				throw new NoAccountsFoundException("This account was not found for " + username);
			}
		}
		
		return account;
	}

	public ArrayList<Account> getApplicationsByUsername(String username) throws SQLException, NoAccountsFoundException {
		ArrayList<Account> applications= null;
		
		try (Connection con = ConnectionUtil.getConnection()){
			applications = accountsDAO.getApplicationsByUsername(username, con);
			
			if(applications == null) {
				throw new NoAccountsFoundException("There are currently no applications awaiting review.");
			}
		}
		
		return applications;
	}

}
