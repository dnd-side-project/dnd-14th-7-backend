//package com.dnd.ahaive.global.common.util;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//@SpringBootTest
//class EmbeddingUtilTest {
//
//  @Autowired
//  private EmbeddingUtil embeddingUtil;
//
//  @Test
//  @DisplayName("텍스트를 입력하면 float 배열 형태의 벡터를 반환한다.")
//  void getVectorEmbedding() {
//
//    // given
//    String text = "Hello, world!";
//
//    // when
//    float[] result = embeddingUtil.getVectorEmbedding(text);
//
//    // then
//    assertNotNull(result);
//    assertThat(result).isInstanceOf(float[].class);
//  }
//}