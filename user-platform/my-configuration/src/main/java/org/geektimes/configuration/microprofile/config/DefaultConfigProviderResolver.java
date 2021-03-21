package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

    private static final ConcurrentMap<ClassLoader, Config> configsRepository = new ConcurrentHashMap<>();

    @Override
    public Config getConfig() {
        return getConfig(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Config getConfig(ClassLoader classLoader) {
        return configsRepository.computeIfAbsent(classLoader, this::newConfig);
    }


    protected Config newConfig(ClassLoader classLoader) {
        return getBuilder().forClassLoader(classLoader).addDefaultSources().addDiscoveredSources().addDiscoveredSources().build();
    }


    @Override
    public ConfigBuilder getBuilder() {

        return new DefaultConfigBuilder(null);
    }


    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        configsRepository.put(classLoader, config);

    }

    @Override
    public void releaseConfig(Config config) {
        if (config == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = DefaultConfigProviderResolver.class.getClassLoader();
            }
            config = getConfig(classLoader);
        }

        if (config != null) {

            List<ClassLoader> targetKeys = new LinkedList<>();
            for (Map.Entry<ClassLoader, Config> entry : configsRepository.entrySet()) {
                if (Objects.equals(config, entry.getValue())) {
                    targetKeys.add(entry.getKey());
                }
            }
            targetKeys.forEach(configsRepository::remove);

            if (config instanceof AutoCloseable) {
                try {
                    ((AutoCloseable) config).close();
                } catch (Exception e) {
                    throw new RuntimeException("Error while closing Config", e);
                }
            }

        }

    }
}
