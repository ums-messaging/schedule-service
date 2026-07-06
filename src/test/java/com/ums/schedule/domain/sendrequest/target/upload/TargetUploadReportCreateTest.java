package com.ums.schedule.domain.sendrequest.target.upload;

import com.ums.schedule.application.sendrequest.target.data.TargetMessageData;
import com.ums.schedule.application.sendrequest.target.command.TargetUploadCreateCommand;
import com.ums.schedule.common.code.mapper.EnumMapperValue;
import com.ums.schedule.common.exception.validation.RequiredException;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.sendrequest.exception.SendRequestNotFoundException;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetListExceedViolationException;
import com.ums.schedule.domain.sendrequest.target.exeption.SendTargetNotFoundException;
import com.ums.schedule.domain.sendrequest.target.upload.builder.TargetUploadCreateCommandBuilder;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadEventEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadFormatEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadStatusEnum;
import com.ums.schedule.domain.sendrequest.target.upload.code.TargetUploadTypeEnum;
import com.ums.schedule.domain.sendrequest.target.upload.exception.UploadKeyGeneratedViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

public class TargetUploadReportCreateTest {
    private final TargetUploadCreateCommandBuilder command = TargetUploadCreateCommandBuilder.builder();
    private final SendRequest sendRequest = SendRequestTestBuilder.builder().build();

//    @Test
//    @DisplayName("SendRequest가 NULL이면 익셉션이 발생한다.")
//    void shouldThrowException_whenSendRequestIsNull() {
//        SendRequestNotFoundException expect = SendRequestNotFoundException.of();
//
//        assertThatThrownBy(() -> TargetUploadReport.of(null, command.build()))
//                .isInstanceOf(expect.getClass())
//                .hasMessage(expect.getMessage());
//    }
//
//    @Test
//    @DisplayName("UPLOAD_TYPE이 NULL이면 익셉션이 발생한다.")
//    void shouldThrowException_whenUploadTypeIsNull() {
//        TargetUploadCreateCommand givenCommand = command.uploadType(null).build();
//
//        RequiredException expect = RequiredException.fieldOf("upload_type");
//        SendRequest sendRequest = SendRequestTestBuilder.builder().build();
//
//        assertThatThrownBy(() -> TargetUploadReport.of(sendRequest, givenCommand))
//                .isInstanceOf(expect.getClass())
//                .hasMessage(expect.getMessage());
//    }
//
//    @Test
//    @DisplayName("state는 CREATE이다.")
//    void shouldReturnStateIsCreate() {
//        TargetUploadReport report = TargetUploadReport.of(sendRequest, command.build());
//        TargetUploadStatusEnum expect = report.getState().getCurrentCode();
//
//        assertThat(expect).isEqualTo(TargetUploadStatusEnum.CREATED);
//    }
//
//    @Test
//    @DisplayName("event는 TARGET_UPLOAD_CREATED가 발행된다.")
//    void shouldPublishTargetUploadCreatedEvent() {
//        TargetUploadReport report = TargetUploadReport.of(sendRequest, command.build());
//        TargetUploadEventEnum expect = report.getEvent();
//
//        assertThat(expect).isEqualTo(TargetUploadEventEnum.TARGET_UPLOAD_CREATED);
//    }
//
//    @Test
//    @DisplayName("create_at이 현재 시각으로 생성된다.")
//    void shouldReturnCreatedAtIsCurrentTime() {
//        TargetUploadReport report = TargetUploadReport.of(sendRequest, command.build());
//        LocalDateTime expect = report.getCreatedAt();
//
//        assertThat(expect.toLocalDate()).isEqualTo(LocalDate.now());
//    }
//
//    @Test
//    @DisplayName("download_key가 생성된다.")
//    void shouldCreateDownloadKey() {
//        TargetUploadCreateCommand given = command.filePrefix("target/upload").build();
//        SendRequest sendRequest = SendRequestTestBuilder.builder().build();
//
//        TargetUploadReport report = TargetUploadReport.of(sendRequest, given);
//
//        assertThat(report.getDownloadKey()).isNotNull();
//    }
//
//
//    @Nested
//    @DisplayName("upload_type이 FILE일 때")
//    class WhenUploadTypeIsFile {
//        private final TargetUploadCreateCommandBuilder command = TargetUploadCreateCommandBuilder.builder()
//                .uploadType(EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.FILE));
//        @Test
//        @DisplayName("upload_type이 FILE일 때 upload_key가 생성된다.")
//        void shouldCreateObjectKey() {
//            SendRequest sendRequest = SendRequestTestBuilder.builder().build();
//            TargetUploadReport report = TargetUploadReport.of(sendRequest, command.build());
//
//            assertThat(report.getUploadKey()).isNotNull();
//        }
//
//        @Test
//        @DisplayName("upload_format이 null이면 csv가 반환된다.")
//        void shouldReturnFileExtensionIsCsv_whenUploadFormatIsNull() {
//            TargetUploadCreateCommand givenCommand = command.uploadFormat(null).build();
//            TargetUploadReport report = TargetUploadReport.of(sendRequest, givenCommand);
//
//            assertThat(report.getUploadFormat()).isEqualTo(TargetUploadFormatEnum.CSV);
//        }
//
//        @Test
//        @DisplayName("file_prefix가 null이면 익셉션이 발생한다.")
//        void shouldThrowException_whenFilePrefixIsNull() {
//            TargetUploadCreateCommand givenCommand = command.filePrefix(null).build();
//
//            UploadKeyGeneratedViolationException expect = UploadKeyGeneratedViolationException.of("file prefix is empty.");
//            assertThatThrownBy(() -> TargetUploadReport.of(sendRequest, givenCommand))
//                    .isInstanceOf(expect.getClass())
//                    .hasMessage(expect.getMessage())
//            ;
//        }
//    }
//
//    @Nested
//    @DisplayName("upload_type이 JSON일 때")
//    class WhenUploadTypeIsJson {
//        private final TargetUploadCreateCommandBuilder command = TargetUploadCreateCommandBuilder.builder()
//                .uploadType(EnumMapperValue.fromEnumMapperType(TargetUploadTypeEnum.JSON));
//        @Test
//        @DisplayName("upload_type이 JSON일 때 total_size가 반영된다.")
//        void shouldApplyTotalSize_whenUploadTypeIsJson() {
//            TargetMessageData targetDto = mock(TargetMessageData.class);
//            TargetUploadCreateCommand command = this.command.targetList(List.of(targetDto)).build();
//
//            TargetUploadReport report = TargetUploadReport.of(sendRequest, command);
//
//            assertThat(report.getTotalCount()).isEqualTo(1);
//        }
//
//        @Test
//        @DisplayName("target_size가 0이면 익셉션이 발생한다.")
//        void shouldThrowException_whenTargetSizeIsZero() {
//            TargetUploadCreateCommand command = this.command.targetList(List.of()).build();
//
//            SendTargetNotFoundException expect = SendTargetNotFoundException.listOf(sendRequest.getId());
//
//            assertThatThrownBy(() -> TargetUploadReport.of(sendRequest, command))
//                    .isInstanceOf(expect.getClass())
//                    .hasMessage(expect.getMessage());
//        }
//
//        @Test
//        @DisplayName("target_size가 1,000건을 초과하면 익셉션이 발생한다.")
//        void shouldThrowException_whenTargetSizeExceedOneThousand(){
//            TargetMessageData targetDto = mock(TargetMessageData.class);
//            List<TargetMessageData> dtos = makeTargetDtos(targetDto);
//            TargetUploadCreateCommand command = this.command.targetList(dtos)
//                    .maxSize(1000).build();
//
//            SendTargetListExceedViolationException expect = SendTargetListExceedViolationException.of(1000);
//
//            assertThatThrownBy(() -> TargetUploadReport.of(sendRequest, command))
//                    .isInstanceOf(expect.getClass())
//                    .hasMessage(expect.getMessage());
//        }
//
//        private List<TargetMessageData> makeTargetDtos(TargetMessageData targetDto) {
//            List<TargetMessageData> dtos = new ArrayList<>();
//            for (int i = 0; i < 1001; i++) {
//                dtos.add(targetDto);
//            }
//            return dtos;
//        }
//    }
}
