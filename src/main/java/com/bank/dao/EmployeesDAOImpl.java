package com.bank.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.bank.model.BankEmployee;

public class EmployeesDAOImpl implements EmployeesDAO {

	@Override
	public BankEmployee getEmployeeByEmployeeID(int employeeID, Connection con) throws SQLException {

		BankEmployee employee = null;

		String sql = "SELECT * FROM bank_app_data.employees WHERE employee_ID = ?";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, employeeID);

		ResultSet rs = pstmt.executeQuery();

		if (rs.next()) {
			int id = rs.getInt("employee_id");
			String firstName = rs.getString("first_name");
			String lastName = rs.getString("last_name");
			LocalDate birthDate = rs.getDate("birth_date").toLocalDate();
			LocalDate hireDate = rs.getDate("hire_date").toLocalDate();
			int salary = rs.getInt("salary");
			employee = new BankEmployee(id, firstName, lastName, birthDate, hireDate, salary);
		}

		return employee;
	}

	@Override
	public BankEmployee onboardNewEmployee(String employeeID, String firstName, String lastName, LocalDate birthDate,
			LocalDate hireDate, Integer salary, Connection con) throws SQLException {

		BankEmployee newUser = null;
		String sql = "INSERT INTO bank_app_data.employees(employee_id, first_name, last_name, birth_date, hire_date, salary) VALUES (?,?,?,?,?,?)";

		PreparedStatement pstmt = con.prepareStatement(sql);

		pstmt.setInt(1, Integer.parseInt(employeeID));
		pstmt.setString(2, firstName);
		pstmt.setString(3, lastName);
		pstmt.setDate(4, Date.valueOf(birthDate));
		pstmt.setDate(5, Date.valueOf(hireDate));
		pstmt.setInt(6, salary);

		int count = pstmt.executeUpdate(); // this returns the number of rows updated

		if (count != 1) {
			return newUser;
		} else {
			newUser = new BankEmployee(Integer.parseInt(employeeID), firstName, lastName, birthDate, hireDate, salary);
			return newUser;
		}
	}

}
