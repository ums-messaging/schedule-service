package com.ums.schedule.email.adaptor.out.smtp;

public class SendWriter {
    public void write(byte[] command) {
        System.out.println(command.toString());
    }

    public void flush() {
        System.out.println();
    }
    public void close() {

    }


}
