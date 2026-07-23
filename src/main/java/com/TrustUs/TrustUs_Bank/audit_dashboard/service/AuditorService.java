package com.TrustUs.TrustUs_Bank.audit_dashboard.service;

import com.TrustUs.TrustUs_Bank.account.dtos.AccountDTO;
import com.TrustUs.TrustUs_Bank.auth_users.dtos.UserDTO;
import com.TrustUs.TrustUs_Bank.transaction.dtos.TransactionDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuditorService {

    Map<String, Long> getSystemTotals();
    Optional<UserDTO> findUserByEmail(String email);
    Optional<AccountDTO> findAccountDetailsByAccountNumber(String accountNumber);
    List<TransactionDTO> findTransactionsByAccountNumber(String accountNumber);
    Optional<TransactionDTO> findTransactionById (Long transactionId);

}
