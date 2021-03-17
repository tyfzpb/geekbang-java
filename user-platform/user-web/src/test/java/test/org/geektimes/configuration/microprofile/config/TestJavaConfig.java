package test.org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestJavaConfig {

    private  Config config = ConfigProvider.getConfig();

    @Test
    public void testConfig(){
        // 系统用户名
        System.out.println(config.getValue("user",String.class));
        // 系统环境变量
        System.out.println(config.getValue("path",String.class));
        // 外部文件
        System.out.println(config.getValue("application.name",String.class));
        System.out.println(config.getValue("java-config.verison",int.class));
        // java 系统属性
        System.out.println("name:"+System.getProperty("application-name"));
        System.out.println("version:"+System.getProperty("application-version"));
    }


    @Test
    public void testSystemEvnConfig(){
        // 系统环境变量
        Assertions.assertNotNull(config.getValue("path",String.class));
        // 系统用户名
        Assertions.assertEquals(config.getValue("user",String.class),"tyfzpb");
    }


    @Test
    public void testSystemPropertyConfig(){
        Assertions.assertEquals(config.getValue("application-name",String.class),"user-web");
        Assertions.assertEquals(config.getValue("application-version",Float.class),1.3f);
    }

    @Test
    public void testFileConfig(){
        // 外部文件
        Assertions.assertEquals(config.getValue("application.name",String.class),"user-web");
        Assertions.assertEquals(config.getValue("java-config.verison",int.class),1);
    }

}
