# ROC1 Banking API

## Description
The Banking API will manage the bank accounts of its users. It will be managed by the Bank's employees and admins. Employees and Admins count as Standard users with additional abilities. All users must be able to update their personal information, such as username, password, first and last names, as well as email. Accounts owned by users must support withdrawal, deposit, and transfer. Transfer of funds should be allowed between accounts owned by the same user, as well as between accounts owned by different users. Standard users should be able to register and login to see their account information. They can have either Checking or Savings accounts. Employees can view all customer information, but not modify in any way. Admins can both view all user information, as well as directly modify it.

## Technologies
* Java
* JDBC
* PostgreSQL
* Log4j
* JUnit

## User Stories
* As a user, I can login.
* As a customer, I can apply for a new bank account with a starting balance.
* As a customer, I can view the balance of a specific account.
* As a customer, I can make a withdrawal or deposit to a specific account.
* As the system, I reject invalid transactions.
	* Ex:
		* A withdrawal that would result in a negative balance.
		* A deposit or withdrawal of negative money.
* As an employee, I can approve or reject an account.
* As an employee, I can view a customer's bank accounts.
* As a user, I can register for a customer account.
* As a customer, I can post a money transfer to another account.
* As a customer, I can accept a money transfer from another account.
* As an employee, I can view a log of all transactions.

## Getting Started
### Getting source files
To get these source files, simply execute the following command in your git bash
`git clone https://github.com/Rod97/java-bank.git`
### Java
* Ensure you have a JDK installed
* Open the project in your IDE
* Modify the ConnectionUtil class to establish your database connection
	* **You may need to change**
	* Driver
	* URL
	* Username
	* Password
I have set them up as environment variables on my machine, but you can hard code them if you need to. However, if you wish to push this to another repo, it is recommended that you **do not** hardcode these variables.
### Database
* All DAO queries are written in PostgreSQL, so you will need a database built on that language
* Your database will need 5 tables
	1. Bank Users
	2. Bank Employees
	3. Transfers
	4. Active Accounts
	5. Pending Accounts

The following script should be enough to create the tables as the Java application expects them:
---
```
CREATE EXTENSION pgcrypto;
   CREATE TABLE bank_user(
	username VARCHAR(16) PRIMARY KEY, --cannot start with .
	user_pass TEXT NOT NULL,
	first_name VARCHAR(50),
	last_name VARCHAR(50)
	);
   CREATE TABLE active_accounts(
	account_number INT PRIMARY KEY CHECK (account_number <= 9999999999 AND CAST (account_number AS TEXT) NOT LIKE '0%'),
	account_owner VARCHAR(16) NOT NULL,
	CONSTRAINT fk_bank_user FOREIGN KEY("account_owner") REFERENCES bank_user(username),
	account_type CHAR(3) NOT NULL,
	balance INT DEFAULT 0, -- will be handled as cents, divide by 100 for display purposes.
	open_date DATE
	);
   CREATE TABLE pending_accounts(
  	application_number SERIAL PRIMARY KEY,
  	account_owner VARCHAR(16),
	CONSTRAINT fk_bank_user FOREIGN KEY("account_owner") REFERENCES bank_user(username),
	account_type CHAR(3) NOT NULL,
	initial_balance_cents INT DEFAULT 0, -- will be handled as cents, divide by 100 for display purposes.
	status VARCHAR(8) DEFAULT 'PENDING'
	);
   CREATE TABLE employees(
	employee_id INT PRIMARY KEY CHECK (employee_id <= 99999999 AND CAST (employee_id AS TEXT) LIKE '62%'),
	first_name VARCHAR(50),
	last_name VARCHAR(50),
	birth_date DATE CHECK (birth_date > '1900-01-01') NOT NULL ,
	hire_date DATE CHECK ((hire_date - birth_date) >= 18) NOT NULL,
	salary INT CHECK(salary > 0)
	);
   CREATE TABLE money_transfers(
	transfer_id SERIAL PRIMARY KEY,
	sending_account INT NOT NULL,
	CONSTRAINT fk_active_account_sending FOREIGN KEY("sending_account") REFERENCES active_accounts(account_number),
	receiving_account INT NOT NULL CHECK (receiving_account<>sending_account),
	CONSTRAINT fk_active_account_receiving FOREIGN KEY("receiving_account") REFERENCES active_accounts(account_number),
	amount INT NOT NULL,
	date_of_transfer DATE,
	status VARCHAR
	);
```

## Usage
Upon first use, you should create an employee and a regular user. After that, you can log in as one or the other and freely navigate their respective menus.
