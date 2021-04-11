package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class StringCodec extends AbstractCodec<String> {
    @Override
    public String decode(ByteBuffer buffer) {
        return _decode(buffer);
    }

    @Override
    public ByteBuffer encode(String value) {
        return _encode(value);
    }
}
