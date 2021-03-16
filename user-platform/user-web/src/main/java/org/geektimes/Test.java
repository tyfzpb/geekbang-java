package org.geektimes;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.geektimes.configuration.microprofile.config.JavaConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Test {

    public static void main(String[] args){
        testMicroprofileCofig();
    }

    private static void testMicroprofileCofig(){
        JavaConfig config = JavaConfig.class.cast(ConfigProvider.getConfig());
        // 系统用户名
        System.out.println(config.getValue("user",String.class));
        // 系统环境变量
        System.out.println(config.getValue("path",String.class));
        // 外部文件
        System.out.println(config.getValue("application.name",String.class));
        System.out.println(config.getValue("java-config.verison",int.class));

        // java 系统属性
        System.out.println(config.getValue("appName",String.class));

    }


}
