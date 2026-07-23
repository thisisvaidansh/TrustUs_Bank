package com.TrustUs.TrustUs_Bank.roles.controller;

import com.TrustUs.TrustUs_Bank.res.Response;
import com.TrustUs.TrustUs_Bank.roles.entity.Role;
import com.TrustUs.TrustUs_Bank.roles.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ADMIN')") //Only Admin has the authority
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Response<Role>> createRole(@RequestBody Role roleRequest){
        return ResponseEntity.ok(roleService.createRole(roleRequest));
    }

    @PutMapping
    public ResponseEntity<Response<Role>> updateRole(@RequestBody Role roleRequest){
        return ResponseEntity.ok(roleService.updateRole(roleRequest));
    }

    @GetMapping
    public ResponseEntity<Response<List<Role>>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<?>> deleteRoles(@PathVariable Long id){
        return ResponseEntity.ok(roleService.deleteRole(id));
    }
}
