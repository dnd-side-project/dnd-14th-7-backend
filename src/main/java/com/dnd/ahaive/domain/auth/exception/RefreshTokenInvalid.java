package com.dnd.ahaive.domain.auth.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class RefreshTokenInvalid extends CustomException {
  public RefreshTokenInvalid(ErrorCode errorCode) {
    super(errorCode);
  }
}
