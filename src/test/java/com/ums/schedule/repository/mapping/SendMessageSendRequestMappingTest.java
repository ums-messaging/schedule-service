package com.ums.schedule.repository.mapping;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class SendMessageSendRequestMappingTest {
//    @Autowired private ScheduleRepository scheduleRepository;
//    @Autowired private SendMessageRepository sendMessageRepository;
//    @Autowired private SendRequestRepository sendRequestRepository;
//    @Autowired private EntityManager entityManager;
//
//    private SendRequest sendRequest ;
//
//    @BeforeEach
//    void setUp() {
//        Schedule schedule = scheduleRepository.saveAndFlush(ScheduleFixture.ofSchedule());
//        sendRequest = ScheduleFixture.ofSendRequest();
//        sendRequest.applySchedule(schedule);
//        entityManager.clear();
//    }
//
//    @Test
//    @DisplayName("연관관계 주인인 SendMessage에서 설정하면 FK가 저장된다.")
//    void shouldPersistSendMessageFK_whenSetBySendRequest() {
//        SendRequest sendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
//        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
//        sendMessage.applySendRequest(sendRequest);
//
//        SendMessage saveSendMessage = sendMessageRepository.saveAndFlush(sendMessage);
//        entityManager.clear();
//
//        assertThat(saveSendMessage.getSendRequest().getId()).isEqualTo(sendRequest.getId());
//    }
//
//    @Test
//    @DisplayName("비주인인 SendRequest에서만 연관관계를 설정하면 FK는 저장되지 않는다.")
//    void shouldNotPersistSendMessageFK_whenSetByInverseOnlySide() {
//        SendRequest sendRequest = sendRequestRepository.saveAndFlush(this.sendRequest);
//        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
//        sendRequest.getSendMessages().add(sendMessage);
//
//        assertThatThrownBy(() -> sendMessageRepository.saveAndFlush(sendMessage))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @DisplayName("FK인 send_request_id는 null을 허용하지 않는다.")
//    void shouldFailPersist_sendRequestIdIsNull() {
//        SendMessage sendMessage = ScheduleFixture.ofSendMessage();
//
//        assertThatThrownBy(() -> sendMessageRepository.saveAndFlush(sendMessage))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }

}
