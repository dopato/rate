package it.tommaso.rate.quote;

public interface QuoteService {

    boolean isTheAmountAvailable(final int amount);
    QuoteDto getQuote(final int loanAmount);
}
