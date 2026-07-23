package com.TrustUs.TrustUs_Bank.account.entity;

import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.enums.AccountStatus;
import com.TrustUs.TrustUs_Bank.enums.AccountType;
import com.TrustUs.TrustUs_Bank.enums.Currency;
import jakarta.persistence.*;
import com.TrustUs.TrustUs_Bank.transaction.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity //-> (JPA) will create us a table for this particular class because of @Entity annotation
@Data
@Builder
@Table(name="accounts")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 15)
    private String accountNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING) //Because AccountType is an Enum
    @Column(nullable = false)
    private AccountType accountType;

    @ManyToOne(fetch = FetchType.LAZY) //Many account can belong to one user
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    private LocalDateTime closedAt;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
