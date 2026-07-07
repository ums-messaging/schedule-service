package com.ums.schedule.common.code.mapper;

import com.ums.schedule.common.exception.EnumMapperNotEmptyException;
import com.ums.schedule.common.exception.EnumMapperNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EnumMapperFactory {
    private final Map<EnumMapper, List<EnumMapperValue>> codeMap = new ConcurrentHashMap<>();

    public void register(Class<? extends EnumMapper> enumMapper) {
        Arrays.stream(enumMapper.getEnumConstants())
                .forEach(e -> put(e, e.code()));
    }

    public void put(EnumMapper key, Class<? extends EnumMapperType> clz) {
        List<EnumMapperValue> enumMapperValues = Arrays.stream(clz.getEnumConstants())
                .map(e -> EnumMapperValue.fromEnumMapperType(e))
                .toList();
        this.codeMap.put(key, enumMapperValues);
    }

    public EnumMapperValue findEnumMapperValue(EnumMapper key, String value) {
        if(!StringUtils.hasText(value)) {
            return null;
        }
        return findEnumMapperList(key)
                .stream()
                .filter(e -> e.value().toUpperCase().equals(value.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> EnumMapperNotFoundException.forEnumMapperValue(key, value));
    }

    public List<EnumMapperValue> findEnumMapperList(EnumMapper key) {
        return Optional.ofNullable(codeMap.get(key))
                .orElseThrow(() -> EnumMapperNotFoundException.forEnumMapper(key));
    }
}
