package org.geektimes.util;

import java.io.*;

public class SerializeUtil {

    public static byte[] serialize(Object obj) {
        byte[] bytes = null;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)
        ) {
            objectOutputStream.writeObject(obj);
            objectOutputStream.flush();
            bytes = outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }


    public static Object deserialize(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        Object obj = null;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)
        ) {
            obj = objectInputStream.readObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return obj;
    }


}
