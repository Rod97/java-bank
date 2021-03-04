package com.bank.ui;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bank.exceptions.InvalidInputException;
import com.bank.model.BankEmployee;
import com.bank.service.EmployeeService;
import com.bank.util.InputCheckerUtil;

public class EmployeeLoginMenu implements Menu {

	public EmployeeService employeeService = new EmployeeService();
	private final Logger logger = Logger.getLogger(EmployeeLoginMenu.class);

	@Override
	public void display() {
		String employeeID = "0";
		int id;
		BankEmployee activeEmployee = null;
		do {
			System.out.println("Please enter your employee ID: ");
			employeeID = Menu.sc.nextLine();

			if (InputCheckerUtil.isNumberNoDecimal(employeeID)) {
				id = Integer.parseInt(employeeID);

				try {
					activeEmployee = employeeService.getEmployeeByEmployeeID(id);

					if (activeEmployee.getEmployeeID() == id) {
						// LOG employee log in

						logger.info("Employee " + activeEmployee.getFirstName() + activeEmployee.getLastName()
								+ ", EmployeeID: " + activeEmployee.getEmployeeID() + "has logged in.");
						System.out.println("Employee " + activeEmployee.getFirstName() + " "
								+ activeEmployee.getLastName() + " logged in successfully");
						Menu activeEmployeeMenu = new ActiveEmployeeMenu();
						activeEmployeeMenu.display();
					}
				} catch (SQLException f) {
					logger.warn(f);
					System.out.println("An unexepected error occured while trying to log you in. Try again.\nIf the problem continues, please go away.\n");

				} catch (InvalidInputException e) {
				
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("Invalid employee ID.");
			}
		} while (activeEmployee == null);
		
	}
}
