package com.perfumepictor.dev.controller;

import com.perfumepictor.dev.dto.CreateFeedRequestDTO;
import com.perfumepictor.dev.dto.GetFeedResponseDTO;
import com.perfumepictor.dev.dto.GetFeedsResponseDTO;
import com.perfumepictor.dev.dto.LikeFeedResponseDTO;
import com.perfumepictor.dev.entity.Feed;
import com.perfumepictor.dev.entity.Like;
import com.perfumepictor.dev.payload.BaseResponse;
import com.perfumepictor.dev.service.FeedService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedsService;

    @PostMapping("/feeds")
    public BaseResponse<GetFeedResponseDTO> createFeed(@RequestBody @Valid final CreateFeedRequestDTO requestDTO) {
        Feed feed = feedsService.createFeed(requestDTO);
        return BaseResponse.onSuccess(GetFeedResponseDTO.from(feed));
    }

    @GetMapping("/feeds")
    public BaseResponse<GetFeedsResponseDTO> getFeeds(@RequestParam(value = "page", defaultValue = "0") int page,
                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        List<Feed> feeds = feedsService.getFeeds(page, size);
        return BaseResponse.onSuccess(GetFeedsResponseDTO.from(feeds));
    }

    @GetMapping("/feeds/my")
    public BaseResponse<GetFeedsResponseDTO> getMyFeeds(@RequestHeader(value = "lastFeedKey", required = false) String lastFeedKey,
                                                      @RequestParam(value = "size", defaultValue = "10") int size) {
        Map<String, Object> feedPage = feedsService.getMyFeeds(lastFeedKey, size);
        return BaseResponse.onSuccess(GetFeedsResponseDTO.from((List<Feed>) feedPage.get("feeds"), (String) feedPage.get("lastFeedKey")));
    }

    @DeleteMapping("/feeds")
    public BaseResponse<Long> deleteFeed(@RequestHeader String feedKey) {
        return BaseResponse.onSuccess(feedsService.deleteFeed(feedKey));
    }

    @PostMapping("/feeds/like")
    public BaseResponse<LikeFeedResponseDTO> likeFeed(@RequestHeader String feedKey) {
        Like like = feedsService.likeFeed(feedKey);
        return BaseResponse.onSuccess(LikeFeedResponseDTO.from(like));
    }

}
