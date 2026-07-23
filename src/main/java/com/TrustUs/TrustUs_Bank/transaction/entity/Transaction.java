package com.TrustUs.TrustUs_Bank.transaction.entity;

import com.TrustUs.TrustUs_Bank.account.entity.Account;
import com.TrustUs.TrustUs_Bank.enums.TransactionStatus;
import com.TrustUs.TrustUs_Bank.enums.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity //-> (JPA) will create us a table for this particular class because of @Entity annotation
@Data
@Builder
@Table(name="transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType transactionType;

    @Column(nullable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    //for transfer
    private String sourceAccount;
    private String destinationAccount;
}




