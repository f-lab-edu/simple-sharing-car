package com.flab.simplesharingcar.annotation;

import com.flab.simplesharingcar.web.validator.StartAndEndTimeValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StartAndEndTimeValidator.class)
public @interface StartAndEndTimeCheck {

    String message() default "시작 시간이 종료 시간보다 늦을 수 없습니다.";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String startTime();

    String endTime();

}
