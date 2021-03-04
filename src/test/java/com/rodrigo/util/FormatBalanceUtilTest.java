package com.rodrigo.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.bank.exceptions.InvalidInputException;
import com.bank.util.FormatBalanceUtil;

public class FormatBalanceUtilTest {

	@Test
	public void appToDatabaseTestInt() throws InvalidInputException {

		int expected = 100;
		int actual = FormatBalanceUtil.appToDatabase("1");

		assertEquals(expected, actual);
	}

	@Test(expected = InvalidInputException.class)
	public void appToDatabaseTestThrow() throws InvalidInputException {

		FormatBalanceUtil.appToDatabase("1.011");
	}
	
	@Test
	public void appToDatabaseTestDecimal() throws InvalidInputException{
		int expected = 101;
		int actual = FormatBalanceUtil.appToDatabase("1.01");
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void databaseToAppTest() {
		String expected = "$1.00";
		String actual = FormatBalanceUtil.databaseToApp(100);
		
		assertEquals(expected, actual);
	}
	

}
