package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class ByteCodec extends AbstractCodec<Byte>{

    @Override
    public Byte decode(ByteBuffer buffer) {
        return Byte.parseByte(_decode(buffer));
    }

    @Override
    public ByteBuffer encode(Byte value) {
        return _encode(String.valueOf(value));
    }
}
