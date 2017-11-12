package com.example.bsc.exception;

public class MalformedPaymentException extends RuntimeException {
	public MalformedPaymentException(String message) {
		super(message);
	}
}
