package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class FloatCodec extends AbstractCodec<Float> {

    @Override
    public Float decode(ByteBuffer buffer) {
        return Float.parseFloat(_decode(buffer));
    }

    @Override
    public ByteBuffer encode(Float value) {
        return _encode(String.valueOf(value));
    }
}
