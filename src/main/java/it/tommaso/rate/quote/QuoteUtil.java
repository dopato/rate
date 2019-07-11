package it.tommaso.rate.quote;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;

import static java.nio.file.Files.newBufferedReader;

final class QuoteUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuoteUtil.class);

    private QuoteUtil() {
        //Prevent Instantiation
    }

    static TreeMap<Double, Integer> readQuotes(final Path marketFilePath) {

        final TreeMap<Double, Integer> quotesMap = new TreeMap<>();

        try (Reader reader = newBufferedReader(marketFilePath);
                CSVParser csvParser = new CSVParser(reader,
                        CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim())) {
            for (CSVRecord csvRecord : csvParser) {
                String rateString = csvRecord.get(1);
                String availableString = csvRecord.get(2);

                final double rate = Double.parseDouble(rateString);
                Integer available = Integer.valueOf(availableString);

                if (quotesMap.containsKey(rate)) {
                    Integer amount = quotesMap.get(rate);
                    available = amount + available;
                }
                quotesMap.put(rate, available);
            }
        } catch (IOException e) {
            throw new Error("Could not initialise the quote service, application can't work without it", e);
        }

        printQuotesMap(quotesMap);

        return quotesMap;
    }

    static double convertToMonthly(final double annualRateWeightedAverage) {
        return Math.pow(annualRateWeightedAverage + 1, (double) 1 / 12) - 1;
    }

    static <T> Collector<T, ?, Double> averagingWeighted(ToDoubleFunction<T> valueFunction,
            ToIntFunction<T> weightFunction) {
        class Box {

            double num = 0;
            long denom = 0;
        }
        return Collector.of(Box::new, (b, e) -> {
            b.num += valueFunction.applyAsDouble(e) * weightFunction.applyAsInt(e);
            b.denom += weightFunction.applyAsInt(e);
        }, (b1, b2) -> {
            b1.num += b2.num;
            b1.denom += b2.denom;
            return b1;
        }, b -> b.num / b.denom);

    }

    static void printQuotesMap(Map<Double, Integer> map) {
        map.forEach((k, v) -> {
            LOGGER.debug("---------------");
            LOGGER.debug("Rate : " + k);
            LOGGER.debug("Available : " + v);
            LOGGER.debug("---------------\n\n");
        });
    }

    static BigDecimal formatToBigdecimal(final double value, final int scale) {
        return BigDecimal.valueOf(value).setScale(scale, BigDecimal.ROUND_HALF_EVEN);
    }
}
