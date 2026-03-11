package com.dnd.ahaive.domain.question.service;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.service.InsightService;
import com.dnd.ahaive.domain.question.service.dto.AiQuestionsResponse;
import com.dnd.ahaive.domain.question.service.dto.QuestionContentDto;
import com.dnd.ahaive.infra.claude.ClaudeAiClient;
import com.dnd.ahaive.infra.claude.prompt.ClaudeAiPrompt;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionsFacadeService {

    private final InsightService insightService;
    private final QuestionService questionService;
    private final ClaudeAiClient aiClient;
    private final ObjectMapper objectMapper;

    public void regenerateQuestions(long insightId, String uuid) {
        Insight insight = insightService.getValidatedInsight(insightId, uuid);

//        현재 질문 조회
        List<QuestionContentDto> questionContents = questionService.getCurrentQuestions(insight.getId());

//        AI 호출 (현재 질문 전달해서 새 질문 생성)
        List<String> existingQuestions = questionContents.stream().map(QuestionContentDto::content).toList();
        String regeneratePrompt = ClaudeAiPrompt.REGENERATE_QUESTIONS_PROMPT(insight.getTitle(), existingQuestions);

        String regeneratedQuestions = aiClient.sendMessage(regeneratePrompt);
        List<String> collectedQuestionContents = parseQuestions(regeneratedQuestions);

//        보관 + 새 질문 저장 (tx 하나로 묶음)
        questionService.storeCurrentQuestionsAndSaveNewQuestions(insight, questionContents, collectedQuestionContents);
    }

    private List<String> parseQuestions(String response) {
        try {
            List<String> questions = objectMapper.readValue(response, AiQuestionsResponse.class).questions();

            if (questions.size() != 3) {
                throw new IllegalStateException("질문 생성에 실패했습니다. 생성된 질문 수: " + questions.size());
            }

            return questions;
        } catch (JacksonException e) {
            log.error("질문 매핑 실패 : {}", e.getMessage(), e);
            throw new IllegalStateException("질문 파싱에 실패했습니다.", e);
        }
    }

}
