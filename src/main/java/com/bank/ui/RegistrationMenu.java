package com.bank.ui;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.UnableToCreateUserException;
import com.bank.model.BankUser;
import com.bank.service.UserCreatorService;

public class RegistrationMenu implements Menu {

	UserCreatorService userCreatorService = new UserCreatorService();
	private final Logger logger = Logger.getLogger(RegistrationMenu.class);

	@Override
	public void display() {
		String username;
		String password;
		// String firstName = null;
		// String lastName = null;
		boolean wasRegistered = false;

		do {

			System.out.println("Please enter the information to register a new user");
			System.out.println("==================");
			System.out.println("1.) Cancel");
			System.out.println("Enter a username: ");

			username = RegistrationMenu.sc.nextLine();

			if (username.equals("1"))
				return;

			System.out.println("1.) Cancel");
			System.out.println("Enter new password: ");

			password = RegistrationMenu.sc.nextLine();

			if (password.equals("1"))
				return;

			BankUser newUser = null;
			try {
				newUser = userCreatorService.createUser(username, password);
			} catch (SQLException g) {
				if (g.getSQLState().equals("23505")) {
					System.out.println("Username is taken.");
				} else if (g.getSQLState().equals("22001")) {
					System.out.println("Username must be less than 16 characters.");
				} else {

					logger.warn(g);
					System.out.println(
							"An unexepected error occured while trying to create a new user. Try again.\nIf the problem continues, please go away.\n");
				}
			} catch (BlankEntryException e) {
				System.out.println(e.getMessage());
			} catch (UnableToCreateUserException d) {

				logger.warn(new SQLException("INSERT failed"));
				System.out.println(d.getMessage());
			}

			if (newUser != null) {
				logger.info("User " + newUser.getUsername() + " was created");
				System.out.println("User Created with the username " + newUser.getUsername());
				wasRegistered = true;
			}
		} while (!wasRegistered);

	}

}
