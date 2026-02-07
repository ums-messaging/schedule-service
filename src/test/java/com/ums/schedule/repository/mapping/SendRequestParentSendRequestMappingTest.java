package com.ums.schedule.repository.mapping;

import com.netflix.discovery.converters.Auto;
import com.ums.schedule.domain.Schedule;
import com.ums.schedule.domain.ScheduleFixture;
import com.ums.schedule.domain.SendRequest;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.repository.SendRequestRepository;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendRequestParentSendRequestMappingTest {
//    @Autowired private ScheduleRepository scheduleRepository;
//    @Autowired private SendRequestRepository sendRequestRepository;
//    @Autowired private EntityManager entityManager;
//
//    private SendRequest sendRequest ;

//    @BeforeEach
//    void setUp() {
//        Schedule schedule = scheduleRepository.saveAndFlush(ScheduleFixture.ofSchedule());
//        SendRequest sendRequest = ScheduleFixture.ofSendRequest();
//        sendRequest.applySchedule(schedule);
//        this.sendRequest = sendRequest;
//        entityManager.clear();
//    }
//
//
//    @Test
//    @DisplayName("연관관계 주인인 ChildSendRequest에서 설정하면 ParentSendRequest FK가 저장된다.")
//    void shouldPersistFKParentSendRequest_whenSetBySendRequest() {
//        SendRequest sendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
//        SendRequest childSendRequest = ScheduleFixture.ofSendRequest();
//        childSendRequest.applyParentSendRequest(sendRequest);
//
//        entityManager.clear();
//
//        SendRequest saveSendRequest = sendRequestRepository.saveAndFlush(childSendRequest);
//        assertThat(saveSendRequest.getParentSendRequest().getId()).isEqualTo(sendRequest.getId());
//    }
//
//    @Test
//    @DisplayName("비주인인 ParentSendRequest 에서만 연관관계를 설정하면 FK는 저장되지 않는다.")
//    void shouldNotPersistSendMessageFK_whenSetByInverseOnlySide() {
//        SendRequest parentSendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
//        SendRequest childSendRequest = ScheduleFixture.ofSendRequest();
//        childSendRequest.applySchedule(parentSendRequest.getSchedule());
//        parentSendRequest.getPChildSendRequestList().add(childSendRequest);
//
//        sendRequestRepository.saveAndFlush(childSendRequest);
//        entityManager.clear();
//
//        SendRequest findSendRequest = sendRequestRepository.findById(childSendRequest.getId()).orElseThrow();
//
//        assertThat(findSendRequest.getParentSendRequest()).isNull();
//    }
}
