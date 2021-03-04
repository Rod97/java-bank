package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.dao.BankUserDAO;
import com.bank.dao.BankUserDAOImpl;
import com.bank.exceptions.InvalidInputException;
import com.bank.util.ConnectionUtil;
import com.bank.util.FormatBalanceUtil;

public class AccountApplicationService {

	public AccountsDAO accountsDAO;
	public BankUserDAO bankUserDAO;

	public AccountApplicationService() {
		this.accountsDAO = new AccountsDAOImpl();
		this.bankUserDAO = new BankUserDAOImpl();
	}

	public boolean applyForNewAccount(String accountType, String initialBalance, String currentUser)
			throws SQLException, InvalidInputException {
		try (Connection con = ConnectionUtil.getConnection()) {

			int formattedBalance = FormatBalanceUtil.appToDatabase(initialBalance);
			return accountsDAO.applyForNewAccount(accountType, formattedBalance, currentUser, con);
		}
	}
}
