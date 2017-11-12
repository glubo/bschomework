package com.example.bsc.service;

import com.example.bsc.dto.BalanceDTO;
import com.example.bsc.dto.CurrencyDTO;
import com.example.bsc.exception.OutputFailedException;

import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.List;

/**
 * this service prints current balances to an output
 */
public class OutputService {
	private BankService bankService;
	private boolean verbose;

	public OutputService(BankService bankService, boolean verbose) {
		this.bankService = bankService;
		this.verbose = verbose;
	}

	/**
	 * Writes out given balances to an output, using exchange rates from bankService
	 * @param outputWriter where to write the output
	 * @param balances
	 * @throws RuntimeException
	 */
	public void writeBalance(Writer outputWriter, List<BalanceDTO> balances) throws OutputFailedException {
		try {
			if (verbose) {
				outputWriter.write("--- Current Balances ---");
				outputWriter.write(System.getProperty("line.separator"));
			}

			for (BalanceDTO balanceDTO : balances) {
				if (balanceDTO == null || balanceDTO.getBalance() == 0) {
					continue; // skip empty balances
				}

				String currencyCode = balanceDTO.getCurrencyCode();
				StringBuilder outputBuilder = new StringBuilder();
				outputBuilder.append(currencyCode);
				outputBuilder.append(" ");
				outputBuilder.append(balanceDTO.getBalance());

				if ((bankService != null)
						&& (bankService.getCurrency(currencyCode) != null)
						&& (bankService.getCurrency(currencyCode).getRateToUSD() != null)
						) {
					CurrencyDTO currencyDTO = bankService.getCurrency(currencyCode);
					outputBuilder.append(" (USD ");
					outputBuilder.append(currencyDTO.getRateToUSD().multiply(BigDecimal.valueOf(balanceDTO.getBalance())));
					outputBuilder.append(")");
				}

				outputBuilder.append(System.getProperty("line.separator"));

				outputWriter.append(outputBuilder.toString());
			}
			if (verbose) {
				outputWriter.write("--- End of Current Balances ---");
				outputWriter.write(System.getProperty("line.separator"));
			}
			outputWriter.flush();
		} catch (IOException e) {
			throw new OutputFailedException();
		}
	}
}
