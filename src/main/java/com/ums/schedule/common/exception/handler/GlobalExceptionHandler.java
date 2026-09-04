package com.ums.schedule.common.exception.handler;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.ums.schedule.adapter.api.request.ApiResponse;
import com.ums.schedule.adapter.api.request.ErrorResponse;
import com.ums.schedule.common.code.api.ApiResponseCode;
import com.ums.schedule.common.code.api.ErrorCode;
import com.ums.schedule.common.code.api.GlobalErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.mapper.exception.EnumMapperNotFoundException;
import com.ums.schedule.common.exception.BusinessException;
import com.ums.schedule.common.exception.ExternalSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.MessageFormat;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final PropertyNamingStrategies.SnakeCaseStrategy SNAKE_CASE_TRANSLATOR =
            (PropertyNamingStrategies.SnakeCaseStrategy) PropertyNamingStrategies.SNAKE_CASE;
    private final EnumMapperFactory factory;

    @Autowired
    public GlobalExceptionHandler() {
        this.factory = new EnumMapperFactory();
        this.factory.register(GlobalErrorCode.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
            List<ErrorResponse> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(this::getErrorResponse)
                    .toList();
            return ResponseEntity.badRequest()
                    .body(ApiResponse.of(ApiResponseCode.BAD_REQUEST, errors));

    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        ApiResponseCode responseCode = ApiResponseCode.valueOf(errorCode.code());

        return ResponseEntity.status(responseCode.getHttpStatus())
                .body(ApiResponse.of(responseCode, MessageFormat.format(
                        e.getErrorCode().description(),
                        e.getArgs()
                )));
    }

    @ExceptionHandler(ExternalSystemException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handle(ExternalSystemException e) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.of(ApiResponseCode.SERVER_ERROR,
                        MessageFormat.format(
                                e.getErrorCode().description(),
                                e.getArgs()
                        )
                        ))
                ;
    }

    @ExceptionHandler(EnumMapperNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handlerEnumMapperException(EnumMapperNotFoundException e) {
        return ResponseEntity.internalServerError()
                .body(ApiResponse.of(ApiResponseCode.SERVER_ERROR, MessageFormat.format(
                        e.getErrorCode().description(),
                        e.getArgs()
                )));
    }


    private ErrorResponse getErrorResponse(FieldError result){
        String message = result.getDefaultMessage();

        String[] split = message.split(":", 2);
        EnumMapperValue errorCode = factory.findEnumMapperValue(GlobalErrorCode.valueOf(split[0]), split[1]);

        return ErrorResponse.of(convertToSnakeCase(result.getField()), errorCode);
    }

    private String normalizeField(String field) {
        return field.startsWith("request.")
                ? field.substring("request.".length())
                : field;
    }

    private String convertToSnakeCase(String fieldName) {
        if (fieldName == null) return null;

        // nested 구조(예: "request.senderKey")가 있을 수 있으므로 점(.)을 기준으로 분할하여 각각 변환
        String[] parts = fieldName.split("\\.");
        for (int i = 0; i < parts.length; i++) {
            parts[i] = SNAKE_CASE_TRANSLATOR.translate(parts[i]);
        }
        return normalizeField(String.join(".", parts));
    }
}
