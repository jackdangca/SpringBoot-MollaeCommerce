package com.molla.services.serviceImp;

import com.molla.model.Role;
import com.molla.repository.RoleRepository;
import com.molla.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepo;

    public RoleServiceImp(RoleRepository roleRepo) {
        this.roleRepo = roleRepo;
    }

    @Override
    public List<Role> listRoles() {
        return (List<Role>) roleRepo.findAll();
    }
}
