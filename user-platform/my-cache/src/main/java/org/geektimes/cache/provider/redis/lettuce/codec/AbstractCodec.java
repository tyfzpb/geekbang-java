package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

abstract class AbstractCodec<T> implements Codec<T> {

    protected String _decode(ByteBuffer buffer) {
        return StandardCharsets.UTF_8.decode(buffer).toString();
    }

    protected ByteBuffer _encode(String value) {
        return ByteBuffer.wrap(value.getBytes(StandardCharsets.UTF_8));
    }

}
