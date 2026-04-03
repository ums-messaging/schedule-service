package com.ums.schedule.send.application.model.dto;

public record SendTargetKeyDto(String targetKey, String contact) {
    public static SendTargetKeyDto of(String targetKey, String email) {
        return new SendTargetKeyDto(targetKey, email);
    }
}
