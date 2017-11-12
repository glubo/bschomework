package com.example.bsc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@ToString
@Getter
public class CurrencyDTO {
	/**
	 * three uppercase letter identification of this currency
	 */
	String code;

	/**
	 * How many USD for 1 of this currency (0.5 means 1 of this currency is 0.5 USD)
	 */
	BigDecimal rateToUSD;
}
