package com.rodrigo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.bank.dao.AccountsDAO;
import com.bank.exceptions.InvalidInputException;
import com.bank.exceptions.NoAccountsFoundException;
import com.bank.model.Account;
import com.bank.service.AccountInfoService;
import com.bank.util.ConnectionUtil;

public class AccountInfoServiceTest {
	@Mock
	ArrayList<Account> mockAccounts;

	public AccountInfoService accountInfoService;

	public static AccountsDAO accountsDAO;
	public static Connection mockConnection;
	public static MockedStatic<ConnectionUtil> mockedStatic;

	private static Account mockAccount;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create mock DAO
		accountsDAO = mock(AccountsDAO.class);
		mockConnection = mock(Connection.class);
		mockAccount = mock(Account.class);

		// instruct methods from mocked class on what to return
		// utilize matchers from org.mockito.ArgumentMatchers

		mockedStatic = Mockito.mockStatic(ConnectionUtil.class);
		mockedStatic.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

		// account by username
		when(accountsDAO.getActiveAccountsByUsername(eq("username"), eq(mockConnection)))
				.thenReturn(new ArrayList<Account>());

		when(accountsDAO.getActiveAccountsByUsername(eq("usernotfound"), eq(mockConnection))).thenReturn(null);

		// accounts by account number
		when(accountsDAO.getAccountByAccountNumber(123456789, "username", mockConnection)).thenReturn(mockAccount);
		when(accountsDAO.getAccountByAccountNumber(213456789, "username", mockConnection)).thenReturn(null);

		// applications by username
		when(accountsDAO.getApplicationsByUsername("username", mockConnection)).thenReturn(new ArrayList<Account>());
		when(accountsDAO.getApplicationsByUsername("usernotfound", mockConnection)).thenReturn(null);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		mockedStatic.close();
	}

	@Before
	public void setUp() throws Exception {
		// Inject the fake objects into the actual UserService object being created
		accountInfoService = new AccountInfoService(accountsDAO);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getAccountsByUsernameTest() throws SQLException, NoAccountsFoundException {

		ArrayList<Account> actual = accountInfoService.getAccountsByUsername("username");

		ArrayList<Account> expected = new ArrayList<>();

		assertEquals(expected, actual);
	}

	@Test(expected = NoAccountsFoundException.class)
	public void getAccountsByUsernameTestFail() throws SQLException, NoAccountsFoundException {

		accountInfoService.getAccountsByUsername("usernotfound");

	}

	@Test
	public void getAccountByAccountNumber() throws SQLException, NoAccountsFoundException, InvalidInputException {

		Account actual = accountInfoService.getAccountByAccountNumber("123456789", "username");
		Account expected = mockAccount;

		assertEquals(expected, actual);
	}

	@Test(expected = NoAccountsFoundException.class)
	public void getAccountByAccountNumberFail() throws SQLException, NoAccountsFoundException, InvalidInputException {

		accountInfoService.getAccountByAccountNumber("213456789", "username");

	}
	
	@Test(expected = InvalidInputException.class)
	public void getAccountByAccountNumberTestTooBig() throws SQLException, NoAccountsFoundException, InvalidInputException {
		accountInfoService.getAccountByAccountNumber("1324567890", "username");
	}
	@Test(expected = InvalidInputException.class)
	public void getAccountByAccountNumberTestTooSmall() throws SQLException, NoAccountsFoundException, InvalidInputException {
		accountInfoService.getAccountByAccountNumber("13245678", "username");
	}

	@Test
	public void getApplicationsByUsernameTest() throws SQLException, NoAccountsFoundException {
		ArrayList<Account> actual = accountInfoService.getApplicationsByUsername("username");
		ArrayList<Account> expected = new ArrayList<Account>();

		assertEquals(expected, actual);
	}

	@Test(expected = NoAccountsFoundException.class)
	public void getApplicationsByUsernameTestFail() throws SQLException, NoAccountsFoundException {
		accountInfoService.getApplicationsByUsername("usernotfound");
	}
}
