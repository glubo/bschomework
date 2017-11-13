package com.example;


import com.example.bsc.dto.PaymentDTO;
import com.example.bsc.exception.MalformedPaymentException;
import com.example.bsc.helper.PaymentHelper;
import org.junit.Test;

import java.math.BigDecimal;

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

	@Test(expected = MalformedPaymentException.class)
	public void testDoubledot() {
		PaymentHelper.parsePayment("USDA 1.0.0");
	}

	@Test
	public void testPositive() {
		PaymentDTO payment = PaymentHelper.parsePayment("USD 100");
		assertEquals("USD", payment.getCurrencyCode());
		assertEquals(0, payment.getAmount().compareTo(new BigDecimal("100")));

		payment = PaymentHelper.parsePayment("USD 10.1");
		assertEquals("USD", payment.getCurrencyCode());
		assertEquals(0, payment.getAmount().compareTo(new BigDecimal("10.1")));
	}

	@Test
	public void testPositiveTrailingWhitespace() {
		PaymentDTO payment = PaymentHelper.parsePayment("USD 100\n \t");
		assertEquals("USD", payment.getCurrencyCode());
		assertEquals(0, payment.getAmount().compareTo(new BigDecimal("100")));

		payment = PaymentHelper.parsePayment("USD 10.1\n \t");
		assertEquals("USD", payment.getCurrencyCode());
		assertEquals(0, payment.getAmount().compareTo(new BigDecimal("10.1")));
	}

	@Test
	public void testNegative() {
		PaymentDTO payment = PaymentHelper.parsePayment("USD -100");
		assertEquals("USD", payment.getCurrencyCode());
		assertEquals(0, payment.getAmount().compareTo(new BigDecimal("-100")));

		payment = PaymentHelper.parsePayment("USD -10.1");
		assertEquals("USD", payment.getCurrencyCode());
		assertEquals(0, payment.getAmount().compareTo(new BigDecimal("-10.1")));
	}
}
