package com.TrustUs.TrustUs_Bank.transaction.services;

import com.TrustUs.TrustUs_Bank.account.entity.Account;
import com.TrustUs.TrustUs_Bank.account.repo.AccountRepo;
import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.auth_users.services.UserService;
import com.TrustUs.TrustUs_Bank.enums.TransactionStatus;
import com.TrustUs.TrustUs_Bank.enums.TransactionType;
import com.TrustUs.TrustUs_Bank.exceptions.BadRequestException;
import com.TrustUs.TrustUs_Bank.exceptions.InsufficientBalanceException;
import com.TrustUs.TrustUs_Bank.exceptions.InvalidTransactionException;
import com.TrustUs.TrustUs_Bank.exceptions.NotFoundException;
import com.TrustUs.TrustUs_Bank.notification.dtos.NotificationDTO;
import com.TrustUs.TrustUs_Bank.notification.services.NotificationService;
import com.TrustUs.TrustUs_Bank.res.Response;
import com.TrustUs.TrustUs_Bank.transaction.dtos.TransactionDTO;
import com.TrustUs.TrustUs_Bank.transaction.dtos.TransactionRequest;
import com.TrustUs.TrustUs_Bank.transaction.entity.Transaction;
import com.TrustUs.TrustUs_Bank.transaction.repo.TransactionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepo transactionRepo;
    private final AccountRepo accountRepo;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public Response<?> createTransaction(TransactionRequest transactionRequest) {
        Transaction transaction = new Transaction();
        transaction.setTransactionType(transactionRequest.getTransactionType());
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription());

        switch (transactionRequest.getTransactionType()){
            case DEPOSIT -> handleDeposit(transactionRequest, transaction);
            case WITHDRAWAL -> handleWithdrawal(transactionRequest, transaction);
            case TRANSFER -> handleTransfer(transactionRequest, transaction);
            default -> throw new InvalidTransactionException("Invalid Transaction Type");
        }

        transaction.setStatus(TransactionStatus.SUCCESS); //if there was a failure it would have been caught earlier
        Transaction savedTxn = transactionRepo.save(transaction);

        //send notification out
        sendTransactionNotifications(savedTxn);

        return Response.builder()
                .statusCode(200)
                .message("Transaction successful")
                .build();
    }

    @Override
    @Transactional //updates automatically, instead of manual saving
    //Here, use can check their transactions
    public Response<List<TransactionDTO>> getTransactionsForMyAccount(String accountNumber, int page, int size) {
        //Get the currently logged-in user
        User user = userService.getCurrentLoggedInUser();

        //Find the account by its number
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        //make sure the account belongs to the user, security check
        if(!account.getUser().getId().equals(user.getId())){
            throw new BadRequestException("Account does not belong to the authenticated user");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("transactionDate").descending());
        Page<Transaction> txns = transactionRepo.findByAccount_AccountNumber(accountNumber, pageable);

        List<TransactionDTO> transactionDTOS = txns.getContent().stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDTO.class))
                .toList();

        return Response.<List<TransactionDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Transaction Received")
                .data(transactionDTOS)
                .meta(Map.of(
                        "currentPage", txns.getNumber(),
                        "totalItems", txns.getTotalPages(),
                        "totalPages", txns.getTotalPages(),
                        "pageSize", txns.getSize()
                ))
                .build();
    }

    private void handleDeposit(TransactionRequest request, Transaction transaction){
        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        account.setBalance(account.getBalance().add(request.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);
    }

    private void handleWithdrawal(TransactionRequest request, Transaction transaction){
        Account account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));
        if(account.getBalance().compareTo(request.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));
        transaction.setAccount(account);
        accountRepo.save(account);
    }

    //only inhouse transfers, not authorized for cross bank transfers
    private void handleTransfer(TransactionRequest request, Transaction transaction){
        Account sourceAccount = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new NotFoundException("Account not found"));

        Account destination = accountRepo.findByAccountNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new NotFoundException("Destination Account not found"));

        if(sourceAccount.getBalance().compareTo(request.getAmount()) < 0){
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }

        //Deduct from source
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        accountRepo.save(sourceAccount);

        //Add to destination
        destination.setBalance(destination.getBalance().add(request.getAmount()));
        accountRepo.save(destination);

        transaction.setAccount(sourceAccount);
        transaction.setSourceAccount(sourceAccount.getAccountNumber());
        transaction.setDestinationAccount(destination.getAccountNumber());
    }

    private void sendTransactionNotifications(Transaction tnx){
        User user = tnx.getAccount().getUser();
        String subject;
        String template;

        Map<String, Object> templateVariables = new HashMap<>();
        templateVariables.put("name", user.getFirstName());
        templateVariables.put("amount", tnx.getAmount());
        templateVariables.put("accountNumber", tnx.getAccount().getAccountNumber());
        templateVariables.put("date", tnx.getTransactionDate());
        templateVariables.put("balance", tnx.getAccount().getBalance());

        if(tnx.getTransactionType() == TransactionType.DEPOSIT){
            subject = "Credit Alert";
            template = "credit-alert";

            NotificationDTO notificationEmailToSendOut = NotificationDTO.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();
            notificationService.sendEmail(notificationEmailToSendOut, user);
        }
        else if (tnx.getTransactionType() == TransactionType.WITHDRAWAL) {

            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmailToSendOut = NotificationDTO.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();
            notificationService.sendEmail(notificationEmailToSendOut, user);

        } else if (tnx.getTransactionType() == TransactionType.TRANSFER) {

            subject = "Debit Alert";
            template = "debit-alert";

            NotificationDTO notificationEmailToSendOut = NotificationDTO.builder()
                    .recipient(user.getEmail())
                    .subject(subject)
                    .templateName(template)
                    .templateVariables(templateVariables)
                    .build();

            notificationService.sendEmail(notificationEmailToSendOut, user);


            //RECEIVER CREDIT ALERT
            Account destination = accountRepo.findByAccountNumber(tnx.getDestinationAccount())
                    .orElseThrow(()-> new NotFoundException("Destination account not found"));

            User receiver = destination.getUser();

            Map<String, Object> recvVars = new HashMap<>();
            recvVars.put("name", receiver.getFirstName());
            recvVars.put("amount", tnx.getAmount());
            recvVars.put("accountNumber", destination.getAccountNumber());
            recvVars.put("date", tnx.getTransactionDate());
            recvVars.put("balance", destination.getBalance());

            NotificationDTO notificationEmailToSendOutToReciever = NotificationDTO.builder()
                    .recipient(receiver.getEmail())
                    .subject("Credit Alert")
                    .templateName("credit-alert")
                    .templateVariables(recvVars)
                    .build();

            notificationService.sendEmail(notificationEmailToSendOutToReciever, user);
        }
    }
}


