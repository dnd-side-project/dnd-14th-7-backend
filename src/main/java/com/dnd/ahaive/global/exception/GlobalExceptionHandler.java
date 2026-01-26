package com.dnd.ahaive.global.exception;


import groovy.util.logging.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//  @ExceptionHandler(ProviderNotFoundException.class)
//  public ResponseEntity<ResponseDTO> providerNotFoundException(ProviderNotFoundException e) {
//    return ResponseEntity.status(e.getErrorCode().getActualStatusCode())
//        .body(ResponseDTO.of(e.getErrorCode()));
//  }

}
