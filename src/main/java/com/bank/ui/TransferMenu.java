package com.bank.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.InvalidInputException;
import com.bank.exceptions.NoAccountsFoundException;
import com.bank.exceptions.UserNotFoundException;
import com.bank.model.Account;
import com.bank.model.Transfer;
import com.bank.service.AccountInfoService;
import com.bank.service.TransferService;
import com.bank.service.UserService;
import com.bank.util.FormatBalanceUtil;
import com.bank.util.InputCheckerUtil;

public class TransferMenu implements Menu {

	private Account accountBeingAccessed;
	private TransferService transferService = new TransferService();
	private AccountInfoService accountInfoService = new AccountInfoService();
	private UserService userService = new UserService();
	private static Logger logger = Logger.getLogger(TransferMenu.class);

	TransferMenu(Account accountBeingAccessed) {
		this.accountBeingAccessed = accountBeingAccessed;
	}

	@Override
	public void display() {

		String choice = "0";
		do {
			System.out.println("\n====== TRANSFER MENU ======\n");
			System.out.println("1.) Send money.");
			System.out.println("2.) View incoming transfers.");
			System.out.println("E.) Exit");

			choice = Menu.sc.nextLine();

			switch (choice) {
			case "1":
				System.out.println("\nWho would you like to send money to?\nEnter their username: ");

				String receiver = Menu.sc.nextLine();
				try {
					if (userService.hasCorrectUsername(receiver)) {
						System.out.println("Enter the account number of an account belonging to " + receiver + ".");
						String receivingAccountNumber = Menu.sc.nextLine();

						Account receivingAccount = accountInfoService.getAccountByAccountNumber(receivingAccountNumber,
								receiver);

						System.out.println(
								"How much would you like to send?\nAvailable: $" + accountBeingAccessed.getBalance());
						String input = Menu.sc.nextLine();

						if (InputCheckerUtil.isNumberWithDecimal(input)) {
							int amount = FormatBalanceUtil.appToDatabase(input);
							System.out.println(transferService.sendFunds(accountBeingAccessed.getAccountNumber(),
									receivingAccount.getAccountNumber(), amount));

						} else {
							System.out.println("That is not a valid number.");
						}
					}
				} catch (SQLException f) {
					logger.warn(f);
					System.out.println(
							"An unexepected error occured while trying to complete your deposit. Try again.\nIf the problem continues, please go away.\n");

				} catch (BlankEntryException | UserNotFoundException | InvalidInputException e) {
					// TODO LOG
					System.out.println(e.getMessage());
				} catch (NoAccountsFoundException d) {
					logger.info("No accounts were found for " + receiver, d);
					System.out.println(d.getMessage());
				}

				choice = "0";
				break;
			case "2":
				try {
					ArrayList<Transfer> transfersForAccount = transferService
							.getTransfersByAccountNumber(accountBeingAccessed.getAccountNumber());

					for (Transfer transfer : transfersForAccount) {
						if (transfer.getStatus().equals("PENDING")) {
							System.out.println(transfer);
						}
					}

					System.out.println("Enter a transfer ID to continue, or enter E to exit.");

					String input = Menu.sc.nextLine();
					boolean transferWasFound = false;
					Transfer transferToProcess = null;
					if (InputCheckerUtil.isNumberNoDecimal(input)) {
						for (Transfer transfer : transfersForAccount) {
							if (transfer.getTranferID().equals(input)) {
								System.out.println(transfer);
								transferToProcess = transfer;
								transferWasFound = true;
							}
						}

						if (transferWasFound) {

							do {
								choice = "5";
								System.out.println("\n1.) Accept");
								System.out.println("2.) Reject");
								System.out.println("E.) Exit");

								choice = Menu.sc.nextLine();

								switch (choice) {
								case "1":
									try {
										System.out.println(transferService.receiveFunds(
												Integer.parseInt(transferToProcess.getReceivingAccount()),
												Integer.parseInt(transferToProcess.getSendingAccount()),
												FormatBalanceUtil
														.appToDatabase(transferToProcess.getAmount().substring(1)),
												Integer.parseInt(input)));
									} catch (NumberFormatException | InvalidInputException e) {
										// TODO Log
										System.out.println(e.getMessage());
										choice = "5";
									}
									break;
								case "2":
									System.out.println(transferService
											.rejectFunds(Integer.parseInt(transferToProcess.getTranferID())));
									break;
								}
							} while (choice.equals("5"));
						} else {
							System.out.println(
									"That transfer ID was not found for any transfers involving this account.");
						}
					}

				} catch (SQLException f) {
					logger.warn(f);
					System.out.println(
							"An unexepected error occured while trying to retrieve incoming transfers. Try again.\nIf the problem continues, please go away.\n");
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
