package com.ums.schedule.application.ums.common.message;

import com.ums.schedule.application.ums.common.config.SendMessageProperties;
import com.ums.schedule.application.ums.common.exception.SendMessageNotConfiguredException;
import com.ums.schedule.application.ums.common.template.model.TemplateResult;
import com.ums.schedule.common.code.api.SendMessageErrorCode;
import com.ums.schedule.common.code.mapper.EnumMapperFactory;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.code.message.MessageConfigurationPrefix;
import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.common.code.message.MessageType;
import com.ums.schedule.fixture.template.TemplateResultBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SendMessageFactoryTest {
    @Mock
    private EnumMapperFactory mapperFactory;
    @Mock
    private SendMessageProperties properties;
    @InjectMocks
    private SendMessageFactory factory;

    private TemplateResult template;

    @BeforeEach
    void setUp() {
        template = TemplateResultBuilder.builder().build();
    }

    @Test
    @DisplayName("템플릿 타입이 존재하지 않으면, NONE이다.")
    void shouldReturnNone_whenTemplateTypeDoesNotExist() {
        doReturn(null).when(mapperFactory).findEnumMapperValue(any(), any());

        SendMessage sendMessage = factory.createSendMessage(template);

        assertThat(sendMessage.getMessageType()).isEqualTo(MessageType.NONE);
    }

    @Test
    @DisplayName("템플릿 타입이 광고이면, 설정 파일에서 광고 문구를 조회한다.")
    void shouldGetConfiguredAdvertisePhrase_whenTemplateTypeIsAdvertise() {
        doReturn(EnumMapperValue.fromEnumMapperType(MessageType.ADVERTISE)).when(mapperFactory).findEnumMapperValue(any(), any());
        doReturn("(광고)").when(properties).getAdvertisingPrefix();

        factory.createSendMessage(template);

        verify(properties).getAdvertisingPrefix();
    }

    @Test
    @DisplayName("설정 파일에서 광고 문구가 존재하지 않으면 예외가 발생한다.")
    void shouldThrowException_whenAdvertisePrefixIsEmpty() {
        doReturn(EnumMapperValue.fromEnumMapperType(MessageType.ADVERTISE)).when(mapperFactory).findEnumMapperValue(any(), any());
        doReturn("").when(properties).getAdvertisingPrefix();

        SendMessageNotConfiguredException expect = SendMessageNotConfiguredException.of(MessageConfigurationPrefix.ADVERTISE_PREFIX);

        assertThatThrownBy(() -> factory.createSendMessage(template))
                .isInstanceOf(expect.getClass())
                .hasMessage(expect.getMessage());
    }


    @Test
    @DisplayName("템플릿 타입이 광고가 아니면, 설정 파일에서 광고 문구를 조회하지 않는다.")
    void shouldNotGetAdvertisingPrefix_whenTemplateTypeIsNotAdvertise() {
        doReturn(EnumMapperValue.fromEnumMapperType(MessageType.NONE)).when(mapperFactory).findEnumMapperValue(any(), any());

        SendMessage message = factory.createSendMessage(template);

        verify(properties, never()).getAdvertisingPrefix();
    }

    @Test
    @DisplayName("템플릿 타입이 광고이면, 메시지 문구가 저장된다.")
    void shouldSaveMessagePrefix_whenMessageTypeIsAdvertise() {
        doReturn(EnumMapperValue.fromEnumMapperType(MessageType.ADVERTISE)).when(mapperFactory).findEnumMapperValue(any(), any());
        doReturn("(광고)").when(properties).getAdvertisingPrefix();

        SendMessage message = factory.createSendMessage(template);

        assertThat(message.getMessagePrefix()).isEqualTo("(광고)");
    }

    @Test
    @DisplayName("템플릿 타입이 광고가 아니면, 메시지 문구는 저장되지 않는다.")
    void shouldNotSaveMessagePrefix_whenMessageTypeIsNotAdvertise() {
        doReturn(EnumMapperValue.fromEnumMapperType(MessageType.NONE)).when(mapperFactory).findEnumMapperValue(any(), any());

        SendMessage message = factory.createSendMessage(template);

        assertThat(message.getMessagePrefix()).isNull();
    }
}
