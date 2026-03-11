package com.dnd.ahaive.global.exception;

public class InvalidInputValueException extends CustomException {

  public InvalidInputValueException(ErrorCode errorCode) {
    super(errorCode);
  }
}
