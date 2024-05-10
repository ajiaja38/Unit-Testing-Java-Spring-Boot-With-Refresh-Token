package com.enigma.challangeunittest.services.interfaces;

import com.enigma.challangeunittest.model.dto.req.LoginUserDto;
import com.enigma.challangeunittest.model.dto.req.RefreshTokenUserDto;
import com.enigma.challangeunittest.model.dto.req.RegisterUserDto;
import com.enigma.challangeunittest.model.dto.res.LoginResponseDto;
import com.enigma.challangeunittest.model.dto.res.RefreshTokenResponseDto;
import com.enigma.challangeunittest.model.dto.res.RegisterResponseDto;

public interface AuthInterface {
  RegisterResponseDto register(RegisterUserDto registerUserDto);
  LoginResponseDto login(LoginUserDto loginUserDto);
  RefreshTokenResponseDto refreshToken(RefreshTokenUserDto refreshTokenUserDto);
}
