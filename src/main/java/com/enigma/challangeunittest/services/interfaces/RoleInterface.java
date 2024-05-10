package com.enigma.challangeunittest.services.interfaces;

import com.enigma.challangeunittest.model.entity.Role;
import com.enigma.challangeunittest.utils.constant.ERole;

public interface RoleInterface {
  Role getOrSave(ERole role);
}
