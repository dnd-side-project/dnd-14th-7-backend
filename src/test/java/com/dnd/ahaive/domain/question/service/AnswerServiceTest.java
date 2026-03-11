package com.dnd.ahaive.domain.question.service;

import static org.assertj.core.api.Assertions.*;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.insight.repository.InsightRepository;
import com.dnd.ahaive.domain.question.controller.dto.AnswerRequestDto;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.repository.AnswerRepository;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import com.dnd.ahaive.domain.user.entity.Provider;
import com.dnd.ahaive.domain.user.entity.User;
import com.dnd.ahaive.domain.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class AnswerServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    InsightRepository insightRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    AnswerService answerService;

    User user;
    Insight insight;
    Question question;

    @BeforeEach
    void setup() {
        user = User.createMember("nickname", 0, "email", Provider.GOOGLE, "provided");
        insight = Insight.from("initThought", "title", user);
        question = Question.of(insight, "archived content", QuestionStatus.WAITING, 0);

        userRepository.save(user);
        insightRepository.save(insight);
        questionRepository.saveAndFlush(question);
    }

    @AfterEach
    void teardown() {
        answerRepository.deleteAllInBatch();
        questionRepository.deleteAllInBatch();
        insightRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
    }

    @Test
    void answeredQuestionCannotRegisterAnswer() {
        // given
        Long questionId = question.getId();
        AnswerRequestDto answerRequest = new AnswerRequestDto("answer");
        String username = user.getUserUuid();

        Question findQuestion = questionRepository.findById(questionId).orElseThrow();
        findQuestion.complete();
        questionRepository.saveAndFlush(findQuestion);

        // when
        assertThatThrownBy(() -> answerService.register(questionId, answerRequest, username))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 답변이 완료된 질문입니다. questionId: " + questionId);
    }

    @Test
    void registerWhenAnswerIsExistingViolateIntegrity() {
        // given
        Long questionId = question.getId();
        AnswerRequestDto answerRequest = new AnswerRequestDto("answer");
        String username = user.getUserUuid();

        Question findQuestion = questionRepository.findById(questionId).orElseThrow();

        Answer answer = Answer.of(findQuestion, "content", false);
        answerRepository.saveAndFlush(answer);

        // when & then
        assertThatThrownBy(() -> answerService.register(questionId, answerRequest, username))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void registerAnswer() {
        // given
        Long questionId = question.getId();
        AnswerRequestDto answerRequest = new AnswerRequestDto("answer");
        String username = user.getUserUuid();

        // when
        Long savedAnswerId = answerService.register(questionId, answerRequest, username);

        // then
        Answer answer = answerRepository.findById(savedAnswerId).orElseThrow();
        Question completedQuestion = questionRepository.findById(questionId).orElseThrow();

        assertThat(answer.getContent()).isEqualTo("answer");

        assertThat(completedQuestion.getStatus()).isEqualTo(QuestionStatus.COMPLETED);
    }
}