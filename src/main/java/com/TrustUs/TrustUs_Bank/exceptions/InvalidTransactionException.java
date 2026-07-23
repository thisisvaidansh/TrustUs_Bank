package com.TrustUs.TrustUs_Bank.exceptions;

public class InvalidTransactionException extends RuntimeException{
    public InvalidTransactionException(String error){
        super(error); //Collection exception using RuntimeException
    }
}
