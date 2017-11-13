package com.example;

import com.example.bsc.dto.BalanceDTO;
import com.example.bsc.dto.CurrencyDTO;
import com.example.bsc.dto.PaymentDTO;
import com.example.bsc.service.BankService;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BankServiceTest {
	@Test
	public void testApplyPayment() {
		BankService bank = new BankService();
		assertEquals(0, bank.retrieveBalances().size());

		bank.applyPayment(new PaymentDTO("USD", BigDecimal.valueOf(100)));
		assertEquals(1, bank.retrieveBalances().size());

		BalanceDTO balance = bank.retrieveBalances().get(0);
		assertEquals("USD", balance.getCurrencyCode());
		assertEquals(0, balance.getBalance().compareTo(BigDecimal.valueOf(100)));

		bank.applyPayment(new PaymentDTO("USD", BigDecimal.valueOf(10)));
		assertEquals(1, bank.retrieveBalances().size());
		balance = bank.retrieveBalances().get(0);
		assertEquals("USD", balance.getCurrencyCode());
		assertEquals(0, balance.getBalance().compareTo(BigDecimal.valueOf(110)));

		bank.applyPayment(new PaymentDTO("CZK", BigDecimal.ONE));
		bank.applyPayment(new PaymentDTO("CZK", new BigDecimal("0.1")));
		assertEquals(2, bank.retrieveBalances().size());
		int checked = 0;
		for (BalanceDTO balanceDTO: bank.retrieveBalances()) {
			switch (balanceDTO.getCurrencyCode()) {
				case "USD":
					checked++;
					assertEquals(0, balanceDTO.getBalance().compareTo(BigDecimal.valueOf(110)));
					break;
				case "CZK":
					checked++;
					assertEquals(0, balanceDTO.getBalance().compareTo(new BigDecimal("1.1")));
					break;
				default:
					assertEquals(true,false);

			}
		}
		assertEquals(2, checked);
	}

	@Test
	public void testCurrencies() {
		BankService  bank = new BankService();
		assertNull(bank.getCurrency("CZK"));

		bank.updateCurrency("CZK", new BigDecimal("2.0"));
		CurrencyDTO czkCurrency = null;
		assertNotNull(czkCurrency = bank.getCurrency("CZK"));
		assertTrue(czkCurrency.getRateToUSD().compareTo(new BigDecimal("2.0")) == 0);

		bank.updateCurrency("CZK", new BigDecimal("1.0"));
		assertNotNull(czkCurrency = bank.getCurrency("CZK"));
		assertTrue(czkCurrency.getRateToUSD().compareTo(new BigDecimal("1.0")) == 0);
	}
}
