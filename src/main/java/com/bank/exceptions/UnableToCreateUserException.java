package com.bank.exceptions;

public class UnableToCreateUserException extends Exception {

	public UnableToCreateUserException() {
		super();
	}

	public UnableToCreateUserException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UnableToCreateUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnableToCreateUserException(String message) {
		super(message);
	}

	public UnableToCreateUserException(Throwable cause) {
		super(cause);
	}

}
