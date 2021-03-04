package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.dao.BankUserDAO;
import com.bank.dao.BankUserDAOImpl;
import com.bank.dao.EmployeesDAO;
import com.bank.dao.EmployeesDAOImpl;
import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.InvalidInputException;
import com.bank.model.BankEmployee;
import com.bank.util.ConnectionUtil;

public class EmployeeOnboardingService {
	public AccountsDAO accountsDAO;
	public BankUserDAO bankUserDAO;
	public EmployeesDAO employeesDAO;

	public EmployeeOnboardingService() {
		this.accountsDAO = new AccountsDAOImpl();
		this.bankUserDAO = new BankUserDAOImpl();
		this.employeesDAO = new EmployeesDAOImpl();
	}

	public BankEmployee onboardNewEmployee(String employeeID, String firstName, String lastName, LocalDate birthDate, LocalDate hireDate, Integer salary) throws SQLException, BlankEntryException, InvalidInputException {
		BankEmployee newEmployee = null;
		try (Connection con = ConnectionUtil.getConnection()) {
			
			
			if(employeeID.matches("^\\s*$") || employeeID == null) {
				throw new BlankEntryException("No EmployeeID was generated"); //ideally should never be thrown unless something went really wrong somehow
			}else if(firstName.matches("^\\s*$") || firstName == null) {
				throw new BlankEntryException("No first name was entered");
			}else if(lastName.matches("^\\s*$") || lastName == null) {
				throw new BlankEntryException("No first name was entered");
			}else if(salary.toString().matches("^\\s*$") || salary == null) {
				throw new BlankEntryException("No salary information was provided");
			}else if(salary <= 0) {
				throw new InvalidInputException("Salary cannot be 0 or less");
			}else {
				newEmployee = employeesDAO.onboardNewEmployee(employeeID, firstName, lastName, birthDate, hireDate, salary, con);
			}
			
			return newEmployee;
		}
	}
}
