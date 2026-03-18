package com.ums.schedule.template.exception;

public class TitleRequiredException extends RequiredException {
    private TitleRequiredException(String message) {
        super(message);
    }

    public static TitleRequiredException of(){
        return new TitleRequiredException("Title");
    }
}
