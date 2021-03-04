package com.rodrigo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.bank.dao.BankUserDAO;
import com.bank.exceptions.BlankEntryException;
import com.bank.exceptions.UnableToCreateUserException;
import com.bank.model.BankUser;
import com.bank.service.UserCreatorService;
import com.bank.util.ConnectionUtil;

public class UserCreatorServiceTest {

	public UserCreatorService userCreatorService;

	public static BankUserDAO bankUserDAO;
	public static Connection mockConnection;
	public static MockedStatic<ConnectionUtil> mockedStatic;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		// Create mock DAO
		bankUserDAO = mock(BankUserDAO.class);
		mockConnection = mock(Connection.class);

		// instruct methods from mocked class on what to return
		// utilize matchers from org.mockito.ArgumentMatchers

		mockedStatic = Mockito.mockStatic(ConnectionUtil.class);
		mockedStatic.when(ConnectionUtil::getConnection).thenReturn(mockConnection);

		when(bankUserDAO.createNewUser(eq("username"), eq("password"), eq(mockConnection)))
				.thenReturn(new BankUser("username"));
		when(bankUserDAO.createNewUser(eq("abcdefghijklmnopq"), eq("password"), eq(mockConnection)))
		.thenReturn(null);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		mockedStatic.close();
	}

	@Before
	public void setUp() throws Exception {
		// Inject the fake objects into the actual UserService object being created
		userCreatorService = new UserCreatorService(bankUserDAO);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateNewUser() throws SQLException, BlankEntryException, UnableToCreateUserException {

		BankUser actual = userCreatorService.createUser("username", "password");

		BankUser expected = new BankUser("username");

		assertEquals(expected, actual);

	}

	@Test(expected = BlankEntryException.class)
	public void testCreateNewUserThrowBlank() throws SQLException, BlankEntryException, UnableToCreateUserException {

		userCreatorService.createUser("", "password");

	}

	@Test(expected = UnableToCreateUserException.class)
	public void testCreateNewUserFail() throws SQLException, BlankEntryException, UnableToCreateUserException {

		userCreatorService.createUser("abcdefghijklmnopq", "password");

	}

}
