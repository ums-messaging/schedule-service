package com.ums.schedule.repository.constraint;

import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
public class SendRequestUniqueConstraintTest {
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private SendRequestRepository sendRequestRepository;
    @Autowired private EntityManager entityManager;

    private Schedule schedule;

    @BeforeEach
    void setup() {
        schedule = scheduleRepository.save(ScheduleFixture.ofSchedule());
    }

    @Test
    @DisplayName("template_id는 null을 허용하지 않는다.")
    void shouldFail_TemplateIdIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "templateId", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("channel은 null을 허용하지 않는다.")
    void shouldFail_ChannelIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "channel", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("retry_cnt는 null을 허용하지 않는다.")
    void shouldFail_RetryCntIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "retryCnt", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("attempt_order_no는 null을 허용하지 않는다.")
    void shouldFail_AttemptOrderNoIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "attemptOrderNo", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("sender은 null을 허용하지 않는다.")
    void shouldFail_SenderIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "sender", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(()->sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("requested_at은 null을 허용하지 않는다.")
    void shouldFail_RequestedAtIsNull(){
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "requestedAt", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
    @Test
    @DisplayName("send_type은 null을 허용하지 않는다.")
    void shouldFail_SendTypeIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "sendType", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("bypass_yn은 null을 허용하지 않는다.")
    void shouldFail_BypassYnIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "bypassYn", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(()->sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("customer_id는 null을 허용하지 않는다.")
    void shouldFail_customerIdIsNull() {
        SendRequest sendRequest =ScheduleFixture.ofSendRequest();
        setField(sendRequest, "customerId", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("status는 null을 허용하지않는다.")
    void shouldFail_StatusIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "status", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("customer_request_id는 null을 허용하지 않는다.")
    void shouldFail_customerRequestIdIsNull() {
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "customerRequestId", null);
        sendRequest.applySchedule(schedule);

        assertThatThrownBy(()->sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("customer_id에 대해 customer_request_id는 중복을 허용하지 않는다.")
    void shouldFailPersist_CustomerRequestIdAboutCustomerId() {
        // given
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        setField(sendRequest, "customerId", "kb_bank");
        setField(sendRequest, "customerRequestId", "customer_request_id");
        sendRequest.applySchedule(schedule);

        SendRequest dupSendRequest = ScheduleFixture.ofSendRequest();
        setField(dupSendRequest, "customerId", "kb_bank");
        setField(dupSendRequest, "customerRequestId", "customer_request_id");
        sendRequest.applySchedule(schedule);

        sendRequestRepository.saveAndFlush(sendRequest);

        assertThatThrownBy(() -> sendRequestRepository.saveAndFlush(dupSendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("FK인 schedule_id는 null을 허용하지 않는다.")
    void shouldFail_ScheduleIdIsNull(){
        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
        assertThatThrownBy(()-> sendRequestRepository.saveAndFlush(sendRequest))
                .isInstanceOf(DataIntegrityViolationException.class);
    }


    @Test
    @Disabled
    @DisplayName("스케쥴 테이블과 연관관계 매핑이 정상적으로 동작한다.")
    void shouldPersistSendRequestFK_Schedule() {
        // given
//        Schedule saveSchedule = scheduleRepository.save(schedule);
//        this.sendRequest.applySchedule(saveSchedule);
//
//        // when
//        SendRequest sendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
//        Long scheduleId = saveSchedule.getId();
//        Long sendRequestId = sendRequest.getId();
//        entityManager.clear();
//
//        // then
//        SendRequest findSendRequest = sendRequestRepository.findById(sendRequestId).orElseThrow();
//        assertThat(findSendRequest.getSchedule().getId()).isEqualTo(scheduleId);
    }
}
