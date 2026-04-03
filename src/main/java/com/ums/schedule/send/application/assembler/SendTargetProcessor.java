package com.ums.schedule.send.application.assembler;

import com.ums.schedule.send.application.model.Job;
import com.ums.schedule.send.application.model.dto.SendTargetDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;
import java.util.concurrent.*;

// 멀티스레드로 구현해서 insert
// request되면, SendTarget 을 DB에서 조회해서 도메인 그룹 별로 묶어 Queue에 Poll
@Component
@RequiredArgsConstructor
public class SendTargetProcessor {
//    private SendTargetRepository repository;
    // Job 생성 => SendTarget 도메인 생성 -> BULK INSERT
    // SendTargetUpload의 STATUS가 COMPLETE가 되면, SENDREQUEST의 상태를 READY로 변경
    // 실시간의 경우, 바로 SENDREQUEST의 상태를 REQUEST로 변경 후 , 발송 WORKER로 뺌
    // 발송 Worker에서는 SENDREQUEST, SENDTARGET 을 도메인 그룹핑해서 Kafka Queue로 offer
    public void process(ExecutorService service) {

    }



}
