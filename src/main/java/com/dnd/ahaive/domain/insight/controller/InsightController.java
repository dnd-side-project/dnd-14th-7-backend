package com.dnd.ahaive.domain.insight.controller;

import com.dnd.ahaive.domain.insight.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InsightController {

  private final InsightService insightService;

}
