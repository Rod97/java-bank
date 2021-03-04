package com.bank.app;

import com.bank.ui.MainMenu;
import com.bank.ui.Menu;

public class Application {

	public static void main(String[] args) {
		/*
		 * TEST CODE TO CHECK CONNECTION
		 * 
		 * try { Connection con = ConnectionUtil.getConnection();
		 * System.out.println(con); } catch (SQLException e) { // TODO Auto-generated
		 * catch block e.printStackTrace(); }
		 */

		Menu mainMenu = new MainMenu();

		mainMenu.display();
	}

}
