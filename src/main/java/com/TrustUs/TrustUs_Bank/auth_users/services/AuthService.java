package com.TrustUs.TrustUs_Bank.auth_users.services;

import com.TrustUs.TrustUs_Bank.auth_users.dtos.LoginRequest;
import com.TrustUs.TrustUs_Bank.auth_users.dtos.LoginResponse;
import com.TrustUs.TrustUs_Bank.auth_users.dtos.RegistrationRequest;
import com.TrustUs.TrustUs_Bank.auth_users.dtos.ResetPasswordRequest;
import com.TrustUs.TrustUs_Bank.res.Response;

public interface AuthService {
    Response<String > register(RegistrationRequest request);
    Response<LoginResponse> login(LoginRequest loginRequest);
    Response<? > forgetPassword(String email);
    Response<? > updatePasswordViaResetCode(ResetPasswordRequest resetPasswordRequest);
}