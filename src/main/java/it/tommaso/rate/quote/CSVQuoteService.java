package it.tommaso.rate.quote;

import it.tommaso.rate.exception.QuoteUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static it.tommaso.rate.quote.QuoteUtil.*;

public class CSVQuoteService implements QuoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CSVQuoteService.class);

    private static final int THREE_YEARS_IN_MONTHS = Period.ofMonths(36).getMonths();
    private static final int ONE_DECIMAL = 1;
    private static final int TWO_DECIMAL = 2;
    private static final int ONE_HUNDRED = 100;

    private final TreeMap<Double, Integer> quotesMap;

    public CSVQuoteService(final Path marketFilePath) {
        this.quotesMap = readQuotes(marketFilePath);
    }

    @Override
    public boolean isTheAmountAvailable(final int amount) {
        return quotesMap.entrySet().stream().mapToInt(Map.Entry::getValue).sum() >= amount;
    }

    @Override
    public QuoteDto getQuote(final int loanAmount) {
        if (!isTheAmountAvailable(loanAmount)) {
            throw new QuoteUnavailableException("Is not possible to provide a quote");
        }

        Map<Double, Integer> inTheLoanMap = selectQuotesInterestedByTheLoan(loanAmount);

        Double annualRateWeightedAverage =
                inTheLoanMap.entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue));
        LOGGER.debug("annualRateWeightedAverage  : " + annualRateWeightedAverage);

        double monthlyRepayment = calculateMonthlyRepayment(annualRateWeightedAverage, loanAmount);
        LOGGER.debug("Monthly repayment : " + monthlyRepayment);

        BigDecimal annualRateWeightedAverageRoundedPercentage =
                formatToBigdecimal(annualRateWeightedAverage * ONE_HUNDRED, ONE_DECIMAL);

        BigDecimal monthlyRepaymentRounded = formatToBigdecimal(monthlyRepayment, TWO_DECIMAL);

        BigDecimal totalRepaymentRounded = formatToBigdecimal(monthlyRepayment * THREE_YEARS_IN_MONTHS, TWO_DECIMAL);

        return new QuoteDto(loanAmount, annualRateWeightedAverageRoundedPercentage, monthlyRepaymentRounded,
                totalRepaymentRounded);
    }

    private Map<Double, Integer> selectQuotesInterestedByTheLoan(final int loanAmount) {
        final Map<Double, Integer> inTheLoanMap = new HashMap<>();
        int remainingAmount = loanAmount;

        while (remainingAmount > 0) {
            Map.Entry<Double, Integer> quote = quotesMap.pollFirstEntry();

            int valueToStore = quote.getValue();

            //Not all the available money may be needed
            if (remainingAmount < quote.getValue()) {
                valueToStore = remainingAmount;
            }
            remainingAmount = remainingAmount - quote.getValue();

            inTheLoanMap.put(quote.getKey(), valueToStore);
        }

        LOGGER.debug("Quotes interested by the Loan");
        printQuotesMap(inTheLoanMap);

        return inTheLoanMap;
    }

    private double calculateMonthlyRepayment(final double annualRateWeightedAverage, final int loanAmount) {
        double monthlyRate = convertToMonthly(annualRateWeightedAverage);

        LOGGER.debug("Monthly Rate: {}", monthlyRate);

        //Please refer to https://en.wikipedia.org/wiki/Amortization_calculator#The_formula

        double expression = monthlyRate / (Math.pow(1 + monthlyRate, THREE_YEARS_IN_MONTHS) - 1);

        return loanAmount * (monthlyRate + expression);
    }

}