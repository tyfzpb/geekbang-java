package org.geektimes.cache.provider.redis.lettuce.codec;

import io.lettuce.core.codec.RedisCodec;
import io.netty.buffer.ByteBuf;
import org.geetimes.util.SerializeUtil;

import java.nio.ByteBuffer;

public class DefaultRedisCodec<K, V> implements RedisCodec<K, V> {

    private static final byte[] EMPTY = new byte[0];
    private final Codec keyCodec;
    private final Codec valueCodec;


    public DefaultRedisCodec(Class<K> k, Class<V> v) {
        this.keyCodec = initCodec(k);
        this.valueCodec = initCodec(v);
    }

    private Codec initCodec(Class clazz) {
        if (clazz == null) {
            throw new RuntimeException("not support types!");
        } else if (clazz.equals(String.class)) {
            return new StringCodec();
        } else if (clazz.equals(Integer.class)) {
            return new IntegerCodec();
        } else if (clazz.equals(Byte.class)) {
            return new ByteCodec();
        } else if (clazz.equals(Short.class)) {
            return new ShortCodec();
        } else if (clazz.equals(Long.class)) {
            return new LongCodec();
        } else if (clazz.equals(Float.class)) {
            return new FloatCodec();
        } else if (clazz.equals(Double.class)) {
            return new DoubleCodec();
        } else {
            throw new RuntimeException("not support types!");
        }
    }


    @Override
    public K decodeKey(ByteBuffer buffer) {
        if (this.keyCodec != null) {
            return (K) keyCodec.decode(buffer);
        }
        return (K) deCode(buffer);
    }

    @Override
    public V decodeValue(ByteBuffer buffer) {
        if (this.valueCodec != null) {
            return (V) valueCodec.decode(buffer);
        }
        return (V) deCode(buffer);
    }

    private Object deCode(ByteBuffer buffer) {
        int remaing = buffer.remaining();
        if (buffer == null || remaing == 0) {
            return null;
        }
        byte[] bytes = new byte[remaing];
        buffer.get(bytes);
        return SerializeUtil.deserizlize(bytes);
    }


    private ByteBuffer encode(Object obj) {
        if (obj == null) {
            return ByteBuffer.wrap(EMPTY);
        }
        byte[] bytes = SerializeUtil.serialize(obj);
        return ByteBuffer.wrap(bytes);
    }


    @Override
    public ByteBuffer encodeKey(K key) {
        if (this.keyCodec != null) {
            return keyCodec.encode(key);
        }
        return encode(key);
    }


    @Override
    public ByteBuffer encodeValue(V value) {
        if (this.valueCodec != null) {
            return valueCodec.encode(value);
        }
        return encode(value);
    }


    private void encode(Object obj, ByteBuf target) {
        byte[] bytes = null;
        if (obj == null) {
            bytes = EMPTY;
        }
        bytes = SerializeUtil.serialize(obj);
        target.writeBytes(bytes);
    }

}
