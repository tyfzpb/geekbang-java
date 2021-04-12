package org.geektimes.cache.serializer;

import javax.cache.CacheException;

/**
 * Serializer of cache
 *
 * @param <S> source
 * @param <T> target
 */
public interface CacheSerializer<S, T> {

    T serialize(S source) throws CacheException;

    S deserialize(T targe) throws CacheException;

}
