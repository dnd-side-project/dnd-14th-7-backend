package com.dnd.ahaive.domain.insight.exception;

import com.dnd.ahaive.global.exception.CustomException;
import com.dnd.ahaive.global.exception.ErrorCode;

public class InsightAccessDeniedException extends CustomException {

    private final String errorMessage;

    public InsightAccessDeniedException(String message) {
        super(ErrorCode.INVALID_REQUEST);
        this.errorMessage = message;
    }


}
