package com.ums.schedule.domain;

import com.ums.schedule.common.code.*;

import java.time.LocalDateTime;

import static org.springframework.test.util.ReflectionTestUtils.setField;

public class ScheduleFixture {
    public static Schedule ofSchedule() {
        Schedule schedule = new Schedule();
        setField(schedule,"name", "schedule_name");
        setField(schedule, "type", ScheduleTypeEnum.REALTIME);
        setField(schedule, "status", ScheduleStatusEnum.RUNNING);

        LocalDateTime scheduleStartAt = LocalDateTime.now().plusDays(1);
        LocalDateTime scheduleEndAt = scheduleStartAt.plusMonths(1);
        setField(schedule, "schedulePeriod", SchedulePeriod.of(scheduleStartAt, scheduleEndAt));
        return schedule;
    }

    public static ScheduleRestrictPolicy ofScheduleRestrictPolicy() {
        ScheduleRestrictPolicy policy = new ScheduleRestrictPolicy();
        setField(policy, "restrictMode", RestrictModeEnum.NIGHT);
        return policy;
    }


    public static SendRequest ofSendRequest() {
        SendRequest sendRequest = new SendRequest();
        setField(sendRequest, "templateId", "template_id");
        setField(sendRequest, "channel", ChannelEnum.EMAIL);
        setField(sendRequest, "retryCnt", 3);
        setField(sendRequest, "attemptOrderNo", 1);
        setField(sendRequest, "sender", "jang314@naver.com");
        setField(sendRequest, "requestedAt", LocalDateTime.now());
        setField(sendRequest, "status", SendRequestStatusEnum.JOB_CREATE);
        setField(sendRequest, "sendType", SendTypeEnum.FIRST);
        setField(sendRequest, "messageType", MessageTypeEnum.ADVERTISE);
        setField(sendRequest, "resumeYn", YesOrNoEnum.N);
        setField(sendRequest, "bypassYn", YesOrNoEnum.N);
        setField(sendRequest, "customerId", "keb_bank");
        setField(sendRequest, "customerRequestId", "customer_request_id");
        return sendRequest;
    }

    public static SendMessage ofSendMessage(){
        SendMessage sendMessage = new SendMessage();
        setField(sendMessage, "receiverId", "jang314");

        return sendMessage;
    }

    public static SendRequestReport ofSendRequestReport() {
        SendRequestReport requestReport = new SendRequestReport();
        setField(requestReport, "totalCnt", 1000L);
        setField(requestReport, "successCnt", 0L);
        setField(requestReport, "failCnt", 0L);
        setField(requestReport, "madeCnt", 0L);
        setField(requestReport, "aggregatedAt", LocalDateTime.now());

        return requestReport;
    }

    public static SendMessageEvent ofSendMessageEvent() {
        SendMessageEvent sendMessageEvent = new SendMessageEvent();
        setField(sendMessageEvent, "status", SendMessageStatusEnum.MAKING);
        setField(sendMessageEvent, "attemptOrderNo", 3);
        setField(sendMessageEvent, "eventedAt", LocalDateTime.now());
        setField(sendMessageEvent, "errorCode", ErrorCodeEnum.DNS_ERROR);

        return sendMessageEvent;
    }
}
