package com.TrustUs.TrustUs_Bank.account.services;

import com.TrustUs.TrustUs_Bank.account.dtos.AccountDTO;
import com.TrustUs.TrustUs_Bank.account.entity.Account;
import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.enums.AccountType;
import com.TrustUs.TrustUs_Bank.res.Response;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountType accountType, User user);
    Response<List<AccountDTO>>getMyAccount();
    Response<?>closeAccount(String accountNumber);

}
