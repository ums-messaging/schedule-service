package com.ums.schedule.email.common.aspect.annotation;

import com.ums.schedule.common.code.request.SendGroupEventType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SendGroupEventExecution {
    SendGroupEventType value();
}
