package com.example.bsc.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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
	int balance;
}
