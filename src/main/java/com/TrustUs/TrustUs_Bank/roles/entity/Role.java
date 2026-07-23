package com.TrustUs.TrustUs_Bank.roles.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity //-> (JPA) will create us a table for this particular class because of @Entity annotation
@Data
@Builder
@Table(name="roles")
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id //JPA will automatically handle it
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Unique value will be generated
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Name is required")
    private String name; //ROLE NAME e.g. CUSTOMER, AUTHOR, ADMIN (not name of a particular person)
}
