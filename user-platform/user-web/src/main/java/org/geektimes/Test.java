package org.geektimes;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

    public static void main(String[] args){
        String passwrod = "sdfsd324";
        String salt = "ty_fzpb_MD5";
        String result = MD5(passwrod,salt);
        System.out.println(result);
    }

    private  static String MD5(String original, String salt){
        String result = null;
        if(original == null) {
            return result;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String str = original + salt;
            byte[] bytes = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            result = new String(bytes,StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            new RuntimeException(e);
        }
        return result;
    }
}
