package com.example;


import com.example.bsc.dto.PaymentDTO;
import com.example.bsc.exception.MalformedPaymentException;
import com.example.bsc.helper.PaymentHelper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PaymentHelperTest {
	@Test(expected = MalformedPaymentException.class)
	public void testMalformed1() {
		PaymentHelper.parsePayment("A 100");
	}

	@Test(expected = MalformedPaymentException.class)
	public void testMalformed2() {
		PaymentHelper.parsePayment("AAA100");
	}

	@Test(expected = MalformedPaymentException.class)
	public void testMalformed3() {
		PaymentHelper.parsePayment("USDA 100");
	}

	@Test
	public void testPositive() {
		PaymentDTO payment = PaymentHelper.parsePayment("USD 100");
		assertEquals(payment.getCurrencyCode(), "USD");
		assertEquals(payment.getAmount(), 100);
	}

	@Test
	public void testPositiveTrailingWhitespace() {
		PaymentDTO payment = PaymentHelper.parsePayment("USD 100\n \t");
		assertEquals(payment.getCurrencyCode(), "USD");
		assertEquals(payment.getAmount(), 100);
	}

	@Test
	public void testNegative() {
		PaymentDTO payment = PaymentHelper.parsePayment("USD -100");
		assertEquals(payment.getCurrencyCode(), "USD");
		assertEquals(payment.getAmount(), -100);
	}
}
