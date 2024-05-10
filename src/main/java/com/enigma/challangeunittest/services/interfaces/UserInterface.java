package com.enigma.challangeunittest.services.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.enigma.challangeunittest.model.entity.AppUser;
import com.enigma.challangeunittest.model.entity.User;

public interface UserInterface extends UserDetailsService {
  AppUser loadUserByUserId(String id);
  List<User> getAllUser();
  User getProfile(String id);
}
