package com.bank.ui;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.IncorrectPasswordException;
import com.bank.exceptions.UserNotFoundException;
import com.bank.service.UserService;

public class LoginMenu implements Menu {
	
	private final Logger logger = Logger.getLogger(LoginMenu.class);

	public UserService userService = new UserService();

	@Override
	public void display() {

		String username;
		String password;
		boolean isCorrectUsername;
		boolean isCorrectPassword;
		boolean isLoggedIn = false;

		do {
			System.out.println("E.) Exit");
			System.out.println("Enter username: ");
			username = Menu.sc.nextLine().toLowerCase();
			if (username.equals("e"))
				break;

			try {
				isCorrectUsername = userService.hasCorrectUsername(username);
				if (isCorrectUsername) {
					System.out.println("E.) Exit");
					System.out.println("Enter password: ");
					password = Menu.sc.nextLine().toLowerCase();
					if (password.equals("e"))
						break;

					try {
						isCorrectPassword = userService.hasCorrectPassword(username, password);
						if (isCorrectPassword) {
							System.out.println("Login successful.\n======");
							isLoggedIn = true;
						}
					} catch (IncorrectPasswordException e) {
						
						logger.info("User " + username + "attempted to use an incorrect password.");
						System.out.println(e.getMessage());

					}
				}
			} catch (SQLException f) {
				logger.warn(f);
				System.out.println("An unexepected error occured while trying to log in. Try again.\nIf the problem continues, please go away.");
				
			} catch (BlankEntryException | UserNotFoundException e) {
				System.out.println(e.getMessage());
			}
		} while (!isLoggedIn);

		if (isLoggedIn) {
			logger.info("User " + username + " logged in successfully.");
			Menu userMenu = new UserMenu(username);
			userMenu.display();
		}
	}

}
