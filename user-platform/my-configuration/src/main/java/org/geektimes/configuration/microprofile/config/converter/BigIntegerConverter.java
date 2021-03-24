package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

import java.math.BigInteger;

public class BigIntegerConverter implements Converter<BigInteger> {

    @Override
    public BigInteger convert(String value) throws IllegalArgumentException, NullPointerException {
        return value != null ? new BigInteger(value) : null;
    }
}
