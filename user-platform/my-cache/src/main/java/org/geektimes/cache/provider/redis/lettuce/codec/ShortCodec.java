package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class ShortCodec extends AbstractCodec<Short> {

    @Override
    public Short decode(ByteBuffer buffer) {
        return Short.parseShort(_decode(buffer));
    }

    @Override
    public ByteBuffer encode(Short value) {
        return _encode(String.valueOf(value));
    }
}
