package com.ums.schedule.adapter.api.email.smtp.response;

public record EmailSmtpResponse(
        int resultCode,
        String resultMessage
) {

    public static EmailSmtpResponse of(String message) {
        String resultCode;
        String resultMessage;
        try {
            resultCode = message.substring(0, 3);
            resultMessage = message.substring(4, message.length()-1) ;
            int code = Integer.parseInt(resultCode);
            return new EmailSmtpResponse(code, resultMessage);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            return new EmailSmtpResponse(701, message);
        }
    }

    public static EmailSmtpResponse of(int code, String message) {
        return new EmailSmtpResponse(code, message);
    }
}
