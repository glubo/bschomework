package com.example;

import com.example.bsc.dto.BalanceDTO;
import com.example.bsc.service.BankService;
import com.example.bsc.service.CurrencyImporterService;
import com.example.bsc.service.PaymentImporterService;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;

public class CurrencyImporterTest {
	@Test
	public void testImport() throws IOException {
		Path tempfilePath = Files.createTempFile("bschomeworktest", "testimport");
		StringBuilder tempContent = new StringBuilder();

		tempContent.append("HKD 100");
		tempContent.append(System.getProperty("line.separator"));
		tempContent.append("CZK 2.2");
		tempContent.append(System.getProperty("line.separator"));
		tempContent.append("HKD 10.0");
		tempContent.append(System.getProperty("line.separator"));
		tempContent.append("HKD 1.1");
		tempContent.append(System.getProperty("line.separator"));

		Files.write(tempfilePath, tempContent.toString().getBytes(StandardCharsets.UTF_8));
		File tempfile = tempfilePath.toFile();
		tempfile.deleteOnExit();

		BankService bank = new BankService();
		CurrencyImporterService currencyImporterService = new CurrencyImporterService(bank);
		currencyImporterService.importFromFile(tempfilePath.toString());

		assertEquals(0, bank.getCurrency("HKD").getRateToUSD().compareTo(new BigDecimal("1.1")));
		assertEquals(0, bank.getCurrency("CZK").getRateToUSD().compareTo(new BigDecimal("2.2")));
	}
}
