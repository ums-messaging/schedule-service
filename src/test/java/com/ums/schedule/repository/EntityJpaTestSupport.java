package com.ums.schedule.repository;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.message.SendMessageBuilder;
import com.ums.schedule.domain.request.message.email.EmailSendMessageBuilder;
import com.ums.schedule.fixture.entity.SendTargetEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.ScheduleEntityBuilder;
import com.ums.schedule.fixture.entity.SendRequestEntityBuilder;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;

import jakarta.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

@DataJpaTest
public class EntityJpaTestSupport {
    @Autowired private EntityManager entityManager;

    public <T> T persist(T entity) {
        entityManager.persist(entity);
        entityManager.flush();
        return entity;
    }

    protected SendMessage givenSendMessage() {
        SendMessage sendMessage = SendMessageBuilder.builder().build();
        persist(sendMessage);
        UUID id = sendMessage.getId();
        entityManager.clear();
        return entityManager.find(SendMessage.class, id);
    }

    protected EmailSendMessage givenEmailSendMessage() {
        SendMessage sendMessage = SendMessageBuilder.builder().build();
        EmailSendMessage emailMessage = EmailSendMessageBuilder.builder()
                .sendMessage(sendMessage).build();

        persist(emailMessage);
        UUID id = emailMessage.getId();
        entityManager.clear();

        return entityManager.find(EmailSendMessage.class, id);
    }
    protected Schedule givenSchedule() {
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        persist(schedule);
        Long id = schedule.getId();
        entityManager.clear();

        return entityManager.find(Schedule.class, id);
    }
    protected SendTarget givenSendTarget(TargetUploadReport targetUploadReport) {
        SendTarget sendTarget = SendTargetEntityBuilder.builder().targetUpload(targetUploadReport).build();
        persist(sendTarget);
        UUID id = sendTarget.getId();
        entityManager.clear();
        return entityManager.find(SendTarget.class, id);
    }

    protected TargetUploadReport givenTargetUploadReport(SendRequest sendRequest) {
        TargetUploadReport targetUploadReport = TargetUploadReportEntityBuilder.builder()
                .sendRequest(sendRequest)
                .build();

        persist(targetUploadReport);
        UUID id = targetUploadReport.getId();
        entityManager.clear();

        return entityManager.find(TargetUploadReport.class, id);
    }
    protected SendRequest givenSendRequest(Schedule schedule, SendMessage sendMessage) {
        SendRequest sendRequest = SendRequestEntityBuilder.builder()
                .id(null)
                .schedule(schedule)
                .sendMessage(sendMessage)
                .build();

        persist(sendRequest);
        Long id = sendRequest.getId();

        return entityManager.find(SendRequest.class, id);
    }
}
