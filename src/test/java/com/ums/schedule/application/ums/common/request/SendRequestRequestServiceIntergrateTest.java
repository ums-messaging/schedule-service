package com.ums.schedule.application.ums.common.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SendRequestRequestServiceIntergrateTest {
    @Autowired private SendRequestRequestService requestService;
    private Long requestId = 888296132123779946L;

    @Test
    void sendTest() {
        requestService.request(requestId);
    }
}