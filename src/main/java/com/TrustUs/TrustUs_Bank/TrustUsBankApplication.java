package com.TrustUs.TrustUs_Bank;

import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.enums.NotificationType;
import com.TrustUs.TrustUs_Bank.notification.dtos.NotificationDTO;
import com.TrustUs.TrustUs_Bank.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class TrustUsBankApplication {

//	private final NotificationService notificationService;
	public static void main(String[] args) {
		SpringApplication.run(TrustUsBankApplication.class, args);
	}

//	@Bean
//	CommandLineRunner runner(){
//		return args -> {
//			NotificationDTO notificationDTO = NotificationDTO.builder()
//					.recipient("vaidanshapps@gmail.com")
//					.subject("Hello Testing Email")
//					.body("Hey, This is a test email")
//					.type(NotificationType.EMAIL)
//					.build();
//
//			notificationService.sendEmail(notificationDTO, new User());
//		};
//	}

}
