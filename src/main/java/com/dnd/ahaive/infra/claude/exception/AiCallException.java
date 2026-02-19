package com.dnd.ahaive.infra.claude.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class AiCallException extends CustomException {

  public AiCallException(ErrorCode errorCode) {
    super(errorCode);
  }
}
