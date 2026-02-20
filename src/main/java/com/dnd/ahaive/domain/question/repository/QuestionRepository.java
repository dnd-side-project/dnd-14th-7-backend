package com.dnd.ahaive.domain.question.repository;

import com.dnd.ahaive.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}
