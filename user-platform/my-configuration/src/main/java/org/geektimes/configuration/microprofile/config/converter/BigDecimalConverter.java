package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

import java.math.BigDecimal;

public class BigDecimalConverter implements Converter<BigDecimal> {

    public static final BigDecimalConverter INSTANCE = new BigDecimalConverter();

    @Override
    public BigDecimal convert(String value) throws IllegalArgumentException, NullPointerException {
        return value != null ? new BigDecimal(value) : null;
    }
}
