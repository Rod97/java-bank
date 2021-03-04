package com.bank.ui;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bank.exceptions.InvalidInputException;
import com.bank.service.AccountApplicationService;

public class AccountApplicationMenu implements Menu {

	static final Logger logger = Logger.getLogger(AccountApplicationMenu.class);

	String currentUser;

	AccountApplicationService accountApplicationService = new AccountApplicationService();

	public AccountApplicationMenu(String username) {
		currentUser = username;
	}

	@Override
	public void display() {

		String choice = "0";
		String accountType = null;
		String initialBalance = null;
		boolean successfullyApplied = false;

		System.out.println("Thank you for your interest in opening a new account.");

		do {
			System.out.println("\nPlease select the type of account you would like to apply for.");
			System.out.println("1.) Checking Account");
			System.out.println("2.) Savings Account");
			System.out.println("3.) Exit");

			choice = Menu.sc.nextLine();

			switch (choice) {
			case "1":
				accountType = "CHK";

				do {
					System.out.println("1.) Cancel");
					System.out.println("Enter initial deposit ammount: ");
					initialBalance = Menu.sc.nextLine();

					if (initialBalance.equals("1"))
						break;

					try {
						successfullyApplied = accountApplicationService.applyForNewAccount(accountType, initialBalance,
								currentUser);
					} catch (SQLException f) {
						logger.warn(f);
						System.out.println(
								"An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, please go away.\n");

					} catch (InvalidInputException e) {

						System.out.println(e.getMessage());
					}
				} while (!successfullyApplied);

				if (successfullyApplied) {
					// Log application event here

					logger.info("User " + currentUser + " successfully submitted an application.\n");

					System.out.println(
							"Account application successfully recieved. Please allow 1-3 business months for us to process your application."
									+ "\nYou can check the status of your application from the User Main Menu.");
				}
				break;
			case "2":
				accountType = "SAV";

				do {
					System.out.println("E.) Exit");
					System.out.println("Enter initial deposit ammount: ");
					initialBalance = Menu.sc.nextLine();

					if (initialBalance.equalsIgnoreCase("e"))
						break;

					try {
						successfullyApplied = accountApplicationService.applyForNewAccount(accountType, initialBalance,
								currentUser);
					} catch (SQLException f) {
						logger.warn(f);
						System.out.println(
								"An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, please go away.\n");

					} catch (InvalidInputException e) {

						System.out.println(e.getMessage());
					}
				} while (!successfullyApplied);
				if (successfullyApplied) {
					// Log application event here

					logger.info("User " + currentUser + " successfully submitted an application.\n");

					System.out.println(
							"Account application successfully recieved. Please allow 1-3 business months for us to process your application."
									+ "\nYou can check the status of your application from the User Home Screen.");
				}
				break;
			case "3":
				break;
			default:
				System.out.println("Not a valid option. Please try again.");
				choice = "0";
			}

		} while (choice.equals("0"));

	}

}
