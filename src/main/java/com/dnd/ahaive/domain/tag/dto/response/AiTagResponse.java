package com.dnd.ahaive.domain.tag.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AiTagResponse {
  private List<String> tags;
}
