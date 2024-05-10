package com.enigma.challangeunittest.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.enigma.challangeunittest.model.entity.Role;
import com.enigma.challangeunittest.utils.constant.ERole;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
  Optional<Role> findByName(ERole role);
}
