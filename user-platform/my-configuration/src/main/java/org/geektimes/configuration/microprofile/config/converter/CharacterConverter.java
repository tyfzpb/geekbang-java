package org.geektimes.configuration.microprofile.config.converter;

import org.eclipse.microprofile.config.spi.Converter;

public class CharacterConverter implements Converter<Character> {

    @Override
    public Character convert(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return Character.valueOf(value.charAt(0));
    }
}
