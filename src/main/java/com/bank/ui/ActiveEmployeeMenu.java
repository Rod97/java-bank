package com.bank.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.NoAccountsFoundException;
import com.bank.exceptions.UserNotFoundException;
import com.bank.model.Account;
import com.bank.model.Transfer;
import com.bank.service.AccountInfoService;
import com.bank.service.TransferService;
import com.bank.service.UserService;

public class ActiveEmployeeMenu implements Menu {

	private UserService userService = new UserService();
	private AccountInfoService accountInfoService = new AccountInfoService();
	private TransferService transferService = new TransferService();
	private final Logger logger = Logger.getLogger(ActiveEmployeeMenu.class);

	public ActiveEmployeeMenu() {
	}

	@Override
	public void display() {

		String choice = "0";

		System.out.println("\n======Welcome======");
		do {
			System.out.println("\n1.) Approve or deny pending account applications.");
			System.out.println("2.) View customer accounts.");
			System.out.println("3.) View Transfers");
			System.out.println("4.) Onboard New Employee"); 
			System.out.println("E.) Exit");

			choice = Menu.sc.nextLine().toLowerCase();

			switch (choice) {
			case "1":
				Menu applicationDecisionMenu = new ApplicationDecisionMenu();
				applicationDecisionMenu.display();
				choice = "0";
				break;

			case "2":
				System.out.println("Enter the username associated with the accounts you wish to see.");

				String username = Menu.sc.nextLine();
				boolean userWasFound = false;
				try {
					userWasFound = userService.hasCorrectUsername(username);
				} catch (SQLException f) {
					logger.warn(f);
					System.out.println(
							"An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, please go away.\n");
				} catch (BlankEntryException | UserNotFoundException e) {

					System.out.println(e.getMessage());
				}
				if (userWasFound) {
					try {
						for (Account account : accountInfoService.getAccountsByUsername(username)) {
							System.out.println(account);
						}
					} catch (SQLException f) {
						logger.warn(f);
						System.out.println(
								"An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, ask someone to fix it.\n");
					} catch (NoAccountsFoundException e) {
						// TODO
						System.out.println(e.getMessage());
					}
				}
				choice = "0";
				break;

			case "3":
				try {

					ArrayList<Transfer> currentTransfers = transferService.getTransfers();

					System.out.println("\n====== TRANSFERS ======");
					for (Transfer transfer : currentTransfers) {
						System.out.println(transfer);
					}
					System.out.println("=========================");
				} catch (SQLException e) {

					logger.warn(e);
					System.out.println(
							"An unexepected error occured while trying to retrieve transfers. Try again.\nIf the problem continues, ask someone to fix it.\n");
				}

				choice = "0";
				break;
			case "4":
				Menu onboardingMenu = new OnboardingMenu();
				onboardingMenu.display();
				choice = "0";
			case "e":
				break;

			default:
				System.out.println("Invalid choice.\n\n");
				choice = "0";
			}
		} while (choice.equalsIgnoreCase("0"));

	}

}
