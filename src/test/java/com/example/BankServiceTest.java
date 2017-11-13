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
		assertEquals(bank.retrieveBalances().size(),0);

		bank.applyPayment(new PaymentDTO("USD", 100));
		assertEquals(bank.retrieveBalances().size(),1);

		BalanceDTO balance = bank.retrieveBalances().get(0);
		assertEquals(balance.getCurrencyCode(), "USD");
		assertEquals(balance.getBalance(), 100);

		bank.applyPayment(new PaymentDTO("USD", 10));
		assertEquals(bank.retrieveBalances().size(),1);
		balance = bank.retrieveBalances().get(0);
		assertEquals(balance.getCurrencyCode(), "USD");
		assertEquals(balance.getBalance(), 110);

		bank.applyPayment(new PaymentDTO("CZK", 1));
		assertEquals(bank.retrieveBalances().size(),2);
		int checked = 0;
		for (BalanceDTO balanceDTO: bank.retrieveBalances()) {
			switch (balanceDTO.getCurrencyCode()) {
				case "USD":
					checked++;
					assertEquals(balanceDTO.getBalance(), 110);
					break;
				case "CZK":
					checked++;
					assertEquals(balanceDTO.getBalance(), 1);
					break;
				default:
					assertEquals(true,false);

			}
		}
		assertEquals(checked, 2);
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
