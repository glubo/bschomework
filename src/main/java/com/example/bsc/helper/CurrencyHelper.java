package com.example.bsc.helper;

import com.example.bsc.dto.CurrencyDTO;
import com.example.bsc.exception.MalformedCurrencyException;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this helper parses currency from string
 */
public class CurrencyHelper {
	static Pattern paymentPattern = Pattern.compile("(?<currency>\\p{Upper}{3}+)\\s+(?<rate>\\d+(?:\\.\\d+)?)\\s*");

	/**
	 *
	 * @param input String in format "CZK 0.046", currency identification should be three uppercase letters followed by an integer
	 * @return
	 */
	public static CurrencyDTO parseCurrency(String input) throws MalformedCurrencyException {
		String currency;
		BigDecimal rate;

		Matcher paymentMatcher = paymentPattern.matcher(input);
		if (!paymentMatcher.matches()) {
			throw new MalformedCurrencyException(String.format("Encountered malformed currency: \"%s\"", input));
		}
		currency = paymentMatcher.group("currency");
		rate = new BigDecimal(paymentMatcher.group("rate"));

		return new CurrencyDTO(currency, rate);
	}
}
