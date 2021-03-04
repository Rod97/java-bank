package com.bank.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.bank.model.Account;
import com.bank.model.Transfer;

public interface AccountsDAO {
	
	public Account deposit(int amount, int accountNumber, Connection con) throws SQLException;
	public Account withdraw(int amount, int accountNumber, Connection con) throws SQLException;
	public boolean applyForNewAccount(String accountType, int initialBalance, String username, Connection con) throws SQLException;
	public ArrayList<Account> getActiveAccountsByUsername(String username, Connection con) throws SQLException;
	public ArrayList<Account> getApplicationsByUsername(String username, Connection con) throws SQLException;
	public Account getAccountByAccountNumber(int accountNumber, String username, Connection con) throws SQLException;
	public void updateStatusOfApplication(int applicationNumber, String decision, Connection con) throws SQLException;
	public int sendFunds(int sendingAccount, int receivingAccount, int amount, Connection con) throws SQLException;
	public int receiveFunds(int receivingAccount, int sendingAccount, int amount, int transferID, Connection con) throws SQLException;
	public int rejectFunds(int transferID, Connection con) throws SQLException;
	public ArrayList<Transfer> getTransfersByAccountNumber(int accountNumber, Connection con) throws SQLException;
	public ArrayList<Transfer> getTransfers(Connection con) throws SQLException;

}
