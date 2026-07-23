package com.TrustUs.TrustUs_Bank.account.dtos;

import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.enums.AccountStatus;
import com.TrustUs.TrustUs_Bank.enums.AccountType;
import com.TrustUs.TrustUs_Bank.enums.Currency;
import com.TrustUs.TrustUs_Bank.transaction.dtos.TransactionDTO;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.transaction.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {

    private Long id;

    private String accountNumber;

    private BigDecimal balance = BigDecimal.ZERO;

    private AccountType accountType;

    @JsonBackReference //To avoid circular dependency. As, this will not be added to account dto, it'll be ignored because it's a back reference
    private User user;

    private Currency currency;

    private AccountStatus status;

    @JsonManagedReference //it helps avoid recursion loop by ignoring the AccountDTO within the TransactionDTO
    private List<TransactionDTO> transactions;

    private LocalDateTime closedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
