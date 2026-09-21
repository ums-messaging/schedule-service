package com.ums.schedule.application.ums.common.send;

import com.ums.schedule.adapter.persistence.TargetMessageQueryRepository;
import com.ums.schedule.application.ums.common.send.email.EmailJobManager;
import com.ums.schedule.application.ums.common.send.model.EmailTargetGroupQueryResult;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.send.email.job.SendJob;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmailJobManagerTest {
    @Mock private TargetMessageQueryRepository repository;
    @InjectMocks private EmailJobManager jobManager;
    @Mock
    private KafkaTemplate<String, Object> template;

    private List<EmailTargetGroupQueryResult> results = new ArrayList<>();
    private SendJob job ;

    @BeforeEach
    void setUp() {
        for(long idx = 1L; idx < 1000L; idx++) {
            EmailTargetGroupQueryResult result = new EmailTargetGroupQueryResult(1L, "test.com", 1000L);
            results.add(result);
        }
        SendRequest sendRequest = SendRequestEntityBuilder.builder().build();
        job = sendRequest.createJob();
    }



}