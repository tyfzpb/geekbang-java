package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.geetimes.util.ThreadLocalUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestJavaConfig {

    private static Config config;

    @BeforeAll
    public static void init() {
        config = ConfigProvider.getConfig();
        ThreadLocalUtil.set(Config.class.getName(), config);
    }

    @Test
    public void tsetThreadLocal() {
        Config threadConfig = ThreadLocalUtil.get(Config.class.getName());
        Assertions.assertEquals(threadConfig,config);
    }

    @Test
    public void testConfig() {
        // 系统用户名
        System.out.println(config.getValue("user", String.class));
        // 系统环境变量
        System.out.println(config.getValue("path", String.class));
        // 外部文件
        System.out.println(config.getValue("application.name", String.class));
        System.out.println(config.getValue("java-config.verison", long.class));
        // java 系统属性
        System.out.println("name:" + config.getValue("application-name", String.class));
        System.out.println("version:" + config.getValue("application-version", Float.class));
        System.out.println("defalutName:" + config.getValue("defalutName", String.class));

    }


    @Test
    public void testSystemEvnConfig() {
        // 系统环境变量
        Assertions.assertNotNull(config.getValue("path", String.class));
        // 系统用户名
        Assertions.assertEquals(config.getValue("user", String.class), "tyfzpb");
    }


    @Test
    public void testSystemPropertyConfig() {
        Assertions.assertEquals(config.getValue("application-name", String.class), "user-platform");
        Assertions.assertEquals(config.getValue("application-version", float.class), 1.1f);
    }

    @Test
    public void testFileConfig() {
        /**
         * 默认配置文件 /META-INF/micrpoprofile-config.properties
         */
        // 外部文件
        Assertions.assertEquals(config.getValue("application.name", String.class), "user-web");
        Assertions.assertEquals(config.getValue("java-config.verison", Integer.class), 1);
        Assertions.assertEquals(config.getValue("java-config.verison", boolean.class), true);
        Assertions.assertEquals(config.getValue("config_ordinal", Float.class), 100.00f);
    }

    @Test
    public void testSerlverConfig() {
        Assertions.assertEquals(config.getValue("testAppName", String.class), "user-web");
    }


}
