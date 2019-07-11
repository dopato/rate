package it.tommaso.rate.validation;

import java.nio.file.Path;
import java.nio.file.Paths;

import static java.util.Objects.isNull;

public class LoanValidatorUtil {

    private LoanValidatorUtil() {
        //prevent instantiation
    }

    public static LoanDto validateLoan(final String[] args) {
        if (isNull(args) || args.length != 2) {
            throw new IllegalArgumentException(
                    "The RateApp service expect exactly two arguments: [market_file_path] [loan_amount]");
        }

        if (!args[0].endsWith(".csv")) {
            throw new IllegalArgumentException("The marketFilePath should have a .csv extensions");
        }

        final Path marketFilePath = Paths.get(args[0]);

        final int loanAmount = Integer.parseInt(args[1]);

        if (isLoanOutOfRequirements(loanAmount)) {
            throw new IllegalArgumentException(
                    "The LoanDto amount must be requested in any £100 increment between £1000 and £15000 inclusive.");
        }

        return new LoanDto(marketFilePath, loanAmount);

    }

    private static boolean isLoanOutOfRequirements(final int loanAmount) {
        if (loanAmount < 1000) {
            return true;
        }

        if (loanAmount > 15000) {
            return true;
        }

        //must be in in any £100 increment
        if (loanAmount % 100 != 0) {
            return true;
        }

        return false;
    }
}
