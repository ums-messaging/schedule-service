package com.ums.schedule.send.application.model.dto;


// SecurityPolicy가 존재하면 birthday포맷 확인 후 검증 처리
// Attachment 경로에 데이터가 존재하지 않으면 에러 처리
// Attachment에 ConvertType이 NONE이 아니면 파일 읽어와서 파싱 처리 -> 에러 시
public record TemplateMatchingDto(
) {
}
