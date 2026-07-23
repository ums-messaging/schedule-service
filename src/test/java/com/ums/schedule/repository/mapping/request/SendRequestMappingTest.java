package com.ums.schedule.repository.mapping.request;

import com.ums.schedule.domain.request.*;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.target_upload.TargetUploadReportEntityBuilder;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import jakarta.persistence.EntityManager;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendRequestMappingTest {
    @Autowired private EntityManager entityManager;

    @Nested
    @DisplayName("schedule 연관관계 테스트")
    class ScheduleMappingTest {
        @Test
        @DisplayName("schedule에서 연관관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistScheduleFK_whenSetByInverseOnlySide() {
            Schedule schedule = ScheduleEntityBuilder.builder().build();

            SendRequest request = SendRequestEntityBuilder.builder().schedule(schedule).build();

            entityManager.persist(schedule);
            entityManager.flush();
            Long id = schedule.getId();

            List<SendRequest> expect =
                    entityManager.createQuery("select s from SendRequest s where s.schedule.id = :id", SendRequest.class)
                    .setParameter("id", id)
                    .getResultList();

            assertThat(expect).hasSize(0);

        }

        @Test
        @DisplayName("send_request에서 schedule과 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkSchedule_whenSetBySendRequest() {
            Schedule schedule = ScheduleEntityBuilder.builder().build();
            SendRequest request = SendRequestEntityBuilder.builder().schedule(schedule).build();

            entityManager.persist(schedule);
            entityManager.persist(request);
            entityManager.flush();

            Long id = schedule.getId();
            entityManager.clear();

            List<SendRequest> result = entityManager
                    .createQuery("select s from SendRequest s where s.schedule.id = :id")
                    .setParameter("id", id)
                    .getResultList();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("FK인 schedule은 NULL을 허용하지 않는다.")
        void shouldThrowException_whenScheduleIsNull() {
            SendRequest request = SendRequestEntityBuilder.builder().build();

            assertThatThrownBy(() -> {
                entityManager.persist(request);
                entityManager.flush();
            }).isInstanceOf(ConstraintViolationException.class);
        }
    }

    @Nested
    @DisplayName("target_upload 연관관계 테스트")
    class TargetUploadMappingTest {
        private Long scheduleId;

        @BeforeEach
        void setUp() {
            Schedule schedule = ScheduleEntityBuilder.builder().build();

            entityManager.persist(schedule);
            scheduleId = schedule.getId();
            entityManager.flush();
            entityManager.clear();
        }

        @Test
        @DisplayName("send_request에서 target_upload와 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkCurrentTargetUpload_whenSetBySendRequest() {
            Schedule schedule = entityManager.find(Schedule.class, scheduleId);
            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().build();
            SendRequest request = SendRequestEntityBuilder.builder().schedule(schedule).build();
//            request.assignTargetUpload(targetUpload);

            entityManager.persist(request);
            entityManager.flush();

            UUID id = targetUpload.getId();
            entityManager.clear();

            TargetUploadReport expect = entityManager.find(TargetUploadReport.class, id);
            assertThat(expect.getId()).isNotNull();

        }

        @Test
        @DisplayName("target_upload에서 연관관계를 설정하면 FK가 저장되지 않는다.")
        void shouldNotPersistCurrentTargetUploadFK_whenSetByInverseOnlySide() {
            Schedule schedule = entityManager.find(Schedule.class, scheduleId);

            TargetUploadReport targetUpload = TargetUploadReportEntityBuilder.builder().build();
            SendRequest request = SendRequestEntityBuilder.builder().schedule(schedule).build();
            request.assignTargetUpload(targetUpload);

            assertThatThrownBy(() -> {
                entityManager.persist(targetUpload);
                entityManager.flush();
            }).hasRootCauseInstanceOf(TransientPropertyValueException.class);
        }
    }
}
