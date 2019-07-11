package it.tommaso.rate.validation;

import java.nio.file.Path;
import java.util.StringJoiner;

public class LoanDto {

    private Path marketFilePath;

    private int loanAmount;

    public LoanDto(final Path marketFilePath, final int loanAmount) {
        this.marketFilePath = marketFilePath;
        this.loanAmount = loanAmount;
    }

    public Path getMarketFilePath() {
        return marketFilePath;
    }

    public void setMarketFilePath(final Path marketFilePath) {
        this.marketFilePath = marketFilePath;
    }

    public int getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(final int loanAmount) {
        this.loanAmount = loanAmount;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", LoanDto.class.getSimpleName() + "[", "]").add("marketFilePath=" + marketFilePath)
                .add("loanAmount=" + loanAmount)
                .toString();
    }
}
