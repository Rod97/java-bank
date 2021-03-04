package com.bank.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.NoAccountsFoundException;
import com.bank.exceptions.UserNotFoundException;
import com.bank.model.Account;
import com.bank.service.AccountActionsService;
import com.bank.service.AccountInfoService;
import com.bank.service.UserService;
import com.bank.util.InputCheckerUtil;

public class ApplicationDecisionMenu implements Menu {

	private AccountInfoService accountInfoService = new AccountInfoService();
	private UserService userService = new UserService();
	private AccountActionsService accountActionsService = new AccountActionsService();
	private Logger logger = Logger.getLogger(ApplicationDecisionMenu.class);

	@Override
	public void display() {

		String username = "";
		ArrayList<Account> pendingAccounts = null;
		boolean userWasFound = false;
		boolean applicationWasFound = false;
		String applicationNumber = "0";
		int applicationNum = 0;
		String decision = "0";

		try {
			do {
				System.out.println("Enter the username of the person whose application(s) you would like to see.\n");
				username = Menu.sc.nextLine();

				try {
					userWasFound = userService.hasCorrectUsername(username);
				} catch (BlankEntryException e) {

					System.out.println(e.getMessage());
				} catch (UserNotFoundException e) {
					logger.info("Username " + username + " was not found.");
					System.out.println(e.getMessage());
				}

				if (userWasFound) {
					pendingAccounts = accountInfoService.getApplicationsByUsername(username);
					for (Account application : pendingAccounts) {
						if (application.getApplicationStatus().equals("PENDING")) {
							System.out.println(application);
						}
					}
				}
			} while (!userWasFound);

			do {
				System.out.println("\n==================\n");
				System.out.println(
						"Enter the application number to make a decision on that application, or enter E to exit.\n");
				applicationNumber = Menu.sc.nextLine();
				if (applicationNumber.equals("e"))
					continue;
				else if (InputCheckerUtil.isNumberNoDecimal(applicationNumber)) {

					applicationNum = Integer.parseInt(applicationNumber);

					for (Account application : pendingAccounts) {
						if (application.getApplicationNumber() == applicationNum) {
							applicationWasFound = true;
						}
					}
					if (!applicationWasFound) {
						logger.info("Application was not found with application number " + applicationNum + " for user "
								+ username);
						System.out.println("No application was found with that application number.");
					}
				} else {
					System.out.println("Not a valid application number. Please input a number from the first column.");
					applicationNumber = "0";
					continue;
				}

				do {
					System.out.println("\n==================\n");
					for (Account account : pendingAccounts) {
						if (applicationNum == account.getApplicationNumber()) {
							System.out.println(account);
						}
					}
					System.out.println("\n1.) Approve");
					System.out.println("2.) Deny");
					System.out.println("E.) Exit");

					decision = Menu.sc.nextLine().toLowerCase();
					switch (decision) {
					case "1":
						decision = "APPROVED";
						accountActionsService.updateStatusOfApplication(applicationNum, decision);
						logger.info("Application " + applicationNum + " successfully approved.");
						break;
					case "2":
						decision = "DENIED";
						accountActionsService.updateStatusOfApplication(applicationNum, decision);
						logger.info("Application " + applicationNum + " successfully denied.");
						break;
					case "e":
						break;

					default:
						System.out.println("Not a valid option.\n");
						decision = "0";
						break;
					}
				} while (decision.equals("0"));
			} while (applicationNumber.equals("0"));

		} catch (SQLException f) {
			logger.warn(f);
			System.out.println(
					"An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, ask someone to fix it.\n");
		} catch (NoAccountsFoundException e) {
			// LOG here
			logger.info("No applications were found for " + username);
			System.out.println(e.getMessage());
		}
	}

}
