package com.example;

import com.example.bsc.dto.BalanceDTO;
import com.example.bsc.service.BankService;
import com.example.bsc.service.PaymentImporterService;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class PaymentImporterTest {
	@Test
	public void testImport() throws IOException {
		Path tempfilePath = Files.createTempFile("bschomeworktest", "testimport");
		StringBuilder tempContent = new StringBuilder();

		tempContent.append("USD 100");
		tempContent.append(System.getProperty("line.separator"));
		tempContent.append("CZK 2.2");
		tempContent.append(System.getProperty("line.separator"));
		tempContent.append("USD 10.0");
		tempContent.append(System.getProperty("line.separator"));
		tempContent.append("USD 1.1");
		tempContent.append(System.getProperty("line.separator"));

		Files.write(tempfilePath, tempContent.toString().getBytes(StandardCharsets.UTF_8));
		File tempfile = tempfilePath.toFile();
		tempfile.deleteOnExit();

		BankService bank = new BankService();
		PaymentImporterService paymentImporterService = new PaymentImporterService(bank);
		paymentImporterService.importFromFile(tempfilePath.toString());


		assertEquals(2, bank.retrieveBalances().size());
		int checked = 0;
		for (BalanceDTO balanceDTO: bank.retrieveBalances()) {
			switch (balanceDTO.getCurrencyCode()) {
				case "USD":
					checked++;
					assertEquals(0, balanceDTO.getBalance().compareTo(new BigDecimal("111.1")));
					break;
				case "CZK":
					checked++;
					assertEquals(0, balanceDTO.getBalance().compareTo(new BigDecimal("2.2")));
					break;
				default:
					assertEquals(true,false);

			}
		}
		assertEquals(2, checked);
	}
}
