package com.ums.schedule.domain.sendrequest.target.exeption;

public class SendTargetTitleParsingException extends SendTargetPolicyViolationException {
    protected SendTargetTitleParsingException(String message) {
        super(message);
    }

    public static SendTargetTitleParsingException of(String targetId, String field) {
        return new SendTargetTitleParsingException("[%s] 대상자 치환 변수 %s이(가) 존재하지 않습니다.".formatted(targetId, field));
    }
}
