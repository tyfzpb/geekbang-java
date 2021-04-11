package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class IntegerCodec extends AbstractCodec<Integer>{
    @Override
    public Integer decode(ByteBuffer buffer) {
        return Integer.parseInt(_decode(buffer));
    }

    @Override
    public ByteBuffer encode(Integer value) {
        return _encode(String.valueOf(value));
    }
}
