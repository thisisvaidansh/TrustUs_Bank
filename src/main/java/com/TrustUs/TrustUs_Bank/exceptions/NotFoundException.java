package com.TrustUs.TrustUs_Bank.exceptions;

public class NotFoundException extends RuntimeException{
    public NotFoundException(String error){
        super(error); //Collection exception using RuntimeException
    }
}
