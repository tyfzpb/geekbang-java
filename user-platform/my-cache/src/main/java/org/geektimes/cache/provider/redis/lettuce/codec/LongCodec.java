package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class LongCodec extends AbstractCodec<Long> {

    @Override
    public Long decode(ByteBuffer buffer) {
        return Long.parseLong(_decode(buffer));
    }

    @Override
    public ByteBuffer encode(Long value) {
        return _encode(String.valueOf(value));
    }
}
