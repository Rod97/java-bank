package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.InvalidInputException;
import com.bank.model.Account;
import com.bank.util.ConnectionUtil;
import com.bank.util.FormatBalanceUtil;

public class AccountActionsService {

	public AccountsDAO accountsDAO = new AccountsDAOImpl();

	public Account deposit(Account accountBeingAccessed, String depositAmount)
			throws SQLException, InvalidInputException, BlankEntryException {

		Account account = null;

		try (Connection con = ConnectionUtil.getConnection()) {

			if (depositAmount.matches("^\\s*$") || depositAmount == null) {
				throw new BlankEntryException("Nothing was entered");
			}
			int formattedDeposit = FormatBalanceUtil.appToDatabase(depositAmount);

			if (formattedDeposit <= 0) {
				throw new InvalidInputException("Deposit amount must be greater than 0");
			}

			account = accountsDAO.deposit(formattedDeposit, accountBeingAccessed.getAccountNumber(), con);

		}
		return account;
	}

	public Account withdraw(Account accountBeingAccessed, String withdrawalAmount) throws SQLException, InvalidInputException, BlankEntryException {
		Account account = null;

		try (Connection con = ConnectionUtil.getConnection()) {
			
			if (withdrawalAmount.matches("^\\s*$") || withdrawalAmount == null) {
				throw new BlankEntryException("Nothing was entered");
			}

			int formattedWithdrawal = FormatBalanceUtil.appToDatabase(withdrawalAmount);
			int formattedCurrentBalance = Integer.parseInt(accountBeingAccessed.getBalance());

			if (formattedWithdrawal > formattedCurrentBalance) {
				throw new InvalidInputException("OOPS! You are attempting to withdraw more money than is in your account.");
			}else if(formattedWithdrawal <= 0) {
				throw new InvalidInputException("Withdrawal amount must be greater than 0");
			}

			account = accountsDAO.withdraw(formattedWithdrawal, accountBeingAccessed.getAccountNumber(), con);

		}
		return account;
	}

	public void updateStatusOfApplication(int applicationNumber, String decision) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()){
			 
			accountsDAO.updateStatusOfApplication(applicationNumber, decision, con);
			System.out.println("Account successfully " + decision.toLowerCase() +".");
		}
	}
}
