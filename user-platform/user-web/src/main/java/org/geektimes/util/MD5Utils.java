package org.geektimes.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class MD5Utils {

    /**
     * md5和sha-1混合加密
     *
     * @param inputText 要加密的内容
     *
     * @return String md5和sha-1混合加密之后的密码
     */
    public static String md5AndSha(String inputText) {
        return sha(md5(inputText));
    }


    /**
     * md5加密
     *
     * @param inputText 要加密的内容
     *
     * @return String  md5加密之后的密码
     */
    public static String md5(String inputText) {
        return encrypt(inputText, "md5");
    }


    /**
     * byte[]字节数组 转换成 十六进制字符串
     *
     * @param arr 要转换的byte[]字节数组
     *
     * @return  String 返回十六进制字符串
     */
    private static String hex(byte[] arr) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < arr.length; ++i) {
            sb.append(Integer.toHexString((arr[i] & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString();
    }



    /**
     * MD5加密,并把结果由字节数组转换成十六进制字符串
     *
     * @param str 要加密的内容
     *
     * @return String 返回加密后的十六进制字符串
     */
    private static String md5Hex(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes());
            return hex(digest);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            return "";
        }
    }

    /**
     * 生成含有固定盐的密码
     *
     * @param password  要加密的密码
     * @param salt 盐
     * @return 32位的密码
     */
    public static String getMD5Hex(String password,String salt){
        if(StringUtils.isEmpty(password)){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder(password);
        int p = password.length();
        int s = salt.length();
        stringBuilder.insert(0,salt.substring(0,s/3)).insert(p/2 + s/3,salt.substring(s/3,2*s/3));
        stringBuilder.append(salt.substring(2*s/3,s));
        return md5Hex(stringBuilder.toString());
    }


    /**
     *  验证含有固定盐的密码
     * @param password 密码
     * @param salt 盐
     * @param md5str 32位字符串密文
     * @return
     */
    public static boolean getSaltverifyMD5Hex(String password,String salt,String md5str){
        String md5Hex = getMD5Hex(password,salt);
        return md5Hex.equals(md5str);
    }

    /**
     * 生成含有固定盐的密码
     *
     * @param password  要加密的密码
     * @param salt 盐
     * @return 40位的密码
     */
    public static String getSaltMD5(String password,String salt){
        StringBuilder sBuilder = new StringBuilder(salt);
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
            salt = sBuilder.toString();
        }else{
            salt = sBuilder.substring(0,16);
        }
        password = md5Hex(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }


    /**
     * 生成含有随机盐的密码
     *
     * @param password 要加密的密码
     *
     * @return String 含有随机盐的密码
     */
    public static String getSaltMD5(String password) {
        // 生成一个16位的随机数
        Random random = new Random();
        StringBuilder sBuilder = new StringBuilder(16);
        sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
        }
        // 生成最终的加密盐
        String salt = sBuilder.toString();
        return getSaltMD5(password,salt);
    }



    /**
     * 验证加盐后是否和原密码一致
     *
     * @param password 原密码
     *
     * @param password 加密之后的密码  40位字符串
     *
     *@return boolean true表示和原密码一致   false表示和原密码不一致
     */
    public static boolean getSaltverifyMD5(String password, String md5str) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        String Salt = new String(cs2);
        return md5Hex(password + Salt).equals(String.valueOf(cs1));
    }


    /**
     * sha-1加密
     *
     * @param inputText  要加密的内容
     *
     * @return  sha-1加密之后的密码
     */
    public static String sha(String inputText) {
        return encrypt(inputText, "sha-1");
    }


    /**
     * md5或者sha-1加密
     *
     * @param inputText   要加密的内容
     *
     * @param algorithmName  加密算法名称：md5或者sha-1，不区分大小写
     *
     * @return String  md5或者sha-1加密之后的结果
     */
    private static String encrypt(String inputText, String algorithmName) {
        if (inputText == null || "".equals(inputText.trim())) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (algorithmName == null || "".equals(algorithmName.trim())) {
            algorithmName = "md5";
        }
        String encryptText = null;
        try {
            MessageDigest m = MessageDigest.getInstance(algorithmName);
            m.update(inputText.getBytes("UTF8"));
            byte s[] = m.digest();
            return hex(s);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encryptText;
    }


    public static String getSaltMd5AndSha(String password,String salt){
        StringBuilder sBuilder = new StringBuilder(salt);
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
            salt = sBuilder.toString();
        }else{
            salt = sBuilder.substring(0,16);
        }
        password = md5AndSha(password + salt);
        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }


    /**
     * 生成含有随机盐的密码
     *
     * @param password 要加密的密码
     *
     * @return String 含有随机盐的密码
     */
    public static String getSaltMd5AndSha(String password) {
        // 生成一个16位的随机数
        Random random = new Random();
        StringBuilder sBuilder = new StringBuilder(16);
        sBuilder.append(random.nextInt(99999999)).append(random.nextInt(99999999));
        int len = sBuilder.length();
        if (len < 16) {
            for (int i = 0; i < 16 - len; i++) {
                sBuilder.append("0");
            }
        }
        // 生成最终的加密盐
        String salt = sBuilder.toString();
        password = md5AndSha(password + salt);

        char[] cs = new char[48];
        for (int i = 0; i < 48; i += 3) {
            cs[i] = password.charAt(i / 3 * 2);
            char c = salt.charAt(i / 3);
            cs[i + 1] = c;
            cs[i + 2] = password.charAt(i / 3 * 2 + 1);
        }
        return String.valueOf(cs);
    }



    /**
     * 验证加盐后是否和原密码一致
     *
     * @param password 原密码
     *
     * @param password 加密之后的密码
     *
     *@return boolean true表示和原密码一致   false表示和原密码不一致
     */
    public static boolean getSaltverifyMd5AndSha(String password, String md5str) {
        char[] cs1 = new char[32];
        char[] cs2 = new char[16];
        for (int i = 0; i < 48; i += 3) {
            cs1[i / 3 * 2] = md5str.charAt(i);
            cs1[i / 3 * 2 + 1] = md5str.charAt(i + 2);
            cs2[i / 3] = md5str.charAt(i + 1);
        }
        String salt = new String(cs2);
        System.out.println("salt:" + salt) ;

        String encrypPassword = md5AndSha(password + salt);

        // 加密密码去掉最后8位数
        encrypPassword = encrypPassword.substring(0 , encrypPassword.length() - 8);

        return encrypPassword.equals(String.valueOf(cs1));
    }



    public static void main(String[] args) {
        // 原密码
        String plaintext = "adfdsfsdfsd";
        String salt = "ty";

        // 获取加盐后的MD5值
        String ciphertext = MD5Utils.getSaltMD5(plaintext,salt);
        System.out.println("加盐后MD5：" + ciphertext);
        System.out.println("加盐后MD5：" + MD5Utils.getSaltMD5(plaintext,salt));
        System.out.println("是否是同一字符串:" + MD5Utils.getSaltverifyMD5(plaintext, ciphertext));


        String md5Hex = getMD5Hex(plaintext,salt);
        System.out.println("getMD5Hex：" + md5Hex);
        System.out.println("是否是同一字符串：" + MD5Utils.getSaltverifyMD5Hex(plaintext,salt,md5Hex));
    }

}
