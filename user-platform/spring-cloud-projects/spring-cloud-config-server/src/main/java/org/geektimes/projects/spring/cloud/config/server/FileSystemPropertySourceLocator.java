package org.geektimes.projects.spring.cloud.config.server;

import org.springframework.cloud.bootstrap.config.PropertySourceLocator;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

@Order(0)
public class FileSystemPropertySourceLocator implements PropertySourceLocator, EnvironmentAware, ResourceLoaderAware {

    private final static String DEFAULT_CONFIG_FILE = "META-INF/config/default.properties";
    private final static String ENCODING = "UTF-8";
    private Environment environment;
    private ResourceLoader resourceLoader;
    private Resource propertiesResource;
    private Properties properties;


    @Override
    public PropertySource<?> locate(Environment environment) {
        this.propertiesResource = getPropertiesResource(DEFAULT_CONFIG_FILE);
        this.properties = loadProperties();
        return new MapPropertySource("fileSystemPropertySource", (Map) properties);
    }

    private Properties loadProperties() {
        EncodedResource encodedResource = new EncodedResource(this.propertiesResource, ENCODING);
        try {
            return loadProperties(encodedResource.getReader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Properties loadProperties(Reader reader) {
        Properties properties = new Properties();
        try {
            properties.load(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return properties;
    }

    private Resource getPropertiesResource(String configFile) {
        ResourceLoader resourceLoader = getResourceLoader();
        return resourceLoader.getResource(configFile);
    }

    private ResourceLoader getResourceLoader() {
        return this.resourceLoader != null ? this.resourceLoader : new DefaultResourceLoader();
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public Resource getPropertiesResource() {
        return propertiesResource;
    }

    public Properties getProperties() {
        return properties;
    }
}
