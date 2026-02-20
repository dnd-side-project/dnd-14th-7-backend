package com.dnd.ahaive.infra.claude.prompt;

import lombok.Getter;

@Getter
public class ClaudeAiPrompt {

  public static String INIT_THOUGHT_TO_INSIGHT_PROMPT(String initThought) {
    return """
        다음 메모를 핵심 인사이트 한 문장으로 변환해줘. 인사이트 문장만 출력하고 "인사이트:" 같은 접두사나 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: 서버 코드에 로그를 충분히 남겨두면 나중에 오류 원인 찾을 때 훨씬 수월하다.
        
        메모: %s
        """.formatted(initThought);
  }

  public static String INIT_THOUGHT_TO_TITLE_PROMPT(String initThought) {
    return """
        다음 메모를 핵심을 담은 짧은 제목으로 변환해줘. 제목만 출력하고 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: 서버 로그의 중요성
        
        메모: %s
        """.formatted(initThought);
  }

  public static String INIT_THOUGHT_TO_TAG_PROMPT(String initThought) {
    return """
        다음 메모를 핵심 키워드 3개로 변환해줘. JSON 형식으로만 출력하고 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: {"tags": ["개발", "디버깅", "로그"]}
        
        메모: %s
        """.formatted(initThought);
  }

  public static String INIT_THOUGHT_TO_QUESTION_PROMPT(String initThought) {
    return """
        다음 메모를 바탕으로 관련된 질문 3개를 생성해줘. JSON 형식으로만 출력하고 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: {"questions": ["이 경험을 한 문장 원칙으로 정리해볼까요?", "다음 프로젝트에서 적용하고 싶은 기준은?", "로그가 없어서 겪었던 실제 문제는?"]}
        
        메모: %s
        """.formatted(initThought);
  }


}
