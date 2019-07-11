package it.tommaso.rate.quote;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

import static it.tommaso.rate.quote.QuoteUtil.averagingWeighted;
import static org.junit.Assert.*;

public class QuoteUtilTest {

    private static final Path VALID_CSV_PATH = Paths.get("src/test/resources/market.csv");
    private static final Path INVALID_CSV_PATH = Paths.get(".csv");
    private static final int NUMBER_OF_DIFFERENT_RATES = 6;
    private static final int AMOUNT_FOR_071_RATE = 580;
    private static final Double RATE_PRESENT_TWICE = 0.071;
    private static final double ANNUAL_RATE = 0.07;
    private static final double EXPECTED_MONTHLY_RATE = 0.005654145387405274;
    private static final double DELTA = 0.0001;
    private static final double EXPECTED_WEIGHTED_AVERAGE = 0.07564806866952792;

    @Test
    public void shouldReturnAQuoteMapAndSumAvailabilityForSameRate() {
        TreeMap<Double, Integer> quotesMap = QuoteUtil.readQuotes(VALID_CSV_PATH);
        assertEquals(NUMBER_OF_DIFFERENT_RATES, quotesMap.size());
        assertEquals(AMOUNT_FOR_071_RATE, (int) quotesMap.get(RATE_PRESENT_TWICE));
    }

    @Test(expected = Error.class)
    public void shouldThrowError() {
        QuoteUtil.readQuotes(INVALID_CSV_PATH);
    }

    @Test
    public void shouldConvertToMonthlyRate() {
        double monthly = QuoteUtil.convertToMonthly(ANNUAL_RATE);
        assertEquals(EXPECTED_MONTHLY_RATE, monthly, DELTA);
    }

    @Test
    public void shouldCalculateTheWeightedAverage() {
        TreeMap<Double, Integer> quotesMap = QuoteUtil.readQuotes(VALID_CSV_PATH);
        Double averagingWeighted =
                quotesMap.entrySet().stream().collect(averagingWeighted(Map.Entry::getKey, Map.Entry::getValue));

        assertEquals(EXPECTED_WEIGHTED_AVERAGE, averagingWeighted, DELTA);
    }

}