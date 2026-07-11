package com.ums.schedule.domain.send.email;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ums.schedule.domain.message.email.EmailAttachment;
import com.ums.schedule.domain.send.email.job.DomainGroup;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.fixture.sendrequest.SendRequestEntityBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.fixture.schedule.ScheduleEntityBuilder;
import com.ums.schedule.fixture.email.attachment.EmailAttachmentBuilder;
import com.ums.schedule.domain.sendrequest.target.SendTarget;
import com.ums.schedule.domain.sendrequest.target.SendTargetTestBuilder;
import com.ums.schedule.intergration.kafka.KafkaTestProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
class DomainGroupKeyTest {
    private KafkaTestProducer producer;
    private Map<String, DomainGroup> groupMap = new HashMap<>();

    @Autowired
    public RedisTemplate<String, String> redisTemplate;
    private final int MAX_SIZE = 10;
    private List<DomainGroup> groupList = new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();


    @Test
    void test() throws JsonProcessingException {
        List<DomainGroup> keys = new ArrayList<>();
        List<List<SendTarget>> targetList = givenTargetList();
        Schedule schedule = ScheduleEntityBuilder.builder().build();
        SendRequest request = SendRequestEntityBuilder.builder().schedule(schedule).build();
        EmailAttachment emailRequest = EmailAttachmentBuilder.builder().sendRequest(request).build();
//        EmailSendJob job = emailRequest.createEmailSendJob("localhost");
//        List<DomainGroupEntry> entryList = job.createDomainGroup(groupMap, targetList);
//        entryList.stream()
//                .map(entry -> {
//                    String json;
//                    try {
//                        json = mapper.writeValueAsString(entry.getMember());
//                    } catch (JsonProcessingException e) {
//                        return null;
//                    }
//                    return redisTemplate.opsForZSet().add(entry.getKey(), json, entry.getScore());
//                })
//                .toList();
//
//        String jobToPayload = mapper.writeValueAsString(job);
//
//        producer.send(job.topic(), jobToPayload);
//
//        // event 정의 : kafka streams 연동하기
//        // consumer에서 뺴왔다고 가정
//        // DNS 쿼리 질의 - 이벤트 발행
//        // mime message 생성 - 이벤트 발행
//        // smtp 연동
//        for(DomainGroup key : keys) {
//            String s = redisTemplate.opsForValue().get("group_id:" + key.groupId());
//            System.out.println("json : " + s);
//        }
    }

    private List<List<SendTarget>> givenTargetList() {
        List<String> emailList = List.of("naver.com", "daum.net", "test.com", "google.com");
        List<List<SendTarget>> pagingList = new ArrayList<>();

        for(int i = 0; i < 100 ; i++) {
            List<SendTarget> targetList = new ArrayList<>();
            for(int j = 0 ; j < 10; j++) {
                for(String email : emailList) {
                    String contact = "jang"+i+"@"+email;
                    SendTarget target = SendTargetTestBuilder.builder().contact(contact).build();
                    targetList.add(target);
                }
            }
            pagingList.add(targetList);
        }
        return pagingList;
    }




}