package com.dnd.ahaive.domain.question.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiQuestionResponse {
  private List<String> questions;
}
