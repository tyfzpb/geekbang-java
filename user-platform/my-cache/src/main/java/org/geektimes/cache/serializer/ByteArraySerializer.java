package org.geektimes.cache.serializer;

import javax.cache.CacheException;
import java.io.*;

public class ByteArraySerializer implements CacheSerializer<Object, byte[]> {
    @Override
    public byte[] serialize(Object obj) throws CacheException {
        byte[] bytes = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new CacheException(e);
        }
        return bytes;
    }

    @Override
    public Object deserialize(byte[] bytes) throws CacheException {
        if (bytes == null) {
            return null;
        }
        Object obj = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            obj = objectInputStream.readObject();
        } catch (Exception e) {
            throw new CacheException(e);
        }

        return obj;
    }
}
