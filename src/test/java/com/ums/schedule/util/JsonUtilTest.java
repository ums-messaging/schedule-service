package com.ums.schedule.util;

import com.ums.schedule.common.util.JsonUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JsonUtilTest {

    @Nested
    @DisplayName("클래스 객체로 역직렬화 할 때")
    class whenDeserializeToObject {
        @Test
        @DisplayName("올바른 JSON 형식이 주어지면, 해당 클래스의 객체를 반환한다.")
        void shouldReturnObject_whenProvideValidJsonString() {
            FakeSchedule schedule = new FakeSchedule(1L, "mySchedule");
            String json = "{\"id\":1, \"name\":\"mySchedule\"}";

            FakeSchedule expect = JsonUtil.toObject(json, FakeSchedule.class);

            assertThat(expect.getId()).isEqualTo(schedule.id);
            assertThat(expect.getName()).isEqualTo(schedule.name);
        }

        @Test
        @DisplayName("유효햐지 않은 JSON 형식이 입력되면 NULL을 반환한다.")
        void shouldReturnNull_whenProvideInvalidJson() {
            String json = "{\"id3\":1, \"subject\":\"mySchedule\"}";

            FakeSchedule expect = JsonUtil.toObject(json, FakeSchedule.class);

            assertThat(expect).isNull();
        }
    }

    @Nested
    @DisplayName("컬렉션 객체로 역직렬화 할 때")
    class whenDeserializeToList {
        @Test
        @DisplayName("올바른 JSON 배열이 입력되면, 지정한 타입의 컬렉션 객체로 역직렬화된다.")
        void shouldReturnListObject_whenProvideValidJsonArrayString() {
            FakeSchedule schedule = new FakeSchedule(1L, "mySchedule");
            String json = "[{\"id\":1, \"name\":\"mySchedule\"}]";

            List<FakeSchedule> expect = JsonUtil.toList(json, FakeSchedule.class);

            assertThat(expect).hasSize(1);
            assertThat(expect.get(0).getId()).isEqualTo(schedule.getId());
            assertThat(expect.get(0).getName()).isEqualTo(schedule.getName());
        }

        @Test
        @DisplayName("유효햐지 않은 JSON 배열 이 입력되면 빈 배열이 반환된다.")
        void shouldReturnEmptyList_whenProvideInvalidArrayJson() {
            String json = "{\"id3\":1, \"subject\":\"mySchedule\"}";

            List<FakeSchedule> expect = JsonUtil.toList(json, FakeSchedule.class);

            assertThat(expect).hasSize(0);
        }
    }

    @Nested
    @DisplayName("Json 형태로 직렬화 할 때")
    class whenSerializeToJson {
        @Test
        @DisplayName("Object 형태의 객체를 직렬화 하면 String 형태의 json이 반환된다.")
        void shouldReturnJsonString_whenProvideValidObject() {
            FakeSchedule schedule = new FakeSchedule(1L, "mySchedule");
            String json = "{\"id\":1,\"name\":\"mySchedule\"}";

            String expect = JsonUtil.toJson(schedule);

            assertThat(expect).isEqualTo(json);
        }

        @Test
        @DisplayName("List 형태의 객체를 직렬화 하면 String 형태의 json이 반환된다.")
        void shouldReturnJsonString_whenProvideValidList() {
            List<FakeSchedule> scheduleList = List.of(new FakeSchedule(1L, "mySchedule"));
            String json = "[{\"id\":1,\"name\":\"mySchedule\"}]";

            String expect = JsonUtil.toJson(scheduleList);

            assertThat(expect).isEqualTo(json);
        }

        @Test
        @DisplayName("Null 값을 입력하면 빈 값 이 반환된다.")
        void shouldReturnJsonIsEmpty_whenProvideInvalidJson() {
            String expect = JsonUtil.toJson(null);
            assertThat(expect).isEmpty();
        }
    }

    static class FakeSchedule {
        private Long id;
        private String name;

        public FakeSchedule() {
        }

        public FakeSchedule(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}