package com.ums.schedule.repository.constraint.schedule;

import com.ums.schedule.domain.schedule.ScheduleJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ScheduleCheckConstraintTest {
    @Autowired private ScheduleJpaRepository scheduleRepository;

    @Nested
    @DisplayName("status 허용 범위 테스트")
    class StatusCheckConstraintTest {
        @Test
        @DisplayName("status에 ACTIVE를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsCreate() {

        }

        @Test
        @DisplayName("status에 INACTIVE를 입력하면 저장된다.")
        void shouldPersist_whenStatusIsInActive() {

        }

        @Test
        @DisplayName("status에 RUNNING을 입력하면 저장된다.")
        void shouldPersist_whenStatusIsRunning() {

        }
    }

    @Nested
    @DisplayName("schedule_type 허용 범위 테스트")
    class ScheduleTypeCheckConstraintTest {

    }

    @Nested
    @DisplayName("cycle_cd 허용 범위 테스트")
    class CycleCdCheckConstraintTest {

    }
}
