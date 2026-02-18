package com.dnd.ahaive.domain.auth.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class TokenNotFound extends CustomException {

  public TokenNotFound(ErrorCode errorCode) {
    super(errorCode);
  }
}
