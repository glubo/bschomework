package com.example.bsc.service;

import com.example.bsc.exception.PaymentsImportFailedException;
import com.example.bsc.helper.PaymentHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * imports payments from a file
 */
public class PaymentImporterService {
	private BankService bankService;

	public PaymentImporterService(BankService bankService) {
		this.bankService = bankService;
	}

	public void importFromFile(String path) throws PaymentsImportFailedException {
		Path currenciesPath = Paths.get(path);
		String line;
		try (BufferedReader paymentsReader = Files.newBufferedReader(currenciesPath)) {
			while ((line = paymentsReader.readLine()) != null) {
				bankService.applyPayment(PaymentHelper.parsePayment(line));
			}
		} catch (IOException e) {
			throw new PaymentsImportFailedException("There was a problem with importing payments from file", e);
		}
	}
}
