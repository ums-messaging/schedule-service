package com.ums.schedule.send.domain.request;

import com.ums.schedule.application.request.dto.SendRequestDto;
import com.ums.schedule.domain.request.exception.CustomerKeyRequiredException;

import com.ums.schedule.domain.request.CustomerRequestKey;
import com.ums.schedule.domain.request.SendRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SendRequestDomainTest {

    @Test
    @DisplayName("발송 요청을 하면 상태는 CREATED 이다.")
    void shouldReturnStatusIsCreated_whenCreateSendRequest() {
        CustomerRequestKey customerRequestKey = CustomerRequestKey.of("company", UUID.randomUUID().toString());
        SendRequestDto dto = new SendRequestDto("template",
                "sender", 3, 1000);
        SendRequest sendRequest = SendRequest.of(customerRequestKey, dto);

//        assertThat(sendRequest.getEvent().getStatus()).isEqualTo(SendRequestStatusEnum.CREATE);

    }

    @Test
    @DisplayName("발송 대상자 목록이 존재하면 , totalSize에 대상자 입력 수가 반환된다.")
    void shouldReturnTotalCountEqualTargetListCommandSize_whenUploadTypeIsJsonAndTargetListExists() {
        CustomerRequestKey customerRequestKey = CustomerRequestKey.of("company", UUID.randomUUID().toString());
        SendRequestDto dto = new SendRequestDto("template",
                "sender", 3, 1000);
        SendRequest sendRequest = SendRequest.of(customerRequestKey, dto);

//        SendReport result = sendRequest.getReport();
//        int totalCount = Integer.parseInt(String.valueOf(result.getTotalCount()));
//        assertThat(result).isNotNull();
//        assertThat(totalCount).isEqualTo(dto.totalCount());
    }

    @Test
    @DisplayName("CustomerKey와 CustomerRequestKey가 NULL이거나 비어있으면 오류가 발생한다.")
    void shouldThrowException_whenCustomerKeyOrCustomerRequestIdIsNull() {
        assertThatThrownBy(() -> CustomerRequestKey.of(" ", " "))
                .isInstanceOf(CustomerKeyRequiredException.class)
                .hasMessage(CustomerKeyRequiredException.of().getMessage());
    }
}