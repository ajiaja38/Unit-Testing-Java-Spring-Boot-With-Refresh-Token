package com.enigma.challangeunittest.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enigma.challangeunittest.model.entity.Role;
import com.enigma.challangeunittest.repository.RoleRepository;
import com.enigma.challangeunittest.services.interfaces.RoleInterface;
import com.enigma.challangeunittest.utils.constant.ERole;

@Service
public class RoleService implements RoleInterface {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  public Role getOrSave(ERole role) {
    
    Optional<Role> optionalRole = roleRepository.findByName(role);

    if (optionalRole.isPresent()) {
      return optionalRole.get();
    }

    Role currentRole = Role.builder()
                           .name(role)
                           .build();

    return this.roleRepository.saveAndFlush(currentRole);

  }
  
}
