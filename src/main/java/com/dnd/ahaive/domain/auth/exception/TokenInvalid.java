package com.dnd.ahaive.domain.auth.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class TokenInvalid extends CustomException {

  public TokenInvalid(ErrorCode errorCode) {
    super(errorCode);
  }
}
