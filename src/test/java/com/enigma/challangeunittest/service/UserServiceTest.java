package com.enigma.challangeunittest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.enigma.challangeunittest.model.entity.User;
import com.enigma.challangeunittest.repository.UserRepository;
import com.enigma.challangeunittest.services.UserService;

public class UserServiceTest {
 
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setUpTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Success Get Profile User")
  public void getProfileUser() {
    User user = User.builder().id("1").build();

    when(this.userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    User result = this.userService.getProfile("1");

    assertEquals(user.getId(), result.getId(), "Id user harus sama");
  }

  @Test
  @DisplayName("User Not Found")
  public void getProfileUserNotFound() {
    when(this.userRepository.findById("1")).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> this.userService.getProfile("1"), "User tidak ditemukan");
  }
}
