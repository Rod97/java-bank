package com.bank.ui;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.InvalidInputException;
import com.bank.model.BankEmployee;
import com.bank.service.EmployeeOnboardingService;
import com.bank.util.GenerateIDUtil;

public class OnboardingMenu implements Menu {

	public EmployeeOnboardingService employeeOnboardingService = new EmployeeOnboardingService();
	private final Logger logger = Logger.getLogger(OnboardingMenu.class);

	@Override
	public void display() {

		String employeeID;
		String firstName;
		String lastName;
		LocalDate birthDate;
		LocalDate hireDate;
		Integer salary;
		BankEmployee newEmployee = null;

		System.out.println("====================");
		System.out.println("Please enter the following information to\n"
				+ "onboard a new employee. Or press 1 to Exit");
		System.out.println("====================");

		do {
			System.out.println("1.) Exit");
			System.out.println("First name: ");

			firstName = Menu.sc.nextLine();

			if (firstName.equals("1")) {
				return;
			}

			System.out.println("1.) Exit");
			System.out.println("Last name: ");

			lastName = Menu.sc.nextLine();

			if (lastName.equals("1")) {
				return;
			}

			System.out.println("1.) Exit");
			System.out.println("Date of birth (yyyy/mm/dd): ");

			String inputDate = Menu.sc.nextLine();

			if (inputDate.equals("1")) {
				return;
			}
			Scanner dateScanner = new Scanner(inputDate).useDelimiter("[/]");
			int year = dateScanner.nextInt();
			int month = dateScanner.nextInt();
			int day = dateScanner.nextInt();

			dateScanner.close();

			birthDate = LocalDate.of(year, month, day);
			System.out.println(birthDate);

			hireDate = LocalDate.now();

			System.out.println("1.) Exit");
			System.out.println("Salary: ");

			salary = Integer.parseInt(Menu.sc.nextLine());

			if (salary == 1) {
				return;
			}

			employeeID = (GenerateIDUtil.generateNewEmployeeID()).toString();

			try {
				newEmployee = employeeOnboardingService.onboardNewEmployee(employeeID, firstName, lastName, birthDate,
						hireDate, salary);
			} catch (SQLException f) {
				logger.warn(f);
				System.out.println(
						"An unexepected error occured while trying to retrieve applications. Try again.\nIf the problem continues, please go away.\n");
			} catch (BlankEntryException | InvalidInputException e) {
				System.out.println(e.getMessage());
			}

			if (newEmployee == null) {
				System.out.println(
						"There was an error onboarding this employee. Review the information submitted and try again.");
			} else {
				System.out.println("Successfully onboarded " + newEmployee.getFirstName() + " "
						+ newEmployee.getLastName() + ".\nThe employee ID associated with this new employee is: "
						+ newEmployee.getEmployeeID()
						+ "\nThis will be used by the employee to log in through the employee portal.");
				logger.info("Successfully onboarded " + newEmployee.getFirstName() + " " + newEmployee.getLastName()
						+ ".\nThe employee ID associated with this new employee is: " + newEmployee.getEmployeeID());
			}

		} while (newEmployee == null);

	}

}
