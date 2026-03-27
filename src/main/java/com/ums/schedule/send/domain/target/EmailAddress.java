package com.ums.schedule.send.domain.target;

public class EmailAddress implements TargetAddress {
    private String email;

    public static EmailAddress of(String contact) {
        return new EmailAddress(contact);
    }

    private EmailAddress(String email) {
        this.email = email;
    }

    @Override
    public String getContact() {
        return this.email;
    }
}
