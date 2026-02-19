package com.dnd.ahaive.domain.auth.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class TokenInvalidType extends CustomException {

  public TokenInvalidType(ErrorCode errorCode) {
    super(errorCode);
  }
}
