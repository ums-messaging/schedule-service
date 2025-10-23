package com.ums.schedule.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/schedule")
public class RequestTestController {
    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }
}
