package com.ums.schedule.email.adaptor.in.rest.message.validator;

import com.ums.schedule.email.adaptor.in.rest.message.request.MessageCreateRequest;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Component
public class SendMessageCreateValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return MessageCreateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MessageCreateRequest request = (MessageCreateRequest) target;
        validateIfConvertTypeExists(request, errors);
        validateIfSecurityPolicyExists(request, errors);
    }

    private void validateIfConvertTypeExists(MessageCreateRequest request, Errors errors) {
        if(existsConvertType(request) || request.securityPolicy() != null) {
            validateAttachmentNameAndDownloadName(request.attachmentName(), request.downloadName(), errors);
        }
    }

    private Boolean existsConvertType(MessageCreateRequest request) {
        return Optional.ofNullable(request.convertType())
                .map(type -> isValidConvertType(type))
                .orElseGet(() -> false);
    }

    private boolean isValidConvertType(String type) {
        return Arrays.stream(ConvertType.values())
                .filter(v -> v.value().equals(type))
                .filter(Objects::nonNull)
                .filter(v -> v != ConvertType.NONE)
                .findFirst()
                .map(v->true)
                .orElseGet(() -> false);
    }

    private void validateIfSecurityPolicyExists(MessageCreateRequest request, Errors errors) {
        if(request.mailType().equals(EmailType.SECURITY.value())) {
            if(request.securityPolicy() == null) {
                errors.rejectValue("securityPolicy", "EMAIL_SEND_REQUEST:SECURITY_POLICY_NOT_NULL");
            }
            validateAttachmentNameAndDownloadName(request.attachmentName(), request.downloadName(), errors);
        }
    }

    private void validateAttachmentNameAndDownloadName(String attachmentName, String downloadName, Errors errors) {
        validateAndSetFieldAddError("attachmentName", attachmentName, "EMAIL_SEND_REQUEST:ATTACHMENT_NAME_REQUIRED", errors);
        validateAndSetFieldAddError("downloadName", downloadName, "EMAIL_SEND_REQUEST:DOWNLOAD_NAME_REQUIRED", errors);
    }

    private void validateAndSetFieldAddError(String field, String value, String message, Errors errors) {
        if(!StringUtils.hasText(value)) {
            errors.rejectValue(field, message);
        }
    }
}
