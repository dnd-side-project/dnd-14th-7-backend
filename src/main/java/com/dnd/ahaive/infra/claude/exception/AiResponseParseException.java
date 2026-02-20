package com.dnd.ahaive.infra.claude.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class AiResponseParseException extends CustomException {

  public AiResponseParseException(ErrorCode errorCode) {
    super(errorCode);
  }
}
