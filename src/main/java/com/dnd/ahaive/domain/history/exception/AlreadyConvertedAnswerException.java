package com.dnd.ahaive.domain.history.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class AlreadyConvertedAnswerException extends CustomException {

  public AlreadyConvertedAnswerException(ErrorCode errorCode) {
    super(errorCode);
  }
}
