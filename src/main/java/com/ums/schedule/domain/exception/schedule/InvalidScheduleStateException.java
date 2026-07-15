package com.ums.schedule.domain.exception.schedule;

import com.ums.schedule.domain.exception.InvalidStateException;

public class InvalidScheduleStateException extends InvalidStateException {
    public InvalidScheduleStateException(String from, String to) {
        super(from, to);
    }
}
