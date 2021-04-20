package org.geektimes.projects.user.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.geektimes.configuration.microprofile.config.source.PropertyFileConfigSourceProvider;
import org.geektimes.web.WebApplicationInitializer;

import javax.annotation.Priority;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.List;

@Priority(0)
public class AuthProertyConfigInitializer implements WebApplicationInitializer {

    private final String propertyFileName = "META-INF/OAuth.properties";

    public void loadOAuthProperty(ClassLoader classLoader) {
        PropertyFileConfigSourceProvider provider = new PropertyFileConfigSourceProvider(propertyFileName, true, classLoader);
        List<ConfigSource> configSourceList = provider.getConfigSources(classLoader);
        ConfigProviderResolver configProviderResolver = ConfigProviderResolver.instance();
        ConfigBuilder configBuilder = configProviderResolver.getBuilder();
        // 配置 ClassLoader
        configBuilder.forClassLoader(classLoader);
        // 默认配置源（内建的，静态的）
        configBuilder.addDefaultSources();
        // 通过发现配置源（动态的）
        configBuilder.addDiscoveredSources();
        // 通过发现配置转换器（动态的）
        configBuilder.addDiscoveredConverters();
        // 增加扩展配置源（基于 Servlet 引擎）
        configBuilder.withSources(configSourceList.toArray(new ConfigSource[configSourceList.size()]));
        // 获取 Config
        Config config = configBuilder.build();
        // 注册 Config 关联到当前 ClassLoader
        configProviderResolver.registerConfig(config, classLoader);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        loadOAuthProperty(servletContext.getClassLoader());
    }
}
