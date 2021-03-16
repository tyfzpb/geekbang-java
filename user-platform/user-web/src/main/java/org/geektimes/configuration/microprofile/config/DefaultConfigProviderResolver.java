package org.geektimes.configuration.microprofile.config;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class DefaultConfigProviderResolver extends ConfigProviderResolver {

    private static final Map<ClassLoader, WeakReference<Config>> configs
            = Collections.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<Config>>());


    @Override
    public Config getConfig() {
        return getConfig(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public Config getConfig(ClassLoader classLoader) {
        Config config = existingConfig(classLoader);
        if (config == null) {
            synchronized (DefaultConfigProviderResolver.class) {
                config = existingConfig(classLoader);
                if (config == null) {
                    config = getBuilder()
                            .forClassLoader(classLoader)
                            .addDefaultSources()
                            .addDiscoveredSources()
                            .addDiscoveredConverters()
                            .build();
                    registerConfig(config, classLoader);
                }
            }
        }
        return config;
    }

    protected Config existingConfig(ClassLoader classLoader) {
        WeakReference<Config> configRef = configs.get(classLoader);
        return configRef != null ? configRef.get() : null;
    }

    @Override
    public ConfigBuilder getBuilder() {
        return new DefaultConfigBuilder();
    }

    @Override
    public void registerConfig(Config config, ClassLoader classLoader) {
        synchronized (DefaultConfigProviderResolver.class) {
            configs.put(classLoader, new WeakReference<>(config));
        }

    }

    @Override
    public void releaseConfig(Config config) {
        if (config == null) {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            if (classLoader == null) {
                classLoader = DefaultConfigProviderResolver.class.getClassLoader();
            }
            config = existingConfig(classLoader);
        }

        if (config != null) {
            synchronized (DefaultConfigProviderResolver.class) {
                Iterator<Map.Entry<ClassLoader, WeakReference<Config>>> it = configs.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<ClassLoader, WeakReference<Config>> entry = it.next();
                    if (entry.getValue().get() != null && entry.getValue().get() == config) {
                        it.remove();
                        break;
                    }
                }

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
}
