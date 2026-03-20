package com.ums.schedule.common.code;

import com.ums.schedule.common.code.exception.EnumMapperNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EnumMapperFactoryTest {

    @Test
    @DisplayName("EnumMapper 등록 후, 코드로 조회하면 해당 EnumMapperValue를 반환한다")
    void shouldReturnEnumMapperValue_whenFindByCodeAfterRegister() {
        EnumMapperFactory factory = new EnumMapperFactory();
        factory.register(TestEnumMapper.class);

        EnumMapperValue result = factory.findEnumMapperValue(TestEnumMapper.TEST_ENUM_MAPPER, "PDF");
        assertThat(result.code()).isEqualTo("PDF");
    }

    @Test
    @DisplayName("존재하지 않는 코드로 조회하면 예외가 발생한다.")
    void shouldThrowException_whenCodeNotExist() {
        EnumMapperFactory factory = new EnumMapperFactory();
        factory.register(TestEnumMapper.class);

        String expectedError = EnumMapperNotFoundException.forEnumMapperValue(TestEnumMapper.TEST_ENUM_MAPPER, "HTML").getMessage();

        assertThatThrownBy(() -> factory.findEnumMapperValue(TestEnumMapper.TEST_ENUM_MAPPER, "HTML"))
                .isInstanceOf(EnumMapperNotFoundException.class)
                .hasMessage(expectedError);
    }

    @Test
    @DisplayName("등록하지 않은 EnumMapper로 코드를 조회하면 예외가 발생한다.")
    void shouldThrowException_whenEnumMapperNotExist() {
        EnumMapperFactory factory = new EnumMapperFactory();

        String expectedError = EnumMapperNotFoundException.forEnumMapper(TestEnumMapper.TEST_ENUM_MAPPER).getMessage();

        assertThatThrownBy(() -> factory.findEnumMapperValue(TestEnumMapper.TEST_ENUM_MAPPER, "PDF"))
                .isInstanceOf(EnumMapperNotFoundException.class)
                .hasMessage(expectedError);
    }

    enum TestEnumMapper implements EnumMapper {
        TEST_ENUM_MAPPER(TestEnumMapperType.class);

        Class<? extends EnumMapperType> code;

        TestEnumMapper(Class<? extends EnumMapperType> code) {
            this.code = code;
        }

        @Override
        public String key() {
            return this.name();
        }

        @Override
        public Class<? extends EnumMapperType> code() {
            return this.code;
        }
    }

    enum TestEnumMapperType implements EnumMapperType {
        PDF("PDF", "PDF");

        private String value;
        private String description;

        TestEnumMapperType(String value, String description) {
            this.value = value;
            this.description = description;
        }

        @Override
        public String code() {
            return this.name();
        }

        @Override
        public String value() {
            return this.value;
        }

        @Override
        public String description() {
            return this.description;
        }
    }

}