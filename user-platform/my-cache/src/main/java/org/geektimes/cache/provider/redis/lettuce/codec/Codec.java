package org.geektimes.cache.provider.redis.lettuce.codec;

import java.nio.ByteBuffer;

public interface Codec<T> {

    T decode(ByteBuffer buffer);

    ByteBuffer encode(T value);

}
