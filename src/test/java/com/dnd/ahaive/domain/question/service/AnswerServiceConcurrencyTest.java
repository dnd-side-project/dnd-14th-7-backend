package com.dnd.ahaive.domain.question.service;

import static org.assertj.core.api.Assertions.*;
import static reactor.netty.http.HttpConnectionLiveness.log;

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
import groovy.util.logging.Slf4j;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class AnswerServiceConcurrencyTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    InsightRepository insightRepository;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private AnswerService answerService;

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
    void registerAnswerConcurrency() throws InterruptedException {
        // given
        int threadCount = 2;
        ExecutorService es = Executors.newFixedThreadPool(threadCount);

        CountDownLatch latch = new CountDownLatch(threadCount);

        AtomicInteger success = new AtomicInteger();
        AtomicInteger failure = new AtomicInteger();
        AtomicInteger totalTryCount = new AtomicInteger();

        Long questionId = question.getId();
        AnswerRequestDto request = new AnswerRequestDto("answer");
        String username = user.getUserUuid();

        // when
        for (int i = 0; i < threadCount; i++) {
            es.submit(() -> {
                try {
                    answerService.register(questionId, request, username);
                    success.incrementAndGet();
                } catch (Exception e) {
                    log.info("예외 발생 : " + e.getMessage(), e);
                    failure.incrementAndGet();
                }
                finally {
                    totalTryCount.incrementAndGet();
                    latch.countDown();
                }
            });
        }

        latch.await();
        es.shutdown();

        // then
        assertThat(success.get()).isEqualTo(1);
        assertThat(failure.get()).isEqualTo(1);
        assertThat(totalTryCount.get()).isEqualTo(threadCount);

        Question answeredQuestion = questionRepository.findById(questionId).orElseThrow(EntityNotFoundException::new);
        List<Answer> answers = answerRepository.findAll();

        assertThat(answeredQuestion.getStatus()).isEqualTo(QuestionStatus.COMPLETED);
        assertThat(answers).hasSize(1);
    }


}