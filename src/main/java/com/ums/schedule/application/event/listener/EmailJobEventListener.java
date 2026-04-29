package com.ums.schedule.application.event.listener;

import com.ums.schedule.domain.request.event.JobCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailJobEventListener {
    private final KafkaTemplate kafkaTemplate;
    //        - SEND_REQUEST - STATUS : SENDING (STATUS가 REQUEST가 아니면 익셉션)

    @EventListener
    public void listen(JobCreatedEvent event) {
        // targetGrouping
        //- SEND_REQUEST_EVENT - EVENT_TYPE : TARGET_GROUP_CREATED
        //    - PAYLOAD : GROUP_TOTAL_COUNT :
        //- TARGET_GROUP_CREATED 발행 → KAFKA 큐 발행
        //- SEND_TARGET - GROUP_KEY 업데이트
//        - HLEO 및 발신 도메인 DNS 질의
//                - REQUEST_ID로 SEND_TARGET 조회 후 TARGET_GROUPING 후 멀티 스레딩 처리
//        - TARGET_GROUP_EVENT 데이터 INSERT 및 SEND_TARGET 그룹키 업데이트
//        - 채널 토픽 발행해 발송 요청 데이터를 KAFKA 큐에 발행

    }
}
