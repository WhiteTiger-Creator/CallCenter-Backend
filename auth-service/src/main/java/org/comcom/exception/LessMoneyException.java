package org.comcom.exception;

public class LessMoneyException extends Exception {
    public LessMoneyException() {
        super("There is insufficient balance.");
    }
}