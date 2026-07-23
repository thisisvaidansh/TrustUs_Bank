package com.TrustUs.TrustUs_Bank.notification.entity;

import com.TrustUs.TrustUs_Bank.account.entity.Account;
import com.TrustUs.TrustUs_Bank.enums.NotificationType;
import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
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
@Table(name="notifications")
@AllArgsConstructor
@NoArgsConstructor
public class Notification {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String recipient;
    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType type; //EMAIL/SMS/PUSH

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();
}
