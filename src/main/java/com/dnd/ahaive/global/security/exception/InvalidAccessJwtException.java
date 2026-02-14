package com.dnd.ahaive.global.security.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class InvalidAccessJwtException extends CustomException {

  public InvalidAccessJwtException(ErrorCode errorCode) {
    super(errorCode);
  }
}
