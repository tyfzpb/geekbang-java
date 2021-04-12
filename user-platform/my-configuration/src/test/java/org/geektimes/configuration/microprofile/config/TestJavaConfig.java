package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.geektimes.util.ThreadLocalUtil;
import org.junit.jupiter.api.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("test config")
public class TestJavaConfig {

    private Logger logger = Logger.getLogger(TestJavaConfig.class.getName());

    private static Config config;

    @BeforeAll
    @DisplayName("beforeAll test config")
    public  void beforeAll() {
        logger.log(Level.INFO,"{0} beforeAll",this.getClass().getName());
        config = ConfigProvider.getConfig();
        ThreadLocalUtil.set(Config.class.getName(), config);
    }

    @Test
    @DisplayName("test threadLocal")
    public void tsetThreadLocal() {
        Config threadConfig = ThreadLocalUtil.get(Config.class.getName());
        Assertions.assertEquals(threadConfig,config);
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
        //Assertions.assertEquals(config.getValue("testAppName", String.class), "user-web");
    }


}
