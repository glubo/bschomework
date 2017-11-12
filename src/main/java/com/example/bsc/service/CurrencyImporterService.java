package com.example.bsc.service;

import com.example.bsc.dto.CurrencyDTO;
import com.example.bsc.exception.CurrencyImportFailedException;
import com.example.bsc.exception.PaymentsImportFailedException;
import com.example.bsc.helper.CurrencyHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * imports currency exchange rates from a file
 */
public class CurrencyImporterService {
	private BankService bankService;

	public CurrencyImporterService(BankService bankService) {
		this.bankService = bankService;
	}

	public void importFromFile(String path) throws PaymentsImportFailedException {
		Path currenciesPath = Paths.get(path);
		String line;
		try (BufferedReader currenciesReader = Files.newBufferedReader(currenciesPath)) {
			while ((line = currenciesReader.readLine()) != null) {
				CurrencyDTO currencyDTO = CurrencyHelper.parseCurrency(line);
				bankService.updateCurrency(currencyDTO.getCode(), currencyDTO.getRateToUSD());
			}
		} catch (IOException e) {
			throw new CurrencyImportFailedException("There was a problem with importing currencies from file", e);
		}
	}
}
