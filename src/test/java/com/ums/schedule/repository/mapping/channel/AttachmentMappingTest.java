package com.ums.schedule.repository.mapping.channel;

import com.ums.schedule.domain.channel.email.attachment.Attachment;
import com.ums.schedule.domain.channel.email.attachment.AttachmentTestBuilder;
import com.ums.schedule.domain.request.SendRequest;
import com.ums.schedule.domain.request.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.domain.target.SendTarget;
import com.ums.schedule.domain.target.SendTargetTestBuilder;
import com.ums.schedule.domain.target.upload.TargetUpload;
import com.ums.schedule.domain.target.upload.TargetUploadTestBuilder;
import com.ums.schedule.repository.DbErrorMessage;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class AttachmentMappingTest {
    @Autowired
    private EntityManager entityManager;
    private UUID targetId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        SendRequest sendRequest = SendRequestTestBuilder.builder().schedule(schedule).build();
        TargetUpload targetUpload = TargetUploadTestBuilder.builder().sendRequest(sendRequest).build();
        SendTarget target = SendTargetTestBuilder.builder().targetUpload(targetUpload).build();
        entityManager.persist(schedule);
        entityManager.persist(sendRequest);
        entityManager.persist(targetUpload);
        entityManager.persist(target);
        entityManager.flush();

        targetId = target.getId();
        entityManager.clear();
    }

    @Nested
    @DisplayName("send_target 연관관계 테스트")
    class SendTargetMappingTest {
        @Test
        @DisplayName("send_target에서 연관관계를 설정하면 FK는 저장되지 않는다.")
        void shouldNotPersistEmailSendRequestFK_whenSetByInverseOnlySide() {
            SendTarget target = entityManager.getReference(SendTarget.class, targetId);
            Attachment attachment = AttachmentTestBuilder.builder().sendTarget(target).build();

            entityManager.persist(target);
            entityManager.flush();
            entityManager.clear();

            List<Attachment> expect = entityManager.createQuery("select a from Attachment a where a.target.id = :id", Attachment.class)
                    .setParameter("id", targetId)
                    .getResultList();

            assertThat(expect).hasSize(0);
        }

        @Test
        @DisplayName("attachment에서 send_target과 연관관계를 설정하면 FK가 저장된다.")
        void shouldPersistFkSendTarget_whenSetByAttachment() {
            SendTarget target = entityManager.getReference(SendTarget.class, targetId);
            Attachment attachment = AttachmentTestBuilder.builder().sendTarget(target).build();

            entityManager.persist(attachment);
            entityManager.flush();

            UUID id = attachment.getId();
            entityManager.clear();

            Attachment result = entityManager.find(Attachment.class, id);
            SendTarget expect = result.getTarget();

            assertThat(expect.getId()).isNotNull();
        }

        @Test
        @DisplayName("FK인 send_target은 NULL을 허용하지 않는다.")
        void shouldThrowException_whenSendTargetIsNull() {
            Attachment attachment = AttachmentTestBuilder.builder().sendTarget(null).build();

            assertThatThrownBy(() -> {
                entityManager.persist(attachment);
                entityManager.flush();
            }).isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContainingAll(DbErrorMessage.NOT_NULL_CONSTRAINT.getMessage(), "TARGET_ID");
        }
    }
}
