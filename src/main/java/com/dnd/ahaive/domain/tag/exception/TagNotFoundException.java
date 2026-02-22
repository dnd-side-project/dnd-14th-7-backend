package com.dnd.ahaive.domain.tag.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class TagNotFoundException extends CustomException {

  public TagNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
