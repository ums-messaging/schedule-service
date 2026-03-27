package com.ums.schedule.send.application.assembler;

import com.ums.schedule.common.code.EnumMapperFactory;
import com.ums.schedule.message.application.assembler.EmailMessageAssembler;
import com.ums.schedule.message.application.command.EmailMessageCommand;
import com.ums.schedule.message.domain.SendMessage;
import com.ums.schedule.repository.ScheduleRepository;
import com.ums.schedule.schedule.domain.Schedule;
import com.ums.schedule.send.application.resolver.SendRequestResolver;
import com.ums.schedule.send.application.service.SendRequestService;
import com.ums.schedule.send.domain.request.SendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class EmailSendRequestProcessor {
    private final SendRequestService sendRequestService;
    private final EmailMessageAssembler assembler;
    private final ScheduleRepository scheduleRepository;
    private final SendTargetAssembler sendTargetAssembler;
    private final EnumMapperFactory factory;
    private final Map<String, SendRequestResolver> resolverMap;

    public SendRequest request(EmailMessageCommand command, String customerId) {
        Schedule schedule = scheduleRepository.findById(command.sendRequest().scheduleId()).orElseThrow();
        SendRequest sendRequest = sendRequestService.createSendRequest(command.sendRequest(), customerId);
        SendMessage message = assembler.createMessage(command);
        sendRequest.applySchedule(schedule);
        sendRequest.applySendMessage(message);
        SendRequestResolver resolver = resolverMap.get(command.sendRequest().uploadType());

        return sendRequest;
    }
//
//    private List<SendTarget> createSendTargetList(List<SendTargetCreateCommand> targetList, SendRequest sendRequest) {
//        sendRequest.changeSendRequestStatus(new ReadyStatus());
//        return targetList.stream()
//                .map(target -> sendTargetAssembler.assemble(EmailAddress.of(target.targetAddress()), target))
//                .map(target -> target.applySendRequest(sendRequest))
//                .toList();
//    }
}
