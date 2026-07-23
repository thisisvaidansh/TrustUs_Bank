package com.TrustUs.TrustUs_Bank.notification.repo;

import com.TrustUs.TrustUs_Bank.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface NotificationRepo extends JpaRepository<Notification, Long> {
}
