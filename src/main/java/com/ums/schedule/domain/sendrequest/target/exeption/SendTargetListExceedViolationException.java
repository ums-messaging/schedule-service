package com.ums.schedule.domain.sendrequest.target.exeption;

public class SendTargetListExceedViolationException extends SendTargetPolicyViolationException {
    protected SendTargetListExceedViolationException(Integer maxSize) {
        super("%,d건을 초과했습니다.".formatted(maxSize));
    }
    public static SendTargetListExceedViolationException of(Integer maxSize) {
        return new SendTargetListExceedViolationException(maxSize);
    }
}
