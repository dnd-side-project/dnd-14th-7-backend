package com.dnd.ahaive.domain.question.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class AnswerNotFoundException extends CustomException {

  public AnswerNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
