package com.dnd.ahaive.domain.insight.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class InsightNotFoundException extends CustomException {

  public InsightNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
