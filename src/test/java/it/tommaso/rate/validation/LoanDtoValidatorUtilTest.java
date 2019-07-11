package it.tommaso.rate.validation;

import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class LoanDtoValidatorUtilTest {

    private static final String VALID_LOAN_AMOUNT = "1200";
    private static final String LESS_THEN_1000_LOAN_AMOUNT = "200";
    private static final String MORE_THEN_15000_LOAN_AMOUNT = "18200";
    private static final String NOT_IN_100_BY_100_LOAN_AMOUNT = "10201";
    private static final String VALID_CSV_PATH = "src/main/test/resources/market.csv";
    private static final String INVALID_CSV_PATH = "src/main/test/resources/market";

    @Test
    public void shouldPassWithValidArguments(){
        String[] args = new String[]{VALID_CSV_PATH,VALID_LOAN_AMOUNT};

        LoanDto loanDto = LoanValidatorUtil.validateLoan(args);

        assertEquals(loanDto.getLoanAmount(), Integer.parseInt(VALID_LOAN_AMOUNT));
        assertEquals(loanDto.getMarketFilePath(), Paths.get(VALID_CSV_PATH));

    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWithSmallAmount(){
        String[] args = new String[]{VALID_CSV_PATH,LESS_THEN_1000_LOAN_AMOUNT};
        LoanValidatorUtil.validateLoan(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWithBigAmount(){
        String[] args = new String[]{VALID_CSV_PATH,MORE_THEN_15000_LOAN_AMOUNT};
        LoanValidatorUtil.validateLoan(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWithNotIn100by100(){
        String[] args = new String[]{VALID_CSV_PATH,NOT_IN_100_BY_100_LOAN_AMOUNT};
        LoanValidatorUtil.validateLoan(args);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWitIfFileIsNotEndingWithDotCSV(){
        String[] args = new String[]{INVALID_CSV_PATH,VALID_LOAN_AMOUNT};
        LoanValidatorUtil.validateLoan(args);
    }


}