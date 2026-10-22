package com.ums.schedule.email.domain.requests.sender;

public record Sender(
        String from,
        String fromName
) {
    public static Sender of(String from, String fromName) {

        return new Sender(
                from,
                fromName
        );
    }
}
