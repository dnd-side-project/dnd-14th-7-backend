package com.dnd.ahaive.domain.insight.controller;

import com.dnd.ahaive.domain.insight.service.InsightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/insights")
public class InsightController {

  private final InsightService insightService;



}
