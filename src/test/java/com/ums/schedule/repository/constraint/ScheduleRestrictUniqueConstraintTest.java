package com.ums.schedule.repository.constraint;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
public class ScheduleRestrictUniqueConstraintTest {
//    @Autowired private ScheduleRepository scheduleRepository;
//    @Autowired private ScheduleRestrictPolicyRepository restrictPolicyRepository;
//    @Autowired private EntityManager entityManager;
//    private Schedule schedule;
//
//    @BeforeEach
//    void setUp() {
//        Schedule schedule = ScheduleFixture.ofSchedule();
//        this.schedule = scheduleRepository.save(schedule);
//    }
//
//    @Test
//    @DisplayName("restrict_mode는 null 을 허용하지 않는다.")
//    void shouldFailPersist_restrictModeIsNull() {
//        ScheduleRestrictPolicy restrictPolicy = ScheduleFixture.ofScheduleRestrictPolicy();
//        restrictPolicy.applySchedule(schedule);
//
//        setField(restrictPolicy, "restrictMode", null);
//        assertThatThrownBy(() -> restrictPolicyRepository.saveAndFlush(restrictPolicy))
//                .isInstanceOf(DataIntegrityViolationException.class);
//    }
//
//    @Test
//    @DisplayName("schedule_id에 대해 restrict_mode는 중복을 허용하지 않는다.")
//    void shouldFailPersist_RestrictModeAboutScheduleId() {
//        // given
//        ScheduleRestrictPolicy dupNightPolicy = new ScheduleRestrictPolicy();
//        setField(dupNightPolicy, "restrictMode", RestrictModeEnum.NIGHT);
//
//        ScheduleRestrictPolicy nightPolicy = new ScheduleRestrictPolicy();
//        setField(nightPolicy, "restrictMode", RestrictModeEnum.NIGHT);
//
//        ScheduleRestrictPolicy weekendPolicy = new ScheduleRestrictPolicy();
//        setField(weekendPolicy, "restrictMode", RestrictModeEnum.WEEKEND);
//
//        nightPolicy.applySchedule(schedule);
//        restrictPolicyRepository.saveAndFlush(nightPolicy);
//
//        weekendPolicy.applySchedule(schedule);
//        restrictPolicyRepository.saveAndFlush(weekendPolicy);
//
//        dupNightPolicy.applySchedule(schedule);
//        assertThatThrownBy(() ->
//                restrictPolicyRepository.saveAndFlush(dupNightPolicy)
//        ).isInstanceOf(DataIntegrityViolationException.class);
//    }
}
