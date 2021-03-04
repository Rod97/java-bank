package com.bank.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import com.bank.model.BankEmployee;

public interface EmployeesDAO {
	public BankEmployee getEmployeeByEmployeeID(int employeeID, Connection con) throws SQLException;
	public BankEmployee onboardNewEmployee(String employeeID, String firstName, String lastName, LocalDate birthDate, LocalDate hireDate, Integer salary, Connection con) throws SQLException;
}
