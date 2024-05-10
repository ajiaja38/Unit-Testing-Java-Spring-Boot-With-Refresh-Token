package com.enigma.challangeunittest.model.dto.req;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RegisterUserDto {

  private String username;
  private String password;
  private String address;
  private int age;
  private String mobilePhone;
  private Date birthDate;

}