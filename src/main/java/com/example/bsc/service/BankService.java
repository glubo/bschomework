package com.example.bsc.service;


import com.example.bsc.dto.BalanceDTO;
import com.example.bsc.dto.CurrencyDTO;
import com.example.bsc.dto.PaymentDTO;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this is core storage for this application's data
 * manages balances and currency rates
 */
public class BankService {
	/**
	 * map [currencyCode => BalanceDTO] inner DTOs are immutable
	 */
	private Map<String, BalanceDTO> balances;
	/**
	 * map [currencyCode => CurrencyDTO] inner DTOs are immutable
	 */
	private Map<String, CurrencyDTO> currencies;

	public BankService() {
		balances = new HashMap<>();
		currencies = new HashMap<>();
	}

	/**
	 * Sets new currency rate to USD
	 * @param code
	 * @param rateToUSD
	 */
	public synchronized void updateCurrency(String code, BigDecimal rateToUSD) {
		currencies.put(code, new CurrencyDTO(code, rateToUSD));
	}

	/**
	 * finds curent currency rate or null
	 * @param code
	 * @return currencyDTO or null if no rate for this currency is available
	 */
	public synchronized CurrencyDTO getCurrency(String code) {
		return currencies.get(code);
	}

	/**
	 * changes balances according to given payment
	 * @param payment
	 */
	public synchronized void applyPayment(PaymentDTO payment) {
		String currencyCode = payment.getCurrencyCode();
		BalanceDTO oldBalance = balances.get(currencyCode);
		if (oldBalance == null || oldBalance.getBalance() == null) {
			oldBalance = new BalanceDTO(currencyCode, BigDecimal.ZERO);
		}

		balances.put(currencyCode, new BalanceDTO(oldBalance.getCurrencyCode(), oldBalance.getBalance().add(payment.getAmount())));
	}

	/**
	 * retrieve list of current balances
	 * @return
	 */
	public synchronized List<BalanceDTO> retrieveBalances() {
		return new ArrayList<>(balances.values());
	}
}
