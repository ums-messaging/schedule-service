package com.ums.schedule.common.util;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;

public class UuidUtil {

    public static byte[] encode(UUID uuid) {
        ByteBuffer buffer = ByteBuffer.wrap(new byte[16]);
        buffer.putLong(uuid.getMostSignificantBits());
        buffer.putLong(uuid.getLeastSignificantBits());
        return buffer.array();
    }

    public static UUID decode(String uuid) {
        byte[] bytes = Base64.getUrlDecoder().decode(uuid);
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        return new UUID(buffer.getLong(), buffer.getLong());
    }
}
