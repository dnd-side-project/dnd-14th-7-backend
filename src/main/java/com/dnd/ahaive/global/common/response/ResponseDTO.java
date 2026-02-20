package com.dnd.ahaive.global.common.response;

import com.dnd.ahaive.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.time.ZoneId;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
@Schema(description = "공통 응답 DTO")

public class ResponseDTO<T> {

  private LocalDateTime localDateTime;
  private int responseCode;
  private String statusCode;
  private String message;

  T data;

  public static <T> ResponseDTO<T> of(ErrorCode errorCode, T data) {
    return ResponseDTO.<T>builder()
        .localDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .statusCode(errorCode.getCode())
        .responseCode(errorCode.getActualStatusCode())
        .data(data)
        .message(errorCode.getMessage())
        .build();
  }

  public static <T> ResponseDTO<T> of(ErrorCode errorCode) {
    return ResponseDTO.<T>builder()
        .localDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .statusCode(errorCode.getCode())
        .responseCode(errorCode.getActualStatusCode())
        .message(errorCode.getMessage())
        .data(null)
        .build();
  }

//  public static <T> ResponseDTO<T> error(ErrorCode errorCode, String overrideMessage) {
//    return ResponseDTO.<T>builder()
//            .localDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
//            .statusCode(errorCode.getCode())
//            .responseCode(errorCode.getActualStatusCode())
//            .message(overrideMessage)
//            .data(null)
//            .build();
//  }
  
  public static <T> ResponseDTO<T> of(T data, String message) {
    return ResponseDTO.<T>builder()
        .localDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .statusCode("SUCCESS")
        .responseCode(200)
        .message(message)
        .data(data)
        .build();
  }

  public static <T> ResponseDTO<T> of(String message) {
    return ResponseDTO.<T>builder()
        .localDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .statusCode("SUCCESS")
        .responseCode(200)
        .message(message)
        .data(null)
        .build();
  }

  public static <T> ResponseDTO<T> created(T data, String message) {
    return ResponseDTO.<T>builder()
        .localDateTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
        .statusCode("CREATED")
        .responseCode(201)
        .message(message)
        .data(data)
        .build();
  }

}
