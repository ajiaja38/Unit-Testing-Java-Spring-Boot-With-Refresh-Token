package com.enigma.challangeunittest.model.dto.res;

import com.enigma.challangeunittest.utils.constant.ERole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RegisterResponseDto {
  private String username;
  private ERole role;
}
