package com.dnd.ahaive.domain.question.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.question.controller.dto.AnswerResponse;
import com.dnd.ahaive.domain.question.controller.dto.QuestionResponse;
import com.dnd.ahaive.domain.question.controller.dto.TotalArchivedQuestionResponse;
import com.dnd.ahaive.domain.question.controller.dto.TotalQuestionDto;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.question.repository.QuestionRepository;
import com.dnd.ahaive.domain.user.entity.Provider;
import com.dnd.ahaive.domain.user.entity.User;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ActiveProfiles("test")
class QuestionServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuestionService questionService;

    User user;
    Insight insight;
    int waitingQuestionCount = 3;
    int completedQuestionCount = 2;
    int archivedQuestionCount = 1;
    List<Question> archivedQuestions = new ArrayList<>();
    List<Question> waitingQuestions = new ArrayList<>();
    List<Question> completedQuestions = new ArrayList<>();

    @BeforeEach
    void setup() {
        user = User.createMember("nickname", 0, "email", Provider.GOOGLE, "provided");
        insight = Insight.from("initThought", "title", user);

        em.persist(user);
        em.persist(insight);

        for (int i = 0; i < waitingQuestionCount; i++) {
            Question question = Question.of(insight, "waiting content" + i, QuestionStatus.WAITING, 0);
            em.persist(question);
            waitingQuestions.add(question);
        }

        for (int i = 0; i < completedQuestionCount; i++) {
            Question question = Question.of(insight, "complete content" + i, QuestionStatus.COMPLETED, 0);
            Answer answer = Answer.of(question, "complete content" + i, false);
            em.persist(question);
            em.persist(answer);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            completedQuestions.add(question);
        }

        for (int i = 0; i < archivedQuestionCount; i++) {
            Question question = Question.of(insight, "archived content" + i, QuestionStatus.ARCHIVED, 0);
            em.persist(question);
            archivedQuestions.add(question);
        }

        em.flush();
        em.clear();
    }

    @Test
    void questionIsSeparatedByStatus() {
        TotalQuestionDto questionAndAnswers = questionService.findQuestionAndAnswers(insight.getId(), user.getNickname());

        // then
        List<QuestionResponse> questions = questionAndAnswers.questions();
        assertThat(questions).hasSize(waitingQuestionCount);
        assertThat(questions).extracting(QuestionResponse::status).allMatch(status -> status == QuestionStatus.WAITING);
        assertThat(questions).isSortedAccordingTo(Comparator.comparing(QuestionResponse::createdDate).reversed());

        List<AnswerResponse> answerResponses = questionAndAnswers.answerCards();
        assertThat(answerResponses).hasSize(completedQuestionCount);
        assertThat(answerResponses).isSortedAccordingTo(Comparator.comparing(AnswerResponse::createdDate).reversed());
    }

    @Test
    void previousQuestionIsOrderedByCreatedDate() {
        TotalArchivedQuestionResponse previousQuestion = questionService.findPreviousQuestion(insight.getId(),
                user.getNickname());

        // then
        List<QuestionResponse> questionResponses = previousQuestion.archivedQuestions();
        assertThat(questionResponses).hasSize(archivedQuestionCount);
        assertThat(questionResponses).isSortedAccordingTo(Comparator.comparing(QuestionResponse::createdDate).reversed());
        assertThat(questionResponses).extracting(QuestionResponse::status).containsOnly(QuestionStatus.ARCHIVED);
    }

    @Test
    void ifQuestionIsNotInInsightThenThrows() {
        // given
        Insight secondInsight = Insight.from("initThought", "title", user);
        em.persist(secondInsight);

        em.flush();
        em.clear();

        // when & then
        Long questionId = archivedQuestions.get(0).getId();
        assertThatThrownBy(
                () -> questionService.rollbackPreviousQuestion(secondInsight.getId(), questionId, user.getNickname()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void waitingQuestionFailRollback() {
        Long questionId = waitingQuestions.get(0).getId();

        assertThatThrownBy(
                () -> questionService.rollbackPreviousQuestion(insight.getId(), questionId, user.getNickname()))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void archivedQuestionRollBack() {
        // given
        Long archivedQuestionId = archivedQuestions.get(0).getId();

        // when
        questionService.rollbackPreviousQuestion(insight.getId(), archivedQuestionId, user.getNickname());

        // then
        Question question = em.find(Question.class, archivedQuestionId);
        assertThat(question.getStatus()).isEqualTo(QuestionStatus.WAITING);
    }
}