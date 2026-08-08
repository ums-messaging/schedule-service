package com.ums.schedule.adapter.api.request.email.validator;

import com.ums.schedule.adapter.api.request.email.request.EmailAttachmentListRequest;
import com.ums.schedule.adapter.api.request.email.request.EmailSendCreateRequest;
import com.ums.schedule.common.code.email.ConvertType;
import com.ums.schedule.common.code.email.EmailType;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class EmailSendRequestValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return EmailSendCreateRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        EmailSendCreateRequest request = (EmailSendCreateRequest) target;
        validateIfConvertTypeExists(request, errors);
        validateIfSecurityPolicyExists(request, errors);
        validateAttachmentList(request, errors);
    }

    private void validateAttachmentList(EmailSendCreateRequest request, Errors errors) {
        EmailAttachmentListRequest attachment = request.attachmentList();
        if(StringUtils.hasText(attachment.attachmentUploadKey())) {
            if(attachment.list().size() == 0) {
                errors.rejectValue("attachmentList", "EMAIL_SEND_REQUEST:ATTACHMENT_LIST_EMPTY");
            }
        }

        if(attachment.list().size() > 0) {
            if(StringUtils.hasText(attachment.attachmentUploadKey())) {
                errors.rejectValue("attachmentUploadKey", "EMAIL_SEND_REQUEST:ATTACHMENT_UPLOAD_KEY_REQUIRED");
            }
        }
    }

    private void validateIfConvertTypeExists(EmailSendCreateRequest request, Errors errors) {
        if(existsConvertType(request)) {
            validateAttachmentNameAndDownloadName(request.attachmentName(), request.downloadName(), errors);
        }
    }

    private Boolean existsConvertType(EmailSendCreateRequest request) {
        return Optional.ofNullable(request.convertType())
                .map(type -> isValidConvertType(type))
                .orElse(false);
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

    private void validateIfSecurityPolicyExists(EmailSendCreateRequest request, Errors errors) {
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
