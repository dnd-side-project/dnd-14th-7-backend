package com.dnd.ahaive.domain.auth.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class AccessTokenInvalid extends CustomException {

  public AccessTokenInvalid(ErrorCode errorCode) {
    super(errorCode);
  }
}
