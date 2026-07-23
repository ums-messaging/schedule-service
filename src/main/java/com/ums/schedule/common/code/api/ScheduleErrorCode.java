package com.ums.schedule.common.code.api;


public enum ScheduleErrorCode implements ErrorCode {
    NOT_FOUND_SCHEDULE(ApiResponseCode.BAD_REQUEST, "존재하지 않는 스케쥴입니다."),
    NOT_RUNNING_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] 실행 중인 스케쥴이 아닙니다."),
    NOT_ACTIVE_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] 활성화 된 스케쥴이 아닙니다."),
    INACTIVE_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] 비활성화 된 스케쥴이 아닙니다."),
    STATE_TO_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] {1} 상태의 스케쥴입니다."),
    EXPIRED_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] 만료된 스케쥴입니다."),
    NOT_START_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] 시작되지 않은 스케쥴입니다."),
    NOT_CONTAINS_PERIOD(ApiResponseCode.CONFLICT, "스케쥴 기간 안에서 설정되어야 합니다."),
    INVALID_PERIOD_FORMAT(ApiResponseCode.BAD_REQUEST, "유효하지 않은 날짜 형식입니다."),
    INVALID_PERIOD_RANGE(ApiResponseCode.BAD_REQUEST, "시작 일은 종료일 보다 이전이어야 합니다."),
    START_AT_BEFORE_NOW(ApiResponseCode.BAD_REQUEST, "시작 일은 현재보다 이전 일 수 없습니다."),
    NOT_EXECUTE_SCHEDULE(ApiResponseCode.CONFLICT, "실행 가능한 스케쥴이 아닙니다."),
    NOT_IN_PERIOD_RESERVATION_DATE(ApiResponseCode.BAD_REQUEST, "예약 날짜는 스케쥴 기간에 포함되어야 합니다."),
    INVALID_RESERVATION_DATE(ApiResponseCode.BAD_REQUEST, "현재 시작에서 한시간 이후 부터 설정 가능합니다."),
    ALREADY_CHANGE_STATE(ApiResponseCode.CONFLICT, "[{0}] 이미 처리된 상태의 스케쥴입니다."),
    NOT_CHANGE_STATE(ApiResponseCode.CONFLICT, "상태를 변경할 수 없습니다."),
    CYCLE_VALUE_NOT_MINUTE(ApiResponseCode.BAD_REQUEST, "주기 값은 (분) 단위이어야 합니다."),
    CYCLE_VALUE_NOT_HOUR(ApiResponseCode.BAD_REQUEST, "주기 값은 (시간) 단위이어야 합니다."),
    CYCLE_VALUE_NOT_DAY(ApiResponseCode.BAD_REQUEST, "주기 값은 (일) 단위이어야 합니다."),
    CYCLE_VALUE_NOT_MONTH(ApiResponseCode.BAD_REQUEST, "주기 값은 (월) 단위이어야 합니다."),
    NOT_UPDATE_RUNNING_SCHEDULE(ApiResponseCode.CONFLICT, "[{0}] 실행 중인 스케쥴은 변경할 수 없습니다.");

    ApiResponseCode responseCode;
    String message;

    ScheduleErrorCode(ApiResponseCode responseCode, String message) {
        this.responseCode = responseCode;
        this.message = message;
    }

    @Override
    public String code() {
        return responseCode.code();
    }

    @Override
    public String value() {
        return this.name();
    }

    @Override
    public String description() {
        return this.message;
    }

    @Override
    public ApiResponseCode responseCode() {
        return responseCode;
    }
}