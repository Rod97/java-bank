package com.bank.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.InvalidInputException;
import com.bank.exceptions.NoAccountsFoundException;
import com.bank.model.Account;
import com.bank.service.AccountActionsService;
import com.bank.service.AccountInfoService;

public class AccountMenu implements Menu {

	private String currentUser;
	private AccountInfoService accountInfoService = new AccountInfoService();
	private AccountActionsService accountActionsService = new AccountActionsService();
	private final Logger logger = Logger.getLogger(AccountMenu.class);

	public AccountMenu(String username) {
		currentUser = username;
	}

	@Override
	public void display() {

		ArrayList<Account> userAccounts = null;

		try {
			userAccounts = accountInfoService.getAccountsByUsername(currentUser);
		} catch (SQLException f) {

			logger.warn(f);
			System.out.println(
					"An unexepected error occured while trying to retrieve your accounts. Try again.\nIf the problem continues, please go away.\n");
			return;

		} catch (NoAccountsFoundException e) {
			logger.warn("No accounts were found for user " + currentUser);
			System.out.println(e.getMessage());
			return;
		}

		String choice = "0";
		do {

			System.out.println("===" + currentUser + "'s Accounts===");

			for (Account account : userAccounts) {
				System.out.println(account);
			}

			Account accountBeingAccessed = null;

			do {
				System.out.println("\nEnter an account number for more options, or enter E to exit.");
				choice = Menu.sc.nextLine();

				if (choice.equals("e"))
					continue;

				try {
					accountBeingAccessed = accountInfoService.getAccountByAccountNumber(choice, currentUser);
					if (accountBeingAccessed != null) {

						do {
							choice = "5";
							System.out.println("\n" + accountBeingAccessed);
							System.out.println("\nWhat would you like to do?");
							System.out.println("\n1.) Deposit");
							System.out.println("2.) Withdraw");
							System.out.println("3.) Account Transfers");
							System.out.println("E.) Exit");

							choice = Menu.sc.nextLine();

							switch (choice) {
							case "1":

								String depositAmount;

								System.out.println("Enter the amount you would like to deposit, or enter E to exit.");

								depositAmount = Menu.sc.nextLine();

								if (depositAmount.equals("e")) {
									break;
								}

								try {
									accountBeingAccessed = accountActionsService.deposit(accountBeingAccessed,
											depositAmount);
									System.out.println("\n===============\nDeposit successful.\n===============");
									logger.info("User " + currentUser + " deposited $" + depositAmount + " into "
											+ accountBeingAccessed);
								} catch (SQLException f) {

									logger.warn(f);
									System.out.println(
											"An unexepected error occured while trying to complete your deposit. Try again.\nIf the problem continues, please go away.\n");

								} catch (InvalidInputException | BlankEntryException e) {

									System.out.println(e.getMessage());
								}

								choice = "5";
								break;
							case "2":

								String withdrawalAmount;

								System.out.println("Enter the amount you would like to withdraw, or enter E to exit.");

								withdrawalAmount = Menu.sc.nextLine();

								if (withdrawalAmount.equals("e")) {
									break;
								}

								try {
									accountBeingAccessed = accountActionsService.withdraw(accountBeingAccessed,
											withdrawalAmount);
									System.out.println("\n===============\nWithdrawal successful.\n===============");
									logger.info("User " + currentUser + " withdrew $" + withdrawalAmount + " from "
											+ accountBeingAccessed);
								} catch (SQLException f) {
									logger.warn(f);
									System.out.println(
											"An unexepected error occured while trying to complete your deposit. Try again.\nIf the problem continues, please go away.\n");

								} catch (InvalidInputException | BlankEntryException e) {

									System.out.println(e.getMessage());

								}
								choice = "5";
								break;
							case "3":
								Menu transferMenu = new TransferMenu(accountBeingAccessed);
								transferMenu.display();
								choice = "5";
							case "e":
								break;
							default:
								System.out.println("Not a valid option. Please try again");
								choice = "5";

							}

						} while (choice.equals("5"));

					}
				} catch (SQLException f) {
					logger.warn(f);
					System.out.println(
							"An unexepected error occured while trying to retreive account information. Try again.\nIf the problem continues, please go away.\n");
				} catch (NoAccountsFoundException e) {
					logger.info("No accounts were found for " + currentUser, e);
					System.out.println(e.getMessage());

				} catch (InvalidInputException e1) {
					System.out.println(e1.getMessage());
				}
			} while (choice.equals("0"));

		} while (choice.equals("0"));

	}

}
