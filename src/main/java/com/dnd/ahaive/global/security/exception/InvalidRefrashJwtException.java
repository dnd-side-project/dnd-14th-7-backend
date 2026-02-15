package com.dnd.ahaive.global.security.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class InvalidRefrashJwtException extends CustomException {

  public InvalidRefrashJwtException(ErrorCode errorCode) {
    super(errorCode);
  }
}
