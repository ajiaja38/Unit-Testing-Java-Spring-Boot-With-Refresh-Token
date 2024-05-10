package com.enigma.challangeunittest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.enigma.challangeunittest.model.dto.req.LoginUserDto;
import com.enigma.challangeunittest.model.dto.req.RefreshTokenUserDto;
import com.enigma.challangeunittest.model.dto.res.LoginResponseDto;
import com.enigma.challangeunittest.model.dto.res.RefreshTokenResponseDto;
import com.enigma.challangeunittest.services.interfaces.AuthInterface;
import com.enigma.challangeunittest.utils.res.Response;
import com.enigma.challangeunittest.utils.res.ResponseMessage;

public class AuthControllerTest {

  @InjectMocks
  private AuthController authController;

  @Mock
  private AuthInterface authInterface;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("Login SuccessFully")
  public void loginHandlerSuccess() {
    LoginResponseDto loginResponseDto = LoginResponseDto.builder()
      .accessToken("accessToken")
      .refreshToken("refreshToken")
      .role("ROLE_USER")
      .build();

    when(this.authInterface.login(any(LoginUserDto.class))).thenReturn(loginResponseDto);

    LoginUserDto loginUserDto = LoginUserDto.builder()
      .username("validUser")
      .password("validPassword")
      .build();

    ResponseEntity<Response<LoginResponseDto>> responseEntity = this.authController.loginHandler(loginUserDto);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode(), "Status code harus OK");
    assertNotNull(responseEntity.getBody(), "Body response tidak kosong");
    assertEquals(HttpStatus.OK.value(), responseEntity.getBody().getCode(), "Code response harus OK");
    assertEquals("Berhasil Login", responseEntity.getBody().getMessage(), "Message response harus sesuai dengan yang ada di controller");
    assertEquals(loginResponseDto, responseEntity.getBody().getData(), "Data response harus sesuai dengan login response dto");
    assertEquals(loginResponseDto.getAccessToken(), responseEntity.getBody().getData().getAccessToken(), "Terdapat accesstoken pada body response dto");
    assertEquals(loginResponseDto.getRefreshToken(), responseEntity.getBody().getData().getRefreshToken(), "Terdapat refreshtoken pada body response");
    assertEquals(loginResponseDto.getRole(), responseEntity.getBody().getData().getRole(), "Terdapat role pada body response");
  }

  @Test
  @DisplayName("Login Failed")
  public void loginHandlerFailed() {
    LoginUserDto loginUserDto = LoginUserDto.builder()
      .username("invalidUser")
      .password("invalidPassword")
      .build();

    when(
      authInterface
      .login(any(LoginUserDto.class))
    )
    .thenThrow(
      new ResponseStatusException(
        HttpStatus.UNAUTHORIZED,
        "Username Atau Password tidak ditemukan!")
      );

    ResponseStatusException response = assertThrows(
      ResponseStatusException.class,
      () -> authController.loginHandler(loginUserDto)
    );

    ResponseMessage responseMessage = ResponseMessage.builder()
    .code(response.getStatusCode().value())
    .message(response.getReason())
    .build();

    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(), "Status code harus UNAUTHORIZED");
    assertEquals("Username Atau Password tidak ditemukan!", response.getReason(), "message error harus sesuai");
    assertEquals(HttpStatus.UNAUTHORIZED.value(), responseMessage.getCode(), "Response Status code harus 401");
    assertEquals("Username Atau Password tidak ditemukan!", responseMessage.getMessage(), "Response Message harus sesuai");
  }

  @Test
  @DisplayName("successfully refresh accessToken")
  public void refreshAccessTokenSuccessTest() {
    RefreshTokenResponseDto expectedRefreshToken = RefreshTokenResponseDto.builder()
    .accessToken("new.AccessToken")
    .build();

    when(this.authInterface.refreshToken(any(RefreshTokenUserDto.class))).thenReturn(expectedRefreshToken);
    
    RefreshTokenUserDto refreshTokenUserDto = RefreshTokenUserDto.builder()
    .refreshToken("validRefreshToken")
    .build();

    ResponseEntity<Response<RefreshTokenResponseDto>> response = this.authController.refreshTokenHandler(refreshTokenUserDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Status code harus 200 ok");
    assertNotNull(response.getBody(), "Body response tidak kosong");
    assertEquals(HttpStatus.CREATED.value(), response.getBody().getCode(), "Code response harus OK");
    assertEquals(expectedRefreshToken, response.getBody().getData(), "Data response harus sesuai dengan refresh token response dto");
    assertEquals("Berhasil Refresh Token", response.getBody().getMessage(), "terdapat message yang sesuai");
    assertEquals(expectedRefreshToken.getAccessToken(), response.getBody().getData().getAccessToken(), "accesstoken terdapat pada response body");
  }

  @Test
  @DisplayName("Failed refresh token")
  public void refreshTokenFailed() {
    RefreshTokenUserDto refreshTokenUserDto = RefreshTokenUserDto.builder()
    .refreshToken("invalidRefreshToken")
    .build();

    when(
      this.authController.refreshTokenHandler(
        any(RefreshTokenUserDto.class))
        )
    .thenThrow(
      new ResponseStatusException(
        HttpStatus.BAD_REQUEST, "refreshToken tidak valid")
    );

    ResponseStatusException response = assertThrows(
      ResponseStatusException.class,
      () -> this.authController.refreshTokenHandler(refreshTokenUserDto)
    );

    ResponseMessage responseMessage = ResponseMessage.builder()
    .code(response.getStatusCode().value())
    .message(response.getReason())
    .build();

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(), "Status code harus Bad Request");
    assertEquals("refreshToken tidak valid", response.getReason(), "message error harus sesuai");
    assertEquals(HttpStatus.BAD_REQUEST.value(), responseMessage.getCode(), "response code harus Bad request");
    assertEquals("refreshToken tidak valid", responseMessage.getMessage());
  }
}
