package com.perfumepictor.dev.controller;

import com.perfumepictor.dev.payload.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Validated
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/hello")
    public BaseResponse<String> ping(){
        return BaseResponse.onSuccess("health check!");
    }
}
