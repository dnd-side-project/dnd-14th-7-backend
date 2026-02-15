package com.dnd.ahaive.global.security.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class UserNotFoundException extends CustomException {

  public UserNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
