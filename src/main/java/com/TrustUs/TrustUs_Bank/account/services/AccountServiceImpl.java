package com.TrustUs.TrustUs_Bank.account.services;

import com.TrustUs.TrustUs_Bank.account.dtos.AccountDTO;
import com.TrustUs.TrustUs_Bank.account.entity.Account;
import com.TrustUs.TrustUs_Bank.account.repo.AccountRepo;
import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.auth_users.services.UserService;
import com.TrustUs.TrustUs_Bank.enums.AccountStatus;
import com.TrustUs.TrustUs_Bank.enums.AccountType;
import com.TrustUs.TrustUs_Bank.enums.Currency;
import com.TrustUs.TrustUs_Bank.exceptions.BadRequestException;
import com.TrustUs.TrustUs_Bank.exceptions.NotFoundException;
import com.TrustUs.TrustUs_Bank.res.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AccountServiceImpl implements AccountService{

    private final AccountRepo accountRepo;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private final Random random = new Random(); //generate random account number

    @Override
    public Account createAccount(AccountType accountType, User user) {

        log.info("Inside createAccount()");

        String accountNumber = generateAccountNumber();
        //creating account with these fields
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(accountType)
                .currency(Currency.USD)
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return accountRepo.save(account);
    }

    @Override
    public Response<List<AccountDTO>> getMyAccount() {
        User user = userService.getCurrentLoggedInUser();
        List<AccountDTO> accounts = accountRepo.findByUserId(user.getId())
                .stream()
                .map(account -> modelMapper.map(account, AccountDTO.class))
                .toList();

        return Response.<List<AccountDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("User account fetched successfully")
                .data(accounts)
                .build();
    }

    @Override
    public Response<?> closeAccount(String accountNumber) {
        User user = userService.getCurrentLoggedInUser();
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(()-> new NotFoundException("Account not found"));

        if(!user.getAccounts().contains(account)){
            throw new NotFoundException("Account doesn't belong to you");
        }
        //withdraw all money if balance is > 0 before closing the account
        if(account.getBalance().compareTo(BigDecimal.ZERO)>0){
            throw new BadRequestException("Account balance must be zero before closing");
        }
        account.setStatus(AccountStatus.CLOSED);
        account.setClosedAt(LocalDateTime.now());
        accountRepo.save(account);

        return Response.builder()
                .statusCode(HttpStatus.OK.value())
                .message("Account closed successfully")
                .build();
    }

    private String generateAccountNumber() {
        String accountNumber;
        do {
            accountNumber = "66" + (random.nextInt(90000000) + 10000000); //generate a random 8 digits number from 10000000 and 99999999 then concatenate 66
        } while (accountRepo.findByAccountNumber(accountNumber).isPresent());

        log.info("account number generated {}", accountRepo);
        return accountNumber;
    }


}
