package com.TrustUs.TrustUs_Bank.auth_users.entity;

import com.TrustUs.TrustUs_Bank.account.entity.Account;
import com.TrustUs.TrustUs_Bank.roles.entity.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity //-> (JPA) will create us a table for this particular class because of @Entity annotation
@Data
@Builder
@Table(name="users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;
    private String phoneNumber;

    @Email
    @Column(unique = true, nullable = false)
    @NotBlank(message = "Email is required")
    private String email;

    private String password;
    private String profilePictureUrl;
    private boolean active = true;

    @ManyToMany(fetch = FetchType.EAGER) //A user can have many roles, and one role can belong to many users
    //For ManyToMany another DB table called JoinTable is expected with it's properties mentioned
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany (mappedBy = "user", cascade = CascadeType.ALL) //mappedBy means, this account is owned by the "user"
    private List<Account> accounts;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}
