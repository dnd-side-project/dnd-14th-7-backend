package com.dnd.ahaive.infra.claude.prompt;

import lombok.Getter;

@Getter
public class ClaudeAiPrompt {

  public static String MEMO_TO_INSIGHT_PROMPT(String memo) {
    return """
        다음 메모를 핵심 인사이트 한 문장으로 변환해줘. 인사이트 문장만 출력하고 "인사이트:" 같은 접두사나 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: 서버 코드에 로그를 충분히 남겨두면 나중에 오류 원인 찾을 때 훨씬 수월하다.
        
        메모: %s
        """.formatted(memo);
  }

  public static String MEMO_TO_TITLE_PROMPT(String memo) {
    return """
        다음 메모를 핵심을 담은 짧은 제목으로 변환해줘. 제목만 출력하고 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: 서버 로그의 중요성
        
        메모: %s
        """.formatted(memo);
  }


}
