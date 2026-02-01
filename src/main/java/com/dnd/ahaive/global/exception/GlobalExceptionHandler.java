package com.dnd.ahaive.global.exception;


import com.dnd.ahaive.global.common.response.ResponseDTO;
import groovy.util.logging.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(VectorEmbeddingException.class)
  public ResponseEntity<ResponseDTO> vectorEmbeddingException(VectorEmbeddingException e) {
    return ResponseEntity.status(e.getErrorCode().getActualStatusCode())
        .body(ResponseDTO.of(e.getErrorCode()));
  }

}
