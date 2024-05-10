package com.enigma.challangeunittest.security;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.challangeunittest.model.entity.AppUser;
import com.enigma.challangeunittest.services.interfaces.UserInterface;

@Component
public class JwtUtils {
  
  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.secretRefresh}")
  private String jwtSecretRefresh;
  
  @Value("${jwt.appName}")
  private String appName;
  
  @Value("${jwt.jwtExpiration}")
  private long jwtExpiration;

  @Value("${jwt.jwtExpirationRefresh}")
  private long jwtExpirationRefresh;

  @Autowired
  private UserInterface userInterface;
  
  public String generatedToken(AppUser appUser) {
    return JWT.create()
              .withIssuer(appName)
              .withSubject(appUser.getId())
              .withExpiresAt(Instant.now().plusSeconds(jwtExpiration))
              .withIssuedAt(Instant.now())
              .withClaim("role", appUser.getRole().name())
              .sign(this.getAlgorithm());
  }

  public String generatedRefreshToken(AppUser appUser) {
    return JWT.create()
              .withIssuer(appName)
              .withSubject(appUser.getId())
              .withExpiresAt(Instant.now().plusSeconds(jwtExpirationRefresh))
              .withIssuedAt(Instant.now())
              .withClaim("role", appUser.getRole().name())
              .sign(this.getAlgorithmRefreshToken());
  }

  public String getIdUserByToken(String token) {
    JWTVerifier verifier = JWT.require(this.getAlgorithm()).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT.getSubject();
  }

  public String getIdUserByRefreshToken(String token) {
    JWTVerifier verifier = JWT.require(this.getAlgorithmRefreshToken()).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    return decodedJWT.getSubject();
  }

  public boolean verifyJwtToken(String token) {
    JWTVerifier verifier = JWT.require(this.getAlgorithm()).build();
    DecodedJWT decodedJWT =  verifier.verify(token);
    return decodedJWT.getIssuer().equals(this.appName);
  }

  public String refreshAccessToken(String token) {
    AppUser appUser = this.userInterface.loadUserByUserId(this.getIdUserByRefreshToken(token));
    System.out.println(appUser);
    return this.generatedToken(appUser);
  }

  public Map<String, String> getUserInfoByToken(String token) {
    try {
      JWTVerifier verifier = JWT.require(this.getAlgorithm()).build();
      DecodedJWT decodedJWT =  verifier.verify(token);
      
      Map<String, String> userInfo = new HashMap<>();
      userInfo.put("userId", decodedJWT.getSubject());
      userInfo.put("role", decodedJWT.getClaim("role").asString());
      
      return userInfo;
    } catch (JWTVerificationException e) {
      throw new RuntimeException();
    }
  }

  private Algorithm getAlgorithm () {
    return Algorithm.HMAC256(this.jwtSecret.getBytes());
  }

  private Algorithm getAlgorithmRefreshToken () {
    return Algorithm.HMAC256(this.jwtSecretRefresh.getBytes());
  }

}
