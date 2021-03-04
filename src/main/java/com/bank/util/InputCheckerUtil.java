package com.bank.util;

public class InputCheckerUtil {

	public static boolean isNumberWithDecimal(String str) {

		if (str.length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {

			if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9') && str.charAt(i) != '.') {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isNumberNoDecimal(String str) {

		if (str.length() == 0) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {

			if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9') ) {
				return false;
			}
		}
		return true;
	}
}
