package com.TrustUs.TrustUs_Bank.exceptions;

public class BadRequestException extends RuntimeException{
    public BadRequestException(String error){
        super(error); //Collection exception using RuntimeException
    }
}
