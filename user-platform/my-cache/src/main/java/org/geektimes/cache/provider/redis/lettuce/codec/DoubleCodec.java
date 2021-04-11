package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

class DoubleCodec extends AbstractCodec<Double> {

    @Override
    public Double decode(ByteBuffer buffer) {
        return Double.parseDouble(_decode(buffer));
    }

    @Override
    public ByteBuffer encode(Double value) {
        return _encode(String.valueOf(value));
    }
}
