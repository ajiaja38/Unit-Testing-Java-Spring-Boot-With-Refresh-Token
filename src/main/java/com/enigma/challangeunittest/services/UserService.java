package com.enigma.challangeunittest.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enigma.challangeunittest.model.entity.AppUser;
import com.enigma.challangeunittest.model.entity.User;
import com.enigma.challangeunittest.repository.UserRepository;
import com.enigma.challangeunittest.services.interfaces.UserInterface;

@Service
public class UserService implements UserInterface {

  @Autowired
  private UserRepository userRepository;
  
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = this.userRepository.findByUsername(username).orElseThrow(
      () -> new UsernameNotFoundException("Invalid credentials user")
    );

    return AppUser.builder()
                  .Id(user.getId())
                  .username(user.getUsername())
                  .password(user.getPassword())
                  .role(user.getRoles().get(0).getName())
                  .build();
  }

  @Override
  public AppUser loadUserByUserId(String id) {
    User user = this.userRepository.findById(id).orElseThrow(
      () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found")
    );

    return AppUser.builder()
                  .Id(user.getId())
                  .username(user.getUsername())
                  .password(user.getPassword())
                  .role(user.getRoles().get(0).getName())
                  .build();
  }

  @Override
  public List<User> getAllUser() {
    return this.userRepository.findAll();
  }

  @Override
  public User getProfile(String id) {
    return this.userRepository.findById(id).orElseThrow(
      () -> new ResponseStatusException(HttpStatus.NOT_FOUND, id + "User Notfound")
    );
  }
  
}
