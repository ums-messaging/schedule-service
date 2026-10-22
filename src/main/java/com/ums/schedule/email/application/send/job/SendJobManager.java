package com.ums.schedule.email.application.send.job;

import com.ums.schedule.email.application.requests.EmailSendRequestReportService;
import com.ums.schedule.email.application.send.job.factory.SendJobFactory;
import com.ums.schedule.email.application.target.reader.SendTargetFileReader;
import com.ums.schedule.common.code.common.ChannelType;
import com.ums.schedule.common.code.mapper.EnumMapperValue;

import com.ums.schedule.email.application.send.job.command.JobCommand;
import com.ums.schedule.email.application.send.job.model.SendJob;
import com.ums.schedule.email.application.send.job.worker.SendJobWorker;
import com.ums.schedule.email.application.target.reader.model.SendTargetReadContext;
import com.ums.schedule.email.domain.requests.result.SendRequestReport;
import com.ums.schedule.email.application.target.reader.model.EmailTargetReadContext;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import com.ums.schedule.email.application.target.reader.model.SendTargetsReadResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendJobManager {
    private final SendJobFactory factory;
    private final SendJobWorker worker;
    private final SendTargetFileReader reader;
    private final EmailSendRequestReportService reportService;

    public boolean supports(EnumMapperValue channelType) {
        return ChannelType.EMAIL == ChannelType.valueOf(channelType.code());
    }

    public SendRequestReport send(JobCommand jobCommand) {
        SendJob sendJob = factory.create(jobCommand);

        Consumer<List<SendTargetGroupedList>> consumer = worker.getSendTargetRequestWorker(sendJob);

        SendTargetReadContext ctx = new EmailTargetReadContext(sendJob, consumer);
        SendTargetsReadResult result = reader.read(jobCommand.downloadUrl(), ctx);
        SendRequestReport sendReport = reportService.updateSendRequestReport(ctx.getRequestId(), result);
        log.info("sendReport = {}", sendReport);
        return sendReport;
    }
}