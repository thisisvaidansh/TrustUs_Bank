package com.TrustUs.TrustUs_Bank.transaction.services;

import com.TrustUs.TrustUs_Bank.res.Response;
import com.TrustUs.TrustUs_Bank.transaction.dtos.TransactionDTO;
import com.TrustUs.TrustUs_Bank.transaction.dtos.TransactionRequest;

import java.util.List;

public interface TransactionService {
    Response<?>createTransaction(TransactionRequest transactionRequest);
    Response<List<TransactionDTO>> getTransactionsForMyAccount(String accountNumber, int page, int size);
}
