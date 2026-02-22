package com.dnd.ahaive.infra.claude.prompt;

import com.dnd.ahaive.domain.insight.entity.InsightCandidate;
import java.util.List;
import java.util.stream.Collectors;
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
        다음 메모를 핵심 키워드 3개로 변환해줘. 마크다운 코드블록 없이 순수 JSON 형식으로만 출력하고 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: {"tags": ["개발", "디버깅", "로그"]}
        
        메모: %s
        """.formatted(initThought);
  }

  public static String INIT_THOUGHT_TO_QUESTION_PROMPT(String initThought) {
    return """
        다음 메모를 바탕으로 관련된 질문 3개를 생성해줘. 마크다운 코드블록 없이 순수 JSON 형식으로만 출력하고 부가 설명은 절대 포함하지 마.
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: {"questions": ["이 경험을 한 문장 원칙으로 정리해볼까요?", "다음 프로젝트에서 적용하고 싶은 기준은?", "로그가 없어서 겪었던 실제 문제는?"]}
        
        메모: %s
        """.formatted(initThought);
  }

  public static String INIT_THOUGHT_TO_INSIGHT_CANDIDATE_PROMPT(String initThought, List<InsightCandidate> latestCandidates) {

    if (latestCandidates == null || latestCandidates.isEmpty()) {
      return """
            다음 메모를 바탕으로 핵심 인사이트 후보 3개를 생성해줘. 마크다운 코드블록 없이 순수 JSON 형식으로만 출력하고 부가 설명은 절대 포함하지 마.
            
            예시:
            메모: 로그 안남겨서 디버깅이 너무 힘들었다
            출력: {"insightCandidates": ["충분한 로그는 문제 해결을 빠르게 한다.", "로그가 없으면 문제 원인 파악이 어려워진다.", "개발 초기부터 로그 전략을 세우는 것이 중요하다."]}
            
            메모: %s
            """.formatted(initThought);
    }

    String previousCandidates = latestCandidates.stream()
        .map(InsightCandidate::getContent)
        .collect(Collectors.joining("\n- ", "- ", ""));

    return """
        다음 메모를 바탕으로 핵심 인사이트 후보 3개를 생성해줘. 마크다운 코드블록 없이 순수 JSON 형식으로만 출력하고 부가 설명은 절대 포함하지 마.
        
        이전에 생성된 후보들과 반드시 다른 관점으로 생성해줘:
        %s
        
        예시:
        메모: 로그 안남겨서 디버깅이 너무 힘들었다
        출력: {"insightCandidates": ["충분한 로그는 문제 해결을 빠르게 한다.", "로그가 없으면 문제 원인 파악이 어려워진다.", "개발 초기부터 로그 전략을 세우는 것이 중요하다."]}
        
        메모: %s
        """.formatted(previousCandidates, initThought);
  }

  public static String ANSWER_TO_INSIGHT_PROMPT(String answer){
    return """
        다음 답변을 핵심 인사이트 한 문장으로 변환해줘. 인사이트 문장만 출력하고 "인사이트:" 같은 접두사나 부가 설명은 절대 포함하지 마.
        
        예시:
        답변: 서버에서 특정 요청이 간헐적으로 실패했는데, 관련 로그가 남아 있지 않아 어떤 조건에서 발생하는지 확인할 수 없었다.\s
                                결국 동일한 상황을 로컬에서 재현하려고 여러 번 테스트했지만, 재현되지 않아 원인 파악에 하루 이상이 소요되었다.
        출력: 간헐적 요청 실패에 대한 로그 부재로 원인 파악에 하루 이상 소요되었으며, 충분한 로그를 남겨두는 것이 중요함을 느꼈다.
        
        답변: %s
        """.formatted(answer);
  }

}
