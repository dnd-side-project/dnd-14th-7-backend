package com.dnd.ahaive.domain.question.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import com.dnd.ahaive.domain.insight.entity.Insight;
import com.dnd.ahaive.domain.question.entity.Answer;
import com.dnd.ahaive.domain.question.entity.Question;
import com.dnd.ahaive.domain.question.entity.QuestionStatus;
import com.dnd.ahaive.domain.user.entity.Provider;
import com.dnd.ahaive.domain.user.entity.User;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class QuestionRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("질문은 최신순으로 나열된다.")
    void questionIsSortedByCreatedAt() {
        //given
        User user = User.createMember("nickname", 0, "email", Provider.GOOGLE, "provided");
        Insight insight = Insight.from("initThought", "title", user);

        em.persist(user);
        em.persist(insight);

        int questionCount = 5;
        for (int i = 0; i < questionCount; i++) {
            Question question = Question.of(insight, "content" + i, QuestionStatus.WAITING, 0);
            em.persist(question);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        em.flush();
        em.clear();

        // when
        List<Question> questions = questionRepository.findAllByInsightIdAndStatusOrderByCreatedAtDesc(
                insight.getId(), QuestionStatus.WAITING);

        // then
        assertThat(questions).hasSize(questionCount);
        assertThat(questions).isSortedAccordingTo(Comparator.comparing(Question::getCreatedAt).reversed());
    }

}