package com.TrustUs.TrustUs_Bank.roles.services;

import com.TrustUs.TrustUs_Bank.res.Response;
import com.TrustUs.TrustUs_Bank.roles.entity.Role;

import java.util.List;

public interface RoleService {

    Response<Role> createRole(Role roleRequest);
    Response<Role> updateRole(Role roleRequest);
    Response<List<Role>> getAllRoles();
    Response<?> deleteRole(Long id);
}
