package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class ByteConverter implements Converter<Byte> {

    @Override
    public Byte convert(String value) {
        return Byte.valueOf(value);
    }
}
