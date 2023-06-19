package com.flab.simplesharingcar.web.validator;

import com.flab.simplesharingcar.annotation.StartAndEndTimeCheck;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StartAndEndTimeValidator implements ConstraintValidator<StartAndEndTimeCheck, Object> {

    private String message;
    private String startDate;
    private String endDate;

    @Override
    public void initialize(StartAndEndTimeCheck constraintAnnotation) {
        message = constraintAnnotation.message();
        startDate = constraintAnnotation.startTime();
        endDate = constraintAnnotation.endTime();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        try {
            LocalDateTime startDateTime = getFieldValue(value, startDate);
            LocalDateTime endDateTime = getFieldValue(value, endDate);
            if (startDateTime.isAfter(endDateTime)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(startDate)
                    .addConstraintViolation();
                return false;
            }
        } catch (NullPointerException npe) {
            log.error("ReservationTimeValidator 예약 시간 정보 null");
            return false;
        }
        return true;
    }

    private LocalDateTime getFieldValue(Object object, String fieldName) {
        Class<?> clazz = object.getClass();
        Field dateField;
        try {
            dateField = clazz.getDeclaredField(fieldName);
            dateField.setAccessible(true);
            Object target = dateField.get(object);

            if (!(target instanceof LocalDateTime) && target != null) {
                throw new ClassCastException("casting exception");
            }
            return (LocalDateTime) target;
        } catch (Exception e) {
            log.error("ReservationTimeValidator.getFieldValue 필드 에러", e);
            throw new IllegalArgumentException("Not Found Field");
        }
    }

}
