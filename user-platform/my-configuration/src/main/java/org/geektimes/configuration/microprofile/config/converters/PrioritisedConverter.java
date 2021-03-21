package org.geektimes.configuration.microprofile.config.converters;

import org.eclipse.microprofile.config.spi.Converter;

public  class PrioritisedConverter<T> implements Converter<T>, Comparable<PrioritisedConverter<T>> {

    private final Class<?> clazz;
    private final int priority;
    private final Converter<T> converter;

    public PrioritisedConverter(Class<?> clazz, int priority, Converter converter) {
        this.clazz = clazz;
        this.priority = priority;
        this.converter = converter;
    }

    public Class<?> getType() {
        return clazz;
    }

    public int getPriority() {
        return priority;
    }


    public Converter<T> getConverter() {
        return converter;
    }


    @Override
    public T convert(String value) throws IllegalArgumentException, NullPointerException {
        return converter.convert(value);
    }

    @Override
    public int compareTo(PrioritisedConverter<T> other) {
        return Integer.compare(other.getPriority(),this.priority);
    }
}
