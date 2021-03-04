package com.bank.model;

public class Transfer {
	private String tranferID;
	private String sendingAccount;
	private String receivingAccount;
	private String amount;
	private String dateOfTransfer;
	private String status;
	
	public Transfer(String tranferID, String sendingAccount, String receivingAccount, String amount,
			String dateOfTransfer, String status) {
		super();
		this.tranferID = tranferID;
		this.sendingAccount = sendingAccount;
		this.receivingAccount = receivingAccount;
		this.amount = amount;
		this.dateOfTransfer = dateOfTransfer;
		this.status = status;
	}

	public String getTranferID() {
		return tranferID;
	}

	public void setTranferID(String tranferID) {
		this.tranferID = tranferID;
	}

	public String getSendingAccount() {
		return sendingAccount;
	}

	public void setSendingAccount(String sendingAccount) {
		this.sendingAccount = sendingAccount;
	}

	public String getReceivingAccount() {
		return receivingAccount;
	}

	public void setReceivingAccount(String receivingAccount) {
		this.receivingAccount = receivingAccount;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getDateOfTransfer() {
		return dateOfTransfer;
	}

	public void setDateOfTransfer(String dateOfTransfer) {
		this.dateOfTransfer = dateOfTransfer;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((dateOfTransfer == null) ? 0 : dateOfTransfer.hashCode());
		result = prime * result + ((receivingAccount == null) ? 0 : receivingAccount.hashCode());
		result = prime * result + ((sendingAccount == null) ? 0 : sendingAccount.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tranferID == null) ? 0 : tranferID.hashCode());
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
		Transfer other = (Transfer) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (dateOfTransfer == null) {
			if (other.dateOfTransfer != null)
				return false;
		} else if (!dateOfTransfer.equals(other.dateOfTransfer))
			return false;
		if (receivingAccount == null) {
			if (other.receivingAccount != null)
				return false;
		} else if (!receivingAccount.equals(other.receivingAccount))
			return false;
		if (sendingAccount == null) {
			if (other.sendingAccount != null)
				return false;
		} else if (!sendingAccount.equals(other.sendingAccount))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tranferID == null) {
			if (other.tranferID != null)
				return false;
		} else if (!tranferID.equals(other.tranferID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Transfer [ID:" + tranferID + " | From: " + sendingAccount + " | To: "
				+ receivingAccount + " | Amount " + amount + " | Date: " + dateOfTransfer + " | Status: " + status
				+ "]";
	}
	
	
	
	
	
}
