package com.enigma.challangeunittest.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.enigma.challangeunittest.model.dto.req.LoginUserDto;
import com.enigma.challangeunittest.model.dto.req.RefreshTokenUserDto;
import com.enigma.challangeunittest.model.dto.req.RegisterUserDto;
import com.enigma.challangeunittest.model.dto.res.LoginResponseDto;
import com.enigma.challangeunittest.model.dto.res.RefreshTokenResponseDto;
import com.enigma.challangeunittest.model.dto.res.RegisterResponseDto;
import com.enigma.challangeunittest.model.entity.AppUser;
import com.enigma.challangeunittest.model.entity.Role;
import com.enigma.challangeunittest.model.entity.User;
import com.enigma.challangeunittest.repository.UserRepository;
import com.enigma.challangeunittest.security.JwtUtils;
import com.enigma.challangeunittest.services.interfaces.AuthInterface;
import com.enigma.challangeunittest.services.interfaces.RoleInterface;
import com.enigma.challangeunittest.utils.constant.ERole;

import jakarta.transaction.Transactional;

@Service
public class AuthService implements AuthInterface {

  @Autowired
  private RoleInterface roleInterface;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtils jwtUtils;
  
  @Override
  @Transactional
  public RegisterResponseDto register(RegisterUserDto registerUserDto) {
    try {
      Role role = this.roleInterface.getOrSave(ERole.USER);
      List<Role> roles = new ArrayList<>();
      roles.add(role);
      
      User user = User.builder()
                      .username(registerUserDto.getUsername())
                      .password(this.passwordEncoder.encode(registerUserDto.getPassword()))
                      .address(registerUserDto.getAddress())
                      .phone(registerUserDto.getMobilePhone())
                      .age(registerUserDto.getAge())
                      .birthDate(registerUserDto.getBirthDate())
                      .createdAt(new Date())
                      .updatedAt(new Date())
                      .roles(roles)
                      .build();

      this.userRepository.save(user);

      return RegisterResponseDto.builder()
                                .username(user.getUsername())
                                .role(role.getName())
                                .build();
                                
    } catch (DataIntegrityViolationException e) {
      throw e;
    }
  }

  @Override
  public LoginResponseDto login(LoginUserDto loginUserDto) {
    try {
      Authentication authentication =  this.authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        loginUserDto.getUsername(),
        loginUserDto.getPassword()
      )
    );

    SecurityContextHolder.getContext().setAuthentication(authentication);
    AppUser appUser = (AppUser) authentication.getPrincipal();
    
    String accessToken = this.jwtUtils.generatedToken(appUser);
    
    String refreshToken = this.jwtUtils.generatedRefreshToken(appUser);
    
    return LoginResponseDto.builder()
                           .accessToken(accessToken)
                           .refreshToken(refreshToken)
                           .role(appUser.getRole().name())
                           .build();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username Atau Password tidak ditemukan!");
    }
  }

  @Override
  public RefreshTokenResponseDto refreshToken(RefreshTokenUserDto refreshTokenUserDto) {
    try {
      return RefreshTokenResponseDto.builder()
      .accessToken(this.jwtUtils.refreshAccessToken(refreshTokenUserDto.getRefreshToken()))
      .build();
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "refreshToken tidak valid");
    }
  }
  
}
