package com.dnd.ahaive.domain.trash.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class PieceNotFoundException extends CustomException {
  public PieceNotFoundException(ErrorCode errorCode) {
    super(errorCode);
  }
}
