package com.example;


import com.example.bsc.dto.CurrencyDTO;
import com.example.bsc.exception.MalformedCurrencyException;
import com.example.bsc.helper.CurrencyHelper;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CurrencyHelperTest {
	@Test(expected = MalformedCurrencyException.class)
	public void testMalformed1() {
		CurrencyHelper.parseCurrency("A 100");
	}

	@Test(expected = MalformedCurrencyException.class)
	public void testMalformed2() {
		CurrencyHelper.parseCurrency("AAA100");
	}

	@Test(expected = MalformedCurrencyException.class)
	public void testMalformed3() {
		CurrencyHelper.parseCurrency("CZKA 100");
	}

	@Test
	public void testPositive() {
		CurrencyDTO currency = CurrencyHelper.parseCurrency("CZK 100");
		assertEquals(currency.getCode(), "CZK");
		assertTrue(currency.getRateToUSD().compareTo(new BigDecimal("100")) == 0);

		currency = CurrencyHelper.parseCurrency("CZK 10.1");
		assertEquals(currency.getCode(), "CZK");
		assertTrue(currency.getRateToUSD().compareTo(new BigDecimal("10.1")) == 0);
	}

	@Test
	public void testPositiveTrailingWhitespace() {
		CurrencyDTO currency = CurrencyHelper.parseCurrency("CZK 100\n \t");
		assertEquals(currency.getCode(), "CZK");
		assertTrue(currency.getRateToUSD().compareTo(new BigDecimal("100")) == 0);

		currency = CurrencyHelper.parseCurrency("CZK 10.1\n \t");
		assertEquals(currency.getCode(), "CZK");
		assertTrue(currency.getRateToUSD().compareTo(new BigDecimal("10.1")) == 0);
	}

	@Test(expected = MalformedCurrencyException.class)
	public void testNegative() {
		CurrencyDTO currency = CurrencyHelper.parseCurrency("CZK -100");
	}
}
