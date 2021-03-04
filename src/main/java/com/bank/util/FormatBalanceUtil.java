 package com.bank.util;

import com.bank.exceptions.InvalidInputException;

public class FormatBalanceUtil {

	public static Integer appToDatabase(String balance) throws InvalidInputException {

		
		
		int toDatabase;
		if (balance.contains(".")) {
			int decimal = balance.indexOf('.');
			if (balance.length() - 1 - decimal > 2) {
				throw new InvalidInputException(
						"Not a valid money amount. Please enter a number with at most 2 decimal places.");
			} else {
				if (balance.length() == 3) {
					toDatabase = Integer.parseInt(balance.substring(1, balance.length()));
				} else {
					toDatabase = Integer.parseInt(
							(((Float) (Float.parseFloat(balance) * 100)).toString()).substring(0, decimal + 2));
				}
			}
		} else {
			toDatabase = Integer.parseInt(balance) * 100;
		}

		return toDatabase;
	}

	public static String databaseToApp(Integer balance) {

		String balanceString = balance.toString();
		int digits = balanceString.length();
		if (digits == 1) {
			balanceString = "$0.0" + balance;
		} else if (digits == 2) {
			balanceString = "$0." + balance;
		} else {

			balanceString = "$" + balanceString.substring(0, digits - 2) + "."
					+ balanceString.substring(digits - 2, digits);
		}
		return balanceString;
	}

}
