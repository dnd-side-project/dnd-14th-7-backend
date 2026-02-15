//package com.dnd.ahaive.global.common.util;
//
//import com.dnd.ahaive.global.exception.ErrorCode;
//import com.dnd.ahaive.global.exception.VectorEmbeddingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestClient;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class EmbeddingUtil {
//
//  private final RestClient restClient;
//
//  public float[] getVectorEmbedding(String text) {
//    try {
//      String url = "https://treasury-daisy-projected-magnetic.trycloudflare.com/api/vector?text=" + text;
//
//      String response = restClient.get()
//          .uri(url)
//          .retrieve()
//          .body(String.class);
//
//      ObjectMapper objectMapper = new ObjectMapper();
//      return objectMapper.readValue(response, float[].class);
//
//    } catch(Exception e) {
//      log.error("Embedding API 호출 실패 : " + e.getMessage());
//      throw new VectorEmbeddingException(ErrorCode.VECTOR_EMBEDDING_API_FAILED);
//    }
//  }
//
//}
