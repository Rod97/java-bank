package com.bank.exceptions;

public class BlankEntryException extends Exception {

	public BlankEntryException() {
		super();
	}

	public BlankEntryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BlankEntryException(String message, Throwable cause) {
		super(message, cause);
	}

	public BlankEntryException(String message) {
		super(message);
	}

	public BlankEntryException(Throwable cause) {
		super(cause);
	}

}
