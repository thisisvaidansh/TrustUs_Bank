package com.TrustUs.TrustUs_Bank.exceptions;

public class InsufficientBalanceException extends RuntimeException{
    public InsufficientBalanceException(String error){
        super(error); //Collection exception using RuntimeException
    }
}
