package com.bank.model;

import java.time.LocalDate;

public class Account {
	private int status; // pending, active, or closed (1,2,3)
	private int applicationNumber; // for pending accounts
	private int accountNumber; // for active and closed accounts
	private String accountOwner; // for active and closed accounts
	private String accountType;
	private String balance; // for pending and active accounts
	private String applicationStatus; // for pending accounts
	private LocalDate openDate; // for active and closed accounts
	private LocalDate closeDate; // for closed accounts

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(int applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public int getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(int accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public void setAccountOwner(String accountOwner) {
		this.accountOwner = accountOwner;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getBalance() {
		String formattedBalance = balance.substring(1,balance.indexOf(".")) + balance.substring(balance.indexOf(".")+1);
		return formattedBalance;
		
	}

	public void setBalance(String balance) {
		this.balance = balance;
	}

	public String getApplicationStatus() {
		return applicationStatus;
	}

	public void setApplicationStatus(String applicationStatus) {
		this.applicationStatus = applicationStatus;
	}

	public LocalDate getCloseDate() {
		return closeDate;
	}

	public void setCloseDate(LocalDate closeDate) {
		this.closeDate = closeDate;
	}

	public LocalDate getOpenDate() {
		return openDate;
	}

	public void setOpenDate(LocalDate openDate) {
		this.openDate = openDate;
	}

	// PENDING ACCOUNT CONSTRUCTOR
	public Account(int status, int applicationNumber, String accountOwner, String accountType, String balance, String applicationStatus) {
		this.status = status;
		this.applicationNumber = applicationNumber;
		this.accountOwner = accountOwner;
		this.accountType = accountType;
		this.balance = balance;
		this.applicationStatus = applicationStatus;
	}

	// ACTIVE ACCOUNT CONSTRUCTOR
	public Account(int status, int accountNumber, String accountOwner, String accountType, String balance,
			LocalDate openDate) {
		this.status = status;
		this.accountNumber = accountNumber;
		this.accountOwner = accountOwner;
		this.accountType = accountType;
		this.balance = balance;
		this.openDate = openDate;
	}

	// CLOSED ACCOUNT CONSTRUCTOR
	public Account(int status, int accountNumber, String accountType, LocalDate closeDate, LocalDate openDate) {
		super();
		this.status = status;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.closeDate = closeDate;
		this.openDate = openDate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + accountNumber;
		result = prime * result + ((accountOwner == null) ? 0 : accountOwner.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((applicationStatus == null) ? 0 : applicationStatus.hashCode());
		result = prime * result + ((balance == null) ? 0 : balance.hashCode());
		result = prime * result + ((closeDate == null) ? 0 : closeDate.hashCode());
		result = prime * result + ((openDate == null) ? 0 : openDate.hashCode());
		result = prime * result + status;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Account other = (Account) obj;
		if (accountNumber != other.accountNumber)
			return false;
		if (accountOwner == null) {
			if (other.accountOwner != null)
				return false;
		} else if (!accountOwner.equals(other.accountOwner))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (applicationStatus == null) {
			if (other.applicationStatus != null)
				return false;
		} else if (!applicationStatus.equals(other.applicationStatus))
			return false;
		if (balance == null) {
			if (other.balance != null)
				return false;
		} else if (!balance.equals(other.balance))
			return false;
		if (closeDate == null) {
			if (other.closeDate != null)
				return false;
		} else if (!closeDate.equals(other.closeDate))
			return false;
		if (openDate == null) {
			if (other.openDate != null)
				return false;
		} else if (!openDate.equals(other.openDate))
			return false;
		if (status != other.status)
			return false;
		return true;
	}

	@Override
	public String toString() {

		if (this.status == 1) { // PENDING ACCOUNT
			return "[#" + applicationNumber + " | " + accountType + " | Pending Initial Balance: " + balance + " | Application Status:"
					+ applicationStatus + "]";
		} else if (this.status == 2) { // ACTIVE ACCOUNT
			return "[Account: " + accountNumber + " | " + accountType + " | Balance: " + balance + " | Opened On: "
					+ openDate + "]";
		} else { // CLOSED ACCOUNT
			return "[Account: " + accountNumber + " | " + accountType + " | Opened On: " + openDate + " | Closed On"
					+ closeDate + "]";
		}
	}

}
