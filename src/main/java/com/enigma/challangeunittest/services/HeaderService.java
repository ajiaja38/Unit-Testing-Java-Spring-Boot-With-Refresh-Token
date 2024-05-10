package com.enigma.challangeunittest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enigma.challangeunittest.security.JwtUtils;

@Service
public class HeaderService {
  
  @Autowired
  private JwtUtils jwtUtils;

  public String userIdByToken(String token) {
    return this.jwtUtils.getIdUserByToken(token);
  }

}
