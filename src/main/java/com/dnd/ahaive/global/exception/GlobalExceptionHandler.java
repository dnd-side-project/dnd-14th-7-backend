package com.dnd.ahaive.global.exception;


import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ResponseDTO> handleUserNotFoundException(UserNotFoundException e) {
    log.error("사용자를 찾을 수 없습니다.", e);
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ResponseDTO> handleEntityNotFoundException(EntityNotFoundException e) {
    log.error(e.getMessage());
    return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ResponseDTO.of(e.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDTO> handleException(Exception e) {
    log.error("처리되지 않은 예외 발생", e);
    return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ResponseDTO.of(ErrorCode.INTERNAL_SERVER_ERROR));
  }

}
