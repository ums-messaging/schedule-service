package com.ums.schedule.domain.schedule.exception;

import com.ums.schedule.common.exception.InvalidStateException;

public class InvalidScheduleStateException extends InvalidStateException {
    public InvalidScheduleStateException(String from, String to) {
        super(from, to);
    }
}
