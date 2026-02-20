package com.dnd.ahaive.global.exception;


import com.dnd.ahaive.domain.auth.exception.RefreshTokenInvalid;
import com.dnd.ahaive.domain.auth.exception.TokenInvalid;
import com.dnd.ahaive.domain.auth.exception.TokenInvalidType;
import com.dnd.ahaive.domain.auth.exception.TokenNotFound;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.exception.UserNotFoundException;
import com.dnd.ahaive.infra.claude.exception.AiCallException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(TokenInvalid.class)
  public ResponseEntity<ResponseDTO> handleTokenInvalidException(TokenInvalid e) {
    log.error("유효하지 않은 토큰입니다.", e);
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(TokenInvalidType.class)
  public ResponseEntity<ResponseDTO> handleTokenInvalidTypeException(TokenInvalidType e) {
    log.error("잘못된 토큰 타입입니다.", e);
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(TokenNotFound.class)
  public ResponseEntity<ResponseDTO> handleTokenNotFoundException(TokenNotFound e) {
    log.error("토큰이 존재하지 않습니다.", e);
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(RefreshTokenInvalid.class)
  public ResponseEntity<ResponseDTO> handleRefreshTokenInvalidException(RefreshTokenInvalid e) {
    log.error("유효하지 않은 리프레시 토큰입니다.", e);
    return ResponseEntity
        .status(HttpStatus.UNAUTHORIZED)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ResponseDTO> handleUserNotFoundException(UserNotFoundException e) {
    log.error("사용자를 찾을 수 없습니다.", e);
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(AiCallException.class)
  public ResponseEntity<ResponseDTO> handleAiCallException(AiCallException e) {
    log.error("AI 호출 중 예외가 발생했습니다.", e);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ResponseDTO.of(e.getErrorCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    log.warn("입력값 검증 실패: {}", e.getBindingResult().getFieldErrors());
    return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getActualStatusCode())
        .body(ResponseDTO.of(ErrorCode.INVALID_INPUT_VALUE));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ResponseDTO> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
    log.warn("잘못된 요청 값: {}", e.getMessage());
    return ResponseEntity.status(ErrorCode.INVALID_INPUT_VALUE.getActualStatusCode())
        .body(ResponseDTO.of(ErrorCode.INVALID_INPUT_VALUE));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ResponseDTO> handleException(Exception e) {
    log.error("처리되지 않은 예외 발생", e);
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ResponseDTO.of(ErrorCode.INTERNAL_SERVER_ERROR));
  }

}
