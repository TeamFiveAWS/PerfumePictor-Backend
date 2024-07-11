package com.perfumepictor.dev.controller;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.dto.CreateFeedResponseDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.payload.BaseResponse;
import com.perfumepictor.dev.service.FeedsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FeedsController {

    private final FeedsService feedsService;

    @PostMapping("/feeds")
    public BaseResponse<CreateFeedResponseDTO> createFeed(@RequestBody @Valid final CreateFeedRequestDTO requestDTO) {
        Feed feed = feedsService.createFeed(requestDTO);
        return BaseResponse.onSuccess(CreateFeedResponseDTO.from(feed));
    }

}
