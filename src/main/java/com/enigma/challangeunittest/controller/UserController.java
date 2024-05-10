package com.enigma.challangeunittest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enigma.challangeunittest.model.entity.User;
import com.enigma.challangeunittest.services.HeaderService;
import com.enigma.challangeunittest.services.interfaces.UserInterface;
import com.enigma.challangeunittest.utils.constant.ApiPathConstant;
import com.enigma.challangeunittest.utils.res.Response;

@RestController
@RequestMapping(
  ApiPathConstant.API +
  ApiPathConstant.VERSION +
  ApiPathConstant.USER
)
public class UserController {
  
  @Autowired
  private UserInterface userInterface;

  @Autowired
  private HeaderService headerService;
  
  @GetMapping
  public ResponseEntity<Response<List<User>>> getAllUserHandler() {
    
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new Response<>(
        HttpStatus.OK.value(),
        "Successfully Get All User",
        this.userInterface.getAllUser()
      )
    );

  }

  @GetMapping("profile")
  public ResponseEntity<Response<User>> getProfile(
    @RequestHeader(HttpHeaders.AUTHORIZATION)
    String authorizationHeader
  ) {
    return ResponseEntity
    .status(HttpStatus.OK)
    .body(
      new Response<>(
        HttpStatus.OK.value(),
        "Berhasil get profile",
        this.userInterface.getProfile(this.headerService.userIdByToken(authorizationHeader.substring(7)))
      )
    );

  }

}
