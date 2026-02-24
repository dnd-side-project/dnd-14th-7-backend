package com.dnd.ahaive.domain.trash.controller;

import com.dnd.ahaive.domain.trash.service.TrashService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TrashController {

  private final TrashService trashService;



}
