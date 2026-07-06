package com.ums.schedule.domain.sendrequest.converter;

import com.ums.schedule.common.util.UuidUtil;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.nio.ByteBuffer;
import java.util.UUID;

@Converter
public class UuidBinaryConverter implements AttributeConverter<UUID, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(UUID attribute) {
        return UuidUtil.encode(attribute);
    }

    @Override
    public UUID convertToEntityAttribute(byte[] dbData) {
        ByteBuffer buffer = ByteBuffer.wrap(dbData);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
