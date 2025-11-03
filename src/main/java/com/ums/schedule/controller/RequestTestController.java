package com.ums.schedule.controller;

import com.ums.schedule.config.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/schedule")
@RequiredArgsConstructor
public class RequestTestController {
    private final KafkaProducer producer;

    @GetMapping("/hello/{message}")
    public String hello(@PathVariable("message") String message) {
        try {
            producer.sendMessage("test-topic", message);
            return "success";
        } catch (Exception e) {
            return "fail";
        }
    }

    @GetMapping("/hello1")
    public String hello1() {
        return "hello";
    }
}
