package org.geektimes.cache.provider.redis.lettuce.codec;

import io.lettuce.core.codec.RedisCodec;
import org.geektimes.cache.serializer.ByteArraySerializer;
import org.geektimes.cache.serializer.CacheSerializer;

import java.nio.ByteBuffer;

public class GenericRedisCodec<K, V> implements RedisCodec<K, V> {

    private static final byte[] EMPTY = new byte[0];
    private final Codec keyCodec;
    private final Codec valueCodec;
    private final CacheSerializer<Object, byte[]> serializer;


    public GenericRedisCodec(Class<K> k, Class<V> v) {
        this.keyCodec = initCodec(k);
        this.valueCodec = initCodec(v);
        this.serializer = new ByteArraySerializer();
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
            return null;
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
        return serializer.deserialize(bytes);
    }


    private ByteBuffer encode(Object obj) {
        if (obj == null) {
            return ByteBuffer.wrap(EMPTY);
        }
        byte[] bytes = serializer.serialize(obj);
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


}
