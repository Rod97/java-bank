package com.bank.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bank.exceptions.NoAccountsFoundException;
import com.bank.model.Account;
import com.bank.service.AccountInfoService;

public class UserMenu implements Menu {

	private final Logger logger = Logger.getLogger(UserMenu.class);
	private String username;
	private AccountInfoService accountInfoService = new AccountInfoService();

	public UserMenu(String username) {
		this.username = username;
	}

	@Override
	public void display() {


		String choice = "0";

		System.out.println("Welcome " + username + "!\nPlease select and option from below.");

		do {
			System.out.println("\n======User Main Menu======\n");
			System.out.println("1.) Apply for a new account");
			System.out.println("2.) View my account(s)");
			System.out.println("3.) View pending account applications");
			System.out.println("E.) Exit");

			choice = Menu.sc.nextLine().toLowerCase();

			switch (choice) {
			case "1":
				Menu accountApplicationMenu = new AccountApplicationMenu(username);
				accountApplicationMenu.display();
				choice = "0";
				break;
			case "2":
				Menu accountMenu = new AccountMenu(username);
				accountMenu.display();
				choice = "0";
				break;
			case "3": 
				System.out.println("\n====== APPLICATIONS ======\n");
				try {
					ArrayList<Account> applications = accountInfoService.getApplicationsByUsername(username);
					for(Account application : applications) {
						if(application.getApplicationStatus().equals("PENDING"))
						System.out.println(application);
					}
					System.out.println("\n");
				} catch (SQLException f ) {
					logger.warn(f);
					System.out.println("An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, please go away.\n");
					
				} catch (NoAccountsFoundException e) {
					logger.info("No applications found for user " + username);
					System.out.println(e.getMessage());
				}
				choice = "0";
				break;
			case "e":
				break;
			default:
				System.out.println("Not a valid option. Please try again");
				choice = "0";
			}
		} while (choice.equals("0"));

	}

}
