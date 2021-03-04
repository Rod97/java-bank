package com.bank.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.bank.model.Account;
import com.bank.model.Transfer;
import com.bank.util.FormatBalanceUtil;
import com.bank.util.GenerateIDUtil;

public class AccountsDAOImpl implements AccountsDAO {

	@Override
	public Account deposit(int amount, int accountNumber, Connection con) throws SQLException {

		con.setAutoCommit(false);
		Account account = null;
		String sql = "SELECT balance FROM bank_app_data.active_accounts WHERE account_number = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, accountNumber);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			int currentBalance = rs.getInt("balance");

			int updatedBalance = currentBalance + amount;

			sql = "UPDATE bank_app_data.active_accounts SET balance = ? WHERE account_number = ?;";
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, updatedBalance);
			pstmt.setInt(2, accountNumber);

			int count = pstmt.executeUpdate();

			if (count != 1)
				return account;

			con.commit();

			sql = "SELECT * FROM bank_app_data.active_accounts WHERE account_number = ?;";
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, accountNumber);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String accountOwner = rs.getString("account_owner");
				String accountType = rs.getString("account_type");
				String newBalance = FormatBalanceUtil.databaseToApp(rs.getInt("balance"));
				LocalDate openDate = rs.getDate("open_date").toLocalDate();

				account = new Account(2, accountNumber, accountOwner, accountType, newBalance, openDate);
			}

		}

		return account;
	}

	@Override
	public Account withdraw(int amount, int accountNumber, Connection con) throws SQLException {

		con.setAutoCommit(false);

		Account account = null;
		String sql = "SELECT balance FROM bank_app_data.active_accounts WHERE account_number = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, accountNumber);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			int currentBalance = rs.getInt("balance");

			int updatedBalance = currentBalance - amount;

			sql = "UPDATE bank_app_data.active_accounts SET balance = ? WHERE account_number = ?;";
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, updatedBalance);
			pstmt.setInt(2, accountNumber);

			int count = pstmt.executeUpdate();

			if (count != 1)
				return account;

			con.commit();

			sql = "SELECT * FROM bank_app_data.active_accounts WHERE account_number = ?;";
			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, accountNumber);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				String accountOwner = rs.getString("account_owner");
				String accountType = rs.getString("account_type");
				String newBalance = FormatBalanceUtil.databaseToApp(rs.getInt("balance"));
				LocalDate openDate = rs.getDate("open_date").toLocalDate();

				account = new Account(2, accountNumber, accountOwner, accountType, newBalance, openDate);
			}

		}

		return account;
	}

	@Override
	public boolean applyForNewAccount(String accountType, int initialBalance, String username, Connection con)
			throws SQLException {

		String sql = "INSERT INTO bank_app_data.pending_accounts(account_owner,account_type,initial_balance_cents,status) VALUES(?,?,?,'PENDING');";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(1, username);
		pstmt.setString(2, accountType);
		pstmt.setInt(3, initialBalance);

		int count = pstmt.executeUpdate();

		if (count != 1) {
			return false;
		}
		return true;
	}

	@Override
	public ArrayList<Account> getActiveAccountsByUsername(String username, Connection con) throws SQLException {
		ArrayList<Account> userAccounts = new ArrayList<>();

		String sql = "SELECT * FROM bank_app_data.active_accounts WHERE account_owner = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(1, username);

		ResultSet rs = pstmt.executeQuery();

		int status = 2;
		Account tempAccount = null;
		while (rs.next()) {
			int accountNumber = rs.getInt("account_number");
			String accountOwner = rs.getString("account_owner");
			String accountType = rs.getString("account_type");
			String balance = FormatBalanceUtil.databaseToApp(rs.getInt("balance"));
			LocalDate openDate = (rs.getDate("open_date")).toLocalDate();

			tempAccount = new Account(status, accountNumber, accountOwner, accountType, balance, openDate);

			userAccounts.add(tempAccount);
		}

		return userAccounts;

	}

	@Override
	public ArrayList<Account> getApplicationsByUsername(String username, Connection con) throws SQLException {

		ArrayList<Account> userAccounts = new ArrayList<>();

		String sql = "SELECT * FROM bank_app_data.pending_accounts WHERE account_owner = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(1, username);

		ResultSet rs = pstmt.executeQuery();

		int status = 1;
		Account tempAccount = null;
		while (rs.next()) {

			int applicationNumber = rs.getInt("application_number");
			String accountOwner = rs.getString("account_owner");
			String accountType = rs.getString("account_type");
			String initialBalance = FormatBalanceUtil.databaseToApp(rs.getInt("initial_balance_cents"));
			String applicationStatus = rs.getString("status");

			tempAccount = new Account(status, applicationNumber, accountOwner, accountType, initialBalance,
					applicationStatus);

			userAccounts.add(tempAccount);
		}
		return userAccounts;
	}

	@Override
	public Account getAccountByAccountNumber(int accountNumber, String username, Connection con) throws SQLException {
		Account account = null;

		String sql = "SELECT * FROM bank_app_data.active_accounts WHERE account_number = ? AND account_owner = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, accountNumber);
		pstmt.setString(2, username);

		ResultSet rs = pstmt.executeQuery();

		int status = 2;
		if (rs.next()) {
			int accountNum = rs.getInt("account_number");
			String accountOwner = rs.getString("account_owner");
			String accountType = rs.getString("account_type");
			String balance = FormatBalanceUtil.databaseToApp(rs.getInt("balance"));
			LocalDate openDate = (rs.getDate("open_date")).toLocalDate();

			account = new Account(status, accountNum, accountOwner, accountType, balance, openDate);
		}

		return account;
	}

	@Override
	public void updateStatusOfApplication(int applicationNumber, String decision, Connection con) throws SQLException {

		con.setAutoCommit(false);
		String sql = "UPDATE bank_app_data.pending_accounts SET status = ? WHERE application_number = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setString(1, decision);
		pstmt.setInt(2, applicationNumber);

		int count = pstmt.executeUpdate();

		if (count != 1) {
			return;
		}

		con.commit();

		if (decision.equals("APPROVED")) {
			sql = "SELECT account_owner, account_type, initial_balance_cents FROM bank_app_data.pending_accounts WHERE application_number = ?;";

			pstmt = con.prepareStatement(sql);

			pstmt.setInt(1, applicationNumber);

			ResultSet rs = pstmt.executeQuery();

			int accountNumber = GenerateIDUtil.generateNewAccountNumber();

			if (rs.next()) {
				sql = "INSERT INTO bank_app_data.active_accounts VALUES(?,?,?,?,?);";

				pstmt = con.prepareStatement(sql);

				pstmt.setInt(1, accountNumber);
				pstmt.setString(2, rs.getString("account_owner"));
				pstmt.setString(3, rs.getString("account_type"));
				pstmt.setInt(4, rs.getInt("initial_balance_cents"));
				pstmt.setDate(5, Date.valueOf(LocalDate.now()));

				count = pstmt.executeUpdate();
				if (count != 1)
					return;
				else
					con.commit();
			}
		}
	}

	@Override
	public int sendFunds(int sendingAccount, int receivingAccount, int amount, Connection con) throws SQLException {

		con.setAutoCommit(false);

		String checkBalance = "SELECT balance FROM active_accounts WHERE account_number = ?;";

		PreparedStatement pstmt = con.prepareStatement(checkBalance);
		pstmt.setInt(1, sendingAccount);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			int balanceSending = rs.getInt("balance");

			if (balanceSending < amount) {
				return 2;
			}
		}

		String sql = "INSERT INTO money_transfers(sending_account ,receiving_account ,amount ,date_of_transfer ,status ) VALUES(?,?,?,?,?);";

		pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, sendingAccount);
		pstmt.setInt(2, receivingAccount);
		pstmt.setInt(3, amount);
		pstmt.setDate(4, Date.valueOf(LocalDate.now()));
		pstmt.setString(5, "PENDING");

		int count = pstmt.executeUpdate();

		if (count != 1) {
			return 1; // change not committed
		}

		con.commit();
		return 0;// change committed

	}

	@Override
	public int receiveFunds(int receivingAccount, int sendingAccount, int amount, int transferID, Connection con) throws SQLException {

		int totalMoneyStart = 0;
		int senderBalance = 0;
		int receiverBalance = 0;
		con.setAutoCommit(false);

		String checkBalance = "SELECT balance FROM active_accounts WHERE account_number = ?;";

		PreparedStatement pstmt = con.prepareStatement(checkBalance);
		pstmt.setInt(1, sendingAccount);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			int balanceSending = rs.getInt("balance");

			if (balanceSending < amount) {
				return 2;
			}

		}

		int[] accountBalance = new int[2];
		String checkTotalMoney = "SELECT balance FROM active_accounts WHERE account_number IN (?,?);";

		pstmt = con.prepareStatement(checkTotalMoney);
		pstmt.setInt(1, sendingAccount);
		pstmt.setInt(2, receivingAccount);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			int i = 0;
			accountBalance[i] = rs.getInt("balance");
			totalMoneyStart += accountBalance[i];
		}

		String readSend = "SELECT * FROM active_accounts WHERE account_number = ?";
		pstmt = con.prepareStatement(readSend);
		pstmt.setInt(1, sendingAccount);

		rs = pstmt.executeQuery();

		if (rs.next()) {
			senderBalance = rs.getInt("balance");
			senderBalance -= amount;

			String writeSend = "UPDATE active_accounts SET balance = ? WHERE account_number = ?;";
			pstmt = con.prepareStatement(writeSend);
			pstmt.setInt(1, senderBalance);
			pstmt.setInt(2, sendingAccount);

			int count = pstmt.executeUpdate();

			if (count != 1) {
				return 1;
			}
		} else {
			return 1;
		}

		String readReceive = "SELECT * FROM active_accounts WHERE account_number = ?;";
		pstmt = con.prepareStatement(readReceive);
		pstmt.setInt(1, receivingAccount);

		rs = pstmt.executeQuery();

		if (rs.next()) {
			receiverBalance = rs.getInt("balance");
			receiverBalance += amount;

			String writeReceive = "UPDATE active_accounts SET balance = ? WHERE account_number = ?;";
			pstmt = con.prepareStatement(writeReceive);
			pstmt.setInt(1, receiverBalance);
			pstmt.setInt(2, receivingAccount);

			int count = pstmt.executeUpdate();

			if (count != 1) {
				return 1;
			}

		} else {
			return 1;
		}

		if (totalMoneyStart != receiverBalance + senderBalance) {
			return 1;
		}
		
		String sql = "UPDATE money_transfers SET status = 'ACCEPTED' WHERE transfer_id = ?;";
		
		pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, transferID);
		int count = pstmt.executeUpdate();
		
		if(count != 1);

		con.commit();
		return 0;// committed
	}

	@Override
	public int rejectFunds(int transferID, Connection con) throws SQLException {
		con.setAutoCommit(false);

		String sql = "UPDATE money_transfers SET status = 'REJECTED' WHERE transfer_id = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);
		pstmt.setInt(1, transferID);

		int count = pstmt.executeUpdate();

		if (count != 1) {
			return 1; // not committed
		}
		con.commit();
		return 0;// committed
	}

	@Override
	public ArrayList<Transfer> getTransfersByAccountNumber(int accountNumber, Connection con) throws SQLException {
		ArrayList<Transfer> transfers = new ArrayList<>();

		String sql = "SELECT * FROM money_transfers WHERE sending_account  = ? OR receiving_account = ?;";

		PreparedStatement pstmt = con.prepareStatement(sql);
		
		pstmt.setInt(1, accountNumber);
		pstmt.setInt(2, accountNumber);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			String transferID = rs.getString("transfer_id");
			String sendingAccount = rs.getString("sending_account");
			String receivingAccount = rs.getString("receiving_account");
			String amount = FormatBalanceUtil.databaseToApp(rs.getInt("amount"));
			String dateOfTransfer = rs.getDate("date_of_transfer").toString();
			String status = rs.getString("status");

			transfers.add(new Transfer(transferID, sendingAccount, receivingAccount, amount, dateOfTransfer, status));
		}

		return transfers;
	}

	@Override
	public ArrayList<Transfer> getTransfers(Connection con) throws SQLException {

		ArrayList<Transfer> transfers = new ArrayList<>();

		String sql = "SELECT * FROM money_transfers;";

		PreparedStatement pstmt = con.prepareStatement(sql);

		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			String transferID = rs.getString("transfer_id");
			String sendingAccount = rs.getString("sending_account");
			String receivingAccount = rs.getString("receiving_account");
			String amount = FormatBalanceUtil.databaseToApp(rs.getInt("amount"));
			String dateOfTransfer = rs.getDate("date_of_transfer").toString();
			String status = rs.getString("status");

			transfers.add(new Transfer(transferID, sendingAccount, receivingAccount, amount, dateOfTransfer, status));
		}

		return transfers;

	}
}
