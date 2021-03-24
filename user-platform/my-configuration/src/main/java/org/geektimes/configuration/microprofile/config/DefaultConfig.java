package org.geektimes.configuration.microprofile.config;


import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.geektimes.configuration.microprofile.config.converter.*;
import org.geektimes.configuration.microprofile.config.source.ConfigSources;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.StreamSupport.stream;

public class DefaultConfig implements Config {

    private final ConfigSources configSources;
    private final Converters converters;


    public DefaultConfig(ConfigSources configSources, Converters converters) {
        this.configSources = configSources;
        this.converters = converters;
        registerDefaultConverter(converters);
    }

    private void registerDefaultConverter(Converters converters) {
        converters.addConverter(boolean.class, BooleanConverter.INSTANCE);
        converters.addConverter(short.class, ShortConverter.INSTANCE);
        converters.addConverter(double.class, DoubleConverter.INSTANCE);
        converters.addConverter(float.class, FloatConverter.INSTANCE);
        converters.addConverter(int.class, IntegerConverter.INSTANCE);
        converters.addConverter(long.class, LongConverter.INSTANCE);
    }


    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        ConfigValue configValue = getConfigValue(propertyName);
        if (configValue == null) {
            return null;
        }
        // String 转换成目标类型
        return convert(configValue.getValue(), propertyType);
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        String propertyValue = null;
        ConfigSource configSource = null;
        for (ConfigSource source : configSources) {
            propertyValue = source.getValue(propertyName);
            if (propertyValue != null) {
                configSource = source;
                break;
            }
        }

        if (propertyValue == null) {
            return null;
        }

        return new DefaultConfigValue(propertyName, propertyValue, transformPropertyValue(propertyValue),
                configSource.getName(),
                configSource.getOrdinal());
    }


    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        List<Converter> converters = this.converters.getConverters(forType);
        return converters.isEmpty() ? Optional.empty() : Optional.ofNullable(converters.get(0));
    }

    public <T> T convert(String propertyName, Class<T> propertyType) {
        if (propertyName != null) {
            Optional<Converter<T>> optional = getConverter(propertyType);
            return optional.get().convert(propertyName);
        }
        return null;
    }


    /**
     * 转换属性值（如果需要）
     *
     * @param propertyValue
     * @return
     */
    protected String transformPropertyValue(String propertyValue) {
        return propertyValue;
    }


    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        T value = getValue(propertyName, propertyType);
        return Optional.ofNullable(value);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return stream(configSources.spliterator(), false)
                .map(ConfigSource::getPropertyNames)
                .collect(LinkedHashSet::new, Set::addAll, Set::addAll);
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return configSources;
    }


    @Override
    public <T> T unwrap(Class<T> type) {
        return null;
    }

}
