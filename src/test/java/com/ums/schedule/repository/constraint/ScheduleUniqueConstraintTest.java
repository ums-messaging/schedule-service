package com.ums.schedule.repository.constraint;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
public class ScheduleUniqueConstraintTest {
//    @Autowired
//    private ScheduleRepository scheduleRepository;
//    private Schedule schedule;
//
//    @BeforeEach
//    void setup() {
//        schedule = ScheduleFixture.ofSchedule();
//    }
//
//    @Test
//    @DisplayName("schedule_name 필드는 null 을 허용하지 않는다.")
//    void shouldFail_whenScheduleNameIsNull() {
//        setField(schedule, "name", null);
//        assertThatThrownBy(() -> scheduleRepository.saveAndFlush(schedule))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @DisplayName("schedule_type 필드는 null을 허용하지 않는다.")
//    void shouldFail_whenScheduleTypeIsNull() {
//        setField(schedule, "type", null);
//        assertThatThrownBy(() -> scheduleRepository.saveAndFlush(schedule))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @DisplayName("status 필드는 null을 허용하지 않는다.")
//    void shouldFail_whenStatusIsNull() {
//        setField(schedule, "status", null);
//        assertThatThrownBy(() -> scheduleRepository.saveAndFlush(schedule))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @DisplayName("schedule_period에 대한 필드는 null을 허용하지 않는다.")
//    void shouldFail_whenSchedulePeriodIsNull() {
//        setField(schedule, "schedulePeriod", null);
//        assertThatThrownBy(() -> scheduleRepository.saveAndFlush(schedule))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
}
