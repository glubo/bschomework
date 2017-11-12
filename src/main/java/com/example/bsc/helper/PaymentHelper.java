package com.example.bsc.helper;

import com.example.bsc.dto.PaymentDTO;
import com.example.bsc.exception.MalformedPaymentException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * this helper parses payment from string
 */
public class PaymentHelper {
	static Pattern paymentPattern = Pattern.compile("(?<currency>\\p{Upper}{3}+)\\s+(?<amount>-?\\d+)\\s*");

	/**
	 *
	 * @param input String in format "USD 100", currency identification should be three uppercase letters followed by an integer
	 * @return
	 */
	public static PaymentDTO parsePayment(String input) throws MalformedPaymentException {
		String currency;
		int amount;

		Matcher paymentMatcher = paymentPattern.matcher(input);
		if (!paymentMatcher.matches()) {
			throw new MalformedPaymentException(String.format("Encountered malformed payment: \"%s\"", input));
		}
		currency = paymentMatcher.group("currency");
		amount = Integer.parseInt(paymentMatcher.group("amount"));

		return new PaymentDTO(currency, amount);
	}
}
