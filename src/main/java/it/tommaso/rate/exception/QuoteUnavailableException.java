package it.tommaso.rate.exception;

public class QuoteUnavailableException extends RuntimeException {

    public QuoteUnavailableException(final String s) {
        super(s);
    }
}
