package com.example.bsc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@ToString
@Getter
public class BalanceDTO {
	/**
	 * Three uppercase letter identification code of currency
	 */
	String currencyCode;
	/**
	 * current balance of this currency
	 */
	BigDecimal balance;
}
