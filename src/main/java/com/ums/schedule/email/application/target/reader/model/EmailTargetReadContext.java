package com.ums.schedule.email.application.target.reader.model;

import com.ums.schedule.email.application.target.reader.listener.model.SendTargetGroupedList;
import com.ums.schedule.email.application.target.reader.listener.model.SendTargetReadRow;
import com.ums.schedule.common.code.api.SendTargetErrorCode;
import com.ums.schedule.orchestrator.application.upload.exception.SendTargetRowException;
import com.ums.schedule.email.application.send.job.model.SendJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.Consumer;


@Slf4j
public class EmailTargetReadContext extends SendTargetReadContext {

    public EmailTargetReadContext(SendJob job, Consumer<List<SendTargetGroupedList>> handler) {
        super(job, handler);
    }

    @Override
    public String getGroupKey(SendTargetReadRow row) {
        String email = (String) row.getMessageVariable().get("email");
        if(StringUtils.hasText(email)) {
            String[] array = email.split("@");
            if(array.length >= 2) {
                return array[1];
            }
        }
        throw SendTargetRowException.of(row.rowNo(), SendTargetErrorCode.INVALID_CONTACT.description());
    }
}