package com.dnd.ahaive.domain.search.controller;

import com.dnd.ahaive.domain.search.controller.dto.SearchResultDto;
import com.dnd.ahaive.domain.search.service.SearchService;
import com.dnd.ahaive.global.common.response.ResponseDTO;
import com.dnd.ahaive.global.security.core.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/search")
    public ResponseDTO<?> search(@RequestParam(name = "searchTerm", required = true) String searchTerm,
                                 @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        SearchResultDto searchResult = searchService.search(customUserDetails.getUuid(), searchTerm);
        return ResponseDTO.of(searchResult, "success");
    }

}
