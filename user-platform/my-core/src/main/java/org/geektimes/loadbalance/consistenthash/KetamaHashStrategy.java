package org.geektimes.loadbalance.consistenthash;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KetamaHashStrategy implements HashStrategy {


    @Override
    public int getHash(String origin) {
        MessageDigest md5Digest = null;
        try {
            md5Digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 not supported", e);
        }
        md5Digest.update(origin.getBytes(StandardCharsets.UTF_8));
        byte[] keyBtyes = md5Digest.digest();
        long rv = ((long) (keyBtyes[3] & 0xFF) << 24)
                | ((long) (keyBtyes[2] & 0xFF) << 16)
                | ((long) (keyBtyes[1] & 0xFF) << 8)
                | (keyBtyes[0] & 0xFF);
        return (int) (rv & 0xffffffffL);
    }
}
