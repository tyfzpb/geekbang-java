package org.geektimes.configuration.microprofile.config.source;

import java.util.Map;

public class JavaSystemPropertiesConfigSource extends MapBasedConfigSource {

    public JavaSystemPropertiesConfigSource() {
        super("Java System Properties", 400);
    }


    @Override
    public void prepareConfigData(Map configData) {
        configData.putAll(System.getProperties());
    }
}
