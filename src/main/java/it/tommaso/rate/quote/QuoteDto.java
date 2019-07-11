

package it.tommaso.rate.quote;

import java.math.BigDecimal;
import java.util.StringJoiner;

public class QuoteDto {

    private int requestAmount;
    private BigDecimal annualInterestRate;
    private BigDecimal monthlyRepayment;
    private BigDecimal totalRepayment;

    public QuoteDto(final int requestAmount, final BigDecimal annualInterestRate, final BigDecimal monthlyRepayment,
            final BigDecimal totalRepayment) {
        this.requestAmount = requestAmount;
        this.annualInterestRate = annualInterestRate;
        this.monthlyRepayment = monthlyRepayment;
        this.totalRepayment = totalRepayment;
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(final int requestAmount) {
        this.requestAmount = requestAmount;
    }

    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }

    public void setAnnualInterestRate(final BigDecimal annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public BigDecimal getMonthlyRepayment() {
        return monthlyRepayment;
    }

    public void setMonthlyRepayment(final BigDecimal monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public BigDecimal getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(final BigDecimal totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    @Override
    public String toString() {
        return new StringJoiner("").add("Requested Amount: £" + requestAmount + "\n")
                .add(String.format("Annual Interest Rate: %s%%" , annualInterestRate)+ "\n")
                .add("Monthly repayment: £" + monthlyRepayment+ "\n")
                .add("Total repayment: £" + totalRepayment+ "\n")
                .toString();
    }

}
