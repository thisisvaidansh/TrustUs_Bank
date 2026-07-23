package com.TrustUs.TrustUs_Bank.auth_users.services;

import com.TrustUs.TrustUs_Bank.auth_users.dtos.UpdatePasswordRequest;
import com.TrustUs.TrustUs_Bank.auth_users.dtos.UserDTO;
import com.TrustUs.TrustUs_Bank.auth_users.entity.User;
import com.TrustUs.TrustUs_Bank.res.Response;
import jakarta.mail.Multipart;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User getCurrentLoggedInUser();
    Response<UserDTO> getMyProfile();
    Response<Page<UserDTO>> getAllUsers(int page, int size); //Page to implement pagination
    Response<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);
    Response<?> uploadProfilePicture(MultipartFile file);
}
