package com.example.bsc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@ToString
public class PaymentDTO {
	/**
	 * Three uppercase letter identification code of currency
	 */
	String currencyCode;
	/**
	 * how many of this currency is added (positive)/substracted(negative) from balance
	 */
	BigDecimal amount;
}
