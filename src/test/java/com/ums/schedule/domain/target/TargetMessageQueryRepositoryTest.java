package com.ums.schedule.domain.target;

import com.github.f4b6a3.tsid.TsidCreator;
import com.ums.schedule.adapter.persistence.TargetGroupQueryResult;
import com.ums.schedule.adapter.persistence.TargetMessageQueryRepository;
import com.ums.schedule.application.sendrequest.target.query.TargetDuplicatedQuery;
import com.ums.schedule.common.code.target.SendTargetStatus;
import com.ums.schedule.config.query.QueryDslConfig;
import com.ums.schedule.domain.message.email.EmailSendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.EmailTargetMessageEntityBuilder;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        TargetMessageQueryRepository.class,
        QueryDslConfig.class
})
class TargetMessageQueryRepositoryTest extends EntityJpaTestSupport {
    @Autowired private TargetMessageQueryRepository repository;

    private TargetUploadReport targetUploadReport;
    private EmailSendMessage sendMessage;
    private TargetMessage targetMessage;

    private Long groupId;
    private String domain;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        sendMessage = givenEmailSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage.getSendMessage());
        this.targetUploadReport = givenTargetUploadReport(sendRequest);

        targetMessage = EmailTargetMessageEntityBuilder.builder()
                .subject("subject")
                .sendMessage(sendMessage)
                .bodyMessage("body message")
                .build();
        initializeTargetMessage(targetMessage, 1L);

        persist(targetMessage);
        entityManager.clear();
    }

    private void initializeTargetMessage(TargetMessage targetMessage, Long id) {
        groupId = TsidCreator.getTsid().toLong();
        ReflectionTestUtils.setField(targetMessage, "id", id);
        ReflectionTestUtils.setField(targetMessage, "groupId", groupId);
        ReflectionTestUtils.setField(targetMessage, "targetUploadReport", targetUploadReport);
        ReflectionTestUtils.setField(targetMessage, "state", SendTargetStatus.CREATE);
        ReflectionTestUtils.setField(targetMessage, "contact", "jang314@test.com");
        ReflectionTestUtils.setField(targetMessage, "targetKey", "jang314");
    }

    @Test
    @DisplayName("")
    void test() {
        TargetDuplicatedQuery query = TargetDuplicatedQuery.of(targetUploadReport.getId(), List.of(targetMessage));
        List<TargetMessage> targetMessages = repository.findByTargetKeysAndContacts(query);

        assertThat(targetMessages).hasSize(1);
    }

    @Test
    @DisplayName("그룹 대상자 조회 테스트")
    void shouldReturnTargetGroupList() {
        List<TargetGroupQueryResult> list = repository.findGroupByGroupIdAndDomain(targetUploadReport.getId().toString(), groupId, "test.com");
        assertThat(list).hasSize(1);
    }

}