package com.enigma.challangeunittest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.challangeunittest.model.dto.req.LoginUserDto;
import com.enigma.challangeunittest.model.dto.req.RefreshTokenUserDto;
import com.enigma.challangeunittest.model.dto.req.RegisterUserDto;
import com.enigma.challangeunittest.model.dto.res.LoginResponseDto;
import com.enigma.challangeunittest.model.dto.res.RefreshTokenResponseDto;
import com.enigma.challangeunittest.model.dto.res.RegisterResponseDto;
import com.enigma.challangeunittest.services.interfaces.AuthInterface;
import com.enigma.challangeunittest.utils.constant.ApiPathConstant;
import com.enigma.challangeunittest.utils.res.Response;

@RestController
@RequestMapping(
  ApiPathConstant.API +
  ApiPathConstant.VERSION +
  ApiPathConstant.AUTH
)
public class AuthController {

  @Autowired
  private AuthInterface authInterface;
  
  @PostMapping("register")
  public ResponseEntity<Response<RegisterResponseDto>> registerHandler(@RequestBody RegisterUserDto registerUserDto) {
    return ResponseEntity
    .status(HttpStatus.CREATED)
    .body(
      new Response<>(
        HttpStatus.CREATED.value(),
        "Berhasil Registrasi User",
        this.authInterface.register(registerUserDto)
      )
    );
  }

  @PostMapping("login")
  public ResponseEntity<Response<LoginResponseDto>> loginHandler(@RequestBody LoginUserDto loginUserDto) {
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new Response<>(
        HttpStatus.OK.value(),
        "Berhasil Login",
        this.authInterface.login(loginUserDto)
      )
    );
  }

  @PutMapping("refreshToken")
  public ResponseEntity<Response<RefreshTokenResponseDto>> refreshTokenHandler(@RequestBody RefreshTokenUserDto refreshTokenUserDto) {
    return ResponseEntity
    .status(HttpStatus.CREATED)
    .body(
      new Response<>(
        HttpStatus.CREATED.value(),
        "Berhasil Refresh Token",
        this.authInterface.refreshToken(refreshTokenUserDto)
      )
    );
  }

}
