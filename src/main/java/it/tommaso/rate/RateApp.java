package it.tommaso.rate;

import it.tommaso.rate.quote.CSVQuoteService;
import it.tommaso.rate.quote.QuoteDto;
import it.tommaso.rate.quote.QuoteService;
import it.tommaso.rate.validation.LoanDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.tommaso.rate.validation.LoanValidatorUtil.validateLoan;

public class RateApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateApp.class);

    public static void main(final String[] args) {

        final LoanDto loanDto = validateLoan(args);

        LOGGER.info("Rate request fields validated");

        final QuoteService quoteService = new CSVQuoteService(loanDto.getMarketFilePath());

        LOGGER.info("Quote Service initialised correctly");

        QuoteDto quote = quoteService.getQuote(loanDto.getLoanAmount());
        System.out.println(quote);

    }

}
