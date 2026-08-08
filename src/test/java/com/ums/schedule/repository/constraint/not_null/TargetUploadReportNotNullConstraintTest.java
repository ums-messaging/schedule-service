package com.ums.schedule.repository.constraint.not_null;

import com.ums.schedule.domain.message.SendMessage;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.target.upload.TargetUploadReport;
import com.ums.schedule.fixture.entity.TargetUploadReportEntityBuilder;
import com.ums.schedule.fixture.field.TargetUploadField;
import com.ums.schedule.repository.DbErrorMessage;
import com.ums.schedule.repository.EntityJpaTestSupport;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
@ActiveProfiles("test")
@DataJpaTest
public class TargetUploadReportNotNullConstraintTest extends EntityJpaTestSupport {
    private static final String ERROR_MESSAGE = DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage();

    private TargetUploadReportEntityBuilder entityBuilder;

    @BeforeEach
    void setUp() {
        Schedule schedule = givenSchedule();
        SendMessage sendMessage = givenSendMessage();
        SendRequest sendRequest = givenSendRequest(schedule, sendMessage);

        entityBuilder = TargetUploadReportEntityBuilder.builder()
                .sendRequest(sendRequest);
    }

    @Test
    @DisplayName("upload_type은 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenUploadTypeIsNull() {
        TargetUploadReport targetUpload = entityBuilder
                .uploadType(null).build();

        assertThatThrownBy(() -> persist(targetUpload))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "UPLOAD_TYPE");
    }

    @Test
    @DisplayName("status는 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenStatusIsNull() {
        TargetUploadReport targetUpload = entityBuilder
                .uploadStatus(null).build();

        assertThatThrownBy(() -> persist(targetUpload))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "STATUS");
    }

    @Test
    @DisplayName("download_key는 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenDownloadKeyIsNull() {
        TargetUploadReport targetUpload = entityBuilder
                .downloadKey(null).build();


        assertThatThrownBy(() -> persist(targetUpload))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "DOWNLOAD_KEY");
    }

    @Test
    @DisplayName("created_at은 NULL을 허용하지 않는다.")
    void shouldNotAllow_whenCreatedAtIsNull(){
        TargetUploadReport targetUpload = entityBuilder
                .createdAt(null).build();

        assertThatThrownBy(() -> persist(targetUpload))
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessageContainingAll(ERROR_MESSAGE, "CREATED_AT");
    }
}
