package com.example.bsc.exception;

public class PaymentsImportFailedException extends RuntimeException {
	public PaymentsImportFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
