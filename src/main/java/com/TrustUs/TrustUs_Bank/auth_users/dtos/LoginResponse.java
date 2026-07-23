package com.TrustUs.TrustUs_Bank.auth_users.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoginResponse {
    //These data will be stored in the client side to authenticate the user
    private String token;
    private List<String> roles;

}
