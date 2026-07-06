package com.ums.schedule.repository.constraint.request;

import com.ums.schedule.domain.sendrequest.code.SendRequestStatusEnum;
import com.ums.schedule.domain.sendrequest.SendRequest;
import com.ums.schedule.domain.sendrequest.SendRequestTestBuilder;
import com.ums.schedule.domain.schedule.Schedule;
import com.ums.schedule.domain.schedule.ScheduleTestBuilder;
import com.ums.schedule.fixture.field.SendRequestField;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SendRequestDefaultConstraintTest {
    @Autowired
    private EntityManager entityManager;

    private Long scheduleId;

    @BeforeEach
    void setUp() {
        Schedule schedule = ScheduleTestBuilder.builder().build();
        entityManager.persist(schedule);
        entityManager.flush();

        this.scheduleId = schedule.getId();
        entityManager.clear();
    }

    @Test
    @DisplayName("status가 NULL이면 status는 CREATE를 반환한다.")
    void shouldReturnStatusCreate_whenStatusIsNull() {
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest request = SendRequestTestBuilder.builder().schedule(schedule)
                .state(null)
                .build();

        entityManager.persist(request);
        entityManager.flush();
        assertThat(request.getState().getCurrentCode()).isEqualTo(SendRequestStatusEnum.CREATE);
    }

    @Test
    @DisplayName("retry_cnt가 NULL이면 retry_cnt는 3을 반환한다.")
    void shouldReturnRetryCntIsThree_whenRetryCntIsNull(){
        Schedule schedule = entityManager.getReference(Schedule.class, scheduleId);
        SendRequest request = SendRequestTestBuilder.builder().schedule(schedule).retryCnt(null).build();

        SendRequestField field = SendRequestField.RETRY_CNT;

        ReflectionTestUtils.setField(request, field.value(), null);

        entityManager.persist(request);
        entityManager.flush();
        assertThat(request.getRetryCnt()).isEqualTo(3);
    }
}
