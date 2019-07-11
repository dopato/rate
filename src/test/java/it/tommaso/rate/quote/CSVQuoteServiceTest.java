package it.tommaso.rate.quote;

import it.tommaso.rate.exception.QuoteUnavailableException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CSVQuoteServiceTest {

    private static final Path VALID_CSV_PATH = Paths.get("src/test/resources/market.csv");
    private static final int REQUESTED_AMOUNT = 1000;
    private static final BigDecimal ANNUAL_RATE = new BigDecimal(7.0).setScale(1);
    private static final BigDecimal MONTHLY_REPAYMENT = new BigDecimal(30.78).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private static final BigDecimal TOTAL_REPAYMENT = new BigDecimal(1108.10).setScale(2, BigDecimal.ROUND_HALF_EVEN);
    private static final int UNAVAILABLE_AMOUNT = 15000;

    private QuoteService quoteService;

    @Before
    public void setUp() {
        quoteService = new CSVQuoteService(VALID_CSV_PATH);
    }

    @Test
    public void shouldGiveAvailability() {
        assertTrue(quoteService.isTheAmountAvailable(1000));
    }

    @Test
    public void shouldRefuseAvailability() {
        assertFalse(quoteService.isTheAmountAvailable(UNAVAILABLE_AMOUNT));
    }

    @Test(expected = QuoteUnavailableException.class)
    public void shouldThrowQuoteUnavailable() {
        quoteService.getQuote(UNAVAILABLE_AMOUNT);
    }

    @Test
    public void shouldCalculateAQuote() {
        QuoteDto quote = quoteService.getQuote(REQUESTED_AMOUNT);
        assertEquals(REQUESTED_AMOUNT, quote.getRequestAmount());
        assertEquals(ANNUAL_RATE, quote.getAnnualInterestRate());
        assertEquals(MONTHLY_REPAYMENT, quote.getMonthlyRepayment());
        assertEquals(TOTAL_REPAYMENT, quote.getTotalRepayment());
    }

}