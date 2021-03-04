package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.exceptions.InvalidInputException;
import com.bank.model.Transfer;
import com.bank.util.ConnectionUtil;

public class TransferService {

	AccountsDAO accountsDAO = new AccountsDAOImpl();
	private final Logger logger = Logger.getLogger(TransferService.class);

	public String sendFunds(int sendingAccount, int receivingAccount, int amount)
			throws SQLException, InvalidInputException {

		try (Connection con = ConnectionUtil.getConnection()) {
			int result = accountsDAO.sendFunds(sendingAccount, receivingAccount, amount, con);

			if (result == 2) {
				throw new InvalidInputException(
						"You are attempting to send more money than is in your account. Try again.");
			} else if (result == 1) {
				logger.warn(new SQLException("Failed transaction"));
				return "An unknown error caused your transfer to fail.";
			} else {
				
				logger.info("SENDING $" + amount + " FROM " + sendingAccount + " TO " +	receivingAccount);
				return "Transfer posted successfully. It must now be accepted from the receiving account.\nFunds will be subtracted from your account "
						+ "once the transfer has been accepted.\nYou may submit additional money transfers, but keep in mind that if your balance drops "
						+ "below the amount you wish to send at the time of accepting the transfer, the transfer will fail.";
			}
		}
	}

	public String receiveFunds(int receivingAccount, int sendingAccount, int amount, int transferID) throws SQLException {

		try (Connection con = ConnectionUtil.getConnection()) {
			int result = accountsDAO.receiveFunds(receivingAccount, sendingAccount, amount, transferID, con);
			if (result == 2) {
				logger.info(sendingAccount + " no longer has enough funds to cover this transfer");
				return "The sending account no longer has enough funds to cover this transfer.";
			} else if (result == 1) {			
				logger.warn("An unknown error caused transfer " + transferID + " to fail.", new SQLException("Failed transaction"));
				return "An unknown error caused your transfer to fail.";
			} else {
				 logger.info("Transfer " + transferID + " accepted by account " + receivingAccount);
				return "Transfer accepted successfully.";
			}
		}
	}

	public String rejectFunds(int transferID) throws SQLException {
		try (Connection con = ConnectionUtil.getConnection()) {
			int result = accountsDAO.rejectFunds(transferID, con);
			if (result == 1) {
				logger.warn(new SQLException("Failed transaction"));
				return "An unknown error caused your transfer to fail.";
			} else {
				logger.info("Transfer " + transferID + " rejected.");
				return "Transfer rejected successfully.";
			}
		}
	}

	public ArrayList<Transfer> getTransfers() throws SQLException {
		ArrayList<Transfer> transfers;
		try (Connection con = ConnectionUtil.getConnection()) {
			transfers = accountsDAO.getTransfers(con);
		}
		return transfers;
	}

	public ArrayList<Transfer> getTransfersByAccountNumber(int accountNumber) throws SQLException {
		ArrayList<Transfer> transfers;
		try (Connection con = ConnectionUtil.getConnection()) {
			transfers = accountsDAO.getTransfersByAccountNumber(accountNumber, con);
		}

		return transfers;

	}
}
