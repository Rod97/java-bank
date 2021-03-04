package com.bank.util;

import java.util.UUID;

public class GenerateIDUtil {
	public static Long generateNewEmployeeID() {

		Long id = 0L;
		UUID temp = UUID.randomUUID();

		id = temp.getMostSignificantBits();
		if (id < 0) {
			id = Math.abs(id);
		}

		id = Long.parseLong("62" + id.toString().substring(0, 6));

		return id;
	}

	public static int generateNewAccountNumber() {

		Long id = 0L;
		UUID temp = UUID.randomUUID();

		id = temp.getMostSignificantBits();
		if (id < 0) {
			id = Math.abs(id);
		}

		int accountNumber = Integer.parseInt(id.toString().substring(0, 9));

		return accountNumber;
	}
}
