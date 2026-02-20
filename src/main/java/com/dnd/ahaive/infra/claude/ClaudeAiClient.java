package com.dnd.ahaive.infra.claude;

import com.dnd.ahaive.global.exception.ErrorCode;
import com.dnd.ahaive.infra.claude.exception.AiCallException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.anthropic.AnthropicChatModel;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClaudeAiClient {
  private final AnthropicChatModel chatModel;

  public String sendMessage(String prompt) {
    try {
      String aiResponse = chatModel.call(prompt);
      return aiResponse.replaceAll("```json|```", "").trim();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new AiCallException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }


}
