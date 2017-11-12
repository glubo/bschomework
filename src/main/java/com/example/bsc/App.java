package com.example.bsc;

import com.example.bsc.exception.InputFailedException;
import com.example.bsc.helper.PaymentHelper;
import com.example.bsc.service.BankService;
import com.example.bsc.service.CurrencyImporterService;
import com.example.bsc.service.OutputService;
import com.example.bsc.service.PaymentImporterService;
import org.apache.commons.cli.*;

import java.io.*;
import java.util.concurrent.*;

/**
 * BSC Homework Application
 */
public class App {
	static int outputDelay = 60;
	static String paymentsFile = null;
	static String currenciesFile = null;
	static boolean verbose = false;

	public static void main(String[] args) {
		parseCli(args);

		BankService bankService = new BankService();
		OutputService outputService = new OutputService(bankService, verbose);
		PaymentImporterService paymentImporterService = new PaymentImporterService(bankService);
		CurrencyImporterService currencyImporterService = new CurrencyImporterService(bankService);
		// ^ IRL this would be taken care of by DI

		// we setup a thread that repeatedly outputs balances
		ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture<?> outputHandle =
				scheduler.scheduleWithFixedDelay(
						() -> {
							outputService.writeBalance(new OutputStreamWriter(System.out), bankService.retrieveBalances());
						},
						1, outputDelay, TimeUnit.SECONDS
				);

		//  setup a thread that read user input
		ExecutorService inputExecutor = Executors.newSingleThreadScheduledExecutor();
		Future<?> inputHandle = inputExecutor.submit(() -> {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			String line;
			try {
				while ((line = in.readLine()) != null) {
					if (line.compareTo("quit") == 0) {
						break;
					}
					bankService.applyPayment(PaymentHelper.parsePayment(line));
				}
			} catch (IOException e) {
				throw new InputFailedException(e);
			}
		});

		try {
			if (paymentsFile != null) {
				paymentImporterService.importFromFile(paymentsFile);
			}
			if (currenciesFile != null) {
				currencyImporterService.importFromFile(currenciesFile);
			}

			// we wait till input or output fails or finishes
			while (true) {
				if (outputHandle.isDone()) {
					outputHandle.get();
					break;
				}
				if (inputHandle.isDone()) {
					inputHandle.get();
					break;
				}
				Thread.sleep(100);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			outputHandle.cancel(true);
			scheduler.shutdown();
			inputHandle.cancel(true);
			inputExecutor.shutdown();
		}
	}

	private static void parseCli(String[] args) {
		Options cliOptions = buildOptions();

		CommandLineParser cliParser = new DefaultParser();
		HelpFormatter cliFormatter = new HelpFormatter();
		CommandLine cmd;

		try {
			cmd = cliParser.parse(cliOptions, args);
			if (cmd.hasOption("h")) {
				cliFormatter.printHelp("homework", cliOptions);
				System.exit(1);
			}

			if (cmd.hasOption("d")) {
				outputDelay = Integer.parseInt(cmd.getOptionValue("d"));
			}

			if (cmd.hasOption("p")) {
				paymentsFile = cmd.getOptionValue("p");
			}

			if (cmd.hasOption("c")) {
				currenciesFile = cmd.getOptionValue("c");
			}

		} catch (ParseException e) {
			System.out.println(e.getMessage());
			cliFormatter.printHelp("utility-name", cliOptions);
			System.exit(1);
		}
	}

	private static Options buildOptions() {
		Options options = new Options();
		final Option helpOption = Option.builder("h")
				.longOpt("help")
				.desc("Show this help")
				.hasArg(false)
				.required(false)
				.build();
		options.addOption(helpOption);
		final Option paymentsOption = Option.builder("p")
				.longOpt("payments")
				.desc("Read payments from file")
				.hasArg(true)
				.required(false)
				.build();
		options.addOption(paymentsOption);
		final Option currenciesOption = Option.builder("c")
				.longOpt("currencies")
				.desc("Read currencies from file")
				.hasArg(true)
				.required(false)
				.build();
		options.addOption(currenciesOption);
		final Option delayOption = Option.builder("d")
				.longOpt("delay")
				.desc("Set the output interval in seconds (default: 60)")
				.hasArg(true)
				.required(false)
				.build();
		options.addOption(delayOption);
		final Option verboseOption = Option.builder("v")
				.longOpt("verbose")
				.desc("More verbose output")
				.hasArg(false)
				.required(false)
				.build();
		options.addOption(verboseOption);
		return options;
	}
}
