package com.ums.schedule.email.application.send.job.command;

public record MessageCommand(
        String subject,
        String header,
        String body,
        String footer,
        String cover,
        String imageDir
) {
}
