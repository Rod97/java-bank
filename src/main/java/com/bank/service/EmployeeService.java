package com.bank.service;

import java.sql.Connection;
import java.sql.SQLException;

import com.bank.dao.AccountsDAO;
import com.bank.dao.AccountsDAOImpl;
import com.bank.dao.BankUserDAO;
import com.bank.dao.BankUserDAOImpl;
import com.bank.dao.EmployeesDAO;
import com.bank.dao.EmployeesDAOImpl;
import com.bank.exceptions.InvalidInputException;
import com.bank.model.BankEmployee;
import com.bank.util.ConnectionUtil;

public class EmployeeService {
	public AccountsDAO accountsDAO;
	public BankUserDAO bankUserDAO;
	public EmployeesDAO employeeDAO;

	public EmployeeService() {
		this.accountsDAO = new AccountsDAOImpl();
		this.bankUserDAO = new BankUserDAOImpl();
		this.employeeDAO = new EmployeesDAOImpl();
	}
	
	public BankEmployee getEmployeeByEmployeeID(Integer employeeID) throws SQLException, InvalidInputException {
		try (Connection con = ConnectionUtil.getConnection()) {
			if(!employeeID.toString().startsWith("62")) {
				throw new InvalidInputException("Invalid employee ID.");
			}else {
				return employeeDAO.getEmployeeByEmployeeID(employeeID,con);
			}
		}

	}
	
	

}
