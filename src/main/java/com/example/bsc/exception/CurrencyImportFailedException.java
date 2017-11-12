package com.example.bsc.exception;

public class CurrencyImportFailedException extends RuntimeException {
	public CurrencyImportFailedException(String message, Throwable cause) {
		super(message, cause);
	}
}
